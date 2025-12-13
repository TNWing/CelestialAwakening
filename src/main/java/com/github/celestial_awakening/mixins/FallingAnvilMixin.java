package com.github.celestial_awakening.mixins;

import com.github.celestial_awakening.events.EventManager;
import com.github.celestial_awakening.init.ItemInit;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Mixin(AnvilBlock.class)
public class FallingAnvilMixin {

    @Inject(method="onLand" , at=@At("TAIL"),cancellable = true)
    public void onLand(Level level, BlockPos pos, BlockState p_48795_, BlockState p_48796_, FallingBlockEntity p_48797_, CallbackInfo ci) {
        if (!level.isClientSide){
            boolean valid=true;
            for (int i=1;i<=5;i++){
                BlockPos blockPos=pos.offset(0,-i,0);
                if (!level.getBlockState(blockPos).is(EventManager.gaiaPlateBlocks[i-1].get())){
                    System.out.println("BLOCK STATE AT " + blockPos + "   Is " + level.getBlockState(blockPos));
                    valid=false;
                }
            }
            if (valid){
                for(int i=1;i<=5;i++){
                    BlockPos blockPos=pos.offset(0,-i,0);
                    level.removeBlock(blockPos,true);
                }
                ItemEntity itemEntity=new ItemEntity(level,pos.getX(),pos.getY(),pos.getZ(),new ItemStack(ItemInit.GAIA_PLATE.get()));
                level.addFreshEntity(itemEntity);
            }
        }
    }

}
