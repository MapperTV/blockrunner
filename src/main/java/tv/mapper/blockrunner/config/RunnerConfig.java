package tv.mapper.blockrunner.config;

import java.util.ArrayList;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;

public class RunnerConfig
{
    public static final CommonConfig COMMON;
    public static final ForgeConfigSpec COMMON_CONFIG;

    static
    {
        final Pair<CommonConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(CommonConfig::new);

        COMMON = specPair.getLeft();
        COMMON_CONFIG = specPair.getRight();
    }

    public static class CommonConfig
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

        CommonConfig(ForgeConfigSpec.Builder COMMON_BUILDER)
        {
            // Defaut config A
            default_config_a.add("minecraft:grass_path");
            default_config_a.add("minecraft:cobblestone");
            default_config_a.add("minecraft:mossy_cobblestone");
            default_config_a.add("minecraft:cobblestone_stairs");
            default_config_a.add("minecraft:cobblestone_slab");
            default_config_a.add("minecraft:mossy_cobblestone_stairs");
            default_config_a.add("minecraft:mossy_cobblestone_slab");
            default_config_a.add("minecraft:infested_cobblestone");

            // Default config B
            default_config_b.add("minecraft:stone_bricks");
            default_config_b.add("minecraft:stone_brick_stairs");
            default_config_b.add("minecraft:mossy_stone_bricks");
            default_config_b.add("minecraft:mossy_stone_brick_stairs");
            default_config_b.add("minecraft:cracked_stone_bricks");
            default_config_b.add("minecraft:chiseled_stone_bricks");
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

            COMMON_BUILDER.comment("Config A").push("config_a");

            CONFIG_A_ENABLE = COMMON_BUILDER.comment("Enable config A? [true / false]").define("configAEnabled", true);
            CONFIG_A_BLOCKS = COMMON_BUILDER.comment("List of blocks for config A (format is \"modid:block_name\")").define("configABlocks", default_config_a);
            CONFIG_A_SPEED = COMMON_BUILDER.comment("Speed bonus [default: 0.15]").defineInRange("configASpeed", 0.15, 0.01, 100.0);

            COMMON_BUILDER.pop();
            COMMON_BUILDER.comment("Config B").push("config_b");

            CONFIG_B_ENABLE = COMMON_BUILDER.comment("Enable config B? [true / false]").define("configBEnabled", true);
            CONFIG_B_BLOCKS = COMMON_BUILDER.comment("List of blocks for config B (format is \"modid:block_name\")").define("configBBlocks", default_config_b);
            CONFIG_B_SPEED = COMMON_BUILDER.comment("Speed bonus [default: 0.3]").defineInRange("configBSpeed", 0.3, 0.01, 100.0);

            COMMON_BUILDER.pop();
            COMMON_BUILDER.comment("Config C").push("config_c");

            CONFIG_C_ENABLE = COMMON_BUILDER.comment("Enable config C? [true / false]").define("configCEnabled", false);
            CONFIG_C_BLOCKS = COMMON_BUILDER.comment("List of blocks for config C (format is \"modid:block_name\")").define("configCBlocks", new ArrayList<>());
            CONFIG_C_SPEED = COMMON_BUILDER.comment("Speed bonus").defineInRange("configCSpeed", 0.01, 0.01, 100.0);

            COMMON_BUILDER.pop();
            COMMON_BUILDER.comment("Config D").push("config_d");

            CONFIG_D_ENABLE = COMMON_BUILDER.comment("Enable config D? [true / false]").define("configDEnabled", false);
            CONFIG_D_BLOCKS = COMMON_BUILDER.comment("List of blocks for config D (format is \"modid:block_name\")").define("configDBlocks", new ArrayList<>());
            CONFIG_D_SPEED = COMMON_BUILDER.comment("Speed bonus").defineInRange("configDSpeed", 0.01, 0.01, 100.0);

            COMMON_BUILDER.pop();
            COMMON_BUILDER.comment("Config E").push("config_e");

            CONFIG_E_ENABLE = COMMON_BUILDER.comment("Enable config E? [true / false]").define("configEEnabled", false);
            CONFIG_E_BLOCKS = COMMON_BUILDER.comment("List of blocks for config E (format is \"modid:block_name\")").define("configEBlocks", new ArrayList<>());
            CONFIG_E_SPEED = COMMON_BUILDER.comment("Speed bonus").defineInRange("configESpeed", 0.01, 0.01, 100.0);

            COMMON_BUILDER.pop();
        }
    }
}