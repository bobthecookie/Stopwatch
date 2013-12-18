package io.github.bobthecookie.stopwatch;

import java.io.File;
import java.util.ArrayList;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Stopwatch extends JavaPlugin {
	private Long startTime;
	private ArrayList<StopwatchPlayer>players = new ArrayList<StopwatchPlayer>(1);
	private ArrayList<Long> playerLoginTimes = new ArrayList<Long>(1);
	private StopwatchPlayerListener playerListener;
	
	@Override
	public void onEnable(){
		startTime = System.currentTimeMillis();
		
		//Load playerListener
		playerListener = new StopwatchPlayerListener(this);
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(playerListener, this); //enabling playerListener
		
		//Load StopwatchUtil
		StopwatchUtil.init(this);
		getLogger().info("StopwatchUtil initialized");
		
		//Load command executor
		StopwatchCommandExecutor commandListener = new StopwatchCommandExecutor();
		getCommand("st").setExecutor(commandListener);
		
		//Load player save files
		File dir = new File(StopwatchVars.BASEPATH+"saves/");
		if(!dir.exists()){
			getLogger().info(dir.getName()+" does not exist! Initializing");
			dir.mkdirs();
			if(dir.exists()){
				getLogger().info(dir.getName()+" successfully initialized");
			}
		}
		//Load config files
		YamlConfiguration config = new YamlConfiguration();		
		StopwatchUtil.loadConfig(config);
		
		getLogger().info("Stopwatch v"+getDescription().getVersion()+" enabled successfully!");
	}
	
	@Override
	public void onDisable(){
		float totalTime = System.currentTimeMillis()-startTime;
		StopwatchUtil.onDisable();
		this.getLogger().info("Plugin ran for "+totalTime+" milliseconds");
	}
	public ArrayList<StopwatchPlayer> getPlayers(){
		return players;
	}
	public ArrayList<Long> getPlayerLoginTimes(){
		return playerLoginTimes;
	}
	public void setPlayers(ArrayList<StopwatchPlayer> arg){
		players = arg;
	}
	public void setPlayerLoginTimes(ArrayList<Long> arg){
		playerLoginTimes = arg;
	}
	public void addPlayer(String name,long time){ //adds a player to the lists
		StopwatchPlayer p = new StopwatchPlayer(name);
		players.add(p);
		playerLoginTimes.add(time);
	}
	public long removePlayer(String n){ //removes player from lists, returns time online
		int index = find(players,n);
		players.remove(index);
		long time = System.currentTimeMillis();
		time -= playerLoginTimes.get(index);
		playerLoginTimes.remove(index);
		return time;
	}
	public void removeAllPlayers(){
		while(!players.isEmpty()){
			StopwatchPlayer p = players.get(0);
			players.remove(0);
			long time = System.currentTimeMillis()-playerLoginTimes.get(0);
			playerLoginTimes.remove(0);
			StopwatchUtil.savePlayerInfo(p.getName(),time, null);
		}
	}
	public long getCurrentSessionTime(StopwatchPlayer p){
		return System.currentTimeMillis()-playerLoginTimes.get(players.indexOf(p));
	}
	private int find(ArrayList<StopwatchPlayer> array,String name){
		for(int i = 0; i < array.size(); i++){
			if(array.get(i).getName().equalsIgnoreCase(name)) return i;
		}
		return -1;
	}
}