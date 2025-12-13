package com.github.celestial_awakening.items;

import com.github.celestial_awakening.capabilities.SearedStoneToolCapability;
import com.github.celestial_awakening.capabilities.SearedStoneToolCapabilityProvider;
import com.github.celestial_awakening.capabilities.SunStaffCapability;
import com.github.celestial_awakening.capabilities.SunStaffCapabilityProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public interface SearedStoneTool {
    //
    int nameColor=0x756252;
    int descColor=0x3D3229;
    default void addSharedAbilities( List<Component> components,ItemStack stack,String earthlinkTip){
        AtomicInteger lvl= new AtomicInteger(1);
        stack.getCapability(SearedStoneToolCapabilityProvider.capability).ifPresent(cap->{
            lvl.set(cap.getUpgradeTier() + 1);
        });
        Component hint=null;
        int tierNum=lvl.get();
        Component tier=Component.translatable("tooltip.celestial_awakening.seared_stone_tools.tier",tierNum).setStyle(Style.EMPTY.withBold(true).withColor(TextColor.fromRgb(nameColor)));
        components.add(tier);
        if (tierNum>1){
            Component earthLink=Component.translatable("tooltip.celestial_awakening.seared_stone_tools.earthlink_name").setStyle(Style.EMPTY.withBold(true).withColor(TextColor.fromRgb(nameColor)));
            components.add(earthLink);
            Component earthLinkEffect=Component.translatable(earthlinkTip).setStyle(Style.EMPTY.withBold(false).withColor(TextColor.fromRgb(descColor)));
            components.add(earthLinkEffect);
            if (tierNum>2){
                Component sturdy=Component.translatable("tooltip.celestial_awakening.seared_stone_tools.sturdy_name").setStyle(Style.EMPTY.withBold(true).withColor(TextColor.fromRgb(nameColor)));
                components.add(sturdy);
                Component sturdyEffect=Component.translatable("tooltip.celestial_awakening.seared_stone_tools.sturdy_desc").setStyle(Style.EMPTY.withBold(false).withColor(TextColor.fromRgb(descColor)));
                components.add(sturdyEffect);
            }
            else{
                hint=Component.translatable("tooltip.celestial_awakening.seared_stone_tools.sturdy_hint");
            }

        }
        else{
            hint=Component.translatable("tooltip.celestial_awakening.seared_stone_tools.earthlink_hint");

        }
        if (hint!=null){
            Component unlock=Component.translatable("tooltip.celestial_awakening.seared_stone_tools.unlock_text");
            components.add(unlock);
            components.add(hint);
        }


    }

    default byte getUpgradeTier(ItemStack stack){
        LazyOptional<SearedStoneToolCapability> optional= stack.getCapability(SearedStoneToolCapabilityProvider.capability);
        if (optional.isPresent()){
            return optional.orElseGet(null).getUpgradeTier();
        }
        return 0;
    }

    default void setUpgradeTier(ItemStack stack,byte t){
        LazyOptional<SearedStoneToolCapability> optional= stack.getCapability(SearedStoneToolCapabilityProvider.capability);
        optional.ifPresent(cap->{
            cap.setUpgradeTier(t);
        });
    }

    default InteractionResultHolder<ItemStack> onRightClick(Level level, Player player, InteractionHand interactionHand){
        ItemStack itemStack = player.getItemInHand(interactionHand);

        AtomicBoolean atomicBoolean= new AtomicBoolean(false);
        if (!level.isClientSide && !player.isUsingItem()) {
            @NotNull LazyOptional<SearedStoneToolCapability> capOptional=itemStack.getCapability(SearedStoneToolCapabilityProvider.capability);
            capOptional.ifPresent(cap->{
                cap.setSmeltActive(!cap.isSmeltActive());
                atomicBoolean.set(true);

            });
        }
        if (atomicBoolean.get()){
            return InteractionResultHolder.success(itemStack);
        }
        return InteractionResultHolder.fail(itemStack);
    }

}
