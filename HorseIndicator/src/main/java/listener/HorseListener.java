package listener;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;

import util.ScoreHelper;

public class HorseListener implements Listener {
  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent event) {
      Player player = event.getPlayer();
      if(ScoreHelper.hasScore(player)) {
          ScoreHelper.removeScore(player);
      }
  }

	@EventHandler
	public void onHorseExit(VehicleExitEvent event) {
		
		Vehicle vehicle = event.getVehicle();

		if (vehicle instanceof AbstractHorse) {
			Entity rider = event.getExited();

			if (rider.getType() == EntityType.PLAYER) {
				Player player = (Player) rider;
				if (ScoreHelper.hasScore(player)) {
					ScoreHelper.removeScore(player);
				  player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
		      
				}
			}
		}
	}

	@EventHandler
	public void onHorseRide(VehicleEnterEvent event) {
		Vehicle vehicle = event.getVehicle();

		if (vehicle instanceof AbstractHorse) {
			AbstractHorse horse = (AbstractHorse) vehicle;
			Entity rider = event.getEntered();

			if (rider.getType() == EntityType.PLAYER) {
				Player player = (Player) rider;

				Double speed = (horse.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getValue());
				Double speedBlock = speed * 50;

				Double jump = (horse.getJumpStrength());
				Double jumpBlock = 
						-0.1817584952 * Math.pow(jump, 3) 
						+ 3.689713992 * Math.pow(jump, 2) 
						+ 2.128599134 * jump
						- 0.343930367;
				Double health = (horse.getMaxHealth());


				ScoreHelper helper = ScoreHelper.createScore(player);
				helper.setTitle("Horse Indicator");
				helper.setSlot(6, "&7&m------------");
				helper.setSlot(5, "&aHealth&f : " + String.format("%.0f", health));
				helper.setSlot(4, "");
				helper.setSlot(3, "&aSpeed&f : " + String.format("%.1f", speedBlock) + " b/s");
				helper.setSlot(2, "&aJump&f  : " + String.format("%.1f", jumpBlock) + " b");
				helper.setSlot(1, "&7&m------------");
			}
		}

	}

}
