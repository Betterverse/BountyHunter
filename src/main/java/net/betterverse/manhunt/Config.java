package net.betterverse.manhunt;

import java.util.Set;
import java.util.TreeSet;

public class Config {
	private static Set<Integer> huntLimits = new TreeSet<Integer>();

	public Config(ManHunt manHunt) {
		huntLimits.add(3); // TODO: Load this value
		huntLimits.add(8);
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
		Integer[] limits = (Integer[]) huntLimits.toArray();
		for(int i = 0; i < limits.length; i++) {
			if(limits[i] == playerCount) return ++i;
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
		Integer[] limits = (Integer[]) huntLimits.toArray();
		// Special case for no hunts allowed
		if(limits[0] > playerCount) return 0;

		int i;
		for(i = 0; i < limits.length; i++) {
			if(limits[i] > playerCount) return ++i;
		}
		return ++i;
	}

	/**
	 * Return the number of seconds to wait between a hunt starting and a hunt beginning
	 *
	 * @return Time in seconds to wait
	 */
	public int getHuntResetTime() {
		return 120; // TODO: Load this value
	}

	/**
	 * @return The price at which a bounty should start
	 */
	public int getBountyStartPrice() {
		return 100; // TODO: Load this value
	}

	/**
	 * Return the number of seconds to wait before checking if a bounty should be raised.
	 * <br />
	 * This value also dictates how often the location should be updated
	 *
	 * @return Time in seconds to wait
	 */
	public int getBountyIncrementTime() {
		return 60; // TODO: Load this value
	}

	/**
	 * @return The amount to increase the bounty by
	 */
	public int getBountyIncrementPrice() {
		return 100; // TODO: Load this value
	}

	/**
	 * @return The maximum price a bounty can be
	 */
	public int getBountyMaxPrice() {
		return 1000; // TODO: Load this value
	}
}
