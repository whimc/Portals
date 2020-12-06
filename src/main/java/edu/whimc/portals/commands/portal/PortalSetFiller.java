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

public class PortalSetFiller extends AbstractSubCommand {

    public PortalSetFiller(Main plugin, String baseCommand, String subCommand) {
        super(plugin, baseCommand, subCommand);
        super.description("Sets the filler of a portal");
        super.arguments("portal block");
    }

    @Override
    protected boolean onCommand(CommandSender sender, String[] args) {
        Portal portal = Portal.getPortal(args[0]);
        if (portal == null) {
            Messenger.msg(sender, ReplaceMessage.PORTAL_DOES_NOT_EXIST, args[0]);
            return true;
        }

        if (!portal.isValid()) {
            Messenger.msg(sender, ReplaceMessage.PORTAL_INVALID, portal.getName());
            Messenger.msg(sender, ReplaceMessage.SUGGEST_DELETE, "/portal remove " + portal.getName());
            return true;
        }

        Material mat = Material.matchMaterial(args[1]);
        if (mat == null || !Portal.isValidFiller(mat)) {
            Messenger.msg(sender, ReplaceMessage.INVALID_FILLER, args[1]);
            return true;
        }

        portal.setFiller(mat);

        Messenger.msg(sender, Messenger.prefix + "&aThe filler of '&2" + portal.getName() + "&a' " +
                "has been set to '&2" + mat.toString() + "&a'!");
        return true;
    }

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
