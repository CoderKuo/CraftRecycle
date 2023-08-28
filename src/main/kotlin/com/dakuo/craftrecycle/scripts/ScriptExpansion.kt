package com.dakuo.craftrecycle.scripts

import java.io.File
import java.io.Reader
import com.dakuo.craftrecycle.scripts.ScriptManager.nashornHooker

class ScriptExpansion:CompiledScript {

    /**
     * 构建JavaScript脚本扩展
     *
     * @property reader js脚本文件
     * @constructor JavaScript脚本扩展
     */
    constructor(reader: Reader) : super(reader)

    /**
     * 构建JavaScript脚本扩展
     *
     * @property file js脚本文件
     * @constructor JavaScript脚本扩展
     */
    constructor(file: File) : super(file)

    /**
     * 构建JavaScript脚本扩展
     *
     * @property script js脚本文本
     * @constructor JavaScript脚本扩展
     */
    constructor(script: String) : super(script)

    override fun loadLib() {
        scriptEngine.eval(
            """
                const Bukkit = Packages.org.bukkit.Bukkit

                
                const plugin = Packages.com.dakuo.craftrecycle.CraftRecycle.INSTANCE.plugin
                const pluginManager = Bukkit.getPluginManager()
                const scheduler = Bukkit.getScheduler()
                
                function sync(task) {
                    if (Bukkit.isPrimaryThread()) {
                        task()
                    } else {
                        scheduler.callSyncMethod(plugin, task)
                    }
                }
                
                function async(task) {
                    scheduler["runTaskAsynchronously(Plugin,Runnable)"](plugin, task)
                }
            """.trimIndent()
        )
    }

    /**
     * 执行指定函数
     *
     * @param function 函数名
     * @param expansionName 脚本名称(默认为unnamed)
     */
    fun run(function: String, expansionName: String = "unnamed") {
        if (nashornHooker.isFunction(scriptEngine, function)) {
            try {
                invoke(function, null)
            } catch (error: Throwable) {
                error.printStackTrace()
            }
        }
    }

}