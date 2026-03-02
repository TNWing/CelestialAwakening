package com.github.celestial_awakening.mixins;

import com.github.celestial_awakening.init.ItemInit;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;


@Mixin(EnchantmentMenu.class)
public class LunaTomeMixin {
    @Redirect(
            method = "lambda$clickMenuButton$1",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"
            )
    )
    private boolean redirectBookCheck(ItemStack instance, Item item) {
        return instance.is(Items.BOOK) || instance.is(ItemInit.LUNA_TOME.get());
    }
}