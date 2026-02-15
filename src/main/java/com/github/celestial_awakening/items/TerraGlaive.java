package com.github.celestial_awakening.items;

import com.github.celestial_awakening.Config;
import com.github.celestial_awakening.capabilities.SunStaffCapability;
import com.github.celestial_awakening.capabilities.SunStaffCapabilityProvider;
import com.github.celestial_awakening.capabilities.TerraGlaiveCapability;
import com.github.celestial_awakening.capabilities.TerraGlaiveCapabilityProvider;
import com.github.celestial_awakening.damage.DamageSourceIgnoreIFrames;
import com.github.celestial_awakening.entity.projectile.GenericShard;
import com.github.celestial_awakening.events.custom_events.MoonScytheAttackEvent;
import com.github.celestial_awakening.init.ItemInit;
import com.github.celestial_awakening.util.MathFuncs;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TerraGlaive extends CustomItem{
    float atkDmg;
    protected int abilityNameColor=0x754417;
    protected int abilityDescColor=0xB86A25;
    private final Multimap<Attribute, AttributeModifier> defaultModifiers;
    Ingredient ingredient= Ingredient.of(ItemInit.GAIA_PLATE.get());
    public TerraGlaive(Properties p_41383_,float dmg,float atkSpd) {
        super(p_41383_);
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", dmg, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier",atkSpd, AttributeModifier.Operation.ADDITION));
        builder.put(ForgeMod.ENTITY_REACH.get(), new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier",0.5f, AttributeModifier.Operation.ADDITION));

        atkDmg=dmg;
        this.defaultModifiers = builder.build();

    }
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack itemStack = player.getItemInHand(interactionHand);
        AtomicInteger abilityCastType= new AtomicInteger();
        abilityCastType.set(1);
        if (!level.isClientSide) {
            @NotNull LazyOptional<TerraGlaiveCapability> capOptional=itemStack.getCapability(TerraGlaiveCapabilityProvider.cap);
            capOptional.ifPresent(cap->{
                if (cap.getSpearCD()==0){
                    cap.setSpearCD((short) 100);
                    Vec3 dir=player.getLookAngle();
                    float hAng= (float) ((Math.toDegrees (Math.atan2(dir.z,dir.x))));
                    float vAng=MathFuncs.getVertAngFromVec(dir);
                    DamageSourceIgnoreIFrames spear=new DamageSourceIgnoreIFrames(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.FALLING_STALACTITE),player);

                    GenericShard earthShard=GenericShard.create(level, GenericShard.Type.EARTH,120,18f,90-hAng,vAng,3.5f,spear,player,false);
                    earthShard.setPos(player.position());
                    level.addFreshEntity(earthShard);
                    itemStack.hurtAndBreak(2,player,(p_40992_) -> {p_40992_.broadcastBreakEvent(EquipmentSlot.MAINHAND);});

                }
            });
        }
        switch (abilityCastType.get()){
            case 1:{
                return InteractionResultHolder.consume(itemStack);
            }
            case 2:{
                player.startUsingItem(interactionHand);
                return InteractionResultHolder.consume(itemStack);
            }
            default:{
                return InteractionResultHolder.fail(itemStack);//failed to use either ability
            }
        }
    }

    public void inventoryTick(ItemStack itemStack, Level level, Entity holder, int vanillaIndex, boolean isSelectedIndex) {
        if (!level.isClientSide()){
            @NotNull LazyOptional<TerraGlaiveCapability> capOptional=itemStack.getCapability(TerraGlaiveCapabilityProvider.cap);
            capOptional.ifPresent(cap->{
                cap.decrementCD();
            });
        }
    }
    public boolean hurtEnemy(ItemStack itemStack, LivingEntity target, LivingEntity attacker) {
        itemStack.hurtAndBreak(1, attacker, (p_43414_) -> {
            p_43414_.broadcastBreakEvent(EquipmentSlot.MAINHAND);
        });
        return true;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> components, TooltipFlag tooltipFlag) {

        Component spearName=Component.translatable("tooltip.celestial_awakening.terra_glaive.spear_name").setStyle(Style.EMPTY.withBold(true).withColor(TextColor.fromRgb(abilityNameColor)));
        components.add(spearName);
        Component spearCtrl=Component.translatable("tooltip.celestial_awakening.item.right_click").setStyle(Style.EMPTY.withBold(true).withColor(TextColor.fromRgb(abilityNameColor)));
        components.add(spearCtrl);
        Component spearDesc=Component.translatable("tooltip.celestial_awakening.terra_glaive.spear_desc").setStyle(Style.EMPTY.withBold(true).withColor(TextColor.fromRgb(abilityDescColor)));
        components.add(spearDesc);
     super.appendHoverText(itemStack, level, components, tooltipFlag);

    }

    public UseAnim getUseAnimation(ItemStack p_43417_) {
        return UseAnim.SPEAR;
    }
    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment)
    {
        return enchantment.category== EnchantmentCategory.WEAPON;

    }
    @Override
    public boolean isValidRepairItem(ItemStack p_41402_, ItemStack p_41403_) {
        return ingredient.test(p_41403_) || super.isValidRepairItem(p_41402_, p_41403_);
    }
    @Override
    public int getEnchantmentValue() {
        return 12;
    }
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot p_43274_) {
        return p_43274_ == EquipmentSlot.MAINHAND ? this.defaultModifiers : super.getDefaultAttributeModifiers(p_43274_);
    }
}
