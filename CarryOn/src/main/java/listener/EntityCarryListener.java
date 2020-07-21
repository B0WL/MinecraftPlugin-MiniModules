package listener;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;


public class EntityCarryListener implements Listener {
	@EventHandler
	public void on_interact_entity(PlayerInteractEntityEvent event) {
		Player player = event.getPlayer();

		if (!player.isSneaking())
			return;
		if (player.getInventory().getItemInMainHand().getType() != Material.AIR)
			return;
		Entity carry_target = event.getRightClicked();
		if (!(carry_target instanceof LivingEntity))
			return;
		List<Entity> carried_entities = player.getPassengers();
		if (!carried_entities.isEmpty())
			return;
		event.setCancelled(true);
		
		do_carry_entity(player, carry_target);
	}

	private void do_carry_entity(Player player, Entity carry_target) {
		player.addPassenger(carry_target);
	}

}
