package com.github.celestial_awakening.datagen.loot;

import com.github.celestial_awakening.init.ItemInit;
import com.github.celestial_awakening.init.LootInit;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.common.loot.LootTableIdCondition;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

public class LunarFishingLootModifier extends LootModifier {
    //LootTableIdCondition
    //LootItemBlockStatePropertyCondition
    public static final RegistryObject<Codec<LunarFishingLootModifier>> CODEC = LootInit.LOOT_SERIALIZER.register("moonstone_fishing",()-> RecordCodecBuilder.create
            (inst->codecStart(inst)
                    .apply(inst,LunarFishingLootModifier::new)));
    /**
     * Constructs a LootModifier.
     *
     * @param conditionsIn the ILootConditions that need to be matched before the loot is modified.
     */
    protected LunarFishingLootModifier(LootItemCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        for (LootItemCondition cond:this.conditions) {
           if (!cond.test(context)){
               return generatedLoot;
           }
        }
        generatedLoot.add(new ItemStack(ItemInit.MOONSTONE.get()));
        return generatedLoot;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }
}
