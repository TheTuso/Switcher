package pl.tuso.switcher

import com.google.inject.Inject
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.plugin.Dependency
import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.proxy.ProxyServer
import net.luckperms.api.LuckPerms
import net.luckperms.api.LuckPermsProvider
import org.slf4j.Logger

@Plugin(
    id = "switcher",
    name = "Switcher",
    version = "1.0",
    url = "https://github.com/TheTuso/Switcher",
    description = "A simple Velocity plugin that allows players to join from multiple game profiles using a domain",
    authors = ["tuso"],
    dependencies = [Dependency(id = "luckperms")])
class Switcher @Inject constructor(private val proxyServer: ProxyServer, val logger: Logger) {
    lateinit var luckPerms: LuckPerms

    init {
        logger.info("Heyo! Enabling Switcher...")
    }

    @Subscribe
    fun onProxyInitialization(initializeEvent: ProxyInitializeEvent) {
        if (proxyServer.pluginManager.isLoaded("luckperms")) luckPerms = LuckPermsProvider.get()
        proxyServer.eventManager.register(this, SwitchRequestListener(this))
    }
}