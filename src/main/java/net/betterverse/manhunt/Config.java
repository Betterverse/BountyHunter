package net.betterverse.manhunt;

import java.io.File;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.bukkit.configuration.file.FileConfiguration;

public class Config {
	private static Set<Integer> huntLimits = new TreeSet<Integer>();

	private static FileConfiguration config;

	public Config(ManHunt manHunt) {
		File configFile = new File(manHunt.getDataFolder(), "config.yml");
		if(!configFile.exists()) {
			manHunt.saveDefaultConfig();
		}

		config = manHunt.getConfig();

		int maxConcurrent = config.getInt("hunts.maximum_concurrent");

		for(int i = 1; i <= maxConcurrent; i++) {
			int minimumPlayers = config.getInt("hunts.hunt_" + i + ".minimum_players");
			huntLimits.add(minimumPlayers);
		}
	}

	/**
	 * Check if a given player count is a hunt limit
	 *
	 * @param playerCount Current player count
	 * @return True if it is, false if not
	 */
	public boolean isHuntLimit(int playerCount) {
		return huntLimits.contains(playerCount);
	}

	/**
	 * Return the hunt number for the given player count
	 *
	 * @param playerCount Current player count
	 * @return Sequential number hunt this is, -1 if not defined
	 */
	public int getHuntNumberForLimit(int playerCount) {
		int i = 1;
		Iterator<Integer> iterator = huntLimits.iterator();
		while(iterator.hasNext()) {
			int limit = iterator.next().intValue();
			if(limit == playerCount) return i;
			i++;
		}
		return -1;
	}

	/**
	 * Return the maximum number of hunts permitted for the given player count
	 *
	 * @param playerCount Current player count
	 * @return The number of hunts to allow
	 */
	public int getMaximumHunts(int playerCount) {
		int i = 0;
		Iterator<Integer> iterator = huntLimits.iterator();
		while(iterator.hasNext()) {
			int limit = iterator.next().intValue();
			if(limit > playerCount) {
				return i;
			}
			i++;
		}
		return i;
	}

	/**
	 * Return the number of seconds to wait between a hunt starting and a hunt beginning
	 *
	 * @return Time in seconds to wait
	 */
	public int getHuntResetTime() {
		return config.getInt("hunts.reset_time");
	}

	/**
	 * @return The price at which a bounty should start
	 */
	public int getBountyStartPrice() {
		return config.getInt("reward.initial");
	}

	/**
	 * Return the number of seconds to wait before checking if a bounty should be raised.
	 * <br />
	 * This value also dictates how often the location should be updated
	 *
	 * @return Time in seconds to wait
	 */
	public int getBountyIncrementTime() {
		return config.getInt("reward.timer");
	}

	/**
	 * @return The amount to increase the bounty by
	 */
	public int getBountyIncrementPrice() {
		return config.getInt("reward.increment");
	}

	/**
	 * @return The maximum price a bounty can be
	 */
	public int getBountyMaxPrice() {
		return config.getInt("reward.maximum");
	}
}
