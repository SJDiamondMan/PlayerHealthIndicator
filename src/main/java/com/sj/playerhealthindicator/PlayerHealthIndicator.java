package com.sj.playerhealthindicator;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.eventbus.api.IEventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(PlayerHealthIndicator.MODID)
public class PlayerHealthIndicator {

    public static final String MODID = "playerhealthindicator";
    private static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    public PlayerHealthIndicator(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();
        modEventBus.addListener(this::clientSetup);
        modEventBus.register(Config.class);

        ModLoadingContext.registerConfig(ModConfig.Type.CLIENT, Config.SPEC);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        LOGGER.info("Player Health Indicator Mod Initialized");
    }

    @net.minecraftforge.eventbus.api.SubscribeEvent
    public void onRenderLiving(RenderLivingEvent.Post<?, ?, ?> event) {
        if (!Config.showHealthIndicators) {
            return;
        }

        if (event.getEntity() instanceof Player player) {
            float health = player.getHealth();
            String healthText = String.format("%.1f", health);

            PoseStack poseStack = event.getPoseStack();
            Font font = Minecraft.getInstance().font;

            poseStack.pushPose();
            poseStack.translate(0, player.getBbHeight() + Config.healthTextHeightOffset, 0);
            poseStack.mulPose(Minecraft.getInstance().gameRenderer.getMainCamera().rotation());
            poseStack.scale(-0.025f, -0.025f, 0.025f);

            int color = Integer.parseInt(Config.healthTextColor, 16);

            font.drawInBatch(healthText, -font.width(healthText) / 2f, 0, color, false,
                    poseStack.last().pose(), Minecraft.getInstance().renderBuffers().bufferSource(),
                    Font.DisplayMode.NORMAL, 0, 15728880);

            poseStack.popPose();
        }
    }
}
