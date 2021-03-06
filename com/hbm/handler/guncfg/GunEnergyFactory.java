package com.hbm.handler.guncfg;

import java.util.ArrayList;

import com.hbm.entity.projectile.EntityBulletBase;
import com.hbm.handler.BulletConfigSyncingUtil;
import com.hbm.handler.BulletConfiguration;
import com.hbm.handler.GunConfiguration;
import com.hbm.interfaces.IBulletImpactBehavior;
import com.hbm.items.ModItems;
import com.hbm.packet.AuxParticlePacketNT;
import com.hbm.packet.PacketDispatcher;
import com.hbm.render.util.RenderScreenOverlay.Crosshair;

import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class GunEnergyFactory {
	
	public static GunConfiguration getEMPConfig() {
		
		GunConfiguration config = new GunConfiguration();
		
		config.rateOfFire = 30;
		config.roundsPerCycle = 1;
		config.gunMode = GunConfiguration.MODE_NORMAL;
		config.firingMode = GunConfiguration.FIRE_MANUAL;
		config.hasReloadAnim = false;
		config.hasFiringAnim = false;
		config.hasSpinup = false;
		config.hasSpindown = false;
		config.reloadDuration = 10;
		config.firingDuration = 0;
		config.ammoCap = 0;
		config.durability = 1500;
		config.reloadType = GunConfiguration.RELOAD_NONE;
		config.allowsInfinity = true;
		config.crosshair = Crosshair.L_SPLIT;
		config.firingSound = "hbm:weapon.teslaShoot";
		
		config.name = "EMP Orb Projector";
		config.manufacturer = "MWT Prototype Labs";
		
		config.config = new ArrayList<Integer>();
		config.config.add(BulletConfigSyncingUtil.SPECIAL_EMP);
		
		return config;
	}
	
	public static GunConfiguration getFlamerConfig() {
		
		GunConfiguration config = new GunConfiguration();
		
		config.rateOfFire = 1;
		config.roundsPerCycle = 1;
		config.gunMode = GunConfiguration.MODE_NORMAL;
		config.firingMode = GunConfiguration.FIRE_AUTO;
		config.hasReloadAnim = false;
		config.hasFiringAnim = false;
		config.hasSpinup = false;
		config.hasSpindown = false;
		config.reloadDuration = 20;
		config.reloadSoundEnd = false;
		config.firingDuration = 0;
		config.ammoCap = 100;
		config.durability = 1000;
		config.reloadType = GunConfiguration.RELOAD_FULL;
		config.allowsInfinity = true;
		config.crosshair = Crosshair.L_CIRCLE;
		config.firingSound = "hbm:weapon.immolatorShoot";
		config.reloadSound = "hbm:weapon.flamerReload";
		
		config.name = "Heavy Duty Flamer";
		config.manufacturer = "MWT Prototype Labs";

		config.comment.add("Dragon-slaying: Advanced techniques, part 1:");
		config.comment.add("Try not to get eaten by the dragon.");
		config.comment.add("");
		config.comment.add("Hope that helps.");
		
		config.config = new ArrayList<Integer>();
		config.config.add(BulletConfigSyncingUtil.FLAMER_NORMAL);
		config.config.add(BulletConfigSyncingUtil.FLAMER_NAPALM);
		config.config.add(BulletConfigSyncingUtil.FLAMER_WP);
		config.config.add(BulletConfigSyncingUtil.FLAMER_GAS);
		
		return config;
	}
	
	public static BulletConfiguration getOrbusConfig() {
		
		BulletConfiguration bullet = new BulletConfiguration();
		
		bullet.ammo = ModItems.gun_emp_ammo;
		
		bullet.velocity = 1F;
		bullet.spread = 0.0F;
		bullet.wear = 10;
		bullet.bulletsMin = 1;
		bullet.bulletsMax = 1;
		bullet.dmgMin = 10;
		bullet.dmgMax = 12;
		bullet.gravity = 0D;
		bullet.maxAge = 100;
		bullet.doesRicochet = false;
		bullet.doesPenetrate = true;
		bullet.doesBreakGlass = false;
		bullet.style = BulletConfiguration.STYLE_ORB;
		bullet.plink = BulletConfiguration.PLINK_NONE;
		bullet.emp = 10;
		
		bullet.effects = new ArrayList();
		bullet.effects.add(new PotionEffect(Potion.moveSlowdown.id, 10 * 20, 1));
		bullet.effects.add(new PotionEffect(Potion.weakness.id, 10 * 20, 4));
		
		return bullet;
	}
	
	public static BulletConfiguration getFlameConfig() {
		
		BulletConfiguration bullet = new BulletConfiguration();
		
		bullet.ammo = ModItems.ammo_fuel;
		bullet.ammoCount = 100;
		
		bullet.velocity = 0.75F;
		bullet.spread = 0.025F;
		bullet.wear = 1;
		bullet.bulletsMin = 3;
		bullet.bulletsMax = 5;
		bullet.dmgMin = 2;
		bullet.dmgMax = 4;
		bullet.gravity = 0.01D;
		bullet.maxAge = 60;
		bullet.doesRicochet = false;
		bullet.doesPenetrate = true;
		bullet.doesBreakGlass = false;
		bullet.style = BulletConfiguration.STYLE_NONE;
		bullet.plink = BulletConfiguration.PLINK_NONE;
		bullet.vPFX = "flame";
		bullet.incendiary = 10;
		
		bullet.bImpact = new IBulletImpactBehavior() {

			@Override
			public void behaveBlockHit(EntityBulletBase bullet, int x, int y, int z) {
				
				NBTTagCompound data = new NBTTagCompound();
				data.setString("type", "vanillaburst");
				data.setString("mode", "flame");
				data.setInteger("count", 15);
				data.setDouble("motion", 0.1D);
				
				PacketDispatcher.wrapper.sendToAllAround(new AuxParticlePacketNT(data, bullet.posX, bullet.posY, bullet.posZ), new TargetPoint(bullet.dimension, bullet.posX, bullet.posY, bullet.posZ, 50));
			}
		};
		
		return bullet;
	}
	
	public static BulletConfiguration getNapalmConfig() {
		
		BulletConfiguration bullet = getFlameConfig();
		
		bullet.ammo = ModItems.ammo_fuel_napalm;
		bullet.wear = 2;
		bullet.dmgMin = 4;
		bullet.dmgMax = 6;
		bullet.maxAge = 200;
		
		return bullet;
	}
	
	public static BulletConfiguration getPhosphorusConfig() {
		
		BulletConfiguration bullet = getFlameConfig();
		
		bullet.ammo = ModItems.ammo_fuel_phosphorus;
		bullet.wear = 2;
		bullet.spread = 0.0F;
		bullet.bulletsMin = 1;
		bullet.bulletsMax = 1;
		bullet.dmgMin = 4;
		bullet.dmgMax = 6;
		bullet.maxAge = 200;
		bullet.vPFX = "smoke";
		
		bullet.bImpact = BulletConfigFactory.getPhosphorousEffect(5, 60 * 20, 25, 0.25);
		
		return bullet;
	}
	
	public static BulletConfiguration getGasConfig() {
		
		BulletConfiguration bullet = getFlameConfig();
		
		bullet.ammo = ModItems.ammo_fuel_gas;
		bullet.wear = 1;
		bullet.spread = 0.05F;
		bullet.gravity = 0D;
		bullet.bulletsMin = 5;
		bullet.bulletsMax = 7;
		bullet.dmgMin = 0;
		bullet.dmgMax = 0;
		bullet.vPFX = "cloud";
		bullet.incendiary = 0;
		
		bullet.bImpact = BulletConfigFactory.getGasEffect(5, 60 * 20);
		
		return bullet;
	}
}
