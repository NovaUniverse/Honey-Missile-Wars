package net.novauniverse.games.honeymissilewars.game;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import net.zeeraa.novacore.spigot.module.modules.game.Game;
import net.zeeraa.novacore.spigot.module.modules.game.GameEndReason;
import net.zeeraa.novacore.spigot.module.modules.game.elimination.PlayerQuitEliminationAction;

public class HoneyMissileWarsGame extends Game {
	private boolean started;
	private boolean ended;
	
	@Override
	public void onLoad() {
		started = false;
		ended = false;
	}
	
	@Override
	public String getName() {
		return "HoneyMissileWars";
	}

	@Override
	public String getDisplayName() {
		return "Honey Missile Wars";
	}

	@Override
	public PlayerQuitEliminationAction getPlayerQuitEliminationAction() {
		return PlayerQuitEliminationAction.DELAYED;
	}

	@Override
	public boolean eliminatePlayerOnDeath(Player player) {
		return false;
	}

	@Override
	public boolean isPVPEnabled() {
		return true;
	}

	@Override
	public boolean autoEndGame() {
		return false;
	}

	@Override
	public boolean hasStarted() {
		return started;
	}

	@Override
	public boolean hasEnded() {
		return ended;
	}

	@Override
	public boolean isFriendlyFireAllowed() {
		return false;
	}

	@Override
	public boolean canAttack(LivingEntity attacker, LivingEntity target) {
		// TODO: check
		return true;
	}

	@Override
	public void onStart() {
		if(started) {
			return;
		}
		started = true;
		this.sendBeginEvent();
	}

	@Override
	public void onEnd(GameEndReason reason) {
		if(!started) {
			return;
		}
		
		if(ended) {
			return;
		}
		
		ended = true;
	}
}