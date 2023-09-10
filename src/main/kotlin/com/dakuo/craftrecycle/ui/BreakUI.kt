package com.dakuo.craftrecycle.ui

import com.dakuo.craftrecycle.common.AName
import taboolib.common5.cchar
import taboolib.module.configuration.Configuration


@AName("break")
class BreakUI(config: Configuration) :DefaultUI(config) {


    fun updateVar(craftRecycleInventory: CraftRecycleInventory){
        val get = this.param["break_slot"]
        val slots = basic.getSlots(get.cchar)
        val inventory = craftRecycleInventory.inventory
        slots.forEach {
            val item = inventory.getItem(it)

        }
    }


}