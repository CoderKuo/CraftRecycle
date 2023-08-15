import com.dakuo.craftrecycle.rule.variable.VariableHandler
import com.dakuo.craftrecycle.rule.variable.VariableTemplate
import java.util.regex.Pattern

fun main() {
    while (true) {
        print("请输入任意键开始: ")
        val readLine = readLine()
        Test().test()
    }

}


class Test(){
    fun test(){
        val startTime = System.currentTimeMillis()

        val first = "当前版本为 {set:version} 版本，{set:num}金币"
        val second = "当前版本为 1-测试 版本，100金币"
        for(i in 1..100000) {
            val template1 = VariableTemplate("name", first)
            template1.match(second)
        }
        println((System.currentTimeMillis()-startTime).toString()+"ms.")


    }
}