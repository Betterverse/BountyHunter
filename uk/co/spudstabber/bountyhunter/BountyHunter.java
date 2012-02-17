package uk.co.spudstabber.bountyhunter;

import java.util.HashMap;
import java.util.logging.Logger;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class BountyHunter extends JavaPlugin {
	
	public BountyPlayer bountyplayer;
	public BountyTimer bountytimer;
	
	private final PlayerKillListener playerKillListener = new PlayerKillListener(this);
	private final PlayerJoinListener playerJoinListener = new PlayerJoinListener(this);
	private final PlayerQuitListener playerQuitListener = new PlayerQuitListener(this);
	
	private PluginDescriptionFile pdf;
	private FileConfiguration config;
	public Logger log = Logger.getLogger("Minecraft");
	private HashMap<Player, Location> bounty = new HashMap<Player,Location>();
	
	
	private String pdfname;
	private String pdfversion;
	private String pluginhead;
	
    public static Economy economy = null;
	
	public void onEnable()
	{
		pdf = this.getDescription();
		pdfname = pdf.getName();
		pdfversion = pdf.getVersion();
		pluginhead = "[" + pdfname + "] ";
		
		setupConfig();
		clearHashMap();
		
		if (config.getBoolean("general.enable") == true) 
		{
			log.info(pluginhead + pdfname + " version " + pdfversion + " is now enabled!");
			registerListeners();
			setupEconomy();
			bountyplayer = new BountyPlayer(this);
			bountytimer = new BountyTimer(this);
		}
		else
		{
			log.info(pluginhead + pdfname + " is disabled via config.yml!");
			this.getPluginLoader().disablePlugin(this);
		}
	}
	
	public void onDisable() {
		this.log.info(pluginhead + pdfname + " is now disabled!");
	}
	
	// Setup section
	
	public void setupConfig()
	{
		config = this.getConfig();
		if (config.get("general.enable") == null) 
		{
			this.getConfig().set("general.enable", true);
		}
		if (config.get("money.percentage-up") == null) 
		{
			this.getConfig().set("money.percentage-up", 10);
		}
		if (config.get("money.start-bounty") == null) 
		{
			this.getConfig().set("money.start-bounty", 100);
		}
		if (config.get("money.max-bounty") == null) 
		{
			this.getConfig().set("money.max-bounty", 1000);
		}
		if (config.get("times.time-after-kill") == null) 
		{
			this.getConfig().set("times.time-after-kill", 60);
		}
		if (config.get("times.offline-time") == null) 
		{
			this.getConfig().set("times.offline-time", 30);
		}
		if (config.get("times.increase-bounty-time") == null) 
		{
			this.getConfig().set("times.increase-bounty-time", 120);
		}
		this.saveConfig();
	}
	
	public void registerListeners()
	{
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvent(Event.Type.ENTITY_DEATH, this.playerKillListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_JOIN, this.playerJoinListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_QUIT, this.playerQuitListener, Event.Priority.Normal, this);
	}
	
	// Economy section
	
	private Boolean setupEconomy()
	{
		RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
      if (economyProvider != null) 
        {
            economy = economyProvider.getProvider();
        }
        return (economy != null);
    }
	
	public Economy getEconomy()
	{
		return economy;
	}
	
	// HashMap section
	
	public void addPlayerToHashMap(Player player, Location location)
	{
		bounty.put(player, location);
	}
	
	public Boolean isPlayerInHashMap(Player player)
	{
		return bounty.containsKey(player);
	}
	
	public Location getLocationFromHashMap(Player player)
	{
		return bounty.get(player);
	}
	
	public void removePlayerFromHashMap(Player player)
	{
		bounty.remove(player);
	}	
	
	public void clearHashMap()
	{
		bounty.clear();
	}
	public boolean isHashMapEmpty()
	{
		return bounty.isEmpty();
	}

}

