package net.betterverse.manhunt;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Hunt implements Runnable {
	private static ManHunt plugin;
	private HuntMode mode;

	private Player target;
	private Location location;
	private int bounty;

	private final Random random = new Random();

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
			
		} else if(mode == HuntMode.RESETART) {
			
		} else {
			plugin.getLogger().severe("HuntMode was not HUNT or RESTART!");
		}
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
		plugin.getServer().broadcastMessage("There is now a bounty on " + target.getDisplayName() + "!");
		announceTargetBounty();
		announceTargetLocation();
	}

	private void announceTargetBounty() {
		plugin.getServer().broadcastMessage("The bounty on " + target.getDisplayName() + " is currently " + "$" + bounty);
	}

	private void announceTargetLocation() {
		plugin.getServer().broadcastMessage(target.getDisplayName() + " can be found near " + location.getBlockX() + "," + location.getBlockY() + "," + location.getBlockZ()); 
	}

	private void scheduleNextRun() {
		if(mode == HuntMode.HUNT) {
			int time = plugin.getConfiguration().getBountyIncrementTime();
			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, this, time * 20);
		} else if(mode == HuntMode.RESETART) {
			int time = plugin.getConfiguration().getHuntResetTime();
			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, this, time * 20);
		}
	}
}
