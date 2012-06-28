package net.betterverse.manhunt;

import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Hunt implements Runnable {
	private static ManHunt plugin;
	private HuntMode mode;

	private Player target;
	private Location location;
	private int bounty;

	private int taskId;

	private static final Random random = new Random();

	public Hunt(ManHunt manHunt) {
		if(plugin == null) {
			plugin = manHunt;
		}

		selectTarget();
		updateLocation();
		bounty = plugin.getConfiguration().getBountyStartPrice();
		announceNewTarget();

		mode = HuntMode.HUNT;
		scheduleNextRun();
	}

	@Override
	public void run() {
		if(mode == HuntMode.HUNT) {
			// Here we are to check to see if we need to update our location and bounty, and notify as such for each case.
			if(bounty < plugin.getConfiguration().getBountyMaxPrice()) {
				bounty += plugin.getConfiguration().getBountyIncrementPrice();
				announceTargetBounty();
			}
			if(updateLocation()) {
				announceTargetLocation();
			}
		} else if(mode == HuntMode.RESTART) {
			// Here we are to check to see if we should start a new hunt based on config values and the number of hunts, if not we should set this Hunt to be removed and not schedule a new task
			int count = plugin.getActiveHuntCount();
			int playerCount = plugin.getServer().getOnlinePlayers().length;
			int maxHunts = plugin.getConfiguration().getMaximumHunts(playerCount);

			if(count > maxHunts) {
				plugin.removeHunt(this);
				// This is a special case return, as we do not want to schedule this hunt for the future
				return;
			} else {
				selectTarget();
				updateLocation();
				bounty = plugin.getConfiguration().getBountyStartPrice();
				announceNewTarget();

				mode = HuntMode.HUNT;
			}
		} else if(mode == HuntMode.DISCONNECT) {
			// Here we are to check to see if our target is still offline, if so we should then remove them from being the target (so the Player object can be gc'd) and then set our mode to RESTART
			if(target.isOnline()) {
				// Peaches and cream
				mode = HuntMode.HUNT;
			} else {
				announceTargetCoward();
				target = null;
				mode = HuntMode.RESTART;
			}
		} else {
			plugin.getLogger().severe("HuntMode was unexpected, could not execute this task!");
		}
		scheduleNextRun();
	}

	/**
	 * Check if a given player is the target of this Hunt
	 *
	 * @param player Player to check
	 * @return true if the player is this Hunt's target
	 */
	public boolean isTarget(Player player) {
		return target.equals(player);
	}

	/**
	 * Get the activity status of this Hunt
	 *
	 * @return true if this Hunt is currently active
	 */
	public boolean isActive() {
		return mode == HuntMode.HUNT;
	}

	protected void targetDisconnected() {
		plugin.getServer().getScheduler().cancelTask(taskId);
		mode = HuntMode.DISCONNECT;
		scheduleNextRun();
	}

	protected void targetKilled(Player killer) {
		// TODO: Economy
		announceTargetKilled(killer.getDisplayName());
		plugin.getServer().getScheduler().cancelTask(taskId);
		mode = HuntMode.RESTART;
		scheduleNextRun();
	}

	/**
	 * Update the location of this Hunt's target
	 *
	 * @return true if the location has been changed, false if not
	 */
	private boolean updateLocation() {
		double x = 50 * Math.round(target.getLocation().getX() / 50);
		double y = 50 * Math.round(target.getLocation().getY() / 50);
		double z = 50 * Math.round(target.getLocation().getZ() / 50);
		Location tempLocation = new Location(target.getWorld(), x, y, z);
		if(!tempLocation.equals(location)) {
			location = tempLocation;
			return true;
		}
		return false;
	}

	private void selectTarget() {
		Player[] players = plugin.getServer().getOnlinePlayers();
		Player tempTarget;
		// I suppose if improperly configured it would be possible for this to be an infinite loop...
		do {
			tempTarget = players[random.nextInt(players.length)];
		} while (plugin.isInHunt(tempTarget));
		target = tempTarget;
	}

	private void announceNewTarget() {
		plugin.getServer().broadcastMessage(ChatColor.RED + "There is now a bounty on " + ChatColor.DARK_RED + target.getDisplayName() + ChatColor.RED + "!");
		announceTargetBounty();
		announceTargetLocation();
	}

	private void announceTargetBounty() {
		plugin.getServer().broadcastMessage(ChatColor.RED + "The bounty on " + ChatColor.DARK_RED + target.getDisplayName() + ChatColor.RED + " is currently " + ChatColor.DARK_RED + "$" + bounty);
	}

	private void announceTargetLocation() {
		plugin.getServer().broadcastMessage(ChatColor.DARK_RED + target.getDisplayName() + ChatColor.RED + " can be found near " + ChatColor.DARK_RED + + location.getBlockX() + "," + location.getBlockY() + "," + location.getBlockZ()); 
	}

	private void announceTargetCoward() {
		plugin.getServer().broadcastMessage(ChatColor.RED + "The bounty was removed from " + ChatColor.DARK_RED + target.getDisplayName() + ChatColor.RED + " for cowardice! Shame on them!");
	}

	private void announceTargetKilled(String killerName) {
		plugin.getServer().broadcastMessage(ChatColor.DARK_RED + killerName + ChatColor.RED + " collected the bounty of " + ChatColor.DARK_RED + "$" + bounty + ChatColor.RED + " on " + ChatColor.DARK_RED + target.getDisplayName() + ChatColor.RED + "'s head!");
	}

	private void scheduleNextRun() {
		if(mode == HuntMode.HUNT) {
			int time = plugin.getConfiguration().getBountyIncrementTime();
			taskId = plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, this, time * 20);
		} else if(mode == HuntMode.RESTART) {
			int time = plugin.getConfiguration().getHuntResetTime();
			taskId = plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, this, time * 20);
		} else if(mode == HuntMode.DISCONNECT) {
			// Time to check for disconnect is one minute or 60 * 20 ticks, this could be configurable
			taskId = plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, this, 1200);
		} else {
			plugin.getLogger().severe("HuntMode was unexpected, could not re-schedule this task!");
		}
	}
}
