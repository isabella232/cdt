/*******************************************************************************
 * Copyright (c) 2006, 2016 QNX Software Systems and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * 	   QNX - Initial API and implementation
 *     Markus Schorn (Wind River Systems)
 *     Sergey Prigogin (Google)
 *******************************************************************************/
package org.eclipse.cdt.internal.core.pdom.dom;

import org.eclipse.cdt.core.dom.ast.IASTFileLocation;
import org.eclipse.cdt.core.dom.ast.IASTName;
import org.eclipse.cdt.core.dom.ast.IASTPreprocessorIncludeStatement;
import org.eclipse.cdt.core.index.IIndexFile;
import org.eclipse.cdt.core.index.IIndexFileLocation;
import org.eclipse.cdt.internal.core.index.IIndexFragment;
import org.eclipse.cdt.internal.core.index.IIndexFragmentFile;
import org.eclipse.cdt.internal.core.index.IIndexFragmentInclude;
import org.eclipse.cdt.internal.core.pdom.db.Database;
import org.eclipse.core.runtime.CoreException;

/**
 * @author Doug Schaefer
 */
public class PDOMInclude implements IIndexFragmentInclude {
	private static final int INCLUDED_FILE		 	=  0;
	private static final int INCLUDED_BY 			=  4;
	private static final int INCLUDES_NEXT 			=  8;
	private static final int INCLUDED_BY_NEXT 		= 12;
	private static final int INCLUDED_BY_PREV 		= 16;
	// If the include name is the same as the end part of the path of the included file,
	// we store the length of the name instead of the name itself, and indicate that
	// by turning on FLAG_DEDUCIBLE_NAME flag. Notice that the length of include name
	// can be different from the node length, if the name is defined by a macro. 
	private static final int INCLUDE_NAME_OR_LENGTH = 20; // TODO: assumes that int and stored pointers are the same size
	private static final int NODE_OFFSET  			= 24; // 3-byte unsigned int (sufficient for files <= 16mb)
	private static final int NODE_LENGTH  			= 27; // short (sufficient for names <= 32k)
	private static final int FLAGS		 			= 29;
	private static final int RECORD_SIZE 			= 30;

	private static final int FLAG_SYSTEM_INCLUDE 		= 0x01;
	private static final int FLAG_INACTIVE_INCLUDE 		= 0x02;
	private static final int FLAG_RESOLVED_BY_HEURISTICS= 0x04;
	private static final int FLAG_DEDUCIBLE_NAME		= 0x08;
	private static final int FLAG_EXPORTED_FILE			= 0x10;
	
	private final PDOMLinkage linkage;
	private final long record;

	// Cached fields
	private String fName;

	public PDOMInclude(PDOMLinkage linkage, long record) {
		this.linkage = linkage;
		this.record = record;
	}

	public PDOMInclude(PDOMLinkage linkage, IASTPreprocessorIncludeStatement include,
			PDOMFile containerFile, PDOMFile targetFile) throws CoreException {
		this.linkage = linkage;
		this.record = linkage.getDB().malloc(RECORD_SIZE);
		IASTName name = include.getName();
		char[] nameChars = name.getSimpleID();
		IASTFileLocation loc = name.getFileLocation();
		// Includes generated by -include or -macro don't have a location
		if (loc != null) {
			linkage.getDB().put3ByteUnsignedInt(record + NODE_OFFSET, loc.getNodeOffset());
			linkage.getDB().putShort(record + NODE_LENGTH, (short) loc.getNodeLength());
		}

		final Database db = linkage.getDB();
		if (targetFile != null) {
			db.putRecPtr(record + INCLUDED_FILE, targetFile.getRecord());
		}
		boolean deducible_name = isDeducibleName(targetFile, nameChars);
		// If the name is the same as an end part of the path of the included file,
		// store the length of the name instead of the name itself.
		if (deducible_name) {
			db.putInt(record + INCLUDE_NAME_OR_LENGTH, nameChars.length);
		} else {
			db.putRecPtr(record + INCLUDE_NAME_OR_LENGTH, db.newString(nameChars).getRecord());
		}
		setFlag(encodeFlags(include, deducible_name));
		setIncludedBy(containerFile);
	}

