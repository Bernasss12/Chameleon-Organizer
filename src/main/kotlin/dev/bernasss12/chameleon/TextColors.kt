package dev.bernasss12.chameleon.kotlin

enum class TextColors(val color: String){
    RED(27.toChar() + "[31m"),
    GREEN(27.toChar() + "[32m"),
    YELLOW(27.toChar() + "[33m"),
    BLUE(27.toChar() + "[34m"),
    WHITE(27.toChar() + "[37m")
}

fun printlnError(string: String){
    println(TextColors.RED.color + string + TextColors.WHITE.color)
}

fun printlnWarning(string: String){
    println(TextColors.YELLOW.color + string + TextColors.WHITE.color)
}
fun printlnInfo(string: String){
    println(TextColors.BLUE.color + "Info: " + string + TextColors.WHITE.color)
}
fun printlnGreen(string: String){
    println(TextColors.GREEN.color + string + TextColors.WHITE.color)
}

fun printlnBlue(string: String){
    println(TextColors.BLUE.color + string + TextColors.WHITE.color)
}


