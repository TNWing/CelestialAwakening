package com.github.celestial_awakening.items;

import com.github.celestial_awakening.nbt_strings.NBTStrings;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Predicate;

public class FluorescentBow extends BowItem {
    public FluorescentBow(Properties p_40660_) {
        super(p_40660_);
    }
    int nameColor =0xe7e82c;
    int descColor =0xe2e2e1;
    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> components, TooltipFlag tooltipFlag) {
        Component dangerSenseName=Component.translatable("tooltip.celestial_awakening.fluorescent_bow.dangersense_name").setStyle(Style.EMPTY.withBold(true).withColor(TextColor.fromRgb(nameColor)));
        components.add(dangerSenseName);
        components.add(Component.translatable("tooltip.celestial_awakening.fluorescent_bow.dangersense_desc"));
        Component revealShotName=Component.translatable("tooltip.celestial_awakening.fluorescent_bow.revealing_shot_name").setStyle(Style.EMPTY.withBold(true).withColor(TextColor.fromRgb(nameColor)));
        components.add(revealShotName);
        components.add(Component.translatable("tooltip.celestial_awakening.fluorescent_bow.revealing_shot_desc"));
        super.appendHoverText(itemStack, level, components, tooltipFlag);
    }

    @Override
    public int getUseDuration(ItemStack p_40680_) {
        return 72000;
    }

    @Override
    public void releaseUsing(ItemStack bowStack, Level p_40668_, LivingEntity p_40669_, int p_40670_) {
        if (p_40669_ instanceof Player player) {
            boolean flag = player.getAbilities().instabuild || EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, bowStack) > 0;
            ItemStack arrowStack = player.getProjectile(bowStack);

            int i = this.getUseDuration(bowStack) - p_40670_;
            i = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(bowStack, p_40668_, player, i, !arrowStack.isEmpty() || flag);
            if (i < 0) return;

            if (!arrowStack.isEmpty() || flag) {
                if (arrowStack.isEmpty()) {
                    arrowStack = new ItemStack(Items.ARROW);
                }

                float f = getPowerForTime(i);
                if (!((double)f < 0.1D)) {
                    boolean flag1 = player.getAbilities().instabuild || (arrowStack.getItem() instanceof ArrowItem && ((ArrowItem)arrowStack.getItem()).isInfinite(arrowStack, bowStack, player));
                    if (!p_40668_.isClientSide) {
                        ArrowItem arrowitem = (ArrowItem)(arrowStack.getItem() instanceof ArrowItem ? arrowStack.getItem() : Items.ARROW);
                        AbstractArrow abstractarrow = arrowitem.createArrow(p_40668_, arrowStack, player);
                        abstractarrow = customArrow(abstractarrow);
                        abstractarrow.setBaseDamage(abstractarrow.getBaseDamage()*7f/8f);
                        abstractarrow.addTag(NBTStrings.fBowBoost);
                        abstractarrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, f * 3.0F, 1.0F);
                        if (f == 1.0F) {
                            abstractarrow.setCritArrow(true);
                        }

                        int j = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER_ARROWS, bowStack);
                        if (j > 0) {
                            abstractarrow.setBaseDamage(abstractarrow.getBaseDamage() + (double)j * 0.5D + 0.5D);
                        }

                        int k = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PUNCH_ARROWS, bowStack);
                        if (k > 0) {
                            abstractarrow.setKnockback(k);
                        }

                        if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS, bowStack) > 0) {
                            abstractarrow.setSecondsOnFire(100);
                        }

                        bowStack.hurtAndBreak(1, player, (p_289501_) -> {
                            p_289501_.broadcastBreakEvent(player.getUsedItemHand());
                        });
                        if (flag1 || player.getAbilities().instabuild && (arrowStack.is(Items.SPECTRAL_ARROW) || arrowStack.is(Items.TIPPED_ARROW))) {
                            abstractarrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                        }

                        p_40668_.addFreshEntity(abstractarrow);
                    }

                    p_40668_.playSound((Player)null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F / (p_40668_.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F);
                    if (!flag1 && !player.getAbilities().instabuild) {
                        arrowStack.shrink(1);
                        if (arrowStack.isEmpty()) {
                            player.getInventory().removeItem(arrowStack);
                        }
                    }

                    player.awardStat(Stats.ITEM_USED.get(this));
                }
            }
        }
    }
    @Override
    public InteractionResultHolder<ItemStack> use(Level lvl, Player player, InteractionHand p_40674_) {
        ItemStack itemstack = player.getItemInHand(p_40674_);
        boolean flag = !player.getProjectile(itemstack).isEmpty();

        InteractionResultHolder<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onArrowNock(itemstack, lvl, player, p_40674_, flag);
        if (ret != null) return ret;

        if (!player.getAbilities().instabuild && !flag) {
            return InteractionResultHolder.fail(itemstack);
        } else {
            player.startUsingItem(p_40674_);
            glowEnemies(lvl,player.position(),player);
            return InteractionResultHolder.consume(itemstack);
        }
    }

    void glowEnemies(Level level, Vec3 centerPos,Player user){
        Vec3 lower=centerPos.subtract(new Vec3(7,3,7));
        Vec3 upper=centerPos.add(new Vec3(7,3,7));
        AABB aabb=new AABB(lower,upper);
        Predicate pred= o -> o!=user && !((LivingEntity)o).hasEffect(MobEffects.INVISIBILITY);
        List<LivingEntity> entities=level.getEntitiesOfClass(LivingEntity.class,aabb,pred);
        for (LivingEntity entity:entities) {
            MobEffectInstance mobeffectinstance = new MobEffectInstance(MobEffects.GLOWING, 80, 0);
            entity.addEffect(mobeffectinstance, user);
        }
    }

    public static float getPowerForTime(int p_40662_) {
        float f = (float)p_40662_ / 15.0F;
        f = (f * f + f * 2.0F) / 3.0F;
        if (f > 1.0F) {
            f = 1.0F;
        }
        return f;
    }
}
