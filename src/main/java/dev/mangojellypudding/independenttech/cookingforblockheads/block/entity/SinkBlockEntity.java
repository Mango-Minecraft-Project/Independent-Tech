package dev.mangojellypudding.independenttech.cookingforblockheads.block.entity;

import dev.mangojellypudding.independenttech.register.ITBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

public class SinkBlockEntity extends BlockEntity {
    private static final int SYNC_INTERVAL = 10;

    public final FluidTank sinkTank = new FluidTank(16000) {
        @Override
        public FluidStack getFluid() {
            return new FluidStack(Fluids.WATER, Integer.MAX_VALUE);
        }

        @Override
        public int getCapacity() {
            return Integer.MAX_VALUE;
        }

        @Override
        public FluidStack drain(FluidStack resource, IFluidHandler.FluidAction action) {
            return this.drain(resource.getAmount(), action);
        }

        public FluidStack drain(int maxDrain, IFluidHandler.FluidAction action) {
            return getFluid();
        }
    };

    private int ticksSinceSync;

    public SinkBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    public SinkBlockEntity(BlockPos pos, BlockState state) {
        super(ITBlockEntityTypes.SINK.get(), pos, state);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, SinkBlockEntity blockEntity) {
        blockEntity.serverTick(level, pos, state);
    }

    public void serverTick(Level level, BlockPos pos, BlockState state) {
        // Sync to clients
        ticksSinceSync++;
        if (ticksSinceSync >= SYNC_INTERVAL) {
            ticksSinceSync = 0;
        }
    }
}