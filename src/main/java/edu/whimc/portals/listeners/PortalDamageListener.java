package edu.whimc.portals.listeners;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustByBlockEvent;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import edu.whimc.portals.Portal;

/**
 * Listens for events that cause damage to the portal.
 */
public class PortalDamageListener implements Listener {

    /**
     * Cancel the combustion event if it is too close to a portal.
     *
     * @param event The EntityCombustByBlockEvent.
     */
    @EventHandler
    public void onCombust(EntityCombustByBlockEvent event) {
        Portal near = getNearPortal(event.getEntity().getLocation().getBlock(), 1);
        if (near != null) event.setCancelled(true);
    }

    /**
     * Cancel damage caused to entities if it is too close to a portal.
     *
     * @param event The EntityDamageByBlockEvent.
     */
    @EventHandler
    public void onDamage(EntityDamageByBlockEvent event) {
        if (event.getCause() != DamageCause.LAVA) return;
        Portal near = getNearPortal(event.getEntity().getLocation().getBlock(), 1);
        if (near != null) event.setCancelled(true);
    }

    /**
     * Checks if a circular area from the source block contains a portal.
     *
     * @param start The block to start measuring from.
     * @param radius The radius considered to be "near".
     * @return The portal that is near.
     */
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