	private byte encodeFlags(IASTPreprocessorIncludeStatement include, boolean deducible_name) {
		byte flags= 0;
		if (include.isSystemInclude()) {
			flags |= FLAG_SYSTEM_INCLUDE;
		}
		if (!include.isActive()) {
			flags |= FLAG_INACTIVE_INCLUDE;
		} else if (include.isResolvedByHeuristics()) {
			flags |= FLAG_RESOLVED_BY_HEURISTICS;
		}
		if (include.isIncludedFileExported()) {
			flags |= FLAG_EXPORTED_FILE;
		}
		if (deducible_name) {
			flags |= FLAG_DEDUCIBLE_NAME;
		}
		return flags;
	}

	public long getRecord() {
		return record;
	}

	public void delete() throws CoreException {
		if (isResolved()) {
			// Remove us from the includedBy chain
			removeThisFromIncludedByChain();
		}
		final Database db = linkage.getDB();
		if ((getFlag() & FLAG_DEDUCIBLE_NAME) == 0) {
			long rec = db.getRecPtr(record + INCLUDE_NAME_OR_LENGTH);
			db.getString(rec).delete();
		}
		// Delete our record
		db.free(record);
	}

	private void removeThisFromIncludedByChain() throws CoreException {
		PDOMInclude prevInclude = getPrevInIncludedBy();
		PDOMInclude nextInclude = getNextInIncludedBy();
		if (prevInclude != null) {
			prevInclude.setNextInIncludedBy(nextInclude);
		} else {
			((PDOMFile) getIncludes()).setFirstIncludedBy(nextInclude);
		}

		if (nextInclude != null)
			nextInclude.setPrevInIncludedBy(prevInclude);
	}

	@Override
	public IIndexFragmentFile getIncludes() throws CoreException {
		long rec = linkage.getDB().getRecPtr(record + INCLUDED_FILE);
		return rec != 0 ? new PDOMFile(linkage, rec) : null;
	}

	void setIncludes(PDOMFile includedFile) throws CoreException {
		long rec = includedFile != null ? includedFile.getRecord() : 0;
		linkage.getDB().putRecPtr(record + INCLUDED_FILE, rec);
	}

	/**
	 * Checks if the name is the same as the end part of the path of the included file.
	 */
	private static boolean isDeducibleName(PDOMFile includedFile, char[] name) throws CoreException {
		if (includedFile == null) {
			return false;
		}
		String s = includedFile.getLocation().getURI().getPath();
		int pos = s.length() - name.length;
		if (pos < 0) {
			return false;
		}
		for (int i = 0; i < name.length; i++, pos++) {
			if (s.charAt(pos) != name[i]) {
				return false;
			}
		}
		return true;
	}

	@Override
	public IIndexFile getIncludedBy() throws CoreException {
		long rec = linkage.getDB().getRecPtr(record + INCLUDED_BY);
		return rec != 0 ? new PDOMFile(linkage, rec) : null;
	}

	void setIncludedBy(PDOMFile includedBy) throws CoreException {
		long rec = includedBy != null ? includedBy.getRecord() : 0;
		linkage.getDB().putRecPtr(record + INCLUDED_BY, rec);
	}

	public PDOMInclude getNextInIncludes() throws CoreException {
		long rec = linkage.getDB().getRecPtr(record + INCLUDES_NEXT);
		return rec != 0 ? new PDOMInclude(linkage, rec) : null;
	}

	public void setNextInIncludes(PDOMInclude include) throws CoreException {
		long rec = include != null ? include.getRecord() : 0;
		linkage.getDB().putRecPtr(record + INCLUDES_NEXT, rec);
	}

	public PDOMInclude getNextInIncludedBy() throws CoreException {
		long rec = linkage.getDB().getRecPtr(record + INCLUDED_BY_NEXT);
		return rec != 0 ? new PDOMInclude(linkage, rec) : null;
	}

	public void setNextInIncludedBy(PDOMInclude include) throws CoreException {
		long rec = include != null ? include.getRecord() : 0;
		linkage.getDB().putRecPtr(record + INCLUDED_BY_NEXT, rec);
	}

	public PDOMInclude getPrevInIncludedBy() throws CoreException {
		long rec = getPrevInIncludedByRecord();
		return rec != 0 ? new PDOMInclude(linkage, rec) : null;
	}

	long getPrevInIncludedByRecord() throws CoreException {
		return linkage.getDB().getRecPtr(record + INCLUDED_BY_PREV);
	}

