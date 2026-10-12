package mekanism.fabric_shim.network;

import java.util.ArrayDeque;
import java.util.Queue;
import net.minecraft.network.RegistryFriendlyByteBuf;
import org.jetbrains.annotations.Nullable;

/**
 * Client-side queue for the extra buffer NeoForge sends with the open-screen packet. Fabric's menu
 * packet has no extra-data slot, so the server sends it as a Mekanism payload just before opening;
 * the menu factory pops the matching buffer when the vanilla open-screen packet arrives.
 */
public final class ContainerOpenData {

    private static final Queue<RegistryFriendlyByteBuf> PENDING = new ArrayDeque<>();

    private ContainerOpenData() {
    }

    public static synchronized void push(RegistryFriendlyByteBuf data) {
        PENDING.add(data);
    }

    @Nullable
    public static synchronized RegistryFriendlyByteBuf poll() {
        return PENDING.poll();
    }
}
