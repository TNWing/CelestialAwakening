package com.github.celestial_awakening.events;

import com.github.celestial_awakening.Config;
import com.github.celestial_awakening.capabilities.LevelCapability;
import com.github.celestial_awakening.capabilities.LevelCapabilityProvider;
import com.github.celestial_awakening.init.ItemInit;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.util.LazyOptional;

public class SuffocatingHeatEvents {
    //Inventory.tick is for thhe player only

    public void onBlockStorageTick(BlockEntity blockEntity){
        Container c;

    }
    public void onInventoryTick(ServerLevel level, Player player){
        Inventory inventory=player.getInventory();
        LazyOptional<LevelCapability> optional=level.getCapability(LevelCapabilityProvider.LevelCap);
        optional.ifPresent(cap-> {
            if (cap.divinerSunControlVal > 0 && level.getDifficulty().getId() != 0) {
                int time = (int) (level.getDayTime() % 24000L);
                int sunVal = cap.divinerSunControlVal;
                float baseChance = Config.divinerSHRotBaseChance;
                if (time % 200 == 0 && level.getRandom().nextInt(1) == 0) {
                    double difficultyChanceMod = Config.divinerSHRotDiffMod.get(level.getDifficulty().getId() - 1);
                    float sunMod=sunVal*1.5f;
                    double chance = baseChance * difficultyChanceMod+sunMod+100;
                    int rotCnt= 0;
                    for(int i = 0; i < inventory.items.size(); ++i) {
                        ItemStack itemStack=inventory.items.get(i);
                        if (itemStack.getFoodProperties(null)!=null && itemStack.getItem() != ItemInit.MUSHY_ROT.get() && level.getRandom().nextDouble() * 100d < chance-rotCnt){
                            int amt=level.getRandom().nextInt(Config.divinerSHItemRotMinAmt,Config.divinerSHItemRotMaxAmt+1);
                            itemStack.shrink(amt);

                            rotCnt+=amt;
                        }
                    }
                    ItemStack rot=new ItemStack(ItemInit.MUSHY_ROT.get(),rotCnt);
                    inventory.add(rot);

                }
            }
        });
    }
}