	public void setPrevInIncludedBy(PDOMInclude include) throws CoreException {
		long rec = include != null ? include.getRecord() : 0;
		linkage.getDB().putRecPtr(record + INCLUDED_BY_PREV, rec);
	}

	@Override
	public IIndexFileLocation getIncludedByLocation() throws CoreException {
		return getIncludedBy().getLocation();
	}

	@Override
	public IIndexFileLocation getIncludesLocation() throws CoreException {
		IIndexFragmentFile includes = getIncludes();
		return includes != null ? includes.getLocation() : null;
	}

	@Override
	public IIndexFragment getFragment() {
		return linkage.getPDOM();
	}

	private void setFlag(byte flag) throws CoreException {
		linkage.getDB().putByte(record + FLAGS, flag);
	}

	private int getFlag() throws CoreException {
		return linkage.getDB().getByte(record + FLAGS);
	}

	@Override
	public boolean isSystemInclude() throws CoreException {
		return (getFlag() & FLAG_SYSTEM_INCLUDE) != 0;
	}

	@Override
	public boolean isActive() throws CoreException {
		return (getFlag() & FLAG_INACTIVE_INCLUDE) == 0;
	}

	@Override
	public boolean isResolved() throws CoreException {
		return linkage.getDB().getRecPtr(record + INCLUDED_FILE) != 0;
	}

	@Override
	public boolean isResolvedByHeuristics() throws CoreException {
		return (getFlag() & FLAG_RESOLVED_BY_HEURISTICS) != 0;
	}

	@Override
	public boolean isIncludedFileExported() throws CoreException {
		return (getFlag() & FLAG_EXPORTED_FILE) != 0;
	}
	
	@Override
	public int getNameOffset() throws CoreException {
		return linkage.getDB().get3ByteUnsignedInt(record + NODE_OFFSET);
	}

	@Override
	public int getNameLength() throws CoreException {
		return linkage.getDB().getShort(record + NODE_LENGTH) & 0xffff;
	}	

	@Override
	public String getFullName() throws CoreException {
		if (fName == null) {
			final Database db = linkage.getDB();
			// The include name is either stored explicitly, or can be deduced from the path
			// of the included file.
			if ((getFlag() & FLAG_DEDUCIBLE_NAME) == 0) {
				long rec = db.getRecPtr(record + INCLUDE_NAME_OR_LENGTH);
				fName = db.getString(rec).getString();
			} else {
				String path = getIncludes().getLocation().getURI().getPath();
				int nameLength = db.getInt(record + INCLUDE_NAME_OR_LENGTH);
				fName = path.substring(Math.max(path.length() - nameLength, 0));
			}
		}
		return fName;
	}

	@Override
	public String getName() throws CoreException {
		final String fullName= getFullName();
		final int idx= Math.max(fullName.lastIndexOf('/'), fullName.lastIndexOf('\\'));
		return fullName.substring(idx + 1);
	}

	public void convertToUnresolved() throws CoreException {
		if (isResolved()) {
			final Database db = linkage.getDB();
			int flag = getFlag();
			// Since included file is going away, the name is no longer deducible.
			if ((flag & FLAG_DEDUCIBLE_NAME) != 0) {
				long rec= db.newString(getFullName()).getRecord();
				db.putRecPtr(record + INCLUDE_NAME_OR_LENGTH, rec);
				setFlag((byte) (flag & ~FLAG_DEDUCIBLE_NAME));
			}
			db.putRecPtr(record + INCLUDED_FILE, 0);
		}
	}

	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder();
		try {
			boolean isSystem = isSystemInclude();
			buf.append(isSystem ? '<' : '"');
			buf.append(getFullName());
			buf.append(isSystem ? '>' : '"');
			IIndexFile includedBy = getIncludedBy();
			if (includedBy != null)
				buf.append(" in ").append(includedBy); //$NON-NLS-1$
			IIndexFragmentFile includes = getIncludes();
			if (includes != null) {
				buf.append(" resolved to ").append(includes); //$NON-NLS-1$
			} else {
				buf.append(" unresolved"); //$NON-NLS-1$
			}
		} catch (CoreException e) {
			buf.append(" (incomplete due to ").append(e.getClass().getName()).append(')'); //$NON-NLS-1$
			e.printStackTrace();
		}
		return buf.toString();
	}
}
