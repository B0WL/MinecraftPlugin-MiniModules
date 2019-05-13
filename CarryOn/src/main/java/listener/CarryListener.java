package listener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Container;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.spigotmc.event.entity.EntityDismountEvent;

import main.CarryOn;

public class CarryListener implements Listener {
	static Map<UUID,ItemStack[]> carryInventory = new HashMap<UUID, ItemStack[]>();

	Material dummyChest = Material.YELLOW_TERRACOTTA;
	Material dummyShulker = Material.PURPUR_BLOCK;
	
	void ejectPassenger(Player player, Entity entity) {
		CarryOn.instance.getLogger().log(Level.INFO, "Eject");
		player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 10, 29);

		if (entity instanceof FallingBlock) {
			Block playerFoot = player.getWorld().getBlockAt(player.getLocation());
			
			if (playerFoot.getType() == Material.AIR) {
				FallingBlock entityFall = (FallingBlock) entity;
				BlockData dataFall = entityFall.getBlockData();
				Material fallMaterial = dataFall.getMaterial();
				
				if(fallMaterial== dummyChest) {
					fallMaterial = Material.CHEST;
				}else if(fallMaterial == dummyShulker) {
					fallMaterial = Material.SHULKER_BOX;
				}

				playerFoot.setBlockData(dataFall);
				playerFoot.setType(fallMaterial);
				BlockState state = playerFoot.getState();
				Container container = (Container)state;
				UUID uuid =player.getUniqueId();
				
				container.getInventory().setContents(carryInventory.get(uuid));
				carryInventory.remove(uuid);
				
				entity.remove();
			}
		}
	}
	
	@EventHandler
	public void onEjectRightClick(PlayerInteractEntityEvent event) {
		Player player = event.getPlayer();
		if (!player.isSneaking()) {
			if (player.eject()) {
				event.setCancelled(true);
			}
		}
	}
	@EventHandler
	public void onEjectSomething(EntityDismountEvent event) {
		Entity entity = event.getDismounted();
		
		if(entity instanceof Player) {
			Player player = (Player) entity;
			Entity mountEntity = event.getEntity();
			
			ejectPassenger(player,mountEntity);
		}
	}
	

	
	@EventHandler
	public void onEntityEvent(EntityDropItemEvent event) {
		EntityType type= event.getEntityType();
		if(type == EntityType.FALLING_BLOCK) {
			
			CarryOn.instance.getLogger().log(Level.INFO,event.getEventName());
			
			
		}
	}
	
	@EventHandler
	public void onCarryAnimal(PlayerInteractEntityEvent event) {
		Player player = event.getPlayer();

		if (player.isSneaking()) {
			if (player.getInventory().getItemInMainHand().getType() == Material.AIR) {
				Entity entity = event.getRightClicked();

				if (entity instanceof LivingEntity || entity instanceof Vehicle) {
					List<Entity> passengers = player.getPassengers();

					if (passengers.isEmpty()) {
						event.setCancelled(true);
						player.addPassenger(entity);
					}
				}
			}
		} 
	}
	@EventHandler
	public void onCarryBlock(PlayerInteractEvent event) {
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			Player player = event.getPlayer();

			if (player.isSneaking()) {
				if (player.getInventory().getItemInMainHand().getType() == Material.AIR) {
					List<Entity> passengers = player.getPassengers();

					if (passengers.isEmpty()) {
						Block block = event.getClickedBlock();
						BlockState state = block.getState();
						
						if (state instanceof Container) {
							event.setCancelled(true);
							
							World world = player.getWorld();
							
							Container container = (Container) state;
							Inventory inventory = container.getInventory();
							carryInventory.put(player.getUniqueId(), inventory.getContents());
							inventory.clear();
							
							Block blockForEntity = block;
							if(block.getType()== Material.CHEST) {
								blockForEntity.setType(dummyChest);
							}else if(block.getType() == Material.SHULKER_BOX) {
								blockForEntity.setType(dummyShulker);
							}
							FallingBlock entityBlock = world.spawnFallingBlock(player.getLocation(), blockForEntity.getBlockData());
							entityBlock.setGravity(false);
							player.addPassenger(entityBlock);

							block.setType(Material.AIR);
						}
					}
				}
			}
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
