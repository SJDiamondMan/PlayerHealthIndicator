package com.sj.playerhealthindicator;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(PlayerHealthIndicator.MODID)
public class PlayerHealthIndicator {

    // Define mod ID in a common place for everything to reference
    public static final String MODID = "playerhealthindicator";

    // Directly reference a logger
    private static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    public PlayerHealthIndicator() {
        // Register the client setup method for mod loading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        FMLJavaModLoadingContext.get().getModEventBus().register(Config.class);
        ModConfig.Type.CLIENT.register(ModConfig.Type.CLIENT, Config.SPEC);

        // Register this class to handle rendering events
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        // Log a message during client setup
        LOGGER.info("Player Health Indicator Mod Initialized");
    }

    /**
     * Handles rendering health indicators above players' heads.
     */
    @net.minecraftforge.eventbus.api.SubscribeEvent
    public void onRenderLiving(RenderLivingEvent.Post<?, ?> event) {
        // Check if health indicators are enabled
        if (!Config.showHealthIndicators) {
            return;
        }

        // Check if the entity being rendered is a player
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();

            // Get the player's health and format it
            float health = player.getHealth();
            String healthText = String.format("%.1f", health); // Format to one decimal place

            // Get the font renderer and prepare the pose stack
            PoseStack poseStack = event.getPoseStack();
            Font font = Minecraft.getInstance().font;

            // Push the current transformation matrix
            poseStack.pushPose();

            // Translate to the player's position and offset above their head
            poseStack.translate(0, player.getBbHeight() + Config.healthTextHeightOffset, 0);
            poseStack.mulPose(Minecraft.getInstance().gameRenderer.getMainCamera().rotation());
            poseStack.scale(-0.025f, -0.025f, 0.025f); // Scale the text size

            // Parse the color from the config
            int color = Integer.parseInt(Config.healthTextColor, 16);

            // Draw the health text
            font.draw(poseStack, healthText, -font.width(healthText) / 2f, 0, color);

            // Pop the transformation matrix
            poseStack.popPose();
        }
    }
}