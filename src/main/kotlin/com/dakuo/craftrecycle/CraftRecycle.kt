package com.dakuo.craftrecycle

import taboolib.common.platform.Plugin
import taboolib.common.platform.function.info
import taboolib.module.configuration.Config
import taboolib.module.configuration.Configuration
import taboolib.platform.BukkitPlugin

object CraftRecycle:Plugin() {


    @Config("config.yml",migrate = true,autoReload = true)
    lateinit var config: Configuration

    val plugin by lazy { BukkitPlugin.getInstance() }

    override fun onEnable() {
        info("CraftRecycle 启动!!!")
    }


}