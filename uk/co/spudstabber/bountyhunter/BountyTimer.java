package uk.co.spudstabber.bountyhunter;

import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class BountyTimer {
	
	public BountyHunter plugin;
	private BountyPlayer bounty;
	
	private Player bountyplayer;
	
	private int timeAfterKill;
	private int percentageUp;
	private int percentageUpTime;
	private int offlineTime;
	
	private int derp;
	private int herp;

	public BountyTimer(BountyHunter plugin)
	{
		this.plugin = plugin;
		this.bounty = plugin.bountyplayer;
		this.timeAfterKill = plugin.getConfig().getInt("times.time-after-kill");
		this.percentageUp = plugin.getConfig().getInt("money.percentage-up");
		this.offlineTime = plugin.getConfig().getInt("times.offline-time");
		this.percentageUpTime = plugin.getConfig().getInt("times.increase-bounty-time");
	}
	
	
	
	public void getNewBountyPlayer()
	{
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable()
			{
				public void run()
				{
					Player[] derp = plugin.getServer().getOnlinePlayers();
					int herp = derp.length;
					Random rand = new Random();
					int orly = rand.nextInt(herp);
					bountyplayer = derp[orly];
					bounty.newBounty(bountyplayer, bountyplayer.getLocation());
					increaseBounty();
				}
			},
		timeAfterKill * 20);
	}
	
	public void getBountyLocation()
	{
		
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable()
		{
			public void run()
			{
				
				int x = 50 * Math.round(bounty.getX() / 50);
				int y = 50 * Math.round(bounty.getY() / 50);
				int z = 50 * Math.round(bounty.getZ() / 50);
				plugin.getServer().broadcastMessage(ChatColor.RED + "The current bountied player is " + ChatColor.DARK_RED + bountyplayer.getName() + ChatColor.RED + ", and their bounty is currently $" + ChatColor.DARK_RED + bounty.getBounty() + "0" + ChatColor.RED + ". The bountied player is currently somewhere near " + x + ", " + y + ", " + z + "!");				

			}
		}, 20);
	}
		
	public void increaseBounty()
	{
		derp = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(this.plugin, new Runnable()
		{
			public void run()
			{
				Double presentbounty = bounty.getBounty();
				Double newbounty = (presentbounty / 100) * (100 + percentageUp);
				try
				{
				Player update = plugin.getServer().getPlayer(bountyplayer.getName());
				if(newbounty.intValue() <= plugin.getConfig().getInt("money.max-bounty"))
				{
					bounty.updateMoney(newbounty.intValue());
				}
				else
				{
					bounty.updateMoney(plugin.getConfig().getInt("money.max-bounty"));
				}
				bounty.updateBounty(update, update.getLocation());
				bountyplayer = update;				
				}
				catch(Exception e)
				{
					plugin.getServer().broadcastMessage(ChatColor.RED + "The bountied player appears to be offline!");
				}
			}
		}, percentageUpTime * 20, percentageUpTime * 20);
		
	}
	
	public void bountyCowardice()
	{
		herp = plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable()
		{
			public void run()
			{
				removeBountiedPlayer();
				plugin.getServer().broadcastMessage(ChatColor.RED + "The bounty was removed from " + ChatColor.DARK_RED + bounty.getPlayer().getName() + ChatColor.RED + " for cowardice! Shame on them!");
				getNewBountyPlayer();
			}
		}, offlineTime * 20);
	}
	
	public void removeBountiedPlayer()
	{
		plugin.clearHashMap();
		plugin.getServer().getScheduler().cancelTask(derp);
	}
	
	public void cancelBountyRemoval()
	{
		plugin.getServer().getScheduler().cancelTask(herp);
	}
}
