package util;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

public class FireworkManager {
	public static void spawnFireworks(Location location, int amount){
    Location loc = location;
    Firework fw = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
    FireworkMeta fwm = fw.getFireworkMeta();
    
    fwm.setPower(2);
    fwm.addEffect(FireworkEffect.builder().withColor(Color.WHITE).flicker(true).build());
    
    for(int i = 0;i<amount; i++){
    	fw.setFireworkMeta(fwm);
    }
}
}
