package main;

import org.bukkit.plugin.java.JavaPlugin;

import listener.EjectListener;
import listener.EntityCarryListener;

public class CarryOn extends JavaPlugin {
	public static CarryOn Instance;

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
			getServer().getPluginManager().registerEvents(new EntityCarryListener(), this);
		} catch (Exception e) {
			getLogger().info("Entity Listen Failed");
		}

		getLogger().info("CarryOn Enabled");
	}

	@Override
	public void onDisable() {
		getLogger().info("CarryOn Disabled");
	}

}
