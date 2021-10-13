package edu.whimc.portals.commands.portal;

import edu.whimc.portals.commands.AbstractRootCommand;
import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;

import edu.whimc.portals.Main;

/**
 * The root command for all portal-related sub commands.
 *
 * @see edu.whimc.portals.Portal
 * @see edu.whimc.portals.Destination
 */
public final class PortalCommand extends AbstractRootCommand {

    public PortalCommand() {
        Permission perm = new Permission(Main.PERM_PREFIX + ".portal.*");
        perm.addParent(Main.PERM_PREFIX + ".*", true);
        Bukkit.getPluginManager().addPermission(perm);

        addSubCommand(new PortalCreate("portal", "create"));
        addSubCommand(new PortalDebug("portal", "debug"));
        addSubCommand(new PortalInfo("portal", "info"));
        addSubCommand(new PortalList("portal", "list"));
        addSubCommand(new PortalPermission("portal", "permission"));
        addSubCommand(new PortalPurge("portal", "purge"));
        addSubCommand(new PortalRefill("portal", "refill"));
        addSubCommand(new PortalRemove("portal", "remove"));
        addSubCommand(new PortalReshape("portal", "reshape"));
        addSubCommand(new PortalSetFiller("portal", "setfiller"));
        addSubCommand(new PortalTeleport("portal", "teleport"));
        addSubCommand(new PortalTool("portal", "tool"));
    }

}