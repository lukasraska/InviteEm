package darkknightcz.InviteEm;

import java.sql.SQLException;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Punishments {
	public static InviteEm plugin;
	
	public Punishments(InviteEm plugin){
		Punishments.plugin=plugin;
	}
	
	public static void warn(Player player){
		List<String> warnings = plugin.getDb().getWarnings(
				player.getName().toLowerCase());
		if (!warnings.isEmpty()) {
			for (String warning : warnings) {
				player.sendMessage(ChatColor.RED
						+ Settings.youHaveBeenWarned.replaceAll(
								"REASON", warning));
			}
			plugin.getDb().setWarnedPlayer(
					player.getName().toLowerCase());
			punishWarn(player.getName().toLowerCase());
		}				
	}
	
	public static void punishBan(String ref, String banned_nick) {
		/* types of punishment - NOTHING, REDUCE, MONEYREDUCE, TEMPBAN, BAN, TEMPBANON, BANON */
		if(Settings.BanAction.contains("NOTHING")){
			return;
		}
		for(String TYPE: Settings.BanAction){
			if(TYPE.equalsIgnoreCase("REDUCE")){
				try{				
					plugin.getDb().setOffset(ref, plugin.getDb().getOffset(ref)+Settings.reduce);
				}catch(SQLException e){
					plugin.log.warning("Database problem, check InviteEm config!");
				}
			}else if(TYPE.equalsIgnoreCase("MONEYREDUCE")){
				plugin.economy.withdrawPlayer(ref, Math.round(Settings.inviteMoney*Settings.MoneyReduceBanCoefficient));
			}else if(TYPE.equalsIgnoreCase("BAN")){
				String command = Settings.BanCommand.replace("NICK", ref).replace("REASON", Settings.banReason.replaceAll("PLAYER",banned_nick));
				Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), command);
				return;
			}else if(TYPE.equalsIgnoreCase("TEMPBAN")){
				String command = Settings.TempBanCommand.replace("NICK", ref).replace("REASON", Settings.banReason.replaceAll("PLAYER",banned_nick)).replace("TIME", Settings.TempBan);
				Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), command);
				return;
			}else if(TYPE.equalsIgnoreCase("BANON")){
				if(plugin.getDb().getWarningsCount(ref)>=Settings.BanOnWarn){
					String command = Settings.BanCommand.replace("NICK", ref).replace("REASON", Settings.banOnWarnReason);
					Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), command);
					return;
				}
			}else if(TYPE.equalsIgnoreCase("TEMPBANON")){
				if(plugin.getDb().getWarningsCount(ref)>=Settings.TempBanOnWarn){
					String command = Settings.TempBanCommand.replace("NICK", ref).replace("REASON", Settings.tempBanOnWarnReason).replace("TIME", Settings.TempBan);
					Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), command);
					return;	
				}
			}
			
		}
	}
	
	public static void punishWarn(String ref) {
		/* types of punishment - NOTHING, REDUCE, MONEYREDUCE, TEMPBAN, BAN, TEMPBANON, BANON */
		if(Settings.WarnAction.contains("NOTHING")){
			return;
		}
		for(String TYPE: Settings.WarnAction){
			if(TYPE.equalsIgnoreCase("REDUCE")){
				try{				
					plugin.getDb().setOffset(ref, plugin.getDb().getOffset(ref)+Settings.reduce);
				}catch(SQLException e){
					plugin.log.warning("Database problem, check InviteEm config!");
				}
			}else if(TYPE.equalsIgnoreCase("MONEYREDUCE")){
				plugin.economy.withdrawPlayer(ref, Math.round(Settings.inviteMoney*Settings.MoneyReduceBanCoefficient));
			}else if(TYPE.equalsIgnoreCase("BAN")){
				String command = Settings.BanCommand.replace("NICK", ref).replace("REASON", Settings.banReason.replaceAll("PLAYER","banned_nick"));
				Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), command);
				return;
			}else if(TYPE.equalsIgnoreCase("TEMPBAN")){
				String command = Settings.TempBanCommand.replace("NICK", ref).replace("REASON", Settings.banReason.replaceAll("PLAYER","banned_nick")).replace("TIME", Settings.TempBan);
				Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), command);
				return;
			}else if(TYPE.equalsIgnoreCase("BANON")){
				if(plugin.getDb().getWarningsCount(ref)>=Settings.BanOnWarn){
					String command = Settings.BanCommand.replace("NICK", ref).replace("REASON", Settings.banOnWarnReason);
					Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), command);
					return;
				}
			}else if(TYPE.equalsIgnoreCase("TEMPBANON")){
				if(plugin.getDb().getWarningsCount(ref)>=Settings.TempBanOnWarn){
					String command = Settings.TempBanCommand.replace("NICK", ref).replace("REASON", Settings.tempBanOnWarnReason).replace("TIME", Settings.TempBan);
					Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), command);
					return;	
				}
			}
			
		}
	}

}
