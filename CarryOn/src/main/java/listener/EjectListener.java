package listener;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.spigotmc.event.entity.EntityDismountEvent;

public class EjectListener implements Listener {
	@EventHandler
	public void on_right_click_for_eject(PlayerInteractEntityEvent event) {
		Player player = event.getPlayer();

		if (player.getPassengers().size() == 0)
			return;
		if (player.isSneaking())
			return;
		if (player.getInventory().getItemInMainHand().getType() != Material.AIR)
			return;

		do_eject_carried_entity(event, player);
	}

	private void do_eject_carried_entity(PlayerInteractEntityEvent event, Player player) {
		if (player.eject())
			event.setCancelled(true);
	}

	@EventHandler
	public void on_eject_something_from_player(EntityDismountEvent event) {
		Entity entity = event.getDismounted();

		if (!(entity instanceof Player))
			return;

		do_add_gravity_to_ejected_entity(event, entity);
	}

	private void do_add_gravity_to_ejected_entity(EntityDismountEvent event, Entity entity) {
		Player eject_player = (Player) entity;
		Entity mount_entity = event.getEntity();

		mount_entity.setGravity(true);
		eject_player.playSound(eject_player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 10, 29);
	}
}
