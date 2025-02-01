package com.sj.playerhealthindicator;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = PlayerHealthIndicator.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {

    // Constants for default values
    private static final String DEFAULT_COLOR = "FF0000"; // Default red color in hex
    private static final double DEFAULT_HEIGHT_OFFSET = 0.7; // Default height offset above the player's head

    // Configuration Builder
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    // Config Options
    private static final ForgeConfigSpec.BooleanValue SHOW_HEALTH_INDICATORS = BUILDER
            .comment("Enable or disable health indicators above players' heads.")
            .define("showHealthIndicators", true);

    private static final ForgeConfigSpec.ConfigValue<String> HEALTH_TEXT_COLOR = BUILDER
            .comment("The color of the health text in hexadecimal format (e.g., FF0000 for red).")
            .define("healthTextColor", DEFAULT_COLOR);

    private static final ForgeConfigSpec.DoubleValue HEALTH_TEXT_HEIGHT_OFFSET = BUILDER
            .comment("The vertical offset of the health text above the player's head.")
            .defineInRange("healthTextHeightOffset", DEFAULT_HEIGHT_OFFSET, 0.0, 5.0);

    // Build the config specification
    static final ForgeConfigSpec SPEC = BUILDER.build();

    // Config Variables
    public static boolean showHealthIndicators;
    public static String healthTextColor;
    public static double healthTextHeightOffset;

    // Load the config values
    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        showHealthIndicators = SHOW_HEALTH_INDICATORS.get();
        healthTextColor = HEALTH_TEXT_COLOR.get();
        healthTextHeightOffset = HEALTH_TEXT_HEIGHT_OFFSET.get();
    }
}