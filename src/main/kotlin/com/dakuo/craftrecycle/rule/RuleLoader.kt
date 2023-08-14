package com.dakuo.craftrecycle.rule

import com.dakuo.craftrecycle.rule.reward.IReward
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.function.info
import taboolib.module.configuration.Configuration
import taboolib.platform.BukkitPlugin
import java.io.File
import java.io.FileFilter

object RuleLoader {

    val rules = mutableListOf<Rule>()

    @Awake(LifeCycle.ENABLE)
    fun loadAll(){
        rules.clear()
        val ruleFile = File(BukkitPlugin.getInstance().dataFolder, "/rule/")
        if (!ruleFile.exists()){
            ruleFile.mkdir()
            BukkitPlugin.getInstance().saveResource("rule/ExampleBreakRule.yml.yml",false)
            BukkitPlugin.getInstance().saveResource("rule/ExampleRecycleRule.yml.yml",false)
        }


        ruleFile.listFiles(FileFilter {
            it.name.endsWith(".yml")
        }).forEach {
            val config = Configuration.loadFromFile(it)

            config.getKeys(false).forEach { key->
                rules.add(Rule(config.getConfigurationSection(key) as Configuration,key))
                info("RULE ${it}>${key} 规则配置已加载")
            }

            info("RULE-FILE ${it.name} 配置文件已加载")
        }

    }

}