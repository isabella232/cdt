/*******************************************************************************
* Copyright (c) 2006, 2010 IBM Corporation and others.
*
* This program and the accompanying materials
* are made available under the terms of the Eclipse Public License 2.0
* which accompanies this distribution, and is available at
* https://www.eclipse.org/legal/epl-2.0/
*
* SPDX-License-Identifier: EPL-2.0
*
* Contributors:
*     IBM Corporation - initial API and implementation
*********************************************************************************/

// This file was generated by LPG

package org.eclipse.cdt.internal.core.lrparser.xlc.cpp;

public interface XlcCPPParsersym {
    public final static int
      TK__Complex = 24,
      TK__Imaginary = 25,
      TK_restrict = 34,
      TK_asm = 7,
      TK_auto = 38,
      TK_bool = 15,
      TK_break = 91,
      TK_case = 92,
      TK_catch = 130,
      TK_char = 16,
      TK_class = 66,
      TK_const = 32,
      TK_const_cast = 58,
      TK_continue = 93,
      TK_default = 94,
      TK_delete = 82,
      TK_do = 95,
      TK_double = 26,
      TK_dynamic_cast = 59,
      TK_else = 136,
      TK_enum = 68,
      TK_explicit = 39,
      TK_export = 96,
      TK_extern = 40,
      TK_false = 51,
      TK_float = 17,
      TK_for = 97,
      TK_friend = 41,
      TK_goto = 98,
      TK_if = 99,
      TK_inline = 42,
      TK_int = 18,
      TK_long = 19,
      TK_mutable = 43,
      TK_namespace = 76,
      TK_new = 83,
      TK_operator = 11,
      TK_private = 131,
      TK_protected = 132,
      TK_public = 133,
      TK_register = 44,
      TK_reinterpret_cast = 60,
      TK_return = 100,
      TK_short = 20,
      TK_signed = 21,
      TK_sizeof = 61,
      TK_static = 35,
      TK_static_cast = 62,
      TK_struct = 69,
      TK_switch = 101,
      TK_template = 67,
      TK_this = 52,
      TK_throw = 74,
      TK_try = 85,
      TK_true = 53,
      TK_typedef = 45,
      TK_typeid = 63,
      TK_typename = 23,
      TK_union = 70,
      TK_unsigned = 22,
      TK_using = 77,
      TK_virtual = 36,
      TK_void = 27,
      TK_volatile = 33,
      TK_wchar_t = 28,
      TK_while = 90,
      TK_integer = 54,
      TK_floating = 55,
      TK_charconst = 56,
      TK_stringlit = 46,
      TK_identifier = 1,
      TK_Completion = 4,
      TK_EndOfCompletion = 14,
      TK_Invalid = 137,
      TK_LeftBracket = 72,
      TK_LeftParen = 5,
      TK_Dot = 134,
      TK_DotStar = 112,
      TK_Arrow = 129,
      TK_ArrowStar = 105,
      TK_PlusPlus = 49,
      TK_MinusMinus = 50,
      TK_And = 13,
      TK_Star = 12,
      TK_Plus = 47,
      TK_Minus = 48,
      TK_Tilde = 10,
      TK_Bang = 57,
      TK_Slash = 113,
      TK_Percent = 114,
      TK_RightShift = 88,
      TK_LeftShift = 89,
      TK_LT = 71,
      TK_GT = 78,
      TK_LE = 102,
      TK_GE = 103,
      TK_EQ = 106,
      TK_NE = 107,
      TK_Caret = 108,
      TK_Or = 109,
      TK_AndAnd = 110,
      TK_OrOr = 115,
      TK_Question = 116,
      TK_Colon = 84,
      TK_ColonColon = 6,
      TK_DotDotDot = 104,
      TK_Assign = 86,
      TK_StarAssign = 117,
      TK_SlashAssign = 118,
      TK_PercentAssign = 119,
      TK_PlusAssign = 120,
      TK_MinusAssign = 121,
      TK_RightShiftAssign = 122,
      TK_LeftShiftAssign = 123,
      TK_AndAssign = 124,
      TK_CaretAssign = 125,
      TK_OrAssign = 126,
      TK_Comma = 73,
      TK_RightBracket = 111,
      TK_RightParen = 79,
      TK_RightBrace = 87,
      TK_SemiColon = 65,
      TK_LeftBrace = 75,
      TK_typeof = 37,
      TK___alignof__ = 64,
      TK___attribute__ = 8,
      TK___declspec = 9,
      TK_MAX = 127,
      TK_MIN = 128,
      TK_vector = 3,
      TK_pixel = 2,
      TK__Decimal32 = 29,
      TK__Decimal64 = 30,
      TK__Decimal128 = 31,
      TK___static_assert = 80,
      TK_ERROR_TOKEN = 81,
      TK_EOF_TOKEN = 135;

