package tv.mapper.blockrunner.config;

import java.util.ArrayList;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import tv.mapper.blockrunner.BlockRunner;

public class ConfigChecker
{
    public static void check()
    {
        if(RunnerConfig.ServerConfig.CONFIG_A_ENABLE.get())
            checkList(RunnerConfig.ServerConfig.CONFIG_A_BLOCKS.get(), 1);
        if(RunnerConfig.ServerConfig.CONFIG_B_ENABLE.get())
            checkList(RunnerConfig.ServerConfig.CONFIG_B_BLOCKS.get(), 2);
        if(RunnerConfig.ServerConfig.CONFIG_C_ENABLE.get())
            checkList(RunnerConfig.ServerConfig.CONFIG_C_BLOCKS.get(), 3);
        if(RunnerConfig.ServerConfig.CONFIG_D_ENABLE.get())
            checkList(RunnerConfig.ServerConfig.CONFIG_D_BLOCKS.get(), 4);
        if(RunnerConfig.ServerConfig.CONFIG_E_ENABLE.get())
            checkList(RunnerConfig.ServerConfig.CONFIG_E_BLOCKS.get(), 5);
    }

    private static void checkList(ArrayList<String> list, int config)
    {
        String attribute = config == 1 ? "A" : config == 2 ? "B" : config == 3 ? "C" : config == 4 ? "D" : "E";

        for(String block : list)
        {
            // Check if a block is listed in two or more configs
            switch(config)
            {
                case 5:
                    if(RunnerConfig.ServerConfig.CONFIG_D_BLOCKS.get().contains(block))
                        BlockRunner.LOGGER.warn("5- Block " + block + " is already listed in config D and will be skipped for config " + attribute + ".");
                case 4:
                    if(RunnerConfig.ServerConfig.CONFIG_C_BLOCKS.get().contains(block))
                        BlockRunner.LOGGER.warn("4- Block " + block + " is already listed in config C and will be skipped for config " + attribute + ".");
                case 3:
                    if(RunnerConfig.ServerConfig.CONFIG_B_BLOCKS.get().contains(block))
                        BlockRunner.LOGGER.warn("3- Block " + block + " is already listed in config B and will be skipped for config " + attribute + ".");
                case 2:
                    if(RunnerConfig.ServerConfig.CONFIG_A_BLOCKS.get().contains(block))
                        BlockRunner.LOGGER.warn("2- Block " + block + " is already listed in config A and will be skipped for config " + attribute + ".");
                    break;
            }

            // Check if a non-existing block exist in one of the configs
            if(!ForgeRegistries.BLOCKS.containsKey(new ResourceLocation(block)))
            {
                BlockRunner.LOGGER.warn("Block " + block + " in BlockRunner's config " + attribute + " is NOT loaded by the game! Please double check your BlockRunner config. This block will be skipped.");
            }
        }
    }
}