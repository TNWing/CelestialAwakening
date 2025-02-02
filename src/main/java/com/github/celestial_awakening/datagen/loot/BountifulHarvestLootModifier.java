package com.github.celestial_awakening.datagen.loot;

import com.github.celestial_awakening.init.LootInit;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

public class BountifulHarvestLootModifier extends LootModifier {
    public static final RegistryObject<Codec<BountifulHarvestLootModifier>> CODEC = LootInit.LOOT_SERIALIZER.register("moonstone_crop_boost",()-> RecordCodecBuilder.create
            (inst->codecStart(inst)
                    .apply(inst,BountifulHarvestLootModifier::new)));
    public BountifulHarvestLootModifier(LootItemCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        for (LootItemCondition cond:this.conditions) {
            if (!cond.test(context)) {
                return generatedLoot;
            }
        }
        return generatedLoot;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }
}
