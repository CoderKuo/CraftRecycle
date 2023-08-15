package com.dakuo.craftrecycle.rule

import java.util.regex.Pattern

class VariableHandler {

    val list = arrayListOf<Variable<*>>()



    private val placeholderPattern = "\\{([^:]+):([^}]+)}".toRegex()

    fun containsVar(string: String): List<Pair<String, String>> {
        val list = mutableListOf<Pair<String, String>>()
        val matches = placeholderPattern.findAll(string)

        for (match in matches) {
            val (operatorKey, dataKey) = match.destructured
            list.add(Pair(operatorKey, dataKey))
        }

        return list
    }

    fun replaceVar(string: String):String{
        val varGetHandler = varGetHandler(containsVar(string))
        var temp = string
        for ((operatorKey, replacement) in varGetHandler) {
            temp = temp.replace("{get:$operatorKey}", replacement)
        }
        return temp
    }

    fun getVariableList(varPairs:List<Pair<String,String>>,template: String,target: String):List<Variable<*>>{
        var regexPattern = template
        varPairs.forEach { (operatorKey, dataKey) ->
            regexPattern = regexPattern.replace("{$operatorKey:$dataKey}", "(?<$dataKey>.*?)")
        }
        val matcher = Pattern.compile(regexPattern).matcher(target)
        while (matcher.find()){
            return varPairs.map {
                val value = matcher.group(it.second)

                val variable: Variable<*> = when {
                    value.toIntOrNull() != null -> Variable(it.second, value.toInt())
                    value.toDoubleOrNull() != null -> Variable(it.second, value.toDouble())
                    value.toBooleanStrictOrNull() != null -> Variable(it.second, value.toBoolean())
                    else -> Variable(it.second, value)
                }
                variable
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

    fun varSetHandler(varPairs: List<Pair<String, String>>, template: String, target: String) {
        val newVariables = getVariableList(varPairs, template, target)
        val existingKeys = list.map { it.key }

        newVariables.forEach { newVar ->
            val existingIndex = existingKeys.indexOf(newVar.key)

            if (existingIndex != -1) {
                list[existingIndex] = newVar
            } else {
                list.add(newVar)
            }
        }
    }


}