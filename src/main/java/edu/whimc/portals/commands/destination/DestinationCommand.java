package edu.whimc.portals.commands.destination;

import edu.whimc.portals.commands.AbstractRootCommand;
import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;

import edu.whimc.portals.Main;

/**
 * The root command for all portal-destination-related sub commands.
 *
 * @see edu.whimc.portals.Destination
 * @see edu.whimc.portals.Portal
 */
public final class DestinationCommand extends AbstractRootCommand {

    public DestinationCommand(Main plugin) {
        Permission perm = new Permission(Main.PERM_PREFIX + ".destination.*");
        perm.addParent(Main.PERM_PREFIX + ".*", true);
        Bukkit.getPluginManager().addPermission(perm);

        addSubCommand(new DestinationChange(plugin, "destination", "change"));
        addSubCommand(new DestinationClear(plugin, "destination", "clear"));
        addSubCommand(new DestinationCreate(plugin, "destination", "create"));
        addSubCommand(new DestinationInfo(plugin, "destination", "info"));
        addSubCommand(new DestinationList(plugin, "destination", "list"));
        addSubCommand(new DestinationPurge(plugin, "destination", "purge"));
        addSubCommand(new DestinationRemove(plugin, "destination", "remove"));
        addSubCommand(new DestinationSet(plugin, "destination", "set"));
        addSubCommand(new DestinationSetHere(plugin, "destination", "sethere"));
        addSubCommand(new DestinationTeleport(plugin, "destination", "teleport"));
    }

}
