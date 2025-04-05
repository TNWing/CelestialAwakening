package com.github.celestial_awakening.items;

import com.github.celestial_awakening.capabilities.MoonScytheCapability;
import com.github.celestial_awakening.capabilities.MoonScytheCapabilityProvider;
import com.github.celestial_awakening.events.custom_events.MoonScytheAttackEvent;
import com.github.celestial_awakening.init.ItemInit;
import com.github.celestial_awakening.util.MathFuncs;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.extensions.IForgeItem;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

import static net.minecraftforge.common.ToolActions.SWORD_SWEEP;

public class MoonScythe extends Item implements IForgeItem {
    protected final float attackDamage;
    protected final float waveDamage;
    protected final float strikeDamage;
    protected final float attackSpd;
    protected final int cd;
    protected final Multimap<Attribute, AttributeModifier> defaultModifiers;
    protected int abilityNameColor=0xbfbfbd;
    protected int abilityDescColor=0xd8d7d5;
    float displayCD;
    Ingredient ingredient= Ingredient.of(ItemInit.MOONSTONE.get());
    public MoonScythe(Properties p_41383_) {
        super(p_41383_);
        this.attackDamage=5.5f;
        this.waveDamage=2.5f;
        this.strikeDamage=4.5f;
        this.attackSpd=-2.8f;
        this.cd=100;
        this.displayCD=5;
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", this.attackDamage, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", attackSpd, AttributeModifier.Operation.ADDITION));
        this.defaultModifiers = builder.build();
    }

    public MoonScythe(Properties p_41383_,float atk,float wave,float strike,float asp,int coolDown) {
        super(p_41383_);

        this.attackDamage=atk;
        this.waveDamage=wave;
        this.strikeDamage=strike;
        this.attackSpd=asp;
        this.cd=coolDown;
        displayCD=((int)(cd/2f))/10f;
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", this.attackDamage, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", asp, AttributeModifier.Operation.ADDITION));
        this.defaultModifiers = builder.build();
    }
    @Override
    public boolean isValidRepairItem(ItemStack p_41402_, ItemStack p_41403_) {
        return ingredient.test(p_41403_) || super.isValidRepairItem(p_41402_, p_41403_);
    }
    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> components, TooltipFlag tooltipFlag) {
        Component abilityName=Component.translatable("tooltip.celestial_awakening.moon_scythe.ability_name").setStyle(Style.EMPTY.withBold(true).withColor(TextColor.fromRgb(abilityNameColor)));
        components.add(abilityName);
        components.add(Component.translatable("tooltip.celestial_awakening.moon_scythe.ability_desc",new Object[]{displayCD}).setStyle(Style.EMPTY.withColor(TextColor.fromRgb(abilityDescColor))));
        super.appendHoverText(itemStack, level, components, tooltipFlag);
    }

    public boolean canAttackBlock(BlockState blockState, Level p_43410_, BlockPos p_43411_, Player p_43412_) {
        return !p_43412_.isCreative();
    }
    public UseAnim getUseAnimation(ItemStack p_43417_) {
        return UseAnim.SPEAR;
    }
    public float getDestroySpeed(ItemStack p_43288_, BlockState p_43289_) {
        return 2.0F;
    }

    public int getUseDuration(ItemStack p_43419_) {
        return 64000;
    }

    public void inventoryTick(ItemStack itemStack, Level level, Entity holder, int vanillaIndex, boolean isSelectedIndex) {
        if (!level.isClientSide()){
            @NotNull LazyOptional<MoonScytheCapability> capOptional=itemStack.getCapability(MoonScytheCapabilityProvider.ScytheCap);
            capOptional.ifPresent(cap->{
                if (cap.getWaveCD()>0){
                    cap.changeWaveCD(-1);
                }
                if (cap.getStrikeCD()>0){
                    cap.changeStrikeCD(-1);
                }
                if (level.getServer().getTickCount()-200>cap.getLastOrbTick()){
                    cap.decrementLunarOrbs(level.getServer().getTickCount());
                }
            });
        }
    }

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
                MinecraftForge.EVENT_BUS.post(new MoonScytheAttackEvent(itemStack,isCrit,attacker.level(),dir,targetPos,player,dmg,hAng,cd));
            }
        }
        return true;
    }

    public boolean mineBlock(ItemStack itemStack, Level p_43400_, BlockState p_43401_, BlockPos p_43402_, LivingEntity p_43403_) {
        if ((double)p_43401_.getDestroySpeed(p_43400_, p_43402_) != 0.0D) {
            itemStack.hurtAndBreak(2, p_43403_, (p_43385_) -> p_43385_.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        }

        return true;
    }
    @Override
    public int getEnchantmentValue() {
        return 12;
    }
    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment)
    {
        return enchantment.category== EnchantmentCategory.WEAPON;

    }

    public float getDamage() {
        return this.attackDamage;
    }

    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot p_43274_) {
        return p_43274_ == EquipmentSlot.MAINHAND ? this.defaultModifiers : super.getDefaultAttributeModifiers(p_43274_);
    }
    @Override
    public boolean canPerformAction(ItemStack stack, net.minecraftforge.common.ToolAction toolAction) {
        return toolAction.equals(SWORD_SWEEP);
    }
}
