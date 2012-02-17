package uk.co.spudstabber.bountyhunter;

import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener extends PlayerListener {
	
	private BountyHunter plugin;

	public PlayerQuitListener(BountyHunter plugin)
	{
		this.plugin = plugin;
	}
	
	public void onPlayerQuit(PlayerQuitEvent e)
	{
		if(plugin.isPlayerInHashMap(e.getPlayer()) == true)
		{
			plugin.bountytimer.bountyCowardice();
			if(this.checkPlayerCount() == true)
			{
				this.lessThanThree();
			}
		}
		else if(this.checkPlayerCount() == true)
		{
			this.lessThanThree();
		}
	}
	
	public boolean checkPlayerCount()
	{
		if(plugin.getServer().getOnlinePlayers().length <= 2)
		{
			return true;
		}
		return false;
	}
	
	public void lessThanThree()
	{
		plugin.bountytimer.cancelBountyRemoval();
		plugin.bountytimer.removeBountiedPlayer();
	}

}
