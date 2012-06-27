package net.betterverse.manhunt;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Hunt implements Runnable {
	private static ManHunt plugin;

	private static Player target;
	private static Location location;

	private static final Random random = new Random();

	public Hunt(ManHunt manHunt) {
		plugin = manHunt;

		Player[] players = plugin.getServer().getOnlinePlayers();
		target = players[random.nextInt(players.length)];
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

}
