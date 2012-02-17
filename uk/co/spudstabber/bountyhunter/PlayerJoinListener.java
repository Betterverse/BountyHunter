package uk.co.spudstabber.bountyhunter;

import org.bukkit.ChatColor;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;

public class PlayerJoinListener extends PlayerListener {
	
	private BountyHunter plugin;

	public PlayerJoinListener(BountyHunter plugin)
	{
		this.plugin = plugin;
	}
	
	public void onPlayerJoin(PlayerJoinEvent e)
	{
		if(plugin.isPlayerInHashMap(e.getPlayer()) == true)
		{
			plugin.bountytimer.cancelBountyRemoval();
			plugin.getServer().broadcastMessage(ChatColor.DARK_RED + plugin.bountyplayer.getPlayer().getName() + ChatColor.RED + " has returned! Let the hunt recommence!");
		}
		else if(plugin.getServer().getOnlinePlayers().length >= 3)
		{
			if(!(plugin.isHashMapEmpty()))
			{
				return;
			}
			
			plugin.bountytimer.getNewBountyPlayer();
		}
	}

}
