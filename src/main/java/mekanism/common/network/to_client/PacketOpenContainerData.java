package mekanism.common.network.to_client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import mekanism.common.Mekanism;
import mekanism.common.network.IMekanismPacket;
import mekanism.fabric_shim.network.ContainerOpenData;
import mekanism.fabric_shim.network.handling.IPayloadContext;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

public record PacketOpenContainerData(byte[] data) implements IMekanismPacket {

    public static final CustomPacketPayload.Type<PacketOpenContainerData> TYPE = new CustomPacketPayload.Type<>(Mekanism.rl("open_container_data"));
    public static final StreamCodec<ByteBuf, PacketOpenContainerData> STREAM_CODEC = StreamCodec.of(
          (buf, packet) -> buf.writeBytes(packet.data),
          buf -> {
              byte[] data = new byte[buf.readableBytes()];
              buf.readBytes(data);
              return new PacketOpenContainerData(data);
          });

    @NotNull
    @Override
    public CustomPacketPayload.Type<PacketOpenContainerData> type() {
        return TYPE;
    }

    @Override
    public void handle(IPayloadContext context) {
        RegistryFriendlyByteBuf buf = new RegistryFriendlyByteBuf(new FriendlyByteBuf(Unpooled.wrappedBuffer(data)), context.player().level().registryAccess());
        ContainerOpenData.push(buf);
    }
}
