package net.betterverse.manhunt;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ManHuntListener implements Listener {
	private static ManHunt plugin;

	public ManHuntListener(ManHunt manHunt) {
		plugin = manHunt;
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onPlayerJoin(PlayerJoinEvent event) {
		int playerCount = plugin.getServer().getOnlinePlayers().length;

		if(!plugin.getConfiguration().isHuntLimit(playerCount)) return;

		int huntNumber = plugin.getConfiguration().getHuntNumberForLimit(playerCount);

		if(plugin.getActiveHuntCount() >= huntNumber) return;

		plugin.startNewHunt();
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
	
		if(plugin.isInHunt(player)) {
			Hunt hunt = plugin.getHuntFromPlayer(player);
			hunt.targetDisconnected();
		}
	}
}
