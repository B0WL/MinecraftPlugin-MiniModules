package main;

import java.io.Serializable;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Container;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;

public class CarriedChest {
	public UUID UUID;
	public BlockData Data;
	public ItemStack[] Items;
	public Entity Chest_entity;

	public CarriedChest(UUID uuid, BlockData data, ItemStack[] items, Entity player) {
		this.UUID = uuid;
		this.Data = data;
		this.Items = items;
		this.Chest_entity = player;
	}

	public void Suicide() {
		CarryOn.Instance.getLogger().log(Level.WARNING,"Suicide chest");
		Block block = this.Chest_entity.getLocation().getBlock();		
		Chest_entity.remove();
		Spawn(block);
	}

	public void Spawn(Block block) {
		block.setBlockData(Data);
		block.setType(Data.getMaterial());
		BlockState state = block.getState();
		Container container = (Container)state;
		container.getInventory().setContents(Items);
	}
	
}