package jiekie.network;

import jiekie.EconomyMod;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

import java.nio.charset.StandardCharsets;

public record MoneyPayload(String formattedMoney) implements CustomPayload {
    public static final Id<MoneyPayload> ID = new Id<>(Identifier.of(EconomyMod.MOD_ID, "money_update"));

    public static final PacketCodec<RegistryByteBuf, MoneyPayload> CODEC =
        PacketCodec.ofStatic(MoneyPayload::write, MoneyPayload::read).cast();

    public static void write(RegistryByteBuf buf, MoneyPayload payload) {
        byte[] bytes = payload.formattedMoney().getBytes(StandardCharsets.UTF_8);
        if(bytes.length > 65535) throw new IllegalArgumentException("Too long");
        buf.writeShort(bytes.length);
        buf.writeBytes(bytes);
    }

    public static MoneyPayload read(RegistryByteBuf buf) {
        int len = buf.readUnsignedShort();
        byte[] bytes = new byte[len];
        buf.readBytes(bytes);
        return new MoneyPayload(new String(bytes, StandardCharsets.UTF_8));
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
