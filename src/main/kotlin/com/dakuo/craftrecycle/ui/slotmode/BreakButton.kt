package com.dakuo.craftrecycle.ui.slotmode

import com.dakuo.craftrecycle.ui.clicked.Clicked
import taboolib.module.ui.ClickEvent

object BreakButton:Mode {


    override val name: List<String> = listOf("BreakButton")

    override val clicked: Clicked = object : Clicked {
        override fun click(clickEvent: ClickEvent) {

        }
    }


}