package edu.whimc.portals.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.EntityBlockFormEvent;

import edu.whimc.portals.Portal;

/**
 * Listens for events where the portal blocks change.
 */
public class PortalBlockChangeListener implements Listener {

    /**
     * Cancel water/lava movement inside portals.
     *
     * @param event The block event.
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onWaterMove(BlockFromToEvent event){
        Portal portal = Portal.getPortal(event.getBlock());
        if (portal != null) event.setCancelled(true);
    }

    /**
     * Cancel block formations inside portals.
     *
     * @param event The block event.
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockForm(BlockFormEvent event) {
        Portal portal = Portal.getPortal(event.getBlock());
        if (portal != null) event.setCancelled(true);
    }

    /**
     * Stop entities from forming blocks inside portals.
     *
     * @param event The block event.
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityBlockForm(EntityBlockFormEvent event) {
        Portal portal = Portal.getPortal(event.getBlock());
        if (portal != null) event.setCancelled(true);
    }

    /**
     * Cancel block ignition inside portals.
     *
     * @param event The block event.
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockIgnite(BlockIgniteEvent event) {
        Portal portal = Portal.getPortal(event.getIgnitingBlock());
        if (portal != null) event.setCancelled(true);
    }

}
