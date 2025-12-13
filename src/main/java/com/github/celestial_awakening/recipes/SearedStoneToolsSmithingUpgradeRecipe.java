package com.github.celestial_awakening.recipes;

import com.github.celestial_awakening.capabilities.CapabilityFromString;
import com.github.celestial_awakening.capabilities.SearedStoneToolCapability;
import com.github.celestial_awakening.capabilities.SearedStoneToolCapabilityProvider;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.SmithingTransformRecipe;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class SearedStoneToolsSmithingUpgradeRecipe extends BasicEnchantedSmithingRecipe implements CapabilityModificationRecipe,EnchantedSmithing{
    ArrayList<Requirement> requirements;

    public ArrayList<Requirement> getRequirements() {
        return requirements;
    }

    public ArrayList<CapabilityResult> getResults() {
        return results;
    }

    ArrayList<CapabilityResult> results;

    public SearedStoneToolsSmithingUpgradeRecipe(ResourceLocation loc, Ingredient template, Ingredient base, Ingredient addition, ItemStack result, HashMap<Enchantment,Integer> enchants,ArrayList<Requirement> requirements_) {
        super(loc,template,base,addition,result,enchants);
        this.requirements=requirements_;
        this.results=new ArrayList<>();
    }
    public SearedStoneToolsSmithingUpgradeRecipe(ResourceLocation loc, Ingredient template, Ingredient base, Ingredient addition, ItemStack result, HashMap<Enchantment,Integer> enchants,ArrayList<Requirement> requirements,ArrayList<CapabilityResult> results) {
        super(loc,template,base,addition,result,enchants);
        this.requirements=requirements;
        this.results=results;
    }

    @Override
    public boolean matches(Container container, Level level) {


        ItemStack containerBase=container.getItem(1);
        AtomicBoolean atomicBoolean=new AtomicBoolean();
        atomicBoolean.set(super.matches(container,level));

        if (atomicBoolean.get()){
            System.out.println("REQUIREMENT CNTR " + requirements.size());
            for (Requirement requirement:requirements){
                System.out.println("NAME " + requirement.capabilityName);
                if (requirement.capabilityName.equalsIgnoreCase("SearedStone")){
                    System.out.println("SEARED");
                    LazyOptional<SearedStoneToolCapability> optional=containerBase.getCapability(SearedStoneToolCapabilityProvider.capability);
                    optional.ifPresent(cap->{
                        switch (requirement.getMethodName()){
                            case "getUpgradeTier":{
                                System.out.println("UPGRADE TIER REQUIREd " + Byte.parseByte(requirement.getResult()));
                                System.out.println("CURRENT TIER " + cap.getUpgradeTier());
                                atomicBoolean.set(cap.getUpgradeTier()==Byte.parseByte( requirement.getResult()));
                            }
                        }
                    });
                }
                if (!atomicBoolean.get()){
                    return false;
                }
            }
        }
        return atomicBoolean.get();
    }

    @Override
    public ItemStack assemble(Container container, RegistryAccess registryAccess) {
        ItemStack stack=super.assemble(container,registryAccess);
        applyEnchants(stack,enchantMap);
        for (CapabilityResult result:results){

            if (result.capabilityName.equalsIgnoreCase("SearedStone")){
                LazyOptional<SearedStoneToolCapability> optional=stack.getCapability(SearedStoneToolCapabilityProvider.capability);
                optional.ifPresent(cap->{
                    switch(result.methodName){
                        case "setUpgradeTier":{
                            cap.setUpgradeTier(Byte.parseByte(result.getParam()[0]));
                            break;
                        }
                    }
                });

            }
        }
        return stack;
    }
}
