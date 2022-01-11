package edu.whimc.portals.commands.portal;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import edu.whimc.portals.Main;
import edu.whimc.portals.commands.AbstractSubCommand;
import edu.whimc.portals.utils.Messenger;
import edu.whimc.portals.utils.Messenger.Message;

/**
 * The command to give the user a portal selector tool.
 * Command: "/portal tool"
 */
public class PortalTool extends AbstractSubCommand {

    /** The item that represents the portal tool */
    private static final ItemStack TOOL = new ItemStack(Material.WOODEN_SWORD);

    /**
     * Constructs a PortalTool.
     *
     * @param plugin the instance of the plugin.
     * @param baseCommand the base command keyword.
     * @param subCommand the sub command keyword.
     */
    protected PortalTool(Main plugin, String baseCommand, String subCommand) {
        super(plugin, baseCommand, subCommand);
        super.description("Gives you the portal selector tool");
        super.requiresPlayer();

        // set up the portal selector tool item
        ItemMeta meta = TOOL.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA + "Portal Tool");
        TOOL.setItemMeta(meta);
    }

    /**
     * {@inheritDoc}
     *
     * Gives the user a portal selector tool.
     */
    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        ((Player) sender).getInventory().addItem(TOOL);
        Messenger.msg(sender, Message.PORTAL_TOOL_GIVEN);
        return true;
    }

}
