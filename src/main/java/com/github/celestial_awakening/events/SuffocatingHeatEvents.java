package com.github.celestial_awakening.events;

import com.github.celestial_awakening.Config;
import com.github.celestial_awakening.capabilities.LevelCapability;
import com.github.celestial_awakening.capabilities.LevelCapabilityProvider;
import com.github.celestial_awakening.init.ItemInit;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.LazyOptional;

import java.util.concurrent.atomic.AtomicInteger;

public class SuffocatingHeatEvents {
    public void onLevelTick(Level level, Inventory inventory){
        LazyOptional<LevelCapability> optional=level.getCapability(LevelCapabilityProvider.LevelCap);
        optional.ifPresent(cap-> {
            if (cap.divinerSunControlVal > 0 && level.getDifficulty().getId() != 0) {
                int time = (int) (level.getDayTime() % 24000L);
                int sunVal = cap.divinerSunControlVal;
                float baseChance = Config.divinerSHRotBase;
                if (time % 200 == 0 && level.getRandom().nextInt(10) == 0) {

                    float difficultyChanceMod = 1 + 0.2f * (level.getDifficulty().getId() - 2);
                    float sunMod=sunVal*1.5f;
                    float chance = baseChance * difficultyChanceMod+sunMod;
                    AtomicInteger rotCnt= new AtomicInteger();
                    inventory.items.stream().unordered().forEach(itemStack -> {
                        if (chance<=0){
                            return;
                        }
                        if (level.getRandom().nextFloat() * 100f < chance-rotCnt.get()) {
                            itemStack.shrink(1);
                            rotCnt.getAndIncrement();
                        }
                    });
                    inventory.add(rotCnt.get(), new ItemStack(ItemInit.MUSHY_ROT.get()));

                }
            }
        });
    }
}
