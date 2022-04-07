package edu.whimc.portals.listeners;

import edu.whimc.portals.Destination;
import edu.whimc.portals.Portal;
import net.citizensnpcs.api.ai.event.NavigationCompleteEvent;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

/**
 * Listens for events where the portal is entered by a Citizen.
 */
public class PortalEnterCitizensListener implements Listener {
    /**
     * Teleport Citizen if its navigation ends in a portal.
     *
     * @param event The Citizens NavigationCompleteEvent
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onCitizenEndNavigation(NavigationCompleteEvent event) {
        // get NPC information
        NPC npc = event.getNPC();
        Location loc = npc.getStoredLocation();

        // TODO: add console logging to errors

        // check valid portal
        Portal portal = Portal.getPortal(loc);
        if (portal == null) { return; }
        if (!portal.hasDestination()) { return; }

        // check valid destination
        Destination dest = portal.getDestination();
        if (!dest.isValid()) { return; }

        // Do nothing if portal does not allow citizens
        if (!portal.getAllowCitizens()) { return; }

        // teleport citizen
        npc.teleport(dest.getLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);
    }
}
