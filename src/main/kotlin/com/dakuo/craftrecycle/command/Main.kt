package com.dakuo.craftrecycle.command

import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.common.platform.command.subCommand
import taboolib.expansion.createHelper

@CommandHeader("CraftRecycle", aliases = ["cr","fenjie","fj","recycle"], permission = "CraftRecycle.access")
object Main {

    @CommandBody
    val help = subCommand {
        execute<ProxyCommandSender>{ sender, context, argument ->
            createHelper()
        }
    }

    @CommandBody(permission = "CraftRecycle.open")
    val open = UICmd.open


}