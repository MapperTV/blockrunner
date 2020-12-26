package tv.mapper.blockrunner.config;

import java.util.ArrayList;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;

public class RunnerConfig
{
    public static final ServerConfig SERVER;
    public static final ForgeConfigSpec SERVER_CONFIG;

    static
    {
        final Pair<ServerConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ServerConfig::new);

        SERVER = specPair.getLeft();
        SERVER_CONFIG = specPair.getRight();
    }

    public static class ServerConfig
    {
        private static ArrayList<String> default_config_a = new ArrayList<>();
        private static ArrayList<String> default_config_b = new ArrayList<>();

        public static ForgeConfigSpec.BooleanValue CONFIG_A_ENABLE;
        public static ConfigValue<ArrayList<String>> CONFIG_A_BLOCKS;
        public static ForgeConfigSpec.DoubleValue CONFIG_A_SPEED;

        public static ForgeConfigSpec.BooleanValue CONFIG_B_ENABLE;
        public static ConfigValue<ArrayList<String>> CONFIG_B_BLOCKS;
        public static ForgeConfigSpec.DoubleValue CONFIG_B_SPEED;

        public static ForgeConfigSpec.BooleanValue CONFIG_C_ENABLE;
        public static ConfigValue<ArrayList<String>> CONFIG_C_BLOCKS;
        public static ForgeConfigSpec.DoubleValue CONFIG_C_SPEED;

        public static ForgeConfigSpec.BooleanValue CONFIG_D_ENABLE;
        public static ConfigValue<ArrayList<String>> CONFIG_D_BLOCKS;
        public static ForgeConfigSpec.DoubleValue CONFIG_D_SPEED;

        public static ForgeConfigSpec.BooleanValue CONFIG_E_ENABLE;
        public static ConfigValue<ArrayList<String>> CONFIG_E_BLOCKS;
        public static ForgeConfigSpec.DoubleValue CONFIG_E_SPEED;

        ServerConfig(ForgeConfigSpec.Builder serverBuilder)
        {
            // Defaut config A
            default_config_a.add("minecraft:grass_path");
            default_config_a.add("#forge:cobblestone");
            default_config_a.add("minecraft:mossy_cobblestone");
            default_config_a.add("minecraft:cobblestone_stairs");
            default_config_a.add("minecraft:cobblestone_slab");
            default_config_a.add("minecraft:mossy_cobblestone_stairs");
            default_config_a.add("minecraft:mossy_cobblestone_slab");
            default_config_a.add("minecraft:infested_cobblestone");

            // Default config B
            default_config_b.add("#minecraft:stone_bricks");
            default_config_b.add("minecraft:stone_brick_stairs");
            default_config_b.add("minecraft:mossy_stone_brick_stairs");
            default_config_b.add("minecraft:infested_stone_bricks");
            default_config_b.add("minecraft:infested_mossy_stone_bricks");
            default_config_b.add("minecraft:infested_cracked_stone_bricks");
            default_config_b.add("minecraft:infested_chiseled_stone_bricks");
            default_config_b.add("minecraft:stone_brick_slab");
            default_config_b.add("minecraft:mossy_stone_brick_slab");
            default_config_b.add("minecraft:polished_andesite");
            default_config_b.add("minecraft:polished_andesite_slab");
            default_config_b.add("minecraft:polished_diorite");
            default_config_b.add("minecraft:polished_diorite_slab");
            default_config_b.add("minecraft:polished_granite");
            default_config_b.add("minecraft:polished_granite_slab");

            serverBuilder.comment("Config A").push("config_a");

            CONFIG_A_ENABLE = serverBuilder.comment("Enable config A? [true / false]").define("configAEnabled", true);
            CONFIG_A_BLOCKS = serverBuilder.comment("List of blocks or tags for config A (format is \"modid:block_name\" for a block and \"#namespace:tag_name\" for a tag)").define("configABlocks",
                default_config_a);
            CONFIG_A_SPEED = serverBuilder.comment("Speed bonus [default: 0.015]").defineInRange("configASpeed", 0.015, -0.1, 1.0);

            serverBuilder.pop();
            serverBuilder.comment("Config B").push("config_b");

            CONFIG_B_ENABLE = serverBuilder.comment("Enable config B? [true / false]").define("configBEnabled", true);
            CONFIG_B_BLOCKS = serverBuilder.comment("List of blocks or tags for config B (format is \"modid:block_name\" for a block and \"#namespace:tag_name\" for a tag)").define("configBBlocks",
                default_config_b);
            CONFIG_B_SPEED = serverBuilder.comment("Speed bonus [default: 0.03]").defineInRange("configBSpeed", 0.03, -0.1, 1.0);

            serverBuilder.pop();
            serverBuilder.comment("Config C").push("config_c");

            CONFIG_C_ENABLE = serverBuilder.comment("Enable config C? [true / false]").define("configCEnabled", false);
            CONFIG_C_BLOCKS = serverBuilder.comment("List of blocks or tags for config C (format is \"modid:block_name\" for a block and \"#namespace:tag_name\" for a tag)").define("configCBlocks",
                new ArrayList<>());
            CONFIG_C_SPEED = serverBuilder.comment("Speed bonus").defineInRange("configCSpeed", 0, -0.1, 1.0);

            serverBuilder.pop();
            serverBuilder.comment("Config D").push("config_d");

            CONFIG_D_ENABLE = serverBuilder.comment("Enable config D? [true / false]").define("configDEnabled", false);
            CONFIG_D_BLOCKS = serverBuilder.comment("List of blocks or tags for config D (format is \"modid:block_name\" for a block and \"#namespace:tag_name\" for a tag)").define("configDBlocks",
                new ArrayList<>());
            CONFIG_D_SPEED = serverBuilder.comment("Speed bonus").defineInRange("configDSpeed", 0, -0.1, 1.0);

            serverBuilder.pop();
            serverBuilder.comment("Config E").push("config_e");

            CONFIG_E_ENABLE = serverBuilder.comment("Enable config E? [true / false]").define("configEEnabled", false);
            CONFIG_E_BLOCKS = serverBuilder.comment("List of blocks or tags for config E (format is \"modid:block_name\" for a block and \"#namespace:tag_name\" for a tag)").define("configEBlocks",
                new ArrayList<>());
            CONFIG_E_SPEED = serverBuilder.comment("Speed bonus").defineInRange("configESpeed", 0, -0.1, 1.0);

            serverBuilder.pop();
        }
    }
}