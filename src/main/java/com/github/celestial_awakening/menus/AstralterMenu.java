package com.github.celestial_awakening.menus;

import com.github.celestial_awakening.blocks.entity.AstralterBlockEntity;
import com.github.celestial_awakening.init.BlockInit;
import com.github.celestial_awakening.init.MenuInit;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;

public class AstralterMenu extends AbstractContainerMenu {
    public final AstralterBlockEntity astralterBlockEntity;
    private final Level level;

    SlotItemHandler sunstoneSlot;
    SlotItemHandler moonstoneSlot;

    public AstralterMenu(int pContainerId, Inventory inv, FriendlyByteBuf extraData) {
        this(pContainerId,inv,inv.player.level().getBlockEntity(extraData.readBlockPos()));
    }

    public AstralterMenu(int containerId, Inventory inv, BlockEntity entity) {
        super(MenuInit.ASTRALTER_MENU.get(),containerId);
        checkContainerSize(inv, 2);
        level=inv.player.level();
        astralterBlockEntity= (AstralterBlockEntity) entity;
        addPlayerHotbar(inv);
        addPlayerInventory(inv);
        this.astralterBlockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(
                itemHandler->{
                    this.addSlot(sunstoneSlot);
                    this.addSlot(moonstoneSlot);
                }
        );
    }
    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot sourceSlot = slots.get(index);
        if (sourceSlot == null || !sourceSlot.hasItem()){
            return ItemStack.EMPTY;
        }
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyStack = sourceStack.copy();
        if (index < INV_FIRST_SLOT_INDEX + TOTAL_SLOT_COUNT) {
            // This is a vanilla container slot so merge the stack into the tile inventory
            if (!moveItemStackTo(sourceStack, ASTRALTER_FIRST_SLOT_INDEX, ASTRALTER_FIRST_SLOT_INDEX
                    + ASTRALTER_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;  // EMPTY_ITEM
            }
        } else if (index < ASTRALTER_FIRST_SLOT_INDEX + ASTRALTER_SLOT_COUNT) {
            // This is a TE slot so merge the stack into the players inventory
            if (!moveItemStackTo(sourceStack, INV_FIRST_SLOT_INDEX, INV_FIRST_SLOT_INDEX + TOTAL_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            System.out.println("Invalid slotIndex:" + index);
            return ItemStack.EMPTY;
        }
        // If stack size == 0 (the entire stack was moved) set slot contents to null
        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(player, sourceStack);
        return copyStack;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(level,astralterBlockEntity.getBlockPos()),player, BlockInit.ASTRALTER_BLOCK.get());
    }

    private static final int HOTBAR_SLOTS = 9;
    private static final int ROWS = 3;
    private static final int COLUMNS = 9;
    private static final int INVENTORY_SLOTS = COLUMNS * ROWS;
    private static final int TOTAL_SLOT_COUNT = HOTBAR_SLOTS + INVENTORY_SLOTS;
    private static final int INV_FIRST_SLOT_INDEX = 0;
    private static final int ASTRALTER_FIRST_SLOT_INDEX = INV_FIRST_SLOT_INDEX + TOTAL_SLOT_COUNT;
    private int ASTRALTER_SLOT_COUNT = 0;
    public int getSlotCnt(){
        return ASTRALTER_SLOT_COUNT;
    }
    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }
}
