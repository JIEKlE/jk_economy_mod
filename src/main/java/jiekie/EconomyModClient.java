package jiekie;

import jiekie.network.MoneyPacketReceiver;
import jiekie.network.MoneyPayload;
import jiekie.renderer.MoneyOverlayRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

@Environment(EnvType.CLIENT)
public class EconomyModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        PayloadTypeRegistry.playS2C().register(MoneyPayload.ID, MoneyPayload.CODEC);
        ClientPlayNetworking.registerGlobalReceiver(MoneyPayload.ID, (payload, context) -> {
            context.client().execute(() -> {
                MoneyPacketReceiver.currentMoney = payload.formattedMoney();
            });
        });

        HudRenderCallback.EVENT.register(new MoneyOverlayRenderer());
    }
}
