package com.github.celestial_awakening.datagen.loot;

import com.github.celestial_awakening.init.ItemInit;
import com.github.celestial_awakening.init.LootInit;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

public class LunarScaleFromFishLootModifier extends LootModifier {
    public static final Codec<LunarScaleFromFishLootModifier> CODEC =
            RecordCodecBuilder.create(inst->codecStart(inst)
                    .apply(inst,LunarScaleFromFishLootModifier::new));
    protected LunarScaleFromFishLootModifier(LootItemCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {

        Entity entity=context.getParamOrNull(LootContextParams.THIS_ENTITY);
        if (entity!=null && entity instanceof LivingEntity){
            LivingEntity livingEntity= (LivingEntity) entity;
            if (livingEntity.getType().getCategory()!=MobCategory.WATER_AMBIENT){
                return generatedLoot;
            }
        }
        for (LootItemCondition cond:this.conditions) {
            if (!cond.test(context)){
                return generatedLoot;
            }
        }

        generatedLoot.add(new ItemStack(ItemInit.LUNAR_SCALE.get()));
        return generatedLoot;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }
}
