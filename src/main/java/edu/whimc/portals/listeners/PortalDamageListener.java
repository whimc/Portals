package edu.whimc.portals.listeners;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustByBlockEvent;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import edu.whimc.portals.Portal;

public class PortalDamageListener implements Listener {

    @EventHandler
    public void onCombust(EntityCombustByBlockEvent event) {
        Portal near = getNearPortal(event.getEntity().getLocation().getBlock(), 1);
        if (near != null) event.setCancelled(true);
    }

    @EventHandler
    public void onDamage(EntityDamageByBlockEvent event) {
        if (event.getCause() != DamageCause.LAVA) return;
        Portal near = getNearPortal(event.getEntity().getLocation().getBlock(), 1);
        if (near != null) event.setCancelled(true);
    }

    private Portal getNearPortal(Block start, int radius){
        if (radius < 0) {
            return null;
        }
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    Portal portal = Portal.getPortal(start.getRelative(x, y, z));
                    if (portal != null) return portal;
                }
            }
        }
        return null;
    }

}
