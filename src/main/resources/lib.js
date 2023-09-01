const Bukkit = Packages.org.bukkit.Bukkit
const plugin = Packages.com.dakuo.craftrecycle.CraftRecycle.INSTANCE.plugin
const pluginManager = Bukkit.getPluginManager()
const scheduler = Bukkit.getScheduler()
const playerUtils = Packages.com.dakuo.craftrecycle.scripts.utils.PlayerUtils.INSTANCE

function sync(task) {
    if (Bukkit.isPrimaryThread()) {
        task()
    } else {
        scheduler.callSyncMethod(plugin, task)
    }
}

function async(task) {
    scheduler["runTaskAsynchronously(Plugin,Runnable)"](plugin, task)
}