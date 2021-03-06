package cn.lambdacraft.terrain.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockFlowing;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.Icon;

public class BlockSlimeFlow extends BlockFlowing {
	public BlockSlimeFlow(int id) {
        super(id, Material.water);
        setCreativeTab(CreativeTabs.tabRedstone);
        blockHardness = 100.0F;
        setLightOpacity(3);
        this.setUnlocalizedName("slimeflow");
	}

	@Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister) {
        theIcon = new Icon[] {
                iconRegister.registerIcon("lambdacraft:slime_still"),
                iconRegister.registerIcon("lambdacraft:slime_flow") };
    }
	
	
}
