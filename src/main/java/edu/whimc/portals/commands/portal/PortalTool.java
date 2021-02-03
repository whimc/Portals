package edu.whimc.portals.commands.portal;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import edu.whimc.portals.Main;
import edu.whimc.portals.commands.AbstractSubCommand;
import edu.whimc.portals.utils.Messenger;
import edu.whimc.portals.utils.Messenger.Message;

/**
 * Provides the user with a tool designed to designate the locations
 * which define the boundaries of a new {@link edu.whimc.portals.Portal}.
 *
 * @see PortalCommand
 * @see edu.whimc.portals.listeners.ToolSelectListener
 */
public final class PortalTool extends AbstractSubCommand {

    private static final ItemStack TOOL = new ItemStack(Main.TOOL_MATERIAL);

    protected PortalTool(String baseCommand, String subCommand) {
        super(baseCommand, subCommand);
        super.setDescription("Gives you the portal selector tool");
        super.setRequiresPlayer(true);

        ItemMeta meta = TOOL.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.AQUA + "Portal Tool");
        TOOL.setItemMeta(meta);
    }

    @Override
    public final boolean onCommand(CommandSender sender, String[] args) {
        ((Player) sender).getInventory().addItem(TOOL);
        Messenger.msg(sender, Message.PORTAL_TOOL_GIVEN);
        return true;
    }

}
