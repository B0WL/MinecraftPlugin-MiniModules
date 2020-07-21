package listener;

import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.Container;
import org.bukkit.block.DoubleChest;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import main.CarriedChest;
import main.CarryOn;

public class ChestCarryListener implements Listener {
	private Material dummy_chest = Material.YELLOW_CONCRETE;
	private Material dummy_shulker = Material.PURPUR_BLOCK;

	@EventHandler
	public void on_carry_block(PlayerInteractEvent event) {
		if (event.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;
		Player player = event.getPlayer();
		if (!player.isSneaking())
			return;
		if (player.getInventory().getItemInMainHand().getType() != Material.AIR)
			return;
		List<Entity> passengers = player.getPassengers();
		if (!passengers.isEmpty())
			return;
		Block block = event.getClickedBlock();
		BlockState state = block.getState();
		if (!(state instanceof Container))
			return;
		event.setCancelled(true);

		do_carry_chest(player, block, state);
	}

	private void do_carry_chest(Player player, Block block, BlockState state) {
		World world = player.getWorld();

		Container container = (Container) state;
		Inventory inventory = container.getInventory();
		InventoryHolder holder = inventory.getHolder();

		if (holder instanceof DoubleChest) {
			DoubleChest double_chest = (DoubleChest) holder;
			Location block_location = block.getLocation();

			Chest left_chest = (Chest) double_chest.getLeftSide();
			Chest right_chest = (Chest) double_chest.getRightSide();

			Location left_location = left_chest.getBlock().getLocation();
			Location right_location = right_chest.getBlock().getLocation();

			if (block_location.distance(left_location) < 0.1) {
				inventory = left_chest.getBlockInventory();
				block = left_chest.getBlock();
			}
			if (block_location.distance(right_location) < 0.1) {
				inventory = right_chest.getBlockInventory();
				block = right_chest.getBlock();
			}
		}

		ItemStack[] items = inventory.getContents();
		inventory.clear();

		Material block_material = block.getType();
		if (block.getType() == Material.CHEST)
			block_material = (dummy_chest);
		if (block.getType() == Material.SHULKER_BOX)
			block_material = (dummy_shulker);

		FallingBlock entity_block = world.spawnFallingBlock(player.getLocation(),
				block_material.createBlockData());
		entity_block.setGravity(false);
		entity_block.setDropItem(false);
		UUID uuid = entity_block.getUniqueId();

		CarryOn.Instance.New_carried_chest.add(new CarriedChest(uuid, block.getBlockData(),items,entity_block));
		player.addPassenger(entity_block);
		block.setType(Material.AIR);
	}

	@EventHandler
	public void on_falled_chest_block(EntityChangeBlockEvent event) {
		Entity entity = event.getEntity();
		UUID uuid = entity.getUniqueId();

		CarriedChest exist_data = null;
		for (CarriedChest data : CarryOn.Instance.New_carried_chest)
			if (data.UUID == uuid)
				exist_data = data;
		if (exist_data == null)
			return;
		Block block = event.getBlock();
		event.setCancelled(true);
		
		exist_data.Spawn(block);
		CarryOn.Instance.New_carried_chest.remove(exist_data);
	}

}
