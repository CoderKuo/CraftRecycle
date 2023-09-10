package com.dakuo.craftrecycle.ui.slotmode

import com.dakuo.craftrecycle.ui.clicked.Clicked
import java.util.concurrent.ConcurrentHashMap

interface Mode {

    val name:List<String>

    val clicked: Clicked

    val param: ConcurrentHashMap<String, Any>
        get() = ConcurrentHashMap<String,Any>()

    val tag:String get() = "none"

    fun addAllParam(map: Map<String,Any>){
        param.putAll(map)
    }


}