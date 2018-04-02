package de.bananaco.report;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;

import de.bananaco.report.commands.Commands;
import de.bananaco.report.listeners.GUIListener;
import de.bananaco.report.listeners.ModChat;
import de.bananaco.report.listeners.ReportListener;
import de.bananaco.report.msg.MessageManager;
import de.bananaco.report.report.ReportManager;

public class ReportPlugin extends JavaPlugin {

	private ReportManager rm;
	private Config config;
	private ReportListener listener;
	private ModChat modchat;
	private Commands command;
	private MessageManager msgManager;
	private GUIListener gui;

	public ReportPlugin() {
		msgManager = new MessageManager(this);
		rm = new ReportManager(this);
		config = new Config(this);
		listener = new ReportListener(this);
		modchat = new ModChat();
		command = new Commands(this);
		gui = new GUIListener(this);
	}

	@Override
	public void onDisable() {
		rm.save();
		log("Disabled");
	}

	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(listener, this);
		getServer().getPluginManager().registerEvents(modchat, this);
		getServer().getPluginManager().registerEvents(gui, this);
		getCommand("report").setExecutor(command);
		getCommand("read").setExecutor(command);
		getCommand("gotoreport").setExecutor(command);
		getCommand("resolve").setExecutor(command);
		getCommand("unresolve").setExecutor(command);
		getCommand("modchat").setExecutor(command);
		getCommand("comment").setExecutor(command);
		getCommand("rgui").setExecutor(command);
		registerPermissions();
		msgManager.load();
		rm.load();
		config.load();
		log("Enabled");
	}

	public void log(String message) {
		String newMessage = ChatColor.stripColor(message);
		getLogger().log(Level.INFO, "{0}", newMessage);
	}

	public void log(String message, Level level) {
		String newMessage = ChatColor.stripColor(message);
		getLogger().log(level, "{0}", newMessage);
	}

	public void registerPermissions() {
		Map<String, Boolean> children = new HashMap<String, Boolean>();
		// Add all the permission nodes we'll be using
		children.put("breport.report", true);
		children.put("breport.read", true);
		children.put("breport.gotoreport", true);
		children.put("breport.resolve", true);
		children.put("breport.unresolve", true);
		children.put("breport.modchat", true);
		children.put("breport.comment", true);
		children.put("breport.gui", true);
		// Put them under a parent
		Permission perm = new Permission("breport.*", PermissionDefault.OP, children);
		getServer().getPluginManager().addPermission(perm);
	}

	public String getName(CommandSender sender) {
		String name;
		if (!(sender instanceof Player)) {
			name = "CONSOLE";
		} else {
			name = sender.getName();
		}
		return name;
	}

	public ReportManager getReportManager() {
		return rm;
	}

	public Config getConf() {
		return config;
	}

	public MessageManager getMsgManager() {
		return msgManager;
	}

	public File getDirectory(String Directory_Name) {
		return new File(getDataFolder(), Directory_Name);
	}
}
