package com.github.celestial_awakening.items;

import com.github.celestial_awakening.capabilities.MoonScytheCapability;
import com.github.celestial_awakening.capabilities.MoonScytheCapabilityProvider;
import com.github.celestial_awakening.events.custom_events.MoonScytheAttackEvent;
import com.github.celestial_awakening.util.MathFuncs;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class MoonlightReaper extends MoonScythe {
    public MoonlightReaper(Properties p_41383_) {
        super(p_41383_,6.5f,3.5f,5.5f,-2.7f,90);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> components, TooltipFlag tooltipFlag) {
        Component abilityName=Component.translatable("tooltip.celestial_awakening.moon_scythe.ability_name").setStyle(Style.EMPTY.withBold(true).withColor(TextColor.fromRgb(abilityNameColor)));
        components.add(abilityName);
        components.add(Component.translatable("tooltip.celestial_awakening.moon_scythe.ability_desc",new Object[]{displayCD}).setStyle(Style.EMPTY.withColor(TextColor.fromRgb(abilityDescColor))));
        Component abilityName2=Component.translatable("tooltip.celestial_awakening.moonlight_reaper.ability_name").setStyle(Style.EMPTY.withBold(true).withColor(TextColor.fromRgb(abilityNameColor)));
        components.add(abilityName2);
        components.add(Component.translatable("tooltip.celestial_awakening.moonlight_reaper.ability_desc").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(abilityDescColor))));
        superAppendHoverText(itemStack, level, components, tooltipFlag);
    }

    @Override
    public boolean hurtEnemy(ItemStack itemStack, LivingEntity target, LivingEntity attacker) {
        itemStack.hurtAndBreak(1, attacker, (p_43414_) -> {
            p_43414_.broadcastBreakEvent(EquipmentSlot.MAINHAND);
        });
        if (attacker instanceof Player){
            Player player=(Player) attacker;
            boolean isFullyCharged = player.getAttackStrengthScale(0.5F) > 0.9F;
            boolean isCrit=player.fallDistance > 0.0F && !player.onGround() && !player.onClimbable() && !player.isInWater() && !player.hasEffect(MobEffects.BLINDNESS) && !player.isPassenger() && target instanceof LivingEntity && !player.isSprinting();
            Vec3 targetPos=target.position().add(0,1,0);
            Vec3 dir=targetPos.subtract(player.position()).normalize();

            dir.multiply(1,0,1);//TODO: replace later (maybe)
            float hAng= MathFuncs.getAngFrom2DVec(dir);
            float dmg=waveDamage;
            if (isCrit){
                dmg=strikeDamage;
            }
            if (isFullyCharged){
                MinecraftForge.EVENT_BUS.post(new MoonScytheAttackEvent(itemStack,isCrit,attacker.level(),dir,targetPos,player,dmg,hAng,cd,true));
            }
        }
        healOnKill(itemStack,target,attacker);
        return true;
    }


    public static void healOnKill(ItemStack itemStack,LivingEntity target,LivingEntity attacker){
        if (!target.isAlive() && attacker instanceof Player){
            Player player= (Player) attacker;
            player.getFoodData().eat(1,1);
            @NotNull LazyOptional<MoonScytheCapability> capOptional=itemStack.getCapability(MoonScytheCapabilityProvider.ScytheCap);
            capOptional.ifPresent(cap->{
                cap.incrementLunarOrbs(attacker.level().getServer().getTickCount());
            });
        }
    }
}
