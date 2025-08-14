package com.github.celestial_awakening.menus;

import com.github.celestial_awakening.networking.ModNetwork;
import com.github.celestial_awakening.networking.packets.AstralterTransmuteC2SPacket;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.item.ItemStack;

public class AstralterScreen extends AbstractContainerScreen<AstralterMenu> {
    int sunstoneAmt=0;
    int moonstoneAmt=0;
    public AstralterScreen(AstralterMenu menu, Inventory inv, Component component) {
        super(menu, inv, component);
        menu.addSlotListener(new ContainerListener() {
            public void slotChanged(AbstractContainerMenu p_97973_, int p_97974_, ItemStack p_97975_) {
                System.out.println("PREV STONE AMT " + AstralterScreen.this.sunstoneAmt + "   " +  AstralterScreen.this.moonstoneAmt);
                AstralterScreen.this.sunstoneAmt=menu.sunstoneSlot.getItem().getCount();
                AstralterScreen.this.moonstoneAmt=menu.moonstoneSlot.getItem().getCount();
                System.out.println("NEW STONE AMT " + AstralterScreen.this.sunstoneAmt + "   " +  AstralterScreen.this.moonstoneAmt);
            }

            public void dataChanged(AbstractContainerMenu abstractContainerMenu, int p_169629_, int p_169630_) {
                //AstralterScreen.this.sunstoneAmt=menu.sunstoneSlot.getItem().getCount();
                //AstralterScreen.this.moonstoneAmt=menu.moonstoneSlot.getItem().getCount();
            }
        });
    }

    protected void init() {
        super.init();
        this.addRenderableWidget(new AstralterTransmuteButton(0,0,0,0, CommonComponents.GUI_DONE));
        //this.leftPos + 164, this.topPos + 107
        // super(p_97992_, p_97993_, 90, 220, CommonComponents.GUI_DONE);
    }


    @Override
    protected void renderBg(GuiGraphics p_283065_, float p_97788_, int p_97789_, int p_97790_) {

    }



    abstract static class AstralterBaseButton extends AbstractButton{

        public AstralterBaseButton(int p_93365_, int p_93366_, int p_93367_, int p_93368_, Component p_93369_) {
            super(p_93365_, p_93366_, p_93367_, p_93368_, p_93369_);
        }



        @Override
        protected void updateWidgetNarration(NarrationElementOutput p_259858_) {

        }
    }

    class AstralterConfirmButton extends AstralterBaseButton{//used to add sun/moon stones

        public AstralterConfirmButton(int p_93365_, int p_93366_, int p_93367_, int p_93368_, Component p_93369_) {
            super(p_93365_, p_93366_, p_93367_, p_93368_, p_93369_);
        }
        @Override
        public void onPress() {

        }

    }

    class AstralterTransmuteButton extends AstralterBaseButton{

        public AstralterTransmuteButton(int p_93365_, int p_93366_, int p_93367_, int p_93368_, Component p_93369_) {
            super(p_93365_, p_93366_, p_93367_, p_93368_, p_93369_);
            this.setTooltip(Tooltip.create(this.createDescription(), (Component)null));
        }
        @Override
        public void onPress() {
            ModNetwork.sendToServer(new AstralterTransmuteC2SPacket(AstralterScreen.this.sunstoneAmt,AstralterScreen.this.moonstoneAmt));
        }
        protected MutableComponent createDescription() {
            return Component.translatable("astralter.transmute");
        }
    }
}
