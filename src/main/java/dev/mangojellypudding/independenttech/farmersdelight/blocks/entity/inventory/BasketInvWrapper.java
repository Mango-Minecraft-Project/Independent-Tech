package dev.mangojellypudding.independenttech.farmersdelight.blocks.entity.inventory;


import dev.mangojellypudding.independenttech.farmersdelight.blocks.entity.Basket;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.NotNull;

public class BasketInvWrapper extends InvWrapper {
    protected final Basket basket;

    public BasketInvWrapper(Basket basket) {
        super(basket);
        this.basket = basket;
    }

    @Override
    public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        if (simulate) {
            return super.insertItem(slot, stack, true);
        } else {
            boolean wasEmpty = basket.isEmpty();
            int originalCount = stack.getCount();
            stack = super.insertItem(slot, stack, false);
            if (wasEmpty && originalCount > stack.getCount()) {
                if (!basket.isOnCustomCooldown()) {
                    basket.setCooldown(8);
                }
            }
            return stack;
        }
    }
}