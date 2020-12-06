package tv.mapper.blockrunner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import tv.mapper.blockrunner.config.RunnerConfig;

@Mod(BlockRunner.MODID)
public class BlockRunner
{
    public static final String MODID = "blockrunner";
    public static final Logger LOGGER = LogManager.getLogger();

    public BlockRunner()
    {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, RunnerConfig.COMMON_CONFIG);

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::serverSetup);

    }

    private void setup(final FMLCommonSetupEvent event)
    {
        LOGGER.info("Block Runner setup started!");
    }

    private void clientSetup(final FMLClientSetupEvent event)
    {}

    private void serverSetup(final FMLDedicatedServerSetupEvent event)
    {}
}
