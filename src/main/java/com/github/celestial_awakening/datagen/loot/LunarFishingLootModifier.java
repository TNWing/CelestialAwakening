package com.github.celestial_awakening.datagen.loot;

import com.github.celestial_awakening.init.ItemInit;
import com.github.celestial_awakening.init.LootInit;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

public class LunarFishingLootModifier extends LootModifier {
    //LootTableIdCondition
    //LootItemBlockStatePropertyCondition
    ArmorMaterial material;
    float chancePerPiece;
    public static final Codec<LunarFishingLootModifier> CODEC = RecordCodecBuilder.create
            (inst->codecStart(inst)
                    .apply(inst,LunarFishingLootModifier::new));
    /**
     * Constructs a LootModifier.
     *
     * @param conditionsIn the ILootConditions that need to be matched before the loot is modified.
     */
    protected LunarFishingLootModifier(LootItemCondition[] conditionsIn) {
        super(conditionsIn);
        for (LootItemCondition cond:conditionsIn) {
            if (cond instanceof ArmorLootCondition){
                material=((ArmorLootCondition) cond).material;
                chancePerPiece=((ArmorLootCondition) cond).chancePerPiece;
            }
        }
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        for (LootItemCondition cond:this.conditions) {
            if (!cond.test(context)){
                return generatedLoot;
            }
        }
        Entity entity=context.getParamOrNull(LootContextParams.KILLER_ENTITY);
        if (entity!=null && entity instanceof LivingEntity livingEntity){
            Iterable<ItemStack> armorSlots=livingEntity.getArmorSlots();
            int cnt=0;

            for (ItemStack armorSlot : armorSlots) {
                if(!armorSlot.isEmpty() && (armorSlot.getItem() instanceof ArmorItem armorItem)) {
                    if (armorItem.getMaterial()==material){
                        cnt++;
                    }
                }
            }
            RandomSource randomSource= context.getLevel().getRandom();
            if (cnt>0 && randomSource.nextFloat()*100<cnt*chancePerPiece){
                //randomSource.next
                if (randomSource.nextInt(10)>5){
                    generatedLoot.add(new ItemStack(ItemInit.MOONSTONE.get()));
                }
                else{
                    generatedLoot.add(new ItemStack(ItemInit.LUNAR_SCALE.get()));
                }
            }

        }
        return generatedLoot;

    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }
}
