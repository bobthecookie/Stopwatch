package io.github.bobthecookie.stopwatch;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public final class StopwatchPlayerListener implements Listener {
	;private final Stopwatch plugin;
	public StopwatchPlayerListener(Stopwatch plugin){
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this,plugin);
	}
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event){
		StopwatchVars.getPlugin().addPlayer(event.getPlayer().getName(), System.currentTimeMillis());
		plugin.getLogger().info(event.getPlayer().getName()+" has joined");
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event){
		long time = StopwatchVars.getPlugin().removePlayer(event.getPlayer().getName());
		StopwatchUtil.savePlayerInfo(event.getPlayer().getName(), time, null);
		plugin.getLogger().info("Saved "+event.getPlayer().getName()+" info");
	}
}
