package com.dakuo.craftrecycle.rule

data class Variable<T : Any>(val key: String, var value: T? = null)
