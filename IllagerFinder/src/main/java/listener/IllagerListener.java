package listener;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import main.IllagerFinder;
import util.FireworkManager;

public class IllagerListener implements Listener {
	@EventHandler
	public void onVillagerInteract(EntitySpawnEvent event) {
		Entity entity = event.getEntity();
		
		switch (entity.getType()) {
		case PILLAGER:
		case VINDICATOR:
		case RAVAGER:
			LivingEntity livingEntity = (LivingEntity) entity;
			FileConfiguration config = IllagerFinder.instance.getConfig();
			
			livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, config.getInt("glowing_tick"), 1));
			FireworkManager.spawnFireworks(event.getLocation(), config.getInt("firework_amount"));
			break;
		default:
			return;
		}
	}
}
