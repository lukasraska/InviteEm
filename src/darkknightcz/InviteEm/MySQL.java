package darkknightcz.InviteEm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class MySQL {
	private String host;
	private String username;
	private String password;
	private String database;

	MySQL() throws SQLException {
		this.host = Settings.host;
		this.username = Settings.username;
		this.password = Settings.password;
		this.database = Settings.database;
		this.createTable();
	}

	public synchronized Connection connect() throws SQLException {
		return DriverManager.getConnection("jdbc:mysql://" + this.host
				+ ":3306/" + this.database + "?autoReconnect=true&user="
				+ this.username + "&password=" + this.password);
	}

	public synchronized Connection AuthMeConnect() throws SQLException {
		return DriverManager.getConnection("jdbc:mysql://"
				+ uk.org.whoami.authme.settings.Settings.getMySQLHost + ":"
				+ uk.org.whoami.authme.settings.Settings.getMySQLPort + "/"
				+ uk.org.whoami.authme.settings.Settings.getMySQLDatabase
				+ "?autoReconnect=true&user="
				+ uk.org.whoami.authme.settings.Settings.getMySQLUsername
				+ "&password="
				+ uk.org.whoami.authme.settings.Settings.getMySQLPassword);
	}

	public synchronized void disconnect(Connection con) throws SQLException {
		con.close();
	}
	
	
	
	public void loadIps(){
		try{
			Connection con = this.connect();
			PreparedStatement pst = con.prepareStatement("SELECT ip FROM inviteem_deniedIps ORDER BY id ASC");
			ResultSet rs = pst.executeQuery();
			while(rs.next()){
				Settings.deniedIps.add(rs.getString("ip"));
			}			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	

	public synchronized boolean isRegistered(String user) {
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			con = this.AuthMeConnect();
			pst = con.prepareStatement("SELECT id FROM "
					+ uk.org.whoami.authme.settings.Settings.getMySQLTablename
					+ " WHERE "
					+ uk.org.whoami.authme.settings.Settings.getMySQLColumnName
					+ "=?;");

			pst.setString(1, user.toLowerCase());
			rs = pst.executeQuery();
			boolean bool = rs.next();
			rs.close();
			pst.close();
			disconnect(con);

			return bool;
		} catch (Exception ex) {
			return false;
		}
	}

	private void createTable() throws SQLException {
		Connection con = this.connect();
		/* invitation */
		PreparedStatement pst = con
				.prepareStatement("CREATE TABLE IF NOT EXISTS `inviteem` (`id` INT NOT NULL AUTO_INCREMENT, `nick` VARCHAR(30) NOT NULL, `ref` VARCHAR(30) NOT NULL, `url` TEXT NULL, `ip` VARCHAR(40) NOT NULL, PRIMARY KEY (`id`), UNIQUE (`nick`)) ENGINE = InnoDB;");
		pst.execute();
		pst.close();

		/* users */
		PreparedStatement pst2 = con
				.prepareStatement("CREATE TABLE IF NOT EXISTS `inviteem_users` ( `id` int(11) NOT NULL AUTO_INCREMENT,  `nick` VARCHAR(30) NOT NULL,  `invitations` int(11) NOT NULL,  PRIMARY KEY (`id`),  UNIQUE KEY `nick` (`nick`)) ENGINE=InnoDB;");
		pst2.execute();
		pst2.close();

		
		/* denied Ips */
		PreparedStatement pst3 = con
				.prepareStatement("CREATE TABLE IF NOT EXISTS `inviteem_deniedIps` ( `id` int(11) NOT NULL AUTO_INCREMENT,  `ip` VARCHAR(40) NOT NULL, PRIMARY KEY (`id`),  UNIQUE KEY `ip` (`ip`)) ENGINE=InnoDB;");
		pst3.execute();
		pst3.close();

		disconnect(con);
	}

	public synchronized boolean isInvited(String user) {
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			con = this.connect();
			pst = con
					.prepareStatement("SELECT id FROM `inviteem` WHERE nick =?;");
			pst.setString(1, user.toLowerCase());
			rs = pst.executeQuery();
			boolean bool = rs.next();
			rs.close();
			pst.close();
			disconnect(con);
			return bool;
		} catch (Exception e) {
			return false;
		}
	}

	public synchronized boolean invite(String user, String sender) {
		if (this.canInvite(sender)) {
			if (this.isInvited(user)) {
				Bukkit.getServer()
						.getPlayer(sender)
						.sendMessage(
								ChatColor.YELLOW
										+ Settings.alreadyInvited.replaceAll(
												"USER", user));
				return false;
			}
			if (this.isRegistered(user)) {
				Bukkit.getServer()
						.getPlayer(sender)
						.sendMessage(
								ChatColor.YELLOW
										+ Settings.alreadyRegistered
												.replaceAll("USER", user));
				return false;
			}

			try {
				Connection con = this.connect();
				
				/* INSERT INVITATION */
				PreparedStatement pst = con
						.prepareStatement("INSERT INTO `inviteem` (`id`, `nick`, `ref`, `url`, `ip`) VALUES (NULL, ?, ?, NULL, ?);");
				pst.setString(1, user);
				pst.setString(2, sender);
				pst.setString(3, Bukkit.getServer().getPlayer(sender.toLowerCase())
						.getAddress().getAddress().getHostAddress());
				pst.execute();
				pst.close();
				
				/* UPDATE PLAYER STATUS */
				pst = con.prepareStatement("UPDATE  `inviteem_users` SET  `invitations` =  `invitations`+1 WHERE  `inviteem_users`.`nick` =?;");
				pst.setString(1, sender);
				pst.execute();
				pst.close();
				
				
				/* CLEANING */
				disconnect(con);
				Bukkit.getServer()
						.getPlayer(sender)
						.sendMessage(
								ChatColor.GREEN
										+ Settings.successfullyInvited
												.replaceAll("USER", user));
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				Bukkit.getServer()
						.getPlayer(sender)
						.sendMessage(
								ChatColor.RED + Settings.somethingWentWrong);
				return false;
			}

		} else {
			Bukkit.getServer().getPlayer(sender)
					.sendMessage(ChatColor.RED + Settings.cannotInvite);
			return false;
		}

	}

	public synchronized void createPlayerStructure(String sender){
		try{
		Connection con = this.connect();
		/*PreparedStatement pst = con.prepareStatement("SELECT COUNT(id) FROM  `inviteem_users` WHERE  `nick` =? LIMIT 1");
		pst.setString(1, sender);
		ResultSet rs = pst.executeQuery();
		rs.next();
		if(rs.getInt(0)==0){*/
		
			PreparedStatement pst = con.prepareStatement("INSERT INTO `inviteem_users` (`id`, `nick`, `invitations`) VALUES (NULL, ?, '0');");
			pst.setString(1, sender.toLowerCase());
			pst.execute();			
		
		}catch(Exception e){
			return;
		}
	}
	
	private synchronized boolean canInvite(String sender) {
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;

		try {
			con = this.connect();
			pst = con
					.prepareStatement("SELECT invitations FROM `inviteem_users` WHERE nick =? LIMIT 1");
			pst.setString(1, sender.toLowerCase());
			rs = pst.executeQuery();
			rs.next();
			Integer number = rs.getInt("invitations");
			rs.close();
			pst.close();
			disconnect(con);
			if (number < Settings.MaxInvitations) {
				return true;
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}
}