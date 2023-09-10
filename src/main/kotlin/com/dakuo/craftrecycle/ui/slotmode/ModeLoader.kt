package com.dakuo.craftrecycle.ui.slotmode

import taboolib.common.LifeCycle
import taboolib.common.io.getInstance
import taboolib.common.io.runningClasses
import taboolib.common.platform.Awake
import taboolib.common.platform.function.info
import taboolib.common.platform.function.submitAsync
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList

object ModeLoader {

    val modes = CopyOnWriteArrayList<Mode>()

    @Awake(LifeCycle.ENABLE)
    fun load(){
        runningClasses.filter {
            Mode::class.java.isAssignableFrom(it) && it != Mode::class.java
        }.map {
            it.getInstance() as? Mode
        }.filter(Objects::nonNull).forEach {
            modes.add(it!!)
        }
    }

    fun register(mode: Mode){
        submitAsync {
            modes.any {
                it.name.containsAll(mode.name)
            }.let {
                if (it){
                    error(mode.javaClass.name+"注册失败，name已经存在")
                }else{
                    modes.add(mode)
                    info("来自JavaScript的Mode $mode 注册成功！")
                }
            }
        }
    }

}