package com.dakuo.craftrecycle

import taboolib.common.platform.Plugin
import taboolib.common.platform.function.info
import taboolib.module.configuration.Config
import taboolib.module.configuration.Configuration

object CraftRecycle:Plugin() {


    @Config("config.yml",migrate = true,autoReload = true)
    lateinit var config: Configuration

    override fun onEnable() {
        info("CraftRecycle 启动!!!")
    }


}