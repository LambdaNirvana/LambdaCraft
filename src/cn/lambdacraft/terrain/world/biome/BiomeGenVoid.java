package cn.lambdacraft.terrain.world.biome;

import cn.lambdacraft.terrain.register.XenBlocks;
import net.minecraft.world.biome.BiomeGenBase;
/**
 * Xen虚空生态地貌
 * @author F
 *
 */
public class BiomeGenVoid extends BiomeGenBase{

	public BiomeGenVoid(int par1) {
		super(par1);
		this.minHeight = -1.95F;
		this.maxHeight = 0.5F;
		this.spawnableMonsterList.clear();
		this.spawnableCreatureList.clear();
		this.spawnableWaterCreatureList.clear();
		this.spawnableCaveCreatureList.clear();
		this.topBlock = ((byte)XenBlocks.grass.blockID);
		this.fillerBlock = ((byte)XenBlocks.dirt.blockID);
		this.setBiomeName("Xen Void");
		this.theBiomeDecorator = new BiomeDecoratorXen(this);
	}
}
