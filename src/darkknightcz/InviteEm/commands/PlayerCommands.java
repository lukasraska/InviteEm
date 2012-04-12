package darkknightcz.InviteEm.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import darkknightcz.InviteEm.MySQL;
import darkknightcz.InviteEm.Settings;

public class PlayerCommands implements CommandExecutor{
	JavaPlugin plugin;
	MySQL db;
	public PlayerCommands(JavaPlugin plugin, MySQL db){
		this.plugin = plugin;
		this.db = db;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		System.out.println(sender);
		if(sender instanceof Player){
			if(cmd.getName().equalsIgnoreCase("inv")){
				if(args.length==1){
					if(args[0].equalsIgnoreCase("url")){
						sender.sendMessage("Implement!");
					}else{						
						this.db.invite(args[0], sender.getName());
						return true;
					}
				}else{
					sender.sendMessage("Usage: /inv nick  | /inv url");
					return true;
				}
			}
			
		}else{
			sender.sendMessage(ChatColor.RED+Settings.youHaveToBePlayer);
			return false;
		}
		
		
		return false;
	}

}
