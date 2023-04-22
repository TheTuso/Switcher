package pl.tuso.switcher

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.player.GameProfileRequestEvent
import com.velocitypowered.api.util.GameProfile
import java.net.InetSocketAddress
import java.net.URL

class SwitchRequestListener(private val switcher: Switcher) {
    private val gson = Gson()
    private val prefix = "switch-"

    @Subscribe
    fun onGameProfileRequest(requestEvent: GameProfileRequestEvent) {
        val virtualHost = InetSocketAddress::getHostString.let { requestEvent.connection.virtualHost.map(it).orElse("").lowercase() }
        val username = getUsernameFromHost(virtualHost) ?: return

        if (username == requestEvent.username) {
            switcher.logger.warn("${requestEvent.username} tried to connect through the switcher with the same username!")
            return
        }

        if (!"^\\w{3,16}$".toRegex().matches(username)) {
            switcher.logger.warn("${requestEvent.username} tried to connect through the switcher with invalid username!")
            return
        }

        if (!switcher.luckPerms.userManager.loadUser(requestEvent.gameProfile.id).get().
            cachedData.permissionData.checkPermission("switcher.domain").asBoolean()) return

        requestEvent.gameProfile = if (requestEvent.isOnlineMode) {
            val userJsonObject = getUserJsonObject(username.lowercase())
            val onlineUniqueId = userJsonObject?.get("id")?.asString
            val onlineUsername = userJsonObject?.get("name")?.asString

            val gameProfile: GameProfile = if (onlineUniqueId != null && onlineUsername != null)
                GameProfile(onlineUniqueId, onlineUsername, listOf())
            else
                GameProfile.forOfflinePlayer(username)

            gameProfile
        } else GameProfile.forOfflinePlayer(username)

        switcher.logger.info("${requestEvent.username} is logging as $username")
    }

    private fun getUsernameFromHost(host: String): String? = "(?<=$prefix)[^.]+".toRegex()
        .matchAt(host, host.indexOf(prefix) + prefix.length)
        ?.value

    private fun getUserJsonObject(username: String): JsonObject? {
        val response = try {
            URL("https://api.mojang.com/users/profiles/minecraft/$username").readText()
        } catch (exception: Exception) {
            switcher.logger.error("$username doesn't exist!")
            return null
        }
        return gson.fromJson(JsonParser.parseString(response), JsonObject::class.java)
    }
}