package mekanism.fabric_shim.inject;

import java.util.OptionalInt;
import java.util.function.Consumer;
import mekanism.common.network.to_client.PacketOpenContainerData;
import mekanism.fabric_shim.common.util.FriendlyByteBufUtil;
import mekanism.fabric_shim.network.PacketDistributor;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;

/**
 * NeoForge's {@code IPlayerExtension.openMenu} overloads that pass extra opening data. Injected onto
 * vanilla {@link Player} + PlayerMixin; delegates to vanilla {@code openMenu(MenuProvider)}. The extra
 * data writer is ignored for 1f (Phase 3 sends it via extended screen handlers), so menus open but
 * without their initial sync payload until then.
 */
public interface MekPlayerExt {

    default OptionalInt openMenu(MenuProvider menuProvider, Consumer<RegistryFriendlyByteBuf> extraDataWriter) {
        Player self = (Player) this;
        if (self instanceof ServerPlayer serverPlayer && extraDataWriter != null) {
            byte[] data = FriendlyByteBufUtil.writeCustomData(extraDataWriter, serverPlayer.level().registryAccess());
            PacketDistributor.sendToPlayer(serverPlayer, new PacketOpenContainerData(data));
        }
        return self.openMenu(menuProvider);
    }

    default OptionalInt openMenu(MenuProvider menuProvider, BlockPos pos) {
        return ((Player) this).openMenu(menuProvider);
    }
}
