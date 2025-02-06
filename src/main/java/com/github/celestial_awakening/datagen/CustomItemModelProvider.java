package com.github.celestial_awakening.datagen;

import com.github.celestial_awakening.CelestialAwakening;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraft.world.item.armortrim.TrimMaterials;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

import java.util.LinkedHashMap;

public class CustomItemModelProvider extends net.minecraftforge.client.model.generators.ItemModelProvider {
    private static LinkedHashMap<ResourceKey<TrimMaterial>, Float> trimMaterials = new LinkedHashMap<>();
    static {
        trimMaterials.put(TrimMaterials.QUARTZ, 0.1F);
        trimMaterials.put(TrimMaterials.IRON, 0.2F);
        trimMaterials.put(TrimMaterials.NETHERITE, 0.3F);
        trimMaterials.put(TrimMaterials.REDSTONE, 0.4F);
        trimMaterials.put(TrimMaterials.COPPER, 0.5F);
        trimMaterials.put(TrimMaterials.GOLD, 0.6F);
        trimMaterials.put(TrimMaterials.EMERALD, 0.7F);
        trimMaterials.put(TrimMaterials.DIAMOND, 0.8F);
        trimMaterials.put(TrimMaterials.LAPIS, 0.9F);
        trimMaterials.put(TrimMaterials.AMETHYST, 1.0F);

    }
    public CustomItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, CelestialAwakening.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        //simpleItem(ItemInit.MOONSTONE);
        //heldItem(ItemInit.MOON_SCYTHE);

        /*
        trimmedArmorItem(ItemInit.MOONSTONE_HELMET);
        trimmedArmorItem(ItemInit.MOONSTONE_CHESTPLATE);
        trimmedArmorItem(ItemInit.MOONSTONE_LEGGINGS);
        trimmedArmorItem(ItemInit.MOONSTONE_BOOTS);

        simpleItem(ItemInit.SUNSTONE);
        trimmedArmorItem(ItemInit.RADIANT_HELMET);
        trimmedArmorItem(ItemInit.RADIANT_CHESTPLATE);
        trimmedArmorItem(ItemInit.RADIANT_LEGGINGS);
        trimmedArmorItem(ItemInit.RADIANT_BOOTS);

        simpleItem(ItemInit.DYING_LIGHT_ESSENCE);
        trimmedArmorItem(ItemInit.REMNANT_HELMET);
        trimmedArmorItem(ItemInit.REMNANT_CHESTPLATE);
        trimmedArmorItem(ItemInit.REMNANT_LEGGINGS);
        trimmedArmorItem(ItemInit.REMNANT_BOOTS);

        simpleItem(ItemInit.PULSATING_DARKNESS);
        trimmedArmorItem(ItemInit.SHADE_CAP);
        trimmedArmorItem(ItemInit.SHADE_CLOAK);
        trimmedArmorItem(ItemInit.SHADE_PANTS);
        trimmedArmorItem(ItemInit.SHADE_SLIPPERS);



        simpleItem(ItemInit.EBONY_FUR);
        trimmedArmorItem(ItemInit.UMBRA_BOOTS);
        trimmedArmorItem(ItemInit.UMBRA_CHESTPLATE);
        trimmedArmorItem(ItemInit.UMBRA_LEGGINGS);
        trimmedArmorItem(ItemInit.UMBRA_HELMET);

         */
    }

    private ItemModelBuilder simpleItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(CelestialAwakening.MODID,"item/" + item.getId().getPath())
                );
    }
    private ItemModelBuilder heldItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/handheld")).texture("layer0",
                new ResourceLocation(CelestialAwakening.MODID,"item/" + item.getId().getPath()));
    }
    private void trimmedArmorItem(RegistryObject<Item> itemRegistryObject) {
        final String MOD_ID = CelestialAwakening.MODID;

        if(itemRegistryObject.get() instanceof ArmorItem armorItem) {
            trimMaterials.entrySet().forEach(entry -> {

                ResourceKey<TrimMaterial> trimMaterial = entry.getKey();
                float trimValue = entry.getValue();

                String armorType = switch (armorItem.getEquipmentSlot()) {
                    case HEAD -> "helmet";
                    case CHEST -> "chestplate";
                    case LEGS -> "leggings";
                    case FEET -> "boots";
                    default -> "";
                };

                String armorItemPath = "item/" + armorItem;
                String trimPath = "trims/items/" + armorType + "_trim_" + trimMaterial.location().getPath();
                String currentTrimName = armorItemPath + "_" + trimMaterial.location().getPath() + "_trim";
                ResourceLocation armorItemResLoc = new ResourceLocation(MOD_ID, armorItemPath);
                ResourceLocation trimResLoc = new ResourceLocation(trimPath); // minecraft namespace
                ResourceLocation trimNameResLoc = new ResourceLocation(MOD_ID, currentTrimName);

                // This is used for making the ExistingFileHelper acknowledge that this texture exist, so this will
                // avoid an IllegalArgumentException
                existingFileHelper.trackGenerated(trimResLoc, PackType.CLIENT_RESOURCES, ".png", "textures");

                // Trimmed armorItem files
                getBuilder(currentTrimName)
                        .parent(new ModelFile.UncheckedModelFile("item/generated"))
                        .texture("layer0", armorItemResLoc)
                        .texture("layer1", trimResLoc);
                // Non-trimmed armorItem file (normal variant)
                this.withExistingParent(itemRegistryObject.getId().getPath(),
                                mcLoc("item/generated"))
                        .override()
                        .model(new ModelFile.UncheckedModelFile(trimNameResLoc))
                        .predicate(mcLoc("trim_type"), trimValue).end()
                        .texture("layer0",
                                new ResourceLocation(MOD_ID,
                                        "item/" + itemRegistryObject.getId().getPath()));
            });
        }
    }
}
