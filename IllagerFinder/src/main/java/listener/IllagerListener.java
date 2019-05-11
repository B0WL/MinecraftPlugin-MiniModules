package listener;

import java.util.Collection;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.Statistic;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerStatisticIncrementEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import main.IllagerFinder;
import util.FireworkManager;

public class IllagerListener implements Listener {
	FileConfiguration config = IllagerFinder.instance.getConfig();

	@EventHandler
	public void onIillagerSpawn(EntitySpawnEvent event) {
		if (config.getBoolean("on_spawn")) {
			Entity entity = event.getEntity();

			switch (entity.getType()) {
			case PILLAGER:
			case VINDICATOR:
			case RAVAGER:
				referIllager(entity);
				break;
			default:
				return;
			}
		}
	}

	@EventHandler
	public void onItemBellRing(PlayerInteractEvent event) {
		if (config.getBoolean("on_portable"))
			if (!event.hasBlock())
				if (event.hasItem()) {
					ItemStack item = event.getItem();

					if (item.getType() == Material.BELL) {
						Player player = event.getPlayer();

						scanningIllager(player);
					}
				}
	}
	

	@EventHandler
	public void onBellRing(PlayerStatisticIncrementEvent event) {
		if (config.getBoolean("on_block")) {
			Statistic statistic = event.getStatistic();

			if (statistic == Statistic.BELL_RING) {
				Player player = event.getPlayer();

				scanningIllager(player);
			}
		}
	}
	
	
	
	
	
	

	void referIllager(Entity entity) {
		LivingEntity livingEntity = (LivingEntity) entity;
		FileConfiguration config = IllagerFinder.instance.getConfig();

		livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, config.getInt("glowing_tick"), 1));
		FireworkManager.spawnFireworks(livingEntity.getLocation(), config.getInt("firework_amount"));
	}

	void scanningIllager(Player player) {
		World world = player.getWorld();
		FileConfiguration config = IllagerFinder.instance.getConfig();
		int bellRange = config.getInt("bell_range");

		Collection<Entity> entities = world.getNearbyEntities(player.getLocation(), bellRange, 32, bellRange,
				entity -> entity.getType() == EntityType.PILLAGER || entity.getType() == EntityType.VINDICATOR
						|| entity.getType() == EntityType.RAVAGER);

		if (entities.size() > 0) {
			player.playSound(player.getLocation(), Sound.BLOCK_BELL_USE, 10, 29);
		}

		for (Entity entity : entities) {
			referIllager(entity);
		}
	}

}
