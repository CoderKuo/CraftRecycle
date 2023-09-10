package com.dakuo.craftrecycle.ui

import com.dakuo.craftrecycle.common.AName
import com.dakuo.craftrecycle.rule.variable.Variable
import com.dakuo.craftrecycle.rule.variable.VariableHandler
import com.dakuo.craftrecycle.ui.clicked.Clicked
import com.dakuo.craftrecycle.ui.slotmode.Mode
import com.dakuo.craftrecycle.ui.slotmode.ModeLoader
import com.dakuo.craftrecycle.ui.slotmode.None
import net.minecraft.world.item.Items
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import taboolib.common.io.getInstance
import taboolib.common.io.runningClasses
import taboolib.library.xseries.XMaterial
import taboolib.module.configuration.Configuration
import taboolib.module.configuration.util.getMap
import taboolib.module.configuration.util.getStringColored
import taboolib.module.configuration.util.getStringListColored
import taboolib.module.ui.Menu
import taboolib.module.ui.buildMenu
import taboolib.module.ui.type.Basic
import taboolib.platform.util.buildItem
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


@AName("default")
open class DefaultUI(val config:Configuration) {

    protected val title = config.getString("title"," ")!!

    protected val param:ConcurrentHashMap<String,Any> = ConcurrentHashMap()

    protected val clickedInUI:MutableList<Clicked> = mutableListOf()

    protected val slots: CopyOnWriteArrayList<List<Char>> = CopyOnWriteArrayList(config.getStringList("slots").map { it.toCharArray().toList() })

    protected val basic:Basic by lazy{
        getBasic()
    }

    protected val settings:List<DefaultSetting> by lazy {
        config.getConfigurationSection("setting")!!.getKeys(false).map {
            val section = config.getConfigurationSection("setting.$it")!!
            val uiSetting = DefaultSetting(it.toCharArray().first(),
                section.getString("mode","none")!!,
                section.getMap("param"),
                section.getString("item.material","AIR")!!,
                section.getStringColored("item.name")!!,
                section.getStringListColored("item.lore"),
                section.getInt("item.data"),
                section.getInt("item.customModel")
            )
            uiSetting
        }
    }

    fun getBasic():Basic{
        val basic = Basic(title)
        basic.slots = this.slots
        basic.items = getItems()
        basic.onClick { e->
            settings.find { it.slot == e.slot }?.getSlotMode()?.clicked?.click(e)
        }
        return basic
    }

    open fun toInventory():Inventory{
        return basic.build()
    }

    private fun getItems():ConcurrentHashMap<Char,ItemStack>{
        val items = mutableMapOf<Char,ItemStack>()
        settings.forEach {
            items[it.slot] = buildItem(XMaterial.matchXMaterial(it.itemMaterial).get()){
                this.name = it.itemName
                this.lore.addAll(it.itemLore)
                this.damage = it.itemData
                this.customModelData = it.itemCustomModel
            }
        }
        return ConcurrentHashMap(items)

    }

    fun addAllParam(param:Map<String,Any>){
        this.param.putAll(param)
    }

    fun insertClicked(clicked: Clicked){
        clickedInUI.add(clicked)
    }


}

data class DefaultSetting(val slot:Char,
                     val mode:String = "none",
                     val param:Map<String,Any>?,
                     val itemMaterial:String = "AIR",
                     val itemName:String = " ",
                     val itemLore:List<String> = arrayListOf(),
                     val itemData:Int = 0,
                     val itemCustomModel:Int = 0){
    fun getSlotMode():Mode{
        return (ModeLoader.modes.find {
            it.name.contains(mode)
        } ?: None).apply {
            this.addAllParam(param)
        }
    }



}
