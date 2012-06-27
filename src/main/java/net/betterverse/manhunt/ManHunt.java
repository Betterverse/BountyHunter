package net.betterverse.manhunt;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.plugin.java.JavaPlugin;

public class ManHunt extends JavaPlugin {
	private static Config config;

	private static List<Hunt> hunts = new ArrayList<Hunt>();

	@Override
	public void onEnable() {
		// Register listeners
		getServer().getPluginManager().registerEvents(new ManHuntListener(this), this);

		// Config
		config = new Config(this);

		// CleanupTask
		ManHuntStartCheckTask task = new ManHuntStartCheckTask(this);
		getServer().getScheduler().scheduleSyncRepeatingTask(this, task, 1L, config.getPlayerCountDelay() * 20);

		// Hello, world.
		getLogger().info("Finished Loading " + getDescription().getFullName());
	}

	@Override
	public void onDisable() {
		getLogger().info("Finished Unloading "+getDescription().getFullName());
	}

	protected Config getConfiguration() {
		return config;
	}
}
