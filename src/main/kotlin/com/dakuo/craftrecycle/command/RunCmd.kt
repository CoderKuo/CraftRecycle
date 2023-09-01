package com.dakuo.craftrecycle.command

import com.dakuo.craftrecycle.CraftRecycle
import com.dakuo.craftrecycle.language.sendLang
import com.dakuo.craftrecycle.scripts.CompiledScript
import com.dakuo.craftrecycle.scripts.NashornHooker
import com.dakuo.craftrecycle.scripts.ScriptExpansion
import com.dakuo.craftrecycle.scripts.ScriptManager
import com.dakuo.craftrecycle.ui.UILoader
import org.bukkit.Bukkit
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.function.getProxyPlayer
import taboolib.common.util.asList
import taboolib.common.util.subList
import taboolib.module.lang.sendErrorMessage
import taboolib.platform.type.BukkitPlayer
import taboolib.platform.util.onlinePlayers
import java.io.InputStreamReader
import javax.script.ScriptEngine

object RunCmd {

    fun loadLib(engine:ScriptEngine){
        CraftRecycle.plugin.getResource("lib.js")?.use { input->
            InputStreamReader(input,"UTF-8").use { reader->
                engine.eval(reader)
            }
        }
    }

    val run = subCommand {
        dynamic("run") {
            dynamic("function") {

                execute<ProxyCommandSender> { sender, context, argument ->

                    val map = HashMap<String,Any>()
                    map["player"] = (sender as BukkitPlayer).player
                    ScriptManager.compiledScripts.getOrElse(context["run"]) {
                        ScriptExpansion(
                            """
                        """.trimIndent()
                        )
                    }.let {
                        loadLib(it.scriptEngine)
                        it.invoke(context["function"],map)
                    }
                }
                dynamic {
                    execute<ProxyCommandSender> { sender, context, argument ->
                        val map = HashMap<String,Any>()
                        map["player"] = (sender as BukkitPlayer).player
                        ScriptManager.compiledScripts.getOrElse(context["run"]) {
                            ScriptExpansion(
                                """
                        """.trimIndent()
                            )
                        }.let {
                            loadLib(it.scriptEngine)
                            it.invoke(context["function"],map,argument.split(" "))
                        }
                    }
                }
            }
        }
    }
}