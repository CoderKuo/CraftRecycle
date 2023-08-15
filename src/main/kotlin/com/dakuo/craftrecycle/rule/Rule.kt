package com.dakuo.craftrecycle.rule

import com.dakuo.craftrecycle.rule.reward.IReward
import com.dakuo.craftrecycle.rule.reward.Reward
import com.dakuo.craftrecycle.rule.variable.VariableHandler
import org.bukkit.inventory.ItemStack
import taboolib.common.io.runningClasses
import taboolib.module.configuration.Configuration
import taboolib.module.nms.getName
import taboolib.platform.util.hasLore

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
            val name = map["name"].toString()
            val lore = map["lore"] as List<String>
            val nbt = map["nbt"] as Map<String,String>?
            val matchRule = MatchRule(name, lore, nbt)
            val handler = matchRule.variableHandler
            handler.insertTemplate("name",name)
            lore.forEachIndexed { index, s ->
                handler.insertTemplate("lore-$index",s)
            }
            nbt?.forEach { (k, v) ->
                handler.insertTemplate(k,v)
            }
            return matchRule
        }
    }

    val variableHandler: VariableHandler = VariableHandler()

    fun matchName(item:ItemStack):Boolean{
        if (name.isNullOrEmpty()) return true
        val itemName = item.getName()
        val template = variableHandler.templates.find {
            it.templateName == "name"
        }
        return template?.match(itemName) ?: (itemName == name)
    }

    fun matchLore(item:ItemStack):Boolean{
        if (lore.isNullOrEmpty()) return true
        val itemLore = if (item.hasLore()) item.itemMeta!!.lore else return false
        return variableHandler.matchFromList(itemLore!!)
    }

}