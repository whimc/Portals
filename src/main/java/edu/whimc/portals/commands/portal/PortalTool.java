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

public class PortalTool extends AbstractSubCommand {

    private static final ItemStack TOOL = new ItemStack(Material.WOODEN_SWORD);

    protected PortalTool(Main plugin, String baseCommand, String subCommand) {
        super(plugin, baseCommand, subCommand);
        super.description("Gives you the portal selector tool");
        super.requiresPlayer();

        ItemMeta meta = TOOL.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA + "Portal Tool");
        TOOL.setItemMeta(meta);
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        ((Player) sender).getInventory().addItem(TOOL);
        Messenger.msg(sender, Message.PORTAL_TOOL_GIVEN);
        return true;
    }

}
