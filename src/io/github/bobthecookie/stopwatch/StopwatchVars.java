package io.github.bobthecookie.stopwatch;

import java.io.File;
import java.io.PrintStream;

public class StopwatchVars {	
	private static long totalOnlineTime = 0; //cumulative total time online
	
	private static Stopwatch	plugin;
	
	private static int numPlayersListedByPercentage = 10;
	

	//Logging/Saving
	public static final String BASEPATH = "plugins/Stopwatch/";
	
	private static PrintStream		errorLogger = System.out;
	private static File			errorLogFile = new File(BASEPATH+"logs/error.txt");
	
	private static PrintStream		centralLogger = System.out;
	private static File			centralLogFile = new File(BASEPATH+"logs/central.txt");	
	
	//Config
	public static final String	DEFAULT_INFO = "Stopwatch v. 0.0.1, made by bob_the_cookie for MCCIV (/r/mcciv).";
	public static final File		CONFIG_FILE = new File(BASEPATH+"config.yml");
	
	private static boolean	saveTotalTime	= true;
	private static boolean	savePlayerTime	= true;
	private static String		info = DEFAULT_INFO;

	//Errors
	private static final String INSUFFICIENT_COMMAND_ARGUMENTS = "Not enough arguments!";
	
	//Getters and Setters
	public static boolean getSavePlayerTime() {
		return savePlayerTime;
	}
	public static void setSavePlayerTime(boolean savePlayerTime) {
		StopwatchVars.savePlayerTime = savePlayerTime;
	}
	public static String getInfo() {
		return info;
	}
	public static void setInfo(String info) {
		StopwatchVars.info = info;
	}
	public static boolean getSaveTotalTime() {
		return saveTotalTime;
	}
	public static void setSaveTotalTime(boolean arg) {
		saveTotalTime = arg;
	}
	/**
	 * Returns the sum of all players' online time
	 * @return
	 */
	public static long getTotalOnlineTime() {
		return totalOnlineTime;
	}
	public static void setTotalOnlineTime(long totalOnlineTime) {
		if(totalOnlineTime == -1) {
			plugin.getLogger().info("ERROR: Central log file is corrupted");
			StopwatchVars.totalOnlineTime = 0;
			return;
		}
		StopwatchVars.totalOnlineTime = totalOnlineTime;
	}
	
	public static int getNumPlayersListedByPercentage() {
		return numPlayersListedByPercentage;
	}
	public static void setNumPlayersListedByPercentage(
			int numPlayersListedByPercentage) {
		StopwatchVars.numPlayersListedByPercentage = numPlayersListedByPercentage;
	}
	public static Stopwatch getPlugin() {
		return plugin;
	}
	public static void setPlugin(Stopwatch plugin) {
		StopwatchVars.plugin = plugin;
	}
	public static PrintStream getErrorLogger() {
		return errorLogger;
	}
	public static void setErrorLogger(PrintStream errorLogger) {
		StopwatchVars.errorLogger = errorLogger;
	}
	public static File getErrorLogFile() {
		return errorLogFile;
	}
	public static void setErrorLogFile(File errorLogFile) {
		StopwatchVars.errorLogFile = errorLogFile;
	}
	public static PrintStream getCentralLogger() {
		return centralLogger;
	}
	public static void setCentralLogger(PrintStream centralLogger) {
		StopwatchVars.centralLogger = centralLogger;
	}
	public static File getCentralLogFile() {
		return centralLogFile;
	}
	public static void setCentralLogFile(File centralLogFile) {
		StopwatchVars.centralLogFile = centralLogFile;
	}
	public static String getDefaultInfo() {
		return DEFAULT_INFO;
	}
	public static String getInsufficientCommandArgumentsMessage(){
		return INSUFFICIENT_COMMAND_ARGUMENTS;
	}
	
	//Other methods
	public static void addTime(long time) {
		totalOnlineTime += time;
	}
}
