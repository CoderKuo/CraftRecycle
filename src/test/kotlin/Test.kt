import com.dakuo.craftrecycle.rule.MatchRule
import java.util.concurrent.CopyOnWriteArrayList

fun main() {

    val list = listOf("xxxxxxx","aaaaaaa","bbbbbbbb")


    val lists = CopyOnWriteArrayList(list.map { it.toCharArray().toList() })
    println(lists)

}