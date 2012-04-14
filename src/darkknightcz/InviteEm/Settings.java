package darkknightcz.InviteEm;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class Settings extends YamlConfiguration {
	public static final String CONFIG_FILE = "config.yml";

	public static String host;
	public static String username;
	public static String password;
	public static String database;

	public static Integer MaxInvitations; /* default invitations per user */
	public static List<String> deniedIps = new ArrayList<String>();

	/* LOCALES */
	public static String kickMessage;
	public static String cannotInvite;
	public static String alreadyInvited;
	public static String alreadyRegistered;
	public static String successfullyInvited;
	public static String somethingWentWrong;
	public static String youHaveToBePlayer;
	public static String ipIsOnList;

	public final Plugin plugin;
	public FileConfiguration configFile = null;

	public Settings(Plugin plugin) {
		this.plugin = plugin;
		this.configFile = plugin.getConfig();
		this.plugin.getConfig().options().copyDefaults(true);
	}

	public void load() {
		host = configFile.getString("settings.database.host", "localhost");
		username = configFile.getString("settings.database.username",
				"username");
		password = configFile.getString("settings.database.password",
				"password");
		database = configFile.getString("settings.database.database",
				"minecraft");

		kickMessage = configFile.getString("locale.KickMessage",
				"You are not invited nor registered!");
		cannotInvite = configFile.getString("locale.CannotInvite",
				"You cannot invite another player!");
		alreadyInvited = configFile.getString("locale.AlreadyInvited",
				"User USER is already invited!");
		alreadyRegistered = configFile.getString("locale.AlreadyRegistered",
				"User USER is already registered!");
		successfullyInvited = configFile.getString("locale.SuccesfullyInvited",
				"User USER has been successfully invited!");
		somethingWentWrong = configFile.getString("locale.SomethingWentWrong",
				"Something went wrong and the user hasn't been invited!");
		youHaveToBePlayer = configFile.getString("locale.YouHaveToBePlayer",
				"You have to be player in order to invite another player!");
		ipIsOnList = configFile
				.getString("locale.IpIsOnList",
						"We are sorry, but new registrations from your IP address are banned!");

		MaxInvitations = configFile.getInt("settings.MaxInvitations");
	}

	public static boolean isOnList(String ip) {
		return deniedIps.contains(ip);

	}
}
