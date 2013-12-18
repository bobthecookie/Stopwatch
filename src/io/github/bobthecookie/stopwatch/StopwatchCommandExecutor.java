package io.github.bobthecookie.stopwatch;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StopwatchCommandExecutor implements CommandExecutor {

	public StopwatchCommandExecutor() {
		StopwatchVars.getPlugin().getLogger()
				.info("StopwatchCommandExecutor initialized");
	}

	@Override
	// public boolean onCommand(CommandSender sender, Command cmd, String label,
	// String[] args) {
	// String command = cmd.getName();
	// if (command.equalsIgnoreCase("st")) {
	// if (args.length > 0) { // /st <subcommand>
	// // if (args[0].equalsIgnoreCase("test")) {
	// // if (args.length == 2) {
	// // StopwatchUtil.savePlayerInfo(args[1],
	// // (long) (1000 * (3600 * Math.random())), null);
	// // } else {
	// // sender.sendMessage(StopwatchVars
	// // .getInsufficientCommandArgumentsMessage());
	// // }
	// // } //end of /st test
	// if (args[0].equalsIgnoreCase("backup")) {
	// if(sender.hasPermission("stopwatch.admin")){
	// StopwatchUtil.backup();
	// }
	// else{
	// sender.sendMessage(StopwatchVars.getPlugin().getCommand("st backup").getPermissionMessage());
	// }
	// }
	// if (args.length > 1) { // /st <subcommand> <arg>
	// if (args[0].equalsIgnoreCase("info")) {
	// if (args[1].equalsIgnoreCase("player")) {
	// if (args.length != 3) {
	// sender.sendMessage(StopwatchVars
	// .getInsufficientCommandArgumentsMessage());
	// } else {
	// String msg = "";
	// long t = Long.parseLong(StopwatchUtil
	// .loadPlayerInfo(args[2])[0].toString());
	// if (t == -1) {
	// msg = "Player does not exist";
	// } else {
	// t /= 1000;
	// int h = ((int) t) / 3600;
	// t -= h * 3600;
	// int m = ((int) t) / 60;
	// t -= m * 60;
	// msg = "Total Online Time: " + h + " hours "
	// + m + " minutes " + t + " seconds";
	// }
	// sender.sendMessage(msg);
	// }
	// } else if (args[1].equalsIgnoreCase("global")) {
	// StopwatchUtil.updateCurrentTotalSessionTime();
	// long t = StopwatchVars.getTotalOnlineTime();
	// t /= 1000;
	// int h = ((int) t) / 3600;
	// t -= h * 3600;
	// int m = ((int) t) / 60;
	// t -= m * 60;
	// sender.sendMessage("Total Online Time: " + h
	// + " hours " + m + " minutes " + t
	// + " seconds");
	// sender.sendMessage("Players by Percentage:");
	//
	// }
	// }
	// if (args.length > 2) { // /st <subcommand> <arg> <arg>
	//
	// }
	// } else { // if args.length <= 1
	// if (args[0].equalsIgnoreCase("info")) {
	// sender.sendMessage(StopwatchVars.getInfo());
	// }
	// }
	// } else { // if args.length == 0 /st
	// sender.sendMessage(StopwatchVars.getInfo());
	// }
	// }
	// return false;
	// }
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		int numArgs = args.length;
		String command = cmd.getName();
		boolean sentByPlayer = sender instanceof Player;
		String info = StopwatchVars.getInfo();

		if (!command.equalsIgnoreCase("st"))
			return false;

		switch (numArgs) {
		case 0: // /st
			sender.sendMessage(StopwatchVars.getInfo());
			break;
		case 1:
			if (args[1].equalsIgnoreCase("help"))
				sender.sendMessage("/st info global\tReturns global info\n/st info player <player>\tReturns player info");
			if (args[0].equalsIgnoreCase("info"))
				sender.sendMessage(StopwatchVars.getInfo());
			if (args[0].equalsIgnoreCase("backup"))
				if (sender.hasPermission("stopwatch.backup")){
					StopwatchUtil.backup();
					sender.sendMessage("Backup complete!");
				}
			if (args[0].equalsIgnoreCase("test")) {
				long t = (long) (Math.random() * 10000);
				String n = String.copyValueOf(new char[] { (char) (Math
						.random() * 256) });
				StopwatchUtil.savePlayerInfo(n, t, null);
			}
			// TODO add permission message
			break;
		case 2: // /st < > < >
			if (args[0].equalsIgnoreCase("info")) { // /st info
				if (args[1].equalsIgnoreCase("global")) {
//					sender.sendMessage(getGlobalTime());
					StopwatchPlayer[] p = StopwatchUtil
							.percentageTimeByPlayer();
					sender.sendMessage("Players listed by percentage of total time they personally were on for:");
					for (int i = 0; i < StopwatchVars
							.getNumPlayersListedByPercentage(); i++) {
						if (p.length == 0)
							break;
						sender.sendMessage((i + 1) + ": "
								+ p[i].formattedInfo());
					}
				}
			}
			if (args[0].equalsIgnoreCase("test")) {
				long t = (long) (Math.random() * 10000000);
				StopwatchUtil.savePlayerInfo(args[1], t, null);
			}
			break;
		case 3: // /st < > < > < >
			if (args[0].equalsIgnoreCase("info")) { // /st info < > < >
				if (args[1].equalsIgnoreCase("player")) // /st info player < >
					sender.sendMessage(getPlayerTime(new StopwatchPlayer(args[2])));
				
			}
		}

		return false;
	}

	private String getGlobalTime() {
		StopwatchUtil.updateCurrentTotalSessionTime();
		long t = StopwatchVars.getTotalOnlineTime();
		return formatTime(t);
	}

	private String getPlayerTime(StopwatchPlayer player) {
		return formatTime(player.getTotalTime());
	}

	private String formatTime(long t) {
		int[] time = formatTimeToNumbers(t);
		if (time == null)
			return "Player does not exist!";
		return "Total Online Time: " + time[0] + " hours " + time[1]
				+ " minutes " + time[2] + " seconds";
	}

	private int[] formatTimeToNumbers(long t) {
		if (t == -1)
			return null;
		t /= 1000;
		int h = ((int) t) / 3600;
		t -= h * 3600;
		int m = ((int) t) / 60;
		t -= m * 60;
		return new int[] { h, m, (int) t };
	}
}
