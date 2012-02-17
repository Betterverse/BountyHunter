package uk.co.spudstabber.bountyhunter;

import org.bukkit.ChatColor;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityListener;

public class PlayerKillListener extends EntityListener {
	
	private BountyHunter plugin;
	
	public PlayerKillListener(BountyHunter plugin)
	{
		this.plugin = plugin;
	}

	public void onEntityDeath(EntityDeathEvent e) {
		
		if (!(e.getEntity() instanceof Player)) 
		{
			return;
		}
		
		Player player = (Player) e.getEntity();
		
		EntityDamageEvent e2 = e.getEntity().getLastDamageCause();
		
		if (!(e2 instanceof EntityDamageByEntityEvent))
		{
			return;
		}
		
		EntityDamageByEntityEvent e3 = (EntityDamageByEntityEvent) e2;
		
		if (!(e3.getDamager() instanceof Player))
		{
			if((e3.getDamager() instanceof Arrow))
			{
				Arrow arrow = (Arrow) e3.getDamager();
				if(arrow.getShooter() instanceof Player)
				{
					Player shooter = (Player) arrow.getShooter();
					if(plugin.isPlayerInHashMap(player) == true)
					{
						plugin.getServer().broadcastMessage(ChatColor.GOLD + player.getName() + ChatColor.GREEN + " has been killed by " + ChatColor.GOLD + shooter.getName() + "!");
						plugin.getServer().broadcastMessage(ChatColor.GOLD + shooter.getName() + ChatColor.GREEN + " has been rewarded the bounty of $" + ChatColor.GOLD + plugin.bountyplayer.getBounty().toString() + "0 !");
						BountyHunter.economy.depositPlayer(shooter.getName(), plugin.bountyplayer.getBounty());
						plugin.bountytimer.removeBountiedPlayer();
						plugin.bountytimer.getNewBountyPlayer();
					}
					else if(plugin.isPlayerInHashMap(shooter))
					{
						plugin.getServer().broadcastMessage(ChatColor.DARK_RED + shooter.getName() + ChatColor.RED + " has struck again! " + ChatColor.DARK_RED + player.getName() + ChatColor.RED + "has fallen at his feet!");
					}
				}
			}
		}
		
		Player killer = (Player) e3.getDamager();
		
		if(plugin.isPlayerInHashMap(player) == true)
		{
			plugin.getServer().broadcastMessage(ChatColor.GOLD + player.getName() + ChatColor.GREEN + " has been killed by " + ChatColor.GOLD + killer.getName() + "!");
			plugin.getServer().broadcastMessage(ChatColor.GOLD + killer.getName() + ChatColor.GREEN + " has been rewarded the bounty of $" + ChatColor.GOLD + plugin.bountyplayer.getBounty().toString() + "0 !");
			BountyHunter.economy.depositPlayer(killer.getName(), plugin.bountyplayer.getBounty());
			plugin.bountytimer.removeBountiedPlayer();
			plugin.bountytimer.getNewBountyPlayer();
		}
		else if(plugin.isPlayerInHashMap(killer))
		{
			plugin.getServer().broadcastMessage(ChatColor.DARK_RED + killer.getName() + ChatColor.RED + " has struck again! " + ChatColor.DARK_RED + player.getName() + ChatColor.RED + " has fallen at his feet!");
		}
	}
	
}
