package com.dakuo.craftrecycle.ui

import taboolib.common.LifeCycle
import taboolib.common.io.runningClasses
import taboolib.common.platform.Awake
import taboolib.common.platform.function.info
import taboolib.module.configuration.Configuration
import taboolib.platform.BukkitPlugin
import java.io.File
import java.io.FileFilter

object UILoader {

    val uiList = mutableListOf<Pair<String,DefaultUI>>()

    @Awake(LifeCycle.ENABLE)
    fun loadAll(){
        uiList.clear()
        val guiFile = File(BukkitPlugin.getInstance().dataFolder, "/gui/")
        if (!guiFile.exists()){
            guiFile.mkdir()
            BukkitPlugin.getInstance().saveResource("gui/ExampleRecycle.yml",false)
            BukkitPlugin.getInstance().saveResource("gui/ExampleBreak.yml",false)
        }

        guiFile.listFiles(FileFilter {
            it.name.endsWith(".yml")
        }).forEach {
            val config = Configuration.loadFromFile(it)

            val mode = config.getString("mode","default")!!

            uiList.add(Pair(it.name.replace(".yml",""),toUIObject(mode,config)))

            info("UI-FILE ${it.name} 配置文件已加载")
        }

    }

    fun toUIObject(mode:String,config:Configuration):DefaultUI{
        return runningClasses.filter {
            DefaultUI::class.java.isAssignableFrom(it)
        }.find {
            it.getAnnotation(UI::class.java).name == mode
        }?.let {
            return (it.getConstructor(Configuration::class.java).newInstance(config) as DefaultUI) ?: DefaultUI(config)
        }!!
    }

}