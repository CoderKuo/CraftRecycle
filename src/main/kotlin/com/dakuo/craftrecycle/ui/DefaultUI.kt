package com.dakuo.craftrecycle.ui

import taboolib.library.xseries.XMaterial
import taboolib.module.configuration.Configuration
import taboolib.module.configuration.util.getStringColored
import taboolib.module.configuration.util.getStringListColored
import taboolib.module.ui.Menu
import taboolib.module.ui.buildMenu
import taboolib.module.ui.type.Basic
import taboolib.platform.util.buildItem

class DefaultUI(val config:Configuration) {

    private val title = config.getString("title"," ")!!

    private val slots: ArrayList<List<Char>> = config.getStringList("slots").map { it.toCharArray().toList() } as ArrayList<List<Char>>

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

    open fun toInventory(){
        buildMenu<Basic> {
            this.slots = this@DefaultUI.slots
            this.items =


        }
    }


}

data class DefaultSetting(val slot:Char,
                     val mode:String = "none",
                     val itemMaterial:String = "AIR",
                     val itemName:String = " ",
                     val itemLore:List<String> = arrayListOf(),
                     val itemData:Int = 0,
                     val itemCustomModel:Int = 0){
    fun toItemStack(){
        buildItem(XMaterial.matchXMaterial(itemMaterial).get()){
            this.name = itemName;
            this.lore.addAll(itemLore);
            this.damage = itemData;
            this.customModelData = itemCustomModel;
        }
    }

}
