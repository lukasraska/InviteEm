package darkknightcz.InviteEm;

import java.util.logging.Logger;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import darkknightcz.InviteEm.commands.PlayerCommands;
import darkknightcz.InviteEm.listeners.InviteEmPlayerListener;

public class InviteEm extends JavaPlugin {
	private MySQL db;
	private Settings settings;
	Logger log;
	
	public void onDisable()
	  {
	    PluginDescriptionFile pdfFile = getDescription();
	    log.info("["+pdfFile.getName()+"] Disabled");
	  }

	  public void onEnable()
	  {
		log = this.getLogger();
	    PluginDescriptionFile pdfFile = getDescription();
	    PluginManager pm = getServer().getPluginManager()
	    		;
	    log.info("["+pdfFile.getName()+"] Loading ...");
	    /* methods */
	    settings = new Settings(this);
	    settings.load();	    
	    try {
			db = new MySQL();
		} catch (Exception e) {
		    log.warning("["+pdfFile.getName()+"] Database error, shutting down");
		    getServer().getPluginManager().disablePlugin(this);
		    return;
		}
	    
	    db.loadIps();	    
	    
	    pm.registerEvents(new InviteEmPlayerListener(this,db), this);
	    
	    PlayerCommands playerCommandsExecutor = new PlayerCommands(this,db);
	    
		this.getCommand("inv").setExecutor(playerCommandsExecutor);
	    
	    log.info("["+pdfFile.getName()+"] Loaded");
	  }
}