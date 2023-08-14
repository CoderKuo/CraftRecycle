package com.dakuo.craftrecycle.rule

import java.util.regex.Pattern

class VariableHandler {

    val list = arrayListOf<Variable>()

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

}