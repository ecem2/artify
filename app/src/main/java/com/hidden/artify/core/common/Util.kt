package com.hidden.artify.core.common

import java.util.regex.Pattern

object Util {

    const val EMPTY_STRING = ""
    const val SPACE = " "
    const val SPACE_CHAR = ' '
    const val INVALID_INDEX = -1
    const val ZERO = 0
    const val ZERO_FLOAT = 0f
    const val ZERO_DOUBLE = 0.0
    const val ZERO_LONG = 0L
    const val HUNDRED = 100
    const val DOUBLE_ZERO = "00"
    const val SLASH = "/"
    const val COMMA = ","
    const val SEMICOLON = ";"
    const val COLON = ":"
    const val POINT = "."
    const val LEFT_PARENTHESIS = "("
    const val RIGHT_PARENTHESIS = ")"
    const val DASH = "-"
    const val TR = "TR"
    val ALPHA_NUMERIC_REGEX = Regex("[^A-Za-z0-9]")
    val PLATE_PATTERN: Pattern =
        Pattern.compile("(0[1-9]|[1-7][0-9]|8[01])([a-pr-vy-zA-PR-V-Y-Z][0-9]{4,5}|[a-pr-vy-zA-PR-V-Y-Z]{2}[0-9]{3,4}|[a-pr-vy-zA-PR-V-Y-Z]{3}[0-9]{2,3})")
    const val PLAY_STORE_URL = "https://play.google.com/store/apps/details?id="
    //const val ARTIFY_INSIDE_DEEPLINK = "artify://inside-deeplink/"
    const val ZERO_GUID = "00000000-0000-0000-0000-000000000000"

}