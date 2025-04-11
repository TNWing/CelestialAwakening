package com.github.celestial_awakening.recipes;

import com.github.celestial_awakening.init.ItemInit;
import com.github.celestial_awakening.nbt_strings.NBTStrings;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.FloatTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;

public class LifeFragFood extends ShapedRecipe {
    public LifeFragFood(ResourceLocation p_273203_, String p_272759_, CraftingBookCategory p_273506_,
                        int p_272952_, int p_272920_, NonNullList<Ingredient> p_273650_, ItemStack p_272852_) {

        super(p_273203_, p_272759_, p_273506_, p_272952_, p_272920_, p_273650_, p_272852_);
    }

    int foodW;
    int foodH;
    @Override
    public boolean matches(CraftingContainer craftingContainer,Level level){
        for(int w = 0; w < craftingContainer.getWidth(); ++w) {
            for (int h = 0; h < craftingContainer.getHeight(); ++h) {
                ItemStack itemStack = craftingContainer.getItem(h*craftingContainer.getHeight() + w);
                if (itemStack.getItem() == ItemInit.LIFE_FRAG.get()){
                    //we are going down the left column, so just check the tile to the right for the food
                    if (h==0 || h==craftingContainer.getHeight()-1 || w>=craftingContainer.getWidth()-2){
                        /*
                        if h==0, then there cannot be a frag on top of the food
                        if h==height-1, then there cannot be frag on bottom of food
                        if w==width-2, there is either no space for a right frag or there is no space at all for the food
                         */
                        return false;
                    }
                    ItemStack stackToRight=craftingContainer.getItem(h*craftingContainer.getHeight() + w + 1);
                    if (stackToRight.getFoodProperties(null)!=null && (!stackToRight.hasTag() ||  !stackToRight.getTag().contains("LifeFragHeal"))){
                        //now, check the other 3 slots
                        ItemStack stackTop=craftingContainer.getItem((h-1)*craftingContainer.getHeight() + w + 1);
                        ItemStack stackBot=craftingContainer.getItem((h+1)*craftingContainer.getHeight() + w + 1);
                        ItemStack stackRight=craftingContainer.getItem((h*craftingContainer.getHeight() + w + 2));
                        if (stackTop.getItem() ==ItemInit.LIFE_FRAG.get() && stackBot.getItem() ==ItemInit.LIFE_FRAG.get() &&stackRight.getItem()==ItemInit.LIFE_FRAG.get()){
                            foodW=w+1;
                            foodH=h;
                            return true;
                        }
                    }
                    return false;
                }
            }
        }
        return false;
    }


    @Override
    public ItemStack assemble(CraftingContainer craftingContainer, RegistryAccess registryAccess) {
        ItemStack foodStack=craftingContainer.getItem(foodH*craftingContainer.getHeight() + foodW);
        ItemStack stackToReturn=new ItemStack(foodStack.getItem());
        FoodProperties properties=stackToReturn.getFoodProperties(null);
        if (properties!=null){
            float healAmt=calcHeal(properties.getNutrition(),properties.getSaturationModifier());
            FloatTag healTag=FloatTag.valueOf(healAmt);
            stackToReturn.addTagElement(NBTStrings.lifeFrag,healTag);
        }
        else{
            stackToReturn=ItemStack.EMPTY;
        }
        return stackToReturn;
    }

    public float calcHeal(int hunger, float saturation){
        float amt=0.075f*hunger + 0.1f*hunger * saturation * 2.0F;
        return amt;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width*height>=4;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return null;
    }
}
