package com.dakuo.craftrecycle.ui.slotmode

import com.dakuo.craftrecycle.ui.clicked.Clicked
import taboolib.module.ui.ClickEvent

object Break:Mode {

    override val name: List<String>  = listOf("break")


    override val clicked: Clicked = object : Clicked {
            override fun click(clickEvent: ClickEvent) {

            }
        }

    override val tag: String
        get() = "break"
}