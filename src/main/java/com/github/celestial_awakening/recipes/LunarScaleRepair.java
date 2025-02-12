package com.github.celestial_awakening.recipes;

import com.github.celestial_awakening.init.ItemInit;
import com.github.celestial_awakening.recipes.serializers.LunarScaleRepairSerializer;
import com.google.common.collect.Lists;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;

import java.util.List;

public class LunarScaleRepair extends CustomRecipe {
    public LunarScaleRepair(ResourceLocation p_252125_, CraftingBookCategory p_249010_) {
        super(p_252125_, p_249010_);
    }

    @Override
    public boolean matches(CraftingContainer craftingContainer, Level level) {
        List<ItemStack> list = Lists.newArrayList();
        int indexToRepair=-1;
        for(int i = 0; i <craftingContainer.getContainerSize(); ++i) {
            ItemStack itemstack = craftingContainer.getItem(i);
            if (!itemstack.isEmpty()) {
                if (!isNotValidRepairable(itemstack)){//repairable
                    if (indexToRepair!=-1){
                        return false;
                    }
                    indexToRepair=i;
                }
                else{//non repairable, should be lunar scale
                    if (itemstack.getItem()!= ItemInit.LUNAR_SCALE.get()){
                        return false;
                    }
                }
                list.add(itemstack);
            }
        }

        return list.size() >1;
    }

    boolean isNotValidRepairable(ItemStack itemStack){
        return !itemStack.isRepairable() || itemStack.getCount()!=1;
    }

    @Override
    public ItemStack assemble(CraftingContainer craftingContainer, RegistryAccess access) {
        int lunarScaleCnt=0;
        ItemStack stackToRepair=ItemStack.EMPTY;
        for(int i = 0; i <craftingContainer.getContainerSize(); ++i) {
            ItemStack itemstack = craftingContainer.getItem(i);
            if (!itemstack.isEmpty()) {
                if (!isNotValidRepairable(itemstack)){//repairable
                    if (stackToRepair!=ItemStack.EMPTY){
                        return ItemStack.EMPTY;
                    }
                    stackToRepair=itemstack;
                }
                else{//non repairable, should be lunar scale
                    if (itemstack.getItem()!= ItemInit.LUNAR_SCALE.get()){
                        return ItemStack.EMPTY;
                    }
                    lunarScaleCnt++;
                }
            }
        }
        Item itemToRepair=stackToRepair.getItem();
        int repairAmt=repairAmt(stackToRepair.getMaxDamage(),lunarScaleCnt);
        int newDura=Math.min(stackToRepair.getMaxDamage(),
                stackToRepair.getMaxDamage()-stackToRepair.getDamageValue() + repairAmt);
        ItemStack stackToReturn=new ItemStack(itemToRepair);

        EnchantmentHelper.setEnchantments(stackToRepair.getAllEnchantments(),stackToReturn);
        stackToReturn.setDamageValue(stackToReturn.getMaxDamage()-newDura);
        return stackToReturn;
    }

    int repairAmt(int maxDura,int scaleCnt){
        double logScaling=Math.log(maxDura)/Math.log(6);
        int amt= (int) (scaleCnt*(3+logScaling));
        return amt;
    }

    @Override
    public boolean canCraftInDimensions(int p_43999_, int p_44000_) {
        return true;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return LunarScaleRepairSerializer.INSTANCE;
    }
}
