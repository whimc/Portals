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

    public PortalCommand(Main plugin) {
        Permission perm = new Permission(Main.PERM_PREFIX + ".portal.*");
        perm.addParent(Main.PERM_PREFIX + ".*", true);
        Bukkit.getPluginManager().addPermission(perm);

        addSubCommand(new PortalCreate(plugin, "portal", "create"));
        addSubCommand(new PortalDebug(plugin, "portal", "debug"));
        addSubCommand(new PortalInfo(plugin, "portal", "info"));
        addSubCommand(new PortalList(plugin, "portal", "list"));
        addSubCommand(new PortalPermission(plugin, "portal", "permission"));
        addSubCommand(new PortalPurge(plugin, "portal", "purge"));
        addSubCommand(new PortalRefill(plugin, "portal", "refill"));
        addSubCommand(new PortalRemove(plugin, "portal", "remove"));
        addSubCommand(new PortalReshape(plugin, "portal", "reshape"));
        addSubCommand(new PortalSetFiller(plugin, "portal", "setfiller"));
        addSubCommand(new PortalTeleport(plugin, "portal", "teleport"));
        addSubCommand(new PortalTool(plugin, "portal", "tool"));
    }

}