package darkknightcz.InviteEm.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import darkknightcz.InviteEm.InviteEm;
import darkknightcz.InviteEm.MySQL;
import darkknightcz.InviteEm.Settings;

public class InviteEmPlayerListener implements Listener {
	private MySQL db;
	private InviteEm plugin;

	public InviteEmPlayerListener(InviteEm plugin) {
		this.plugin = plugin;
		this.db = plugin.getDb();
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerLogin(PlayerLoginEvent event) {
		if ((event.getResult() != PlayerLoginEvent.Result.ALLOWED)
				|| (event.getPlayer() == null)) {
			return;
		}
		if (db.isRegistered(event.getPlayer().getName())) {
			this.db.createPlayerStructure(event.getPlayer().getName());
			plugin.tryRegisterMoney(event.getPlayer(), 1);
			return;
		}
		if (Settings.isOnList(event.getAddress().getHostAddress())) {
			event.disallow(PlayerLoginEvent.Result.KICK_OTHER,
					Settings.ipIsOnList);
			return;
		}
		if (db.isInvited(event.getPlayer().getName())) {
			this.db.createPlayerStructure(event.getPlayer().getName());
			plugin.tryRegisterMoney(event.getPlayer(), 0); // 0 at invite
			return;
		}

		event.disallow(PlayerLoginEvent.Result.KICK_OTHER, Settings.kickMessage);
		return;
	}
}
