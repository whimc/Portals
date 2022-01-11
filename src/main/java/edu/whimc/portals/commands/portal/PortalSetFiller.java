package edu.whimc.portals.commands.portal;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;

import edu.whimc.portals.Main;
import edu.whimc.portals.Portal;
import edu.whimc.portals.commands.AbstractSubCommand;
import edu.whimc.portals.utils.Messenger;
import edu.whimc.portals.utils.Messenger.ReplaceMessage;

/**
 * The command to set the filler block of the portal.
 * Command: "/portal setfiller"
 */
public class PortalSetFiller extends AbstractSubCommand {

    /**
     * Constructs a PortalSetFiller.
     *
     * @param plugin the instance of the plugin.
     * @param baseCommand the base command keyword.
     * @param subCommand the sub command keyword.
     */
    public PortalSetFiller(Main plugin, String baseCommand, String subCommand) {
        super(plugin, baseCommand, subCommand);
        super.description("Sets the filler of a portal");
        super.arguments("portal block");
    }

    /**
     * {@inheritDoc}
     *
     * Sets the filler block of the portal.
     */
    @Override
    protected boolean onCommand(CommandSender sender, String[] args) {
        // notify if portal does not exist and abort command execution
        Portal portal = Portal.getPortal(args[0]);
        if (portal == null) {
            Messenger.msg(sender, ReplaceMessage.PORTAL_DOES_NOT_EXIST, args[0]);
            return true;
        }

        // notify if portal is invalid, suggest deletion, and abort command execution
        if (!portal.isValid()) {
            Messenger.msg(sender, ReplaceMessage.PORTAL_INVALID, portal.getName());
            Messenger.msg(sender, ReplaceMessage.SUGGEST_DELETE, "/portal remove " + portal.getName());
            return true;
        }

        // notify if filler is invalid and abort command execution
        Material mat = Material.matchMaterial(args[1]);
        if (mat == null || !Portal.isValidFiller(mat)) {
            String validFillers = String.join(", ", Portal.getValidFillers()
                    .stream()
                    .map(Material::toString)
                    .sorted()
                    .collect(Collectors.toList()));
            Messenger.msg(sender, ReplaceMessage.INVALID_FILLER, args[1], validFillers);
            return true;
        }

        // set the filler block of the portal
        portal.setFiller(mat);
        Messenger.msg(sender, ReplaceMessage.PORTAL_FILLER_SET, portal.getName(), mat.toString());
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected List<String> onTabComplete(CommandSender sender, String[] args) {
        if (args.length == 2) {
            return Portal.getValidFillers()
                    .stream()
                    .map(Material::toString)
                    .filter(v -> v.toLowerCase().startsWith(args[1].toLowerCase()))
                    .sorted()
                    .collect(Collectors.toList());
        }

        return Portal.getTabCompletedPortals(args[0]);
    }

}
