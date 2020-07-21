package main;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.io.BukkitObjectInputStream;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import listener.ChestCarryListener;
import listener.EjectListener;
import listener.EntityCarryListener;

public class CarryOn extends JavaPlugin {
	public static CarryOn Instance;
	public List<CarriedChest> New_carried_chest = new ArrayList<CarriedChest>();

	@Override
	public void onEnable() {
		Instance = this;

		getConfig().options().copyDefaults(true);
		saveConfig();
		reloadConfig();

		try {
			getServer().getPluginManager().registerEvents(new EjectListener(), this);
		} catch (Exception e) {
			getLogger().info("Eject Listen Failed");
			getServer().getPluginManager().disablePlugin(this);
		}
		try {
			getServer().getPluginManager().registerEvents(new ChestCarryListener(), this);
		} catch (Exception e) {
			getLogger().info("Chest Listen Failed");
		}
		try {
			getServer().getPluginManager().registerEvents(new EntityCarryListener(), this);
		} catch (Exception e) {
			getLogger().info("Entity Listen Failed");
		}

		getLogger().info("CarryOn Enabled");
	}

	@Override
	public void onDisable() {
		New_carried_chest.forEach(x -> x.Suicide());
		getLogger().info("CarryOn Disabled");
	}



}
