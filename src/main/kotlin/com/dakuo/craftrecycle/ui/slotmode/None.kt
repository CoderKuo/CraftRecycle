package com.dakuo.craftrecycle.ui.slotmode

import com.dakuo.craftrecycle.ui.clicked.Clicked
import taboolib.module.ui.ClickEvent

object None:Mode {

    override val name = listOf("none","None")

    override val clicked = object : Clicked {
        override fun click(clickEvent: ClickEvent) {
            clickEvent.isCancelled = true
        }
    }

}