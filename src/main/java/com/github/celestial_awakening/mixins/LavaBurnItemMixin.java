package com.github.celestial_awakening.mixins;


import com.github.celestial_awakening.init.ItemInit;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class LavaBurnItemMixin {
    @Inject(method="lavaHurt", at=@At("HEAD"))
    public void onLavaBurn(CallbackInfo ci){
        Entity entity=(Entity)(Object)this;
        if (entity instanceof ItemEntity itemEntity){
            ItemStack itemStack=itemEntity.getItem();
            if (itemStack.is(Items.DEEPSLATE)){
                itemStack=new ItemStack(ItemInit.SCORCHED_STONE.get(),itemStack.getCount());
                itemEntity.setItem(itemStack);
            }
        }
    }
}
