package com.hbm.blocks.fluid;

import java.util.Random;

import com.hbm.blocks.ModBlocks;
import com.hbm.entity.mob.EntityNuclearCreeper;
import com.hbm.lib.Library;
import com.hbm.lib.ModDamageSource;
import com.hbm.lib.RefStrings;
import com.hbm.main.MainRegistry;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;

public class ToxicBlock extends BlockFluidClassic {

	@SideOnly(Side.CLIENT)
	public static IIcon stillIcon;
	@SideOnly(Side.CLIENT)
	public static IIcon flowingIcon;
	public Random rand = new Random();

	public static DamageSource damageSource;

	public ToxicBlock(Fluid fluid, Material material, DamageSource damage) {
		super(fluid, material);
		damageSource = damage;
		setQuantaPerBlock(4);
		setCreativeTab(null);
		displacements.put(this, false);
	}

	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		return (side == 0 || side == 1) ? stillIcon : flowingIcon;
	}

	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister register) {
		stillIcon = register.registerIcon(RefStrings.MODID + ":toxic_still");
		flowingIcon = register.registerIcon(RefStrings.MODID + ":toxic_flowing");
	}

	@Override
	public boolean canDisplace(IBlockAccess world, int x, int y, int z) {

		if (world.getBlock(x, y, z).getMaterial().isLiquid()) {
			return false;
		}
		return super.canDisplace(world, x, y, z);
	}

	@Override
	public boolean displaceIfPossible(World world, int x, int y, int z) {

		if (world.getBlock(x, y, z).getMaterial().isLiquid()) {
			return false;
		}
		return super.displaceIfPossible(world, x, y, z);
	}

	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
		entity.setInWeb();
		// if(entity instanceof EntityLivingBase)
		// {
		// entity.attackEntityFrom(ModDamageSource.mudPoisoning, 8);
		// }
		if (entity instanceof EntityPlayer && Library.checkForHazmat((EntityPlayer) entity)) {
			/*
			 * Library.damageSuit(((EntityPlayer)entity), 0);
			 * Library.damageSuit(((EntityPlayer)entity), 1);
			 * Library.damageSuit(((EntityPlayer)entity), 2);
			 * Library.damageSuit(((EntityPlayer)entity), 3);
			 */

		} else if (entity instanceof EntityCreeper) {
			EntityNuclearCreeper creep = new EntityNuclearCreeper(world);
			creep.setLocationAndAngles(entity.posX, entity.posY, entity.posZ, entity.rotationYaw, entity.rotationPitch);
			if (!entity.isDead)
				if (!world.isRemote)
					world.spawnEntityInWorld(creep);
			entity.setDead();
		} else if (entity instanceof EntityVillager) {
			EntityZombie creep = new EntityZombie(world);
			creep.setLocationAndAngles(entity.posX, entity.posY, entity.posZ, entity.rotationYaw, entity.rotationPitch);
			entity.setDead();
			if (!world.isRemote)
				world.spawnEntityInWorld(creep);
		} else if (entity instanceof EntityLivingBase && !(entity instanceof EntityNuclearCreeper)
				&& !(entity instanceof EntityMooshroom) && !(entity instanceof EntityZombie)) {
			entity.attackEntityFrom(ModDamageSource.radiation, 2.5F);
		}
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block block)
    {
		super.onNeighborBlockChange(world, x, y, z, block);
		
		if(reactToBlocks(world, x + 1, y, z))
			world.setBlock(x, y, z, ModBlocks.block_waste);
		if(reactToBlocks(world, x - 1, y, z))
			world.setBlock(x, y, z, ModBlocks.block_waste);
		if(reactToBlocks(world, x, y + 1, z))
			world.setBlock(x, y, z, ModBlocks.block_waste);
		if(reactToBlocks(world, x, y - 1, z))
			world.setBlock(x, y, z, ModBlocks.block_waste);
		if(reactToBlocks(world, x, y, z + 1))
			world.setBlock(x, y, z, ModBlocks.block_waste);
		if(reactToBlocks(world, x, y, z - 1))
			world.setBlock(x, y, z, ModBlocks.block_waste);
    }
	
	public boolean reactToBlocks(World world, int x, int y, int z) {
		if(world.getBlock(x, y, z).getMaterial() != ModBlocks.fluidtoxic) {
			if(world.getBlock(x, y, z).getMaterial().isLiquid()) {
				return true;
			}
		}
		return false;
	}

	public int tickRate(World p_149738_1_) {
		return 15;
	}

}