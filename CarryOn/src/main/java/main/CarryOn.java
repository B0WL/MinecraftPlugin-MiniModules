package main;

import org.bukkit.plugin.java.JavaPlugin;

import listener.CarryListener;


public class CarryOn  extends JavaPlugin{

	public static CarryOn instance;
	@Override
	public void onEnable() {
		instance = this;
		getConfig().options().copyDefaults(true);
		saveConfig();
		reloadConfig();

		
		try {
			getServer().getPluginManager().registerEvents(new CarryListener(), this);
		} catch (Exception e) {
			getLogger().info("Carry Listen Failed");
			getServer().getPluginManager().disablePlugin(this);
		}
		
		getLogger().info("onEnable has been invoked!");
			
	}

	@Override
	public void onDisable() {
		getLogger().info("onDisable has been invoked!");
	}

}
