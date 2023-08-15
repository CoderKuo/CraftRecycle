import java.util.regex.Pattern

data class Variable(val key:String,val value:Any = ""){

}


fun String.containsVar():List<Pair<String,String>>{
    val list = mutableListOf<Pair<String,String>>()
    this.forEachIndexed { index, c ->
        if (c == '{'){
            val endIndex = this.indexOf('}', index) ?: -1
            if (endIndex == -1) return@forEachIndexed
            val key = this.substring(index, endIndex+1)
            val operatorKey = key.replace("{","").replace("}","").split(":")[0]
            val dataKey = key.replace("{","").replace("}","").split(":")[1]
            list.add(Pair(operatorKey,dataKey))
        }
    }
    return list
}

fun String.replaceVar():String{
    val containsVar = this.containsVar()
    val varGetHandler = varGetHandler(containsVar)
    var temp = this
    varGetHandler.forEach {
        temp = temp.replace("{get:${it.first}}",it.second.toString())
    }
    return temp
}

fun getVariableList(varPairs:List<Pair<String,String>>,template: String,target: String):List<Variable>{
    var temp = template
    varPairs.forEach {
        temp = temp.replace("{${it.first}:${it.second}}","(?<${it.second}>.*?)")
    }
    val matcher = Pattern.compile(temp).matcher(target)
    while (matcher.find()){
        return varPairs.map {
            val value = matcher.group(it.second)
            Variable(it.second,value)
        }.toList()
    }
    return mutableListOf()
}


fun varGetHandler(varPairs:List<Pair<String,String>>):List<Pair<String,String>>{
    val templist = mutableListOf<Pair<String,String>>()
    varPairs.filter {
        it.first == "get"
    }.forEach { pair->
        list.find { it.key == pair.second }?.let {
            templist.add(Pair(it.key,it.value.toString()))
        }
    }
    return templist
}

fun varSetHandler(varPairs: List<Pair<String, String>>, template: String, target: String){
    varPairs.filter {
        it.first == "set"
    }.let {
        list.addAll(getVariableList(varPairs,template,target))
    }
}

val list = mutableListOf<Variable>()

fun main() {


//    println("template: $template")
//    println("target: $target")
//
//    val containsVar = template.containsVar()
//    varSetHandler(containsVar,template,target)
    val getTemplate = "取出变量测试{get:version} {get:num}"
//
//    val containsVar1 = getTemplate.containsVar()
//    val varGetHandler = varGetHandler(containsVar1)
//    varGetHandler.forEach {
//        println("${it.first} : ${it.second}")
//    }
//
//    println(getTemplate.replaceVar())
    val template = "当前版本为 {set:version} 版本，{set:num}金币"
    val target = "当前版本为 1-测试 版本，100金币"
    val variableHandler = com.dakuo.craftrecycle.rule.VariableHandler()
    val startTime = System.currentTimeMillis()
    for (i in 1..1000) {
        val containsVar2 = variableHandler.containsVar("$template,{set:i} 次")
        variableHandler.varSetHandler(containsVar2, "$template,{set:i} 次", "$target,$i 次")
    }

    for (i in 1..1000) {
        variableHandler.replaceVar("$getTemplate {get:i}")
    }

    variableHandler.list.forEach {
        println(it.value?.javaClass)
    }
    println((System.currentTimeMillis() - startTime).toString()+"ms.")

}
