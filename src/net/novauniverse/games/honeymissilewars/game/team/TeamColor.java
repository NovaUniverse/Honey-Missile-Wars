package net.novauniverse.games.honeymissilewars.game.team;

import org.bukkit.ChatColor;

public enum TeamColor {
	RED(ChatColor.RED, "Red"), GREEN(ChatColor.GREEN, "Green");

	private ChatColor chatColor;
	private String name;

	private TeamColor(ChatColor chatColor, String name) {
		this.chatColor = chatColor;
		this.name = name;
	}

	public ChatColor getChatColor() {
		return chatColor;
	}

	public String getName() {
		return name;
	}

	public String getLowerCaseName() {
		return name.toLowerCase();
	}

	public TeamColor getOpposite() {
		switch (this) {
		case GREEN:
			return RED;

		case RED:
			return GREEN;

		default:
			return null;
		}
	}
}