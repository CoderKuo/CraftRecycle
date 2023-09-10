package com.dakuo.craftrecycle.scripts

import com.dakuo.craftrecycle.CraftRecycle
import org.bukkit.Bukkit
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.function.getDataFolder
import java.io.File
import java.io.InputStreamReader

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
        compiledScripts.clear()
        val dirFile = File(getDataFolder(), "scripts")
        if (!dirFile.exists()){
            dirFile.mkdir()
        }
        val dir = dirFile.listFiles() ?: arrayOf()
        for (file in dir) {
            val fileName = file.path.replace("plugins${File.separator}CraftRecycle${File.separator}scripts${File.separator}", "")
            try {
                compiledScripts[fileName] = CompiledScript(file)
            } catch (error: Throwable) {
                error.printStackTrace()
            }
        }
    }

    @Awake(LifeCycle.ENABLE)
    private fun enable(){
        val enableList = CraftRecycle.config.getStringList("js.enable")
        enableList.forEach {
            val file = File(getDataFolder(), it)
            val inputStreamReader = InputStreamReader(file.inputStream(), "UTF-8")
            nashornHooker.getNashornEngine().eval(inputStreamReader)
        }
    }

}