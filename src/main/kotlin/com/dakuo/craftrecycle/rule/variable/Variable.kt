package com.dakuo.craftrecycle.rule.variable

data class Variable(val key: String){

    constructor(key: String,value:Any?) : this(key){
        setValue(value)
    }

    private var value:Any? = null


    fun setValue(v:Any?): Variable {
        value = v
        return this
    }

    fun getValue() = value

    fun hasValue() = value != null

    override fun toString(): String {
        return "{$key:${value}}"
    }
}

data class VariableTemplate(val templateName:String,val template: String){



    private val placeholderPattern = "\\{(?<function>[^:]+):(?<key>[^}]+)}".toRegex()

    val matchResults:Sequence<MatchResult> get() {
        return placeholderPattern.findAll(template)
    }

    val functionKeyPair:List<Pair<String,String>> get() {
        return matchResults.map {
            val groups = it.groups as MatchNamedGroupCollection
            Pair(groups["function"]!!.value,groups["key"]!!.value)
        }.toList()
    }

    val newTemplate:String get() {
        var temp = template
        matchResults.forEach {
            val groups = it.groups as MatchNamedGroupCollection
            temp = temp.replace(groups[0]!!.value,"(?<${groups["key"]?.value}>.*?)")
        }
        return temp
    }

    val newRegex:Regex = newTemplate.toRegex()

    fun match(string: String):Boolean{
        val findAll = newRegex.findAll(string)
        var temp = template
        functionKeyPair.forEach { (function,key)->
            findAll.filter {
                val groups = it.groups as MatchNamedGroupCollection
                groups[key] != null
            }.forEach {
                val groups = it.groups as MatchNamedGroupCollection
                temp = temp.replace("{${function}:${key}}",groups[key]!!.value)
            }
        }
        return temp == string
    }


}