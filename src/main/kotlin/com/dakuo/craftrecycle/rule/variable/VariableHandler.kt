package com.dakuo.craftrecycle.rule.variable

import java.util.concurrent.CopyOnWriteArrayList

class VariableHandler {

    val cache = CopyOnWriteArrayList<Variable>()

    val templates = CopyOnWriteArrayList<VariableTemplate>()

    fun insertTemplate(templateName:String,template:String){
        templates.add(VariableTemplate(templateName,template))
    }

    fun match(templateName: String,str:String):Boolean{
        return templates.find {
            it.templateName == templateName
        }?.match(str) ?: false
    }

    fun matchFromList(strList: List<String>):Boolean{
        return templates.filter {
            it.templateName.startsWith("lore-")
        }.all {
            strList.any { str->
                it.match(str)
            }
        }
    }

    fun getTemplatesFromList(strList:List<String>):List<Pair<Int,VariableTemplate>>{
        val list = mutableListOf<Pair<Int,VariableTemplate>>()
        strList.forEachIndexed{ index, s ->
            templates.find { template->
                template.match(s)
            }?.let {
                list.add(Pair(index,it))
            }
        }
        return list
    }

}