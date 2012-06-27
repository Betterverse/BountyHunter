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
		announceTarget();

		mode = HuntMode.HUNT;
		scheduleNextRun();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

	private void updateLocation() {
		double x = 50 * Math.round(target.getLocation().getX() / 50);
		double y = 50 * Math.round(target.getLocation().getY() / 50);
		double z = 50 * Math.round(target.getLocation().getZ() / 50);
		location = new Location(target.getWorld(), x, y, z);
	}

	private void selectTarget() {
		Player[] players = plugin.getServer().getOnlinePlayers();
		target = players[random.nextInt(players.length)];
	}

	private void announceTarget() {
		// TODO: Announce
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
