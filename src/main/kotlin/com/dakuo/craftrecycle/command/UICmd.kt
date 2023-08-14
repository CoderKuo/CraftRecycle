package com.dakuo.craftrecycle.command

import com.dakuo.craftrecycle.language.sendLang
import com.dakuo.craftrecycle.ui.UILoader
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.ProxyPlayer
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.function.getProxyPlayer
import taboolib.module.lang.sendErrorMessage
import taboolib.platform.type.BukkitPlayer
import taboolib.platform.util.onlinePlayers

object UICmd {

    val open = subCommand {
        dynamic("ui") {
            suggestion<ProxyCommandSender>{_,_->
                UILoader.uiList.map { it.first }
            }
            dynamic("player"){
                suggestion<ProxyCommandSender>{ _,_->
                    onlinePlayers.map { it.name }
                }

                execute<ProxyCommandSender>{ sender, context, argument ->
                    val pair = UILoader.uiList.find {
                        it.first == context["ui"]
                    }
                    if (pair == null){
                        sender.sendErrorMessage("没有找到名称为${context.get("ui")}的菜单")
                        return@execute
                    }
                    pair.second.toInventory().let {
                        val player = (getProxyPlayer(context["player"]) as BukkitPlayer).player
                        player.openInventory(it)
                        sender.sendLang("cmd-openUI", mapOf(Pair("{player}", player.name), Pair("{ui}", context["ui"])))
                    }

                }
            }
        }
    }

}