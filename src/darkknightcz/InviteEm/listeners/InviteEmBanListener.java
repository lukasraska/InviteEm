package darkknightcz.InviteEm.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import darkknightcz.InviteEm.InviteEm;
import darkknightcz.InviteEm.MySQL;
import darkknightcz.InviteEm.Punishments;

public class InviteEmBanListener implements Listener {	
	private MySQL db;

	public InviteEmBanListener(InviteEm plugin) {
		this.db = plugin.getDb();
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void gotBan(PlayerCommandPreprocessEvent event) {
		if (event.getMessage().contains("/ban")) {
			String[] args = event.getMessage().split(" ");
			if (args.length > 1) {
				if (args[0].toLowerCase().equals("/ban")) {
					String ref = db.warnBan(args[1].toLowerCase());
					if (ref != null) {
						if(Bukkit.getPlayer(ref).isOnline()){
							Punishments.warn(Bukkit.getPlayer(ref));
							db.setWarned(ref);
						}
						
						Punishments.punishBan(ref, args[1]);						
					}
				} else {
					return;
				}
			}
		}
	}	
	
	
}
