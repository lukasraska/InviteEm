package darkknightcz.InviteEm.economy;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import darkknightcz.InviteEm.InviteEm;
import darkknightcz.InviteEm.Settings;

public class RegisterMoney implements Runnable {
	private Player player;
	private InviteEm plugin;
	private int type;

	public RegisterMoney(InviteEm plugin, Player player, int type) {
		this.plugin = plugin;
		this.player = player;
		this.type = type;
	}

	@Override
	public void run() {
		if (player.isOnline()) {
			if (type == 0) {
				plugin.economy.depositPlayer(player.getName(),
						Settings.registerMoney);
				player.sendMessage(ChatColor.GREEN
						+ Settings.registerMoneyMessage.replaceAll(
								"MONEY",
								Settings.inviteMoney + " "
										+ plugin.economy.getName()));
			} else {
				plugin.economy.depositPlayer(player.getName(),
						Settings.inviteMoney);
				player.sendMessage(ChatColor.GREEN
						+ Settings.inviteMoneyMessage.replaceAll(
								"MONEY",
								Settings.inviteMoney + " "
										+ plugin.economy.getName()).replaceAll(
								"PLAYER", player.getName()));
			}
		}
	}

}