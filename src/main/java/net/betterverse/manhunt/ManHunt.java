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

		// Hello, world.
		getLogger().info("Finished Loading " + getDescription().getFullName());
	}

	@Override
	public void onDisable() {
		getLogger().info("Finished Unloading "+getDescription().getFullName());
	}

	/**
	 * Get the number of currently running hunts
	 *
	 * @return The number of currently running hunts
	 */
	public int getActiveHuntCount() {
		return hunts.size();
	}

	protected Config getConfiguration() {
		return config;
	}

	protected void startNewHunt() {
		Hunt hunt = new Hunt(this);
		hunts.add(hunt);
	}
}
