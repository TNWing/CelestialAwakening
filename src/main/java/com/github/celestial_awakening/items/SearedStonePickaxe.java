package com.github.celestial_awakening.items;

import com.github.celestial_awakening.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.Tags;

import javax.annotation.Nullable;
import java.util.List;

public class SearedStonePickaxe extends PickaxeItem implements SearedStoneTool {
    public SearedStonePickaxe(Tier p_42961_, int p_42962_, float p_42963_, Properties p_42964_) {
        super(p_42961_, p_42962_, p_42963_, p_42964_);
    }
    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> components, TooltipFlag tooltipFlag) {
        addSharedAbilities(components,itemStack,"tooltip.celestial_awakening.seared_stone_pickaxe.earthlink_desc");
        super.appendHoverText(itemStack, level, components, tooltipFlag);
    }
    @Override
    public boolean mineBlock(ItemStack stack, Level p_43283_, BlockState p_43284_, BlockPos p_43285_, LivingEntity p_43286_) {
        if (p_43284_.getDestroySpeed(p_43283_, p_43285_) != 0.0F) {
            int breakAmt=1;
            if (getUpgradeTier(stack)>1 && (stack.getDamageValue()/(float)stack.getMaxDamage())<=0.1f) {
                breakAmt = 0;
            }
            stack.hurtAndBreak(breakAmt, p_43286_, (p_43276_) -> {
                p_43276_.broadcastBreakEvent(EquipmentSlot.MAINHAND);
            });
        }
        return true;
    }
    @Override
    public float getDestroySpeed(ItemStack itemStack, BlockState blockState) {
        float spd=super.getDestroySpeed(itemStack, blockState);
        if (getUpgradeTier(itemStack)>0 && !blockState.is(Tags.Blocks.ORES) && blockState.is(BlockTags.MINEABLE_WITH_PICKAXE)){
            if (getUpgradeTier(itemStack)>1 && (itemStack.getDamageValue()/(float)itemStack.getMaxDamage())>0.1f){
                spd*= 3;
            }
            else{
                spd*= 3;
            }

        }
        return spd;
    }

}
