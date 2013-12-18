package io.github.bobthecookie.stopwatch;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class StopwatchUtil {
	public static void init(Stopwatch stopwatch) {
		StopwatchVars.setPlugin(stopwatch);
		StopwatchVars.setTotalOnlineTime(loadCentralLog(StopwatchVars
			.getCentralLogFile()));
		StopwatchVars.setCentralLogger(initLogger(
				StopwatchVars.getCentralLogger(),
				StopwatchVars.getCentralLogFile(), System.out));
		StopwatchVars.setErrorLogger(initLogger(StopwatchVars.getErrorLogger(),
				StopwatchVars.getErrorLogFile(), System.out));
	}

	public static StopwatchPlayer[] percentageTimeByPlayer() {
		backup();
		File savesDirectory = new File(StopwatchVars.BASEPATH + "saves/");
		File[] files = savesDirectory.listFiles();
		int playerCount = files.length;
		StopwatchPlayer[] p = new StopwatchPlayer[playerCount];
		for(int i = 0; i < playerCount; i++){
			p[i] = new StopwatchPlayer(files[i].getName().replace(".txt", ""));
		}
		for(int i = 0; i < p.length; i++){
			swap(p,findMin(p,i),i);
		}
		StopwatchVars.setNumPlayersListedByPercentage(p.length);
		return p;
	}
	private static void swap(StopwatchPlayer[] p,int a,int b){
		StopwatchPlayer t = p[a];
		p[a] = p[b];
		p[b] = t;
	}
	private static int findMin(StopwatchPlayer[] p,int min){
		int index = min;
		for(int i = min; i < p.length; i++){
			if(p[i].compareTo(p[index]) < 0)
				index = i;
		}
		return index;
	}

	public static void updateCurrentTotalSessionTime() {
		long t = 0;
		Scanner f = null;
		try{
			f = new Scanner(StopwatchVars.getCentralLogFile());
		} catch(FileNotFoundException e){
			e.printStackTrace(StopwatchVars.getErrorLogger());
		}
		Object obj = null;
		while(f.hasNext()){
			obj = f.next();
		}
		t = (Long)obj;
		for (StopwatchPlayer p: StopwatchVars.getPlugin().getPlayers()){
			t += StopwatchVars.getPlugin().getCurrentSessionTime(p);
			p.setTotalTime(StopwatchVars.getPlugin().getCurrentSessionTime(p));
		}
		StopwatchVars.getCentralLogger().print(t);
	}

	private static long loadCentralLog(File file) {
		Scanner infile = null;
		try {
			infile = new Scanner(file);
		} catch (FileNotFoundException e) {
			return 0;
		}
		try {
			return infile.nextLong();
		} catch (NoSuchElementException e) {
			return -1; // code for "logfile is being a little bitch"
		}
	}

	public static void onDisable() {
		StopwatchVars.getPlugin().removeAllPlayers();
		StopwatchVars.getCentralLogger().print(
				StopwatchVars.getTotalOnlineTime());
		StopwatchVars.getPlugin().getLogger()
				.info("Saved " + StopwatchVars.getTotalOnlineTime());
	}

	public static void backup() {
		onDisable();
		for (Player p : StopwatchVars.getPlugin().getServer()
				.getOnlinePlayers()) {
			StopwatchVars.getPlugin().addPlayer(p.getName(),
					System.currentTimeMillis());
		}
	}

	private static PrintStream initLogger(PrintStream logger, File file,
			PrintStream defaultStream) {
		if (!file.exists()) { // if the file doesn't exist create it
			file.getParentFile().mkdirs(); // create directories if they don't
											// exist
			try {
				file.createNewFile(); // create the file
				StopwatchVars.getPlugin().getLogger().info(file.getName() + " created!");
			} catch (IOException e) {
				StopwatchVars.getPlugin().getLogger()
						.info("IOException on " + file.getName() + " creation");
			}
		}
		try {
			logger = new PrintStream(file);
		} catch (FileNotFoundException e) {
			StopwatchVars
					.getPlugin()
					.getLogger()
					.info("Could not initialize logger, " + file.getPath()
							+ " does not exist");
			StopwatchVars.getPlugin().getLogger().info("Set logger to default");
			logger = System.out;
		}
		return logger;
	}

	public static void savePlayerInfo(String name, long timeOnline,
			String[] args) {
		if (StopwatchVars.getSavePlayerTime()) {
			File dir = new File(StopwatchVars.BASEPATH + "saves");
			if (!dir.exists()) {
				StopwatchVars.getPlugin().getLogger()
						.info(dir.getName() + " does not exist! Initializing");
				dir.mkdirs();
				if (dir.exists()) {
					StopwatchVars.getPlugin().getLogger()
							.info(dir.getName() + " successfully initialized");
				}
			}
			File file = new File(StopwatchVars.BASEPATH + "saves/" + name
					+ ".txt");
			long totalTime = timeOnline;
			if (!file.exists()) {
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
					e.printStackTrace(StopwatchVars.getErrorLogger());
				}
			}
			// StopwatchVars.getPlugin().getLogger().info(file.getPath()+" exists");
			try {
				Scanner fileScanner = new Scanner(file);
				// StopwatchVars.getPlugin().getLogger().info(""+totalTime);
				if (fileScanner.hasNextLong())
					totalTime += fileScanner.nextLong();
				// StopwatchVars.getPlugin().getLogger().info(""+totalTime);
			} catch (FileNotFoundException e) {
				StopwatchVars.getPlugin().getLogger()
						.info(e.getCause().toString());
				e.printStackTrace(StopwatchVars.getErrorLogger());
			} catch (NoSuchElementException e2) {
				StopwatchVars.getPlugin().getLogger()
						.info("No such element in " + file.getName());
			}
			PrintWriter writer = null;
			try {
				writer = new PrintWriter(file);
			} catch (FileNotFoundException e) {
				StopwatchVars.getPlugin().getLogger()
						.info(e.getCause().toString());
				e.printStackTrace(StopwatchVars.getErrorLogger());
			}
			writer.print(totalTime);
			writer.flush();
			StopwatchVars.getPlugin().getLogger()
					.info("Saved info for player " + name);
//			StopwatchVars.getCentralLogger().append("Saved info for player "+name+"\n");
		}
		if (StopwatchVars.getSaveTotalTime()) {
			StopwatchVars.addTime(timeOnline);
			StopwatchVars.getPlugin().getLogger()
					.info(StopwatchVars.getTotalOnlineTime() + "");
		}
	}

	public static StopwatchPlayer loadPlayerInfo(String player) {
		final int numInfoItems = 1; // to be changed later

		File f = new File(StopwatchVars.BASEPATH + "saves/" + player + ".txt");
		if (!f.exists()) {
			return new StopwatchPlayer(player,-1);
		}
		Scanner infile = null;
		try {
			infile = new Scanner(f);
		} catch (FileNotFoundException e) {
			StopwatchVars.getPlugin().getLogger().info("Scanner for " + player + ".txt could not be created");
			return new StopwatchPlayer(player,-1);
		}
		StopwatchPlayer p = new StopwatchPlayer(player,infile.nextLong());
		return p;
	}

	public static void loadConfig(YamlConfiguration config) {
		Configuration defaults = new YamlConfiguration();
		defaults.set("saveTotalTime", StopwatchVars.getSaveTotalTime());
		defaults.set("savePlayerTime", StopwatchVars.getSavePlayerTime());
		defaults.set("info", StopwatchVars.getDefaultInfo());
		config.setDefaults(defaults);

		if (StopwatchVars.CONFIG_FILE.exists()) {
			try {
				config.load(StopwatchVars.CONFIG_FILE);
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (InvalidConfigurationException e1) {
				e1.printStackTrace();
			}

			if (config.isSet("saveTotalTime")) {
				StopwatchVars.setSaveTotalTime(config
						.getBoolean("saveTotalTime"));
			} else {
				try {
					config.save(StopwatchVars.CONFIG_FILE);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (config.isSet("savePlayerTime")) {
				StopwatchVars.setSavePlayerTime(config
						.getBoolean("savePlayerTime"));
			}
			if (config.isSet("info")) {
				StopwatchVars.setInfo(config.getString("info"));
			}
		} else {
			try {
				StopwatchVars.CONFIG_FILE.createNewFile();
				config.save(StopwatchVars.CONFIG_FILE);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
