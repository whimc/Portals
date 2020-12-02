package edu.whimc.portals.listeners;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.EntityBlockFormEvent;

import edu.whimc.portals.Portal;

public class WaterMoveListener implements Listener{

	@EventHandler(priority = EventPriority.HIGH)
	public void onWaterMove(BlockFromToEvent event){
		Location loc = event.getBlock().getLocation();
		Portal portal = Portal.getPortal(loc);
		if (portal != null) event.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onBlockForm(BlockFormEvent event) {
		Location loc = event.getBlock().getLocation();
		Portal portal = Portal.getPortal(loc);
		if (portal != null) event.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onEntityBlockForm(EntityBlockFormEvent event) {
		Location loc = event.getBlock().getLocation();
		Portal portal = Portal.getPortal(loc);
		if (portal != null) event.setCancelled(true);
	}
}
