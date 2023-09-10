package com.dakuo.craftrecycle.ui

import com.dakuo.craftrecycle.rule.variable.VariableHandler
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory

class CraftRecycleInventory(val player: Player, val ui: DefaultUI) {

    lateinit var inventory: Inventory

    val varHandler:VariableHandler = VariableHandler()

    val basic = ui.getBasic()

    constructor(player: Player,ui:String) : this(player,UILoader.uiList.find { it.first == ui }?.second ?: error("没有找到名为 $ui 的菜单")){

    }

    init {

    }

    fun open(){
        player.openInventory(getBukkitInventory())
    }

    fun getBukkitInventory():Inventory{
        if (!::inventory.isInitialized){
            inventory = ui.toInventory()
        }
        return inventory
    }

    fun isOpen():Boolean{
        return inventory.viewers.contains(player)
    }






}