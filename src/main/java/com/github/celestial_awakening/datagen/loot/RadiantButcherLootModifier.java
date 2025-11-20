package com.github.celestial_awakening.datagen.loot;

import com.github.celestial_awakening.init.ItemInit;
import com.github.celestial_awakening.init.LootInit;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

public class RadiantButcherLootModifier extends LootModifier {
    ArmorMaterial material;
    float chancePerPiece;
    public static final RegistryObject<Codec<RadiantButcherLootModifier>> CODEC = LootInit.LOOT_MOD_SERIALIZER.register("radiant_butcher",()-> RecordCodecBuilder.create
            (inst->codecStart(inst)
                    .apply(inst,RadiantButcherLootModifier::new)));
    /**
     * Constructs a LootModifier.
     *
     * @param conditionsIn the ILootConditions that need to be matched before the loot is modified.
     */
    protected RadiantButcherLootModifier(LootItemCondition[] conditionsIn) {
        super(conditionsIn);
        for (LootItemCondition cond:conditionsIn) {
            if (cond instanceof ArmorLootCondition){
                material=((ArmorLootCondition) cond).material;
            }
        }
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        Entity entity=context.getParamOrNull(LootContextParams.THIS_ENTITY);
        if (entity!=null && entity instanceof LivingEntity){
            LivingEntity livingEntity= (LivingEntity) entity;
            if (!(livingEntity instanceof Animal)){
                return generatedLoot;
            }
        }
        for (LootItemCondition cond:this.conditions) {
            if (!cond.test(context)){
                return generatedLoot;
            }
        }
        Entity killer=context.getParamOrNull(LootContextParams.KILLER_ENTITY);
        if (killer!=null && killer instanceof LivingEntity livingEntity){
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
                if (randomSource.nextInt(10)>4){
                    generatedLoot.add(new ItemStack(Items.BONE));
                }
                else{
                    generatedLoot.add(new ItemStack(ItemInit.LIFE_FRAG.get()));
                }
            }
        }


        return generatedLoot;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }
}
