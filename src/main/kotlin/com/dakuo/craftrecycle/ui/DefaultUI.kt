package com.dakuo.craftrecycle.ui

import com.dakuo.craftrecycle.common.AName
import com.dakuo.craftrecycle.ui.slotmode.Mode
import com.dakuo.craftrecycle.ui.slotmode.None
import net.minecraft.world.item.Items
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import taboolib.common.io.getInstance
import taboolib.common.io.runningClasses
import taboolib.library.xseries.XMaterial
import taboolib.module.configuration.Configuration
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

    private val title = config.getString("title"," ")!!

    private val slots: CopyOnWriteArrayList<List<Char>> = CopyOnWriteArrayList(config.getStringList("slots").map { it.toCharArray().toList() })

    private val settings:List<DefaultSetting> by lazy {
        config.getConfigurationSection("setting")!!.getKeys(false).map {
            val section = config.getConfigurationSection("setting.$it")!!
            val uiSetting = DefaultSetting(it.toCharArray().first(),
                section.getString("mode","none")!!,
                section.getString("item.material","AIR")!!,
                section.getStringColored("item.name")!!,
                section.getStringListColored("item.lore"),
                section.getInt("item.data"),
                section.getInt("item.customModel")
            )
            uiSetting
        }
    }

    open fun toInventory():Inventory{
        return buildMenu<Basic>(title) {
            this.slots = this@DefaultUI.slots
            this.items = getItems()
            onClick(lock = false){ e->
                settings.find { it.slot == e.slot }?.getSlotMode()?.clicked?.click(e)
            }

        }
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



}

data class DefaultSetting(val slot:Char,
                     val mode:String = "none",
                     val itemMaterial:String = "AIR",
                     val itemName:String = " ",
                     val itemLore:List<String> = arrayListOf(),
                     val itemData:Int = 0,
                     val itemCustomModel:Int = 0){
    fun getSlotMode():Mode{
        return runningClasses.filter {
            Mode::class.java.isAssignableFrom(it) && it != Mode::class.java
        }.map {
            it.getInstance() as? Mode
        }.filter(Objects::nonNull).find{
            it!!.name.contains(mode)
        } ?: None
    }



}
