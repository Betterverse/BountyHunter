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
			
		} else if(mode == HuntMode.RESTART) {
			
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
		target = players[random.nextInt(players.length)];
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
