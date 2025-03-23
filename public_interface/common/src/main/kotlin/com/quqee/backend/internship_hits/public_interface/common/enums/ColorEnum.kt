package com.quqee.backend.internship_hits.public_interface.common.enums

import com.quqee.backend.internship_hits.public_interface.common.exception.ExceptionInApplication

enum class ColorEnum(val hexColor: String) {
    RED("#FF0000"),
    GREEN("#00FF00"),
    BLUE("#0000FF"),
    YELLOW("#FFFF00"),
    CYAN("#00FFFF"),
    MAGENTA("#FF00FF"),
    WHITE("#FFFFFF"),
    BLACK("#000000"),
    GRAY("#808080"),
    LIGHT_GRAY("#D3D3D3"),
    DARK_GRAY("#A9A9A9"),
    ORANGE("#FFA500"),
    PINK("#FFC0CB"),
    PURPLE("#800080"),
    BROWN("#A52A2A"),
    SILVER("#C0C0C0"),
    GOLD("#FFD700"),
    MAROON("#800000"),
    OLIVE("#808000"),
    LIME("#00FF00"),
    TEAL("#008080"),
    NAVY("#000080"),
    INDIGO("#4B0082"),
    AQUA("#00FFFF"),
    FUCHSIA("#FF00FF"),
    LAVENDER("#E6E6FA"),
    BEIGE("#F5F5DC"),
    IVORY("#FFFFF0"),
    KHAKI("#F0E68C"),
    TAN("#D2B48C"),
    WHEAT("#F5DEB3"),
    CORAL("#FF7F50"),
    SALMON("#FA8072"),
    ORCHID("#DA70D6"),
    TURQUOISE("#40E0D0"),
    VIOLET("#EE82EE"),
    CRIMSON("#DC143C"),
    LAVENDER_BLUSH("#FFF0F5"),
    PALE_VIOLET_RED("#DB7093"),
    PALE_GOLDENROD("#EEE8AA"),
    PALE_GREEN("#98FB98"),
    PALE_TURQUOISE("#AFEEEE"),
    PALE_BLUE("#AFEEEE"),
    PALE_PINK("#FFB6C1"),
    PALE_BROWN("#987654"),
    PALE_ORANGE("#FFA07A"),
    PALE_PURPLE("#9370DB"),
    PALE_RED("#DB7093"),
    PALE_YELLOW("#EEE8AA"),
    PALE_CYAN("#E0FFFF"),
    PALE_MAGENTA("#F0F"),
    PALE_WHITE("#F0F8FF"),
    PALE_BLACK("#000000"),
    PALE_GRAY("#D3D3D3");

    companion object {
        fun fromHex(hexColor: String): ColorEnum {
            return entries.find { it.hexColor == hexColor } ?: throw ExceptionInApplication(ExceptionType.NOT_FOUND, "Color not found")
        }
    }
}