package com.github.celestial_awakening.init;

import com.github.celestial_awakening.CelestialAwakening;
import com.github.celestial_awakening.enchantments.GaiaLinkEnchantment;
import com.github.celestial_awakening.enchantments.GaiaRemnantsEnchantment;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid= CelestialAwakening.MODID)
public class EnchantmentInit {
    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, CelestialAwakening.MODID);
    public static final RegistryObject<Enchantment> GAIA_LINK=ENCHANTMENTS.register("gaia_link",()->new GaiaLinkEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> GAIA_REMNANTS=ENCHANTMENTS.register("gaia_remnants",()->new GaiaRemnantsEnchantment(Enchantment.Rarity.RARE,  EquipmentSlot.MAINHAND));

}
