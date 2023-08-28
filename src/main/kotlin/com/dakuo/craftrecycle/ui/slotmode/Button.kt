package com.dakuo.craftrecycle.ui.slotmode

import com.dakuo.craftrecycle.ui.slotmode.clicked.Clicked
import taboolib.module.ui.ClickEvent

object Button:Mode {


    override val name: List<String> = listOf("button","Button")

    override val clicked: Clicked = object : Clicked {
        override fun click(clickEvent: ClickEvent) {
            clickEvent.isCancelled = true
            clickEvent.getItems(' ').plus(clickEvent.getItems('o')).forEach {

            }
        }
    }





}