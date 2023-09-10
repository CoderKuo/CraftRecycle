package com.dakuo.craftrecycle.rule

import com.dakuo.craftrecycle.common.AName
import com.dakuo.craftrecycle.rule.match.IMatch
import com.dakuo.craftrecycle.rule.reward.IReward
import com.dakuo.craftrecycle.rule.variable.Variable
import com.dakuo.craftrecycle.rule.variable.VariableHandler
import org.bukkit.Bukkit
import org.bukkit.inventory.ItemStack
import taboolib.common.io.runningClasses
import taboolib.library.configuration.ConfigurationSection
import taboolib.module.configuration.Configuration
import taboolib.module.nms.getName
import taboolib.platform.util.hasLore

open class Rule(config:Configuration, name:String) {

    companion object{
        @JvmStatic
        fun toMatchRule(section:ConfigurationSection):List<IMatch>{
            val matches = mutableListOf<IMatch>()
            section.getKeys(false).forEach { key->
                matches.add(runningClasses.find {
                    IMatch::class.java.isAssignableFrom(it) && it != IMatch::class.java && it.getAnnotation(AName::class.java).name.contains(key)
                }?.getConstructor(ConfigurationSection::class.java)?.newInstance(section) as IMatch)
            }
            return matches
        }
    }

    protected open val matchRules:List<IMatch> = config.getConfigurationSection("match").let {
        return@let toMatchRule(it!!)
    }

    protected open val rewardRules:List<RewardRule> = config.getConfigurationSection("reward").let {
        RewardRule.toRewardRules(it as Configuration)
    }


}

data class RewardRule(val name:String?,val matchMode:String?,val rewards:List<IReward>){
    companion object{

        @JvmStatic
        fun toRewardRules(config:Configuration):List<RewardRule>{
            return config.getKeys(false).map {
                val matchMode = config.getString("$it.matchMode","any")!!
                val rewards = toRewards(config)
                RewardRule(it,matchMode,rewards)
            }
        }

        @JvmStatic
        fun toRewards(config:ConfigurationSection):List<IReward>{
            val rewards = mutableListOf<IReward>()
            config.getKeys(false).forEach {key->
                if (key != "matchMode"){
                    rewards.add(runningClasses.find {
                        IReward::class.java.isAssignableFrom(it) && it != IReward::class.java && it.getAnnotation(AName::class.java).name.contains(key)
                    }?.getConstructor(ConfigurationSection::class.java)?.newInstance(config) as IReward)
                }
            }
            return rewards
        }
    }

}