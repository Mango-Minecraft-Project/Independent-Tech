package dev.mangojellypudding.independenttech.cookingforblockheads.block;

import com.mojang.serialization.MapCodec;
import dev.mangojellypudding.independenttech.cookingforblockheads.block.entity.SinkBlockEntity;
import dev.mangojellypudding.independenttech.register.ITBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.fluids.FluidActionResult;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.Nullable;

public class SinkBlock extends BaseEntityBlock {
    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty FLIPPED = BooleanProperty.create("flipped");


    public SinkBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, FLIPPED);
    }

    public boolean shouldBePlacedFlipped(BlockPlaceContext context, Direction facing) {
        BlockPos pos = context.getClickedPos();
        Player placer = context.getPlayer();
        if (placer == null) {
            return Math.random() < 0.5;
        }

        boolean flipped;
        double dir = 0;
        if (facing.getAxis() == Direction.Axis.Z) {
            dir = pos.getX() + 0.5f - placer.getX();
            dir *= -1;
        } else if (facing.getAxis() == Direction.Axis.X) {
            dir = pos.getZ() + 0.5f - placer.getZ();
        }
        if (facing.getAxisDirection() == Direction.AxisDirection.POSITIVE) {
            flipped = dir < 0;
        } else {
            flipped = dir > 0;
        }
        return flipped;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = super.getStateForPlacement(context);
        return state.setValue(FLIPPED, shouldBePlacedFlipped(context, state.getValue(FACING)));
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult blockHitResult) {
        if (itemStack.isEmpty()) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof SinkBlockEntity sink)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        FluidActionResult result = FluidUtil.tryFillContainerAndStow(itemStack, SinkBlockEntity.fluidHandler, new InvWrapper(player.getInventory()), Integer.MAX_VALUE, player, true);

//        if (result.success) {
//
//        }
//
//        if (!FluidUtil.interactWithFluidHandler(player, hand, level, pos, blockHitResult.getDirection())) {
//            new FluidBucketWrapper(itemStack).canFillFluidType(new FluidStack(Fluids.WATER, Integer.MAX_VALUE));
//            if (itemStack.getItem() == Items.GLASS_BOTTLE) {
//                ItemStack filledBottle = PotionContents.createItemStack(Items.POTION, Potions.WATER);
//                if (itemStack.getCount() == 1) {
//                    player.setItemInHand(hand, filledBottle);
//                } else if (player.addItem(filledBottle)) {
//                    itemStack.shrink(1);
//                }
//                level.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.NEUTRAL, 1f, 1f);
//            } else {
//                spawnParticlesAndPlaySound(level, pos, state);
//            }
//        }

        return ItemInteractionResult.SUCCESS;
    }

    private void spawnParticlesAndPlaySound(Level level, BlockPos pos, BlockState state) {
        float dripWaterX = 0f;
        float dripWaterZ = 0f;
        switch (state.getValue(FACING)) {
            case NORTH -> {
                dripWaterZ = 0.25f;
                dripWaterX = -0.05f;
            }
            case SOUTH -> dripWaterX = 0.25f;
            case WEST -> {
                dripWaterX = 0.25f;
                dripWaterZ = 0.25f;
            }
            case EAST -> dripWaterZ = -0.05f;
        }

        float particleX = (float) pos.getX() + 0.5f;
        float particleY = (float) pos.getY() + 1.25f;
        float particleZ = (float) pos.getZ() + 0.5f;
        level.addParticle(ParticleTypes.SPLASH, (double) particleX + dripWaterX, (double) particleY - 0.45f, (double) particleZ + dripWaterZ, 0, 0, 0);
        for (int i = 0; i < 5; i++) {
            level.addParticle(ParticleTypes.SPLASH,
                    (double) particleX + Math.random() - 0.5f,
                    (double) particleY + Math.random() - 0.5f,
                    (double) particleZ + Math.random() - 0.5f,
                    0,
                    0,
                    0);
        }

        level.playSound(null, pos, SoundEvents.WATER_AMBIENT, SoundSource.BLOCKS, 0.1f, level.random.nextFloat() + 0.5f);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SinkBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null : createTickerHelper(type, ITBlockEntityTypes.SINK.get(), SinkBlockEntity::serverTick);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return null;
    }
}