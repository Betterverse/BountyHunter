package net.betterverse.manhunt;

import java.util.Set;
import java.util.TreeSet;

public class Config {
	private static Set<Integer> huntLimits = new TreeSet<Integer>();

	public Config(ManHunt manHunt) {
		// TODO Auto-generated constructor stub
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

	public int getHuntResetTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getBountyStartPrice() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getBountyIncrementTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getBountyIncrementPrice() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getBountyMaxPrice() {
		// TODO Auto-generated method stub
		return 0;
	}
}
