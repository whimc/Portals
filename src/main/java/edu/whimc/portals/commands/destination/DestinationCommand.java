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

    public DestinationCommand() {
        Permission perm = new Permission(Main.PERM_PREFIX + ".destination.*");
        perm.addParent(Main.PERM_PREFIX + ".*", true);
        Bukkit.getPluginManager().addPermission(perm);

        addSubCommand(new DestinationChange("destination", "change"));
        addSubCommand(new DestinationClear("destination", "clear"));
        addSubCommand(new DestinationCreate("destination", "create"));
        addSubCommand(new DestinationInfo("destination", "info"));
        addSubCommand(new DestinationList("destination", "list"));
        addSubCommand(new DestinationPurge("destination", "purge"));
        addSubCommand(new DestinationRemove("destination", "remove"));
        addSubCommand(new DestinationSet("destination", "set"));
        addSubCommand(new DestinationSetHere("destination", "sethere"));
        addSubCommand(new DestinationTeleport("destination", "teleport"));
    }

}
