package com.dakuo.craftrecycle.scripts.utils

import org.bukkit.entity.Player
import taboolib.common.platform.function.getProxyPlayer
import taboolib.platform.type.BukkitPlayer

object PlayerUtils {
    fun getPlayer(name:String):Player{
        return (getProxyPlayer(name) as BukkitPlayer).player
    }
}