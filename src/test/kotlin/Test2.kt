

import java.util.regex.Pattern

val placeholderPattern = "\\{([^:]+):([^}]+)}".toRegex()

fun containsVar(string: String): List<Pair<String, String>> {
    val list = mutableListOf<Pair<String, String>>()
    val matches = placeholderPattern.findAll(string)

    for (match in matches) {
        val (operatorKey, dataKey) = match.destructured
        list.add(Pair(operatorKey, dataKey))
    }

    return list
}

fun main() {
    println(containsVar("当前版本为 {set:version} 版本，{set:num}金币"))
}