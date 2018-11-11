package com.github.ruany.lanbroadcaster.velocity;

import com.github.ruany.lanbroadcaster.LANBroadcaster;
import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.text.serializer.ComponentSerializers;
import org.slf4j.Logger;

@Plugin(id = "lanbroadcaster",
        name = "LANBroadcaster",
        version = "1.6.1",
        authors = "Ruan",
        description = "Broadcasts a Minecraft server over LAN.")
public class LANBroadcasterPlugin {
    @Inject
    private ProxyServer proxy;
    @Inject
    private Logger logger;
    private LANBroadcaster broadcaster;

    @Subscribe
    public void onDisable(ProxyShutdownEvent event) {
        broadcaster.setRunning(false);
    }

    @Subscribe
    public void onEnable(ProxyInitializeEvent event) {
        broadcaster = new LANBroadcaster(LANBroadcaster.createSocket(),
                proxy.getBoundAddress().getPort(),
                ComponentSerializers.LEGACY.serialize(proxy.getConfiguration().getMotdComponent()),
                proxy.getBoundAddress().getHostString(), new LoggerWrapper(logger));
        proxy.getScheduler().buildTask(this, broadcaster).schedule();
    }
}