      public final static String orderedTerminalSymbols[] = {
                 "",
                 "identifier",
                 "pixel",
                 "vector",
                 "Completion",
                 "LeftParen",
                 "ColonColon",
                 "asm",
                 "__attribute__",
                 "__declspec",
                 "Tilde",
                 "operator",
                 "Star",
                 "And",
                 "EndOfCompletion",
                 "bool",
                 "char",
                 "float",
                 "int",
                 "long",
                 "short",
                 "signed",
                 "unsigned",
                 "typename",
                 "_Complex",
                 "_Imaginary",
                 "double",
                 "void",
                 "wchar_t",
                 "_Decimal32",
                 "_Decimal64",
                 "_Decimal128",
                 "const",
                 "volatile",
                 "restrict",
                 "static",
                 "virtual",
                 "typeof",
                 "auto",
                 "explicit",
                 "extern",
                 "friend",
                 "inline",
                 "mutable",
                 "register",
                 "typedef",
                 "stringlit",
                 "Plus",
                 "Minus",
                 "PlusPlus",
                 "MinusMinus",
                 "false",
                 "this",
                 "true",
                 "integer",
                 "floating",
                 "charconst",
                 "Bang",
                 "const_cast",
                 "dynamic_cast",
                 "reinterpret_cast",
                 "sizeof",
                 "static_cast",
                 "typeid",
                 "__alignof__",
                 "SemiColon",
                 "class",
                 "template",
                 "enum",
                 "struct",
                 "union",
                 "LT",
                 "LeftBracket",
                 "Comma",
                 "throw",
                 "LeftBrace",
                 "namespace",
                 "using",
                 "GT",
                 "RightParen",
                 "__static_assert",
                 "ERROR_TOKEN",
                 "delete",
                 "new",
                 "Colon",
                 "try",
                 "Assign",
                 "RightBrace",
                 "RightShift",
                 "LeftShift",
                 "while",
                 "break",
                 "case",
                 "continue",
                 "default",
                 "do",
                 "export",
                 "for",
                 "goto",
                 "if",
                 "return",
                 "switch",
                 "LE",
                 "GE",
                 "DotDotDot",
                 "ArrowStar",
                 "EQ",
                 "NE",
                 "Caret",
                 "Or",
                 "AndAnd",
                 "RightBracket",
                 "DotStar",
                 "Slash",
                 "Percent",
                 "OrOr",
                 "Question",
                 "StarAssign",
                 "SlashAssign",
                 "PercentAssign",
                 "PlusAssign",
                 "MinusAssign",
                 "RightShiftAssign",
                 "LeftShiftAssign",
                 "AndAssign",
                 "CaretAssign",
                 "OrAssign",
                 "MAX",
                 "MIN",
                 "Arrow",
                 "catch",
                 "private",
                 "protected",
                 "public",
                 "Dot",
                 "EOF_TOKEN",
                 "else",
                 "Invalid"
             };

    public final static boolean isValidForParser = true;
}
