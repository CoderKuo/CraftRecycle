package com.dakuo.craftrecycle.language

import com.dakuo.craftrecycle.CraftRecycle
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.function.info
import taboolib.module.chat.colored
import taboolib.module.configuration.Configuration
import taboolib.platform.BukkitPlugin
import java.io.File

object Lang{

    @JvmStatic
    lateinit var lang:Configuration

    @Awake(LifeCycle.ENABLE)
    fun loadLang(){
        val lang = CraftRecycle.config.getString("lang")
        val langFile = File(BukkitPlugin.getInstance().dataFolder, "lang/${lang}.yml")
        if (!langFile.exists()){
            if (lang == "zh_CN"){
                BukkitPlugin.getInstance().saveResource("lang/zh_CN.yml",false)
            }else{
                error("指定的 ${lang} 语言不存在,请检查lang目录下的文件")
            }
        }
        val loadFromFile = Configuration.loadFromFile(langFile)
        info("语言文件加载成功！ 当前语言为：${lang}")
        this.lang = loadFromFile
    }

}

fun ProxyCommandSender.sendLang(key:String, map: Map<String,String>){
    Lang.lang.getString(key)?.let { str ->
        if (str!=""){
            var msg = str.colored()
            map.forEach {
                msg = msg.replace(it.key,it.value)
            }
            this.sendMessage(msg)
        }
    }
}
