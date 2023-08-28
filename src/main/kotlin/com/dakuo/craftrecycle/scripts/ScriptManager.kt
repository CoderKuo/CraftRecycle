package com.dakuo.craftrecycle.scripts

import org.bukkit.Bukkit
import taboolib.common.platform.function.getDataFolder
import java.io.File

object ScriptManager {

    private fun check(clazz: String): Boolean {
        return try {
            Class.forName(clazz)
            true
        } catch (error: Throwable) {
            false
        }
    }

    val nashornHooker:NashornHooker =
        when {
            // jdk自带nashorn
            check("jdk.nashorn.api.scripting.NashornScriptEngineFactory") -> LegacyNashornHookerImpl()
            // 主动下载nashorn
            else -> NashornHookerImpl()
        }


    val scriptEngine = nashornHooker.getNashornEngine()

    val compiledScripts = HashMap<String,CompiledScript>()

    init {
        loadScripts()
    }

    /**
     * 加载全部脚本
     */
    private fun loadScripts() {
        val dir = File(getDataFolder(), "scripts").listFiles() ?: arrayOf()
        for (file in dir) {
            val fileName = file.path.replace("plugins${File.separator}CraftRecycle${File.separator}scripts${File.separator}", "")
            try {
                compiledScripts[fileName] = CompiledScript(file)
            } catch (error: Throwable) {
                error.printStackTrace()
            }
        }
    }

}