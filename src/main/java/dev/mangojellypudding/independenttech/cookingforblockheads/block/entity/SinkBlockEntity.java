package dev.mangojellypudding.independenttech.cookingforblockheads.block.entity;

import dev.mangojellypudding.independenttech.register.ITBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;

public class SinkBlockEntity extends BlockEntity {
    private static final int SYNC_INTERVAL = 10;

    // 这里水槽不包含实际内部状态，所以提供一个静态字段即可，且不需要任何setChange或序列化保存
    public static final IFluidHandler fluidHandler = new IFluidHandler()
    {
        @Override
        public int getTanks()
        {
            return 1;
        }

        @Override
        public @NotNull FluidStack getFluidInTank(int tank)
        {
            return new FluidStack(Fluids.WATER, Integer.MAX_VALUE);
        }

        @Override
        public int getTankCapacity(int tank)
        {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isFluidValid(int tank, @NotNull FluidStack fluidStack)
        {
            return true;
        }

        // 返回实际填充量
        // fluidAction在这里用来检查是不是模拟操作
        // 如果是模拟操作，则不改变容器内状态，但是此处水槽本身无实际的内部状态，因此可以不管
        @Override
        public int fill(FluidStack fluidStack, @NotNull FluidAction fluidAction)
        {
            return fluidStack.getAmount(); // 如果想把它当流体垃圾桶
            //return 0; 不接受任何流体
        }

        // 返回实际排出量
        @Override
        public @NotNull FluidStack drain(FluidStack fluidStack, @NotNull FluidAction fluidAction)
        {
            if(fluidStack.getFluid() == Fluids.WATER)
                return fluidStack.copy(); // 总是返回请求量
            return FluidStack.EMPTY;
        }

        @Override
        public @NotNull FluidStack drain(int amount, FluidAction fluidAction)
        {
            if(amount > 0)
                return new FluidStack(Fluids.WATER, amount);
            return FluidStack.EMPTY;
        }
    };

    // 注册能力，这里给你放到mod主类去注册了，回头你自己整理一下就行
    public static void registerCapability(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                ITBlockEntityTypes.SINK.get(),
                (be, side) -> SinkBlockEntity.fluidHandler
        );
    }

    // 如果需要主动输出能力，则在tick中自己使用level.getCapability获取对面的流体能力
    // 然后自行操作对方的流体容器

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