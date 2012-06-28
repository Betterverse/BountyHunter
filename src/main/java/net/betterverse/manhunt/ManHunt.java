package net.betterverse.manhunt;

import java.util.ArrayList;
import java.util.List;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class ManHunt extends JavaPlugin {
	private static Config config;

	private static List<Hunt> hunts = new ArrayList<Hunt>();

	private static Economy economy;

	@Override
	public void onEnable() {
		// Register listeners
		getServer().getPluginManager().registerEvents(new ManHuntListener(this), this);

		// Config
		config = new Config(this);

		// Economy
		setupEconomy();

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

	/**
	 * Check to see if a player is in a hunt
	 *
	 * @param player Player to check
	 * @return true if they are in an active hunt, false if not
	 */
	public boolean isInHunt(Player player) {
		for(Hunt hunt : hunts) {
			if(hunt.isActive() && hunt.isTarget(player)) return true;
		}
		return false;
	}

	/**
	 * Get the active hunt a player is in
	 *
	 * @param player Player to get hunt from
	 * @return The Hunt, or null if not found
	 */
	public Hunt getHuntFromPlayer(Player player) {
		for(Hunt hunt : hunts) {
			if(hunt.isActive() && hunt.isTarget(player)) return hunt;
		}
		return null;
	}

	protected Economy getEconomy() {
		return economy;
	}

	protected Config getConfiguration() {
		return config;
	}

	protected void startNewHunt() {
		Hunt hunt = new Hunt(this);
		hunts.add(hunt);
	}

	protected void removeHunt(Hunt hunt) {
		hunts.remove(hunt);
	}

	private void setupEconomy() {
		RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
		if (economyProvider != null) {
			economy = economyProvider.getProvider();
		}
	}
}
