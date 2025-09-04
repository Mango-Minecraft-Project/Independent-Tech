package dev.mangojellypudding.independenttech.farmersdelight.blocks.entity;

import dev.mangojellypudding.independenttech.IndependentTech;
import dev.mangojellypudding.independenttech.farmersdelight.blocks.BasketBlock;
import dev.mangojellypudding.independenttech.farmersdelight.blocks.entity.inventory.BasketInvWrapper;
import dev.mangojellypudding.independenttech.register.ITBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

import java.util.function.BooleanSupplier;

@EventBusSubscriber(modid = IndependentTech.MODID)
public class BasketBlockEntity extends RandomizableContainerBlockEntity implements Basket
{
    private NonNullList<ItemStack> items = NonNullList.withSize(27, ItemStack.EMPTY);
    private int transferCooldown = -1;

    public BasketBlockEntity(BlockPos pos, BlockState state) {
        super(ITBlockEntityTypes.BASKET.get(), pos, state);
    }

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                ITBlockEntityTypes.BASKET.get(),
                (be, context) -> new BasketInvWrapper(be)
        );
    }

    public static void pushItemsTick(Level level, BlockPos pos, BlockState state, BasketBlockEntity blockEntity) {
        --blockEntity.transferCooldown;
        if (!blockEntity.isOnCooldown()) {
            blockEntity.setCooldown(0);
            int facing = state.getValue(BasketBlock.FACING).get3DDataValue();
            blockEntity.tryTransfer(() -> blockEntity.collectItems(level, facing));
        }
    }

    @Override
    protected void loadAdditional(CompoundTag compound, HolderLookup.Provider registries) {
        super.loadAdditional(compound, registries);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        if (!this.tryLoadLootTable(compound)) {
            ContainerHelper.loadAllItems(compound, this.items, registries);
        }
        this.transferCooldown = compound.getInt("TransferCooldown");
    }

    @Override
    public void saveAdditional(CompoundTag compound, HolderLookup.Provider registries) {
        super.saveAdditional(compound, registries);
        if (!this.trySaveLootTable(compound)) {
            ContainerHelper.saveAllItems(compound, this.items, registries);
        }

        compound.putInt("TransferCooldown", this.transferCooldown);
    }

    @Override
    public int getContainerSize() {
        return this.items.size();
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        this.unpackLootTable(null);
        return ContainerHelper.removeItem(this.getItems(), index, count);
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        this.unpackLootTable(null);
        this.getItems().set(index, stack);
        if (stack.getCount() > this.getMaxStackSize()) {
            stack.setCount(this.getMaxStackSize());
        }
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container." + IndependentTech.MODID + ".farmersdelight.basket");
    }

    // -- STANDARD INVENTORY STUFF --
    @Override
    protected NonNullList<ItemStack> getItems() {
        return this.items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> itemsIn) {
        this.items = itemsIn;
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory player) {
        return ChestMenu.threeRows(id, player, this);
    }

    @Override
    public void setCooldown(int ticks) {
        this.transferCooldown = ticks;
    }

    public boolean isOnCooldown() {
        return this.transferCooldown > 0;
    }

    @Override
    public boolean isOnCustomCooldown() {
        return this.transferCooldown > 8;
    }

    @Override
    public void tryTransfer(BooleanSupplier transfer) {
        if (this.level != null && !this.level.isClientSide) {
            if (!this.isOnCooldown() && this.getBlockState().getValue(BlockStateProperties.ENABLED)) {
                boolean flag = false;
                if (!this.isFull()) {
                    flag = transfer.getAsBoolean();
                }

                if (flag) {
                    this.setCooldown(8);
                    this.setChanged();
                }
            }
        }
    }

    protected boolean isFull() {
        for (ItemStack itemstack : this.items) {
            if (itemstack.isEmpty() || itemstack.getCount() != itemstack.getMaxStackSize()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public double getLevelX() {
        return (double) this.worldPosition.getX() + 0.5D;
    }

    @Override
    public double getLevelY() {
        return (double) this.worldPosition.getY() + 0.5D;
    }

    @Override
    public double getLevelZ() {
        return (double) this.worldPosition.getZ() + 0.5D;
    }
}