package ch.iagentur.ringieradsdk.demo.extensions

fun List<String>.toStringWithNewLine(): String {
    var message = ""
    forEachIndexed { index, size ->
        message += if (index == this.size - 1) {
            size
        } else {
            ("$size\n")
        }
    }
    return message
}