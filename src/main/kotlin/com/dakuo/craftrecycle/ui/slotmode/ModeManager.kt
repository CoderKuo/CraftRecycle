package com.dakuo.craftrecycle.ui.slotmode

import java.util.concurrent.CopyOnWriteArrayList

object ModeManager {

    //
    val slotModes:CopyOnWriteArrayList<Mode> = CopyOnWriteArrayList()

    init {
        registerSlotMode(Break)
    }

    fun registerSlotMode(mode: Mode){
        slotModes.add(mode)
    }

}