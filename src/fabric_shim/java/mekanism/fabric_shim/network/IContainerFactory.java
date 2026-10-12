package mekanism.fabric_shim.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

/**
 * Stand-in for NeoForge's {@code IContainerFactory}: a {@link MenuType.MenuSupplier} that also reads
 * the extra opening data from a {@link RegistryFriendlyByteBuf}. Same surface as upstream.
 */
public interface IContainerFactory<T extends AbstractContainerMenu> extends MenuType.MenuSupplier<T> {

    T create(int windowId, Inventory inv, RegistryFriendlyByteBuf data);

    @Override
    default T create(int windowId, Inventory inv) {
        RegistryFriendlyByteBuf data = ContainerOpenData.poll();
        if (data != null) {
            try {
                return create(windowId, inv, data);
            } finally {
                data.release();
            }
        }
        return create(windowId, inv, null);
    }
}
