package me.zombie_striker.qg.miscitems;

import java.util.List;

import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.zombie_striker.qg.Main;
import me.zombie_striker.qg.MaterialStorage;
import me.zombie_striker.qg.guns.utils.WeaponSounds;
import me.zombie_striker.qg.miscitems.ThrowableItems.ThrowableHolder;

public class IncendaryGrenades extends GrenadeBase {

	public IncendaryGrenades(ItemStack[] ingg, double cost, double damage, double explosionreadius, String name,
			String displayname, List<String> lore, MaterialStorage ms) {
		super(ingg, cost, damage, explosionreadius, name, displayname, lore, ms);
	}

	@Override
	public void onLeftClick(Player thrower) {
		if (grenadeHolder.containsKey(thrower)) {
			thrower.sendMessage(Main.prefix + "You already pulled the pin!");
			thrower.playSound(thrower.getLocation(), WeaponSounds.RELOAD_BULLET.getName(), 1, 1);
			return;
		}
		thrower.getWorld().playSound(thrower.getLocation(), WeaponSounds.RELOAD_MAG_IN.getName(), 2, 1);
		final ThrowableHolder h = new ThrowableHolder(thrower.getUniqueId(), thrower);
		h.setTimer(new BukkitRunnable() {

			int k = 0;

			@Override
			public void run() {
				try {
					h.getHolder().getWorld().spawnParticle(org.bukkit.Particle.EXPLOSION_HUGE,
							h.getHolder().getLocation(), 0);
					for(int i = 0; i < 4; i ++) {
						//TODO: Check: This goes in three directions, and one stays still
						h.getHolder().getWorld().spawnParticle(org.bukkit.Particle.LAVA,
								h.getHolder().getLocation(), i);
					}
					h.getHolder().getWorld().playSound(h.getHolder().getLocation(), WeaponSounds.HISS.getName(), 2f,
							1f);
				} catch (Error e3) {
					h.getHolder().getWorld().playEffect(h.getHolder().getLocation(), Effect.valueOf("CLOUD"), 0);
					h.getHolder().getWorld().playSound(h.getHolder().getLocation(), Sound.valueOf("EXPLODE"), 3, 0.7f);
				}
				k++;
				if (k == 1) {
					if (h.getHolder() instanceof Player) {
						((LivingEntity) h.getHolder()).setFireTicks(h.getHolder().getMaxFireTicks()/5);
					}
				} else if (k == 40) {
					if (h.getHolder() instanceof Item) {
						h.getHolder().remove();
					}
					grenadeHolder.remove(h.getHolder());
					this.cancel();
				} else {
					for(Entity e : h.getHolder().getNearbyEntities(radius, radius, radius))
						if(e instanceof LivingEntity)
							e.setFireTicks(20);
					k++;
				}
			}
		}.runTaskTimer(Main.getInstance(),5*20,10));
		grenadeHolder.put(thrower, h);

	}

}
