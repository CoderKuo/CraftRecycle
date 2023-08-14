package com.dakuo.craftrecycle.rule

import com.dakuo.craftrecycle.rule.reward.IReward
import com.dakuo.craftrecycle.rule.reward.Reward
import org.bukkit.inventory.ItemStack
import taboolib.common.io.runningClasses
import taboolib.module.configuration.Configuration
import taboolib.platform.util.hasName

open class Rule(config:Configuration, name:String) {

    protected open val matchRules:List<MatchRule> = config.getMapList("match").map {
        MatchRule.toMatchRule(it)
    }

    protected open val rewardRules:List<RewardRule> = config.getConfigurationSection("reward").let {
        RewardRule.toRewardRules(it as Configuration)
    }


    fun match(item:ItemStack):IReward{
        matchRules.filter {
            TODO()
        }
        TODO()
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
        fun toRewards(config:Configuration):List<IReward>{
            val rewards = mutableListOf<IReward>()
            config.getKeys(false).forEach {key->
                if (key != "matchMode"){
                    rewards.add(runningClasses.find {
                        IReward::class.java.isAssignableFrom(it) && it != IReward::class.java && it.getAnnotation(Reward::class.java).name == key
                    }?.getConstructor(Configuration::class.java)?.newInstance(config) as IReward)
                }
            }
            return rewards
        }
    }




}
data class MatchRule(val name:String?,val lore:List<String>?,val nbt:Map<String,String>?){

    companion object{
        @JvmStatic
        fun toMatchRule(map: Map<*, *>):MatchRule{
            return MatchRule(map["name"].toString(),map["lore"] as List<String>, map["nbt"] as Map<String, String>?)
        }
    }


    fun matchName(item:ItemStack){

    }

    fun String.containsVar():Variable{
        generateRegexPattern(this)
        TODO()
    }

    fun generateRegexPattern(template: String): String {
        val placeholders = "\\{set:(.*?)}".toRegex().findAll(template).map { it.groupValues[1] }

        var regexPattern = template
        for (placeholder in placeholders) {
            val escapedPlaceholder = Regex.escape(placeholder)
            regexPattern = regexPattern.replace("{set:$placeholder}", "(?<$escapedPlaceholder>.+?)")
        }

        return regexPattern
    }



}