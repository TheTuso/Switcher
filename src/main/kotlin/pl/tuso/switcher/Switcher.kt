package pl.tuso.switcher

import com.google.inject.Inject
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.proxy.ProxyServer
import org.slf4j.Logger

@Plugin(
    id = "switcher",
    name = "Switcher",
    version = "1.0",
    url = "https://github.com/TheTuso/Switcher",
    description = "A Simple Velocity plugin that allows players to join from multiple game profiles using a domain",
    authors = ["tuso"])
class Switcher @Inject constructor(val proxyServer: ProxyServer, val logger: Logger) {
    init {
        logger.info("Heyo! Enabling Switcher...")
    }

    @Subscribe
    fun onProxyInitialization(initializeEvent: ProxyInitializeEvent) {

    }
}