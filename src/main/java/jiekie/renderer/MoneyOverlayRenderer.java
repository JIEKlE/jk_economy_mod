package jiekie.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import jiekie.EconomyMod;
import jiekie.network.MoneyPacketReceiver;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public class MoneyOverlayRenderer implements HudRenderCallback {
    private static final Identifier TEXT_LOGO = Identifier.of(EconomyMod.MOD_ID, "textures/gui/text_logo.png");

    @Override
    public void onHudRender(DrawContext context, RenderTickCounter tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        if(client.player == null || client.options.hudHidden) return;

        // default size
        int paddingLeft = 5;
        int paddingTop = 5;
        int innerMargin = 3;

        // player head
        int headSize = 32;
        int headX = paddingLeft + innerMargin;
        int headY = paddingTop + innerMargin;

        // logo size
        int logoWidth = 64;
        int logoHeight = 17;
        int logoX = headX + headSize + innerMargin;
        int logoY = headY;

        // text size
        String moneyText = MoneyPacketReceiver.currentMoney;
        Text text = Text.literal(moneyText).formatted(Formatting.WHITE);
        int moneyTextWidth = Math.min(client.textRenderer.getWidth(text), 64);
        int moneyTextHeight = 12;
        int moneyX = logoX + (64 - moneyTextWidth);
        int moneyY = logoY + logoHeight + innerMargin;

        // background size
        int boxLeft = paddingLeft;
        int boxTop = paddingTop;
        int boxRight = logoX + logoWidth + innerMargin;
        int boxBottom = moneyY + moneyTextHeight + innerMargin;
        context.fill(boxLeft, boxTop, boxRight, boxBottom, 0x88000000);

        // player head
        int scale = 4;
        Identifier texture = ((AbstractClientPlayerEntity) client.player).getSkinTextures().texture();
        RenderSystem.setShaderTexture(0, texture);
        context.getMatrices().push();
        context.getMatrices().translate(headX, headY, 0);
        context.getMatrices().scale(scale, scale, 1.0f);
        context.drawTexture(texture, 0, 0, 8.0f, 8.0f, 8, 8, 64, 64);
        context.drawTexture(texture, 0, 0, 40.0f, 8.0f, 8, 8, 64, 64);
        context.getMatrices().pop();

        // money
        context.drawText(client.textRenderer, text, moneyX, moneyY, 0xFFFFFF, false);

        // logo
        RenderSystem.setShaderTexture(0, TEXT_LOGO);
        context.drawTexture(TEXT_LOGO, logoX, logoY, 0, 0, logoWidth, logoHeight, logoWidth, logoHeight);
    }
}
