package net.alexandr.modernmushrooms;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockMushroomCap;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlockWithMetadata;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.world.BlockEvent;

@Mod(modid = "modernmushrooms", name = "Modern Mushrooms", version = "1.0")
public class ModernMushrooms {

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {

        
        // Make blocks have metadata
    	MinecraftForge.EVENT_BUS.register(new DropEventHandler());
        Item.itemsList[99] = new ItemBlockWithMetadata(Block.blocksList[99].blockID - 256, Block.blocksList[99]);
        Item.itemsList[100] = new ItemBlockWithMetadata(Block.blocksList[100].blockID - 256, Block.blocksList[100]);
    }

    //Replace the drops, so that only 3 mushroom variants are dropped
    public static class DropEventHandler {

        @ForgeSubscribe
        public void onBlockHarvested(BlockEvent.HarvestDropsEvent event) {
            // Check if the block is a mushroom cap (red or brown)
            Block block = event.block;
            if (block.getClass().getSimpleName().equals("BlockMushroomCap")) {
                // Check if the harvester and their tool are valid
                if (event.harvester != null && event.harvester.getHeldItem() != null) {
                    // Check if the tool has Silk Touch
                    int silkTouchLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.silkTouch.effectId, event.harvester.getHeldItem());
                    if (silkTouchLevel > 0) {
                        // Determine the type of mushroom block to drop based on metadata
                        int metadata = event.blockMetadata; // Get metadata of the broken block
                        int newMetadata = (metadata == 10 || metadata == 15) ? 15 : 14;

                        // Get the block ID of the same type
                        int blockID = block.blockID;

                        // Clear existing drops
                        event.drops.clear();

                        // Add the mushroom block with the appropriate metadata
                        event.drops.add(new ItemStack(blockID, 1, newMetadata));
                    }
                }
            }
        }
    }
}
