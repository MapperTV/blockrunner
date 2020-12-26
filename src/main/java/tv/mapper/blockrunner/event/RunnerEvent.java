package tv.mapper.blockrunner.event;

import java.util.UUID;

import net.minecraft.block.Block;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import tv.mapper.blockrunner.BlockRunner;
import tv.mapper.blockrunner.config.ConfigChecker;
import tv.mapper.blockrunner.config.RunnerConfig;

@Mod.EventBusSubscriber(modid = BlockRunner.MODID)
public class RunnerEvent
{
    private static AttributeModifier roadAttributeA;
    private static AttributeModifier roadAttributeB;
    private static AttributeModifier roadAttributeC;
    private static AttributeModifier roadAttributeD;
    private static AttributeModifier roadAttributeE;

    private static int attribute = 0;
    private static int previousAttribute = 0;
    private static boolean firstLaunch = true;

    public static void init()
    {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(RunnerEvent::configChange);
    }

    static void configChange(final ModConfig.Reloading event)
    {
        ModConfig config = event.getConfig();

        if(config.getConfigData().get("config_a.configASpeed") != null)
            roadAttributeA = new AttributeModifier(UUID.fromString("aed652ce-7d33-480e-89ca-dacf858d7596"), "Road Speed Modifier A", config.getConfigData().get(
                "config_a.configASpeed"), AttributeModifier.Operation.ADDITION);
        if(config.getConfigData().get("config_b.configBSpeed") != null)
            roadAttributeB = new AttributeModifier(UUID.fromString("1df40977-2b88-4f19-97c8-bcb2fe280054"), "Road Speed Modifier B", config.getConfigData().get(
                "config_b.configBSpeed"), AttributeModifier.Operation.ADDITION);
        if(config.getConfigData().get("config_c.configCSpeed") != null)
            roadAttributeC = new AttributeModifier(UUID.fromString("181b23a1-4974-4832-bd10-41f38e8bbe10"), "Road Speed Modifier C", config.getConfigData().get(
                "config_c.configCSpeed"), AttributeModifier.Operation.ADDITION);
        if(config.getConfigData().get("config_d.configDSpeed") != null)
            roadAttributeD = new AttributeModifier(UUID.fromString("bfab8ac9-05ee-4ffe-89ff-218742d32cc2"), "Road Speed Modifier D", config.getConfigData().get(
                "config_d.configDSpeed"), AttributeModifier.Operation.ADDITION);
        if(config.getConfigData().get("config_e.configESpeed") != null)
            roadAttributeE = new AttributeModifier(UUID.fromString("29520a66-3e58-423a-ab0a-cad04e9f5e41"), "Road Speed Modifier E", config.getConfigData().get(
                "config_e.configESpeed"), AttributeModifier.Operation.ADDITION);
        BlockRunner.LOGGER.info("Updated config changes!");
        ConfigChecker.check();
    }

    // Initialize attribute after server started to be sure they get custom config values, otherwise they won't for some reason I can't find
    private static void initAttributes()
    {
        roadAttributeA = new AttributeModifier(UUID.fromString(
            "aed652ce-7d33-480e-89ca-dacf858d7596"), "Road Speed Modifier A", RunnerConfig.ServerConfig.CONFIG_A_SPEED.get(), AttributeModifier.Operation.ADDITION);
        roadAttributeB = new AttributeModifier(UUID.fromString(
            "1df40977-2b88-4f19-97c8-bcb2fe280054"), "Road Speed Modifier B", RunnerConfig.ServerConfig.CONFIG_B_SPEED.get(), AttributeModifier.Operation.ADDITION);
        roadAttributeC = new AttributeModifier(UUID.fromString(
            "181b23a1-4974-4832-bd10-41f38e8bbe10"), "Road Speed Modifier C", RunnerConfig.ServerConfig.CONFIG_C_SPEED.get(), AttributeModifier.Operation.ADDITION);
        roadAttributeD = new AttributeModifier(UUID.fromString(
            "bfab8ac9-05ee-4ffe-89ff-218742d32cc2"), "Road Speed Modifier D", RunnerConfig.ServerConfig.CONFIG_D_SPEED.get(), AttributeModifier.Operation.ADDITION);
        roadAttributeE = new AttributeModifier(UUID.fromString(
            "29520a66-3e58-423a-ab0a-cad04e9f5e41"), "Road Speed Modifier E", RunnerConfig.ServerConfig.CONFIG_E_SPEED.get(), AttributeModifier.Operation.ADDITION);

        firstLaunch = false;
    }

    @SubscribeEvent
    public static void detectRoad(LivingEvent.LivingUpdateEvent event)
    {
        LivingEntity player = (LivingEntity)event.getEntity();

        if(firstLaunch)
            initAttributes();

        if(player instanceof PlayerEntity && !player.getEntityWorld().isRemote)
        {
            if(player.isOnGround())
            {
                Block block = player.getEntityWorld().getBlockState(new BlockPos(player.getPosX(), player.getBoundingBox().minY - (double)0.05F, player.getPosZ())).getBlock();
                String playerBlock = block.getRegistryName().toString();

                previousAttribute = attribute;
                attribute = 0;

                if(RunnerConfig.ServerConfig.CONFIG_A_BLOCKS.get().contains(playerBlock) && RunnerConfig.ServerConfig.CONFIG_A_ENABLE.get())
                    attribute = 1;
                else if(RunnerConfig.ServerConfig.CONFIG_B_BLOCKS.get().contains(playerBlock) && RunnerConfig.ServerConfig.CONFIG_B_ENABLE.get())
                    attribute = 2;
                else if(RunnerConfig.ServerConfig.CONFIG_C_BLOCKS.get().contains(playerBlock) && RunnerConfig.ServerConfig.CONFIG_C_ENABLE.get())
                    attribute = 3;
                else if(RunnerConfig.ServerConfig.CONFIG_D_BLOCKS.get().contains(playerBlock) && RunnerConfig.ServerConfig.CONFIG_D_ENABLE.get())
                    attribute = 4;
                else if(RunnerConfig.ServerConfig.CONFIG_E_BLOCKS.get().contains(playerBlock) && RunnerConfig.ServerConfig.CONFIG_E_ENABLE.get())
                    attribute = 5;

                if(attribute >= 0 && attribute <= 5 && attribute != previousAttribute)
                {
                    if(BlockRunner.debug)
                        BlockRunner.LOGGER.debug(
                            "Walking on block " + playerBlock + ", applying attribute n°" + attribute + " TEST " + roadAttributeA.getAmount() + "/" + RunnerConfig.ServerConfig.CONFIG_A_SPEED.get());

                    applyAttribute(previousAttribute, attribute, (PlayerEntity)player);
                }
            }
        }
    }

    private static void resetAttribute(int previousAttribute, PlayerEntity entity)
    {
        if(BlockRunner.debug)
            BlockRunner.LOGGER.debug("Previous: " + previousAttribute);

        switch(previousAttribute)
        {
            case 1:
                entity.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(roadAttributeA);
                break;
            case 2:
                entity.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(roadAttributeB);
                break;
            case 3:
                entity.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(roadAttributeC);
                break;
            case 4:
                entity.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(roadAttributeD);
                break;
            case 5:
                entity.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(roadAttributeE);
                break;
            case 0:
            default:
                break;
        }
    }

    private static void applyAttribute(int previousAttribute, int attribute, PlayerEntity entity)
    {
        resetAttribute(previousAttribute, entity);

        switch(attribute)
        {
            case 0:
                break;
            case 1:
                if(!entity.getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(roadAttributeA))
                    entity.getAttribute(Attributes.MOVEMENT_SPEED).applyPersistentModifier(roadAttributeA);
                break;
            case 2:
                if(!entity.getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(roadAttributeB))
                    entity.getAttribute(Attributes.MOVEMENT_SPEED).applyPersistentModifier(roadAttributeB);
                break;
            case 3:
                if(!entity.getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(roadAttributeC))
                    entity.getAttribute(Attributes.MOVEMENT_SPEED).applyPersistentModifier(roadAttributeC);
                break;
            case 4:
                if(!entity.getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(roadAttributeD))
                    entity.getAttribute(Attributes.MOVEMENT_SPEED).applyPersistentModifier(roadAttributeD);
                break;
            case 5:
                if(!entity.getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(roadAttributeE))
                    entity.getAttribute(Attributes.MOVEMENT_SPEED).applyPersistentModifier(roadAttributeE);
                break;
            default:
                BlockRunner.LOGGER.error("Invalid attribute selected!");
                break;
        }

        if(BlockRunner.debug)
            BlockRunner.LOGGER.debug("New speed: " + entity.getAttribute(Attributes.MOVEMENT_SPEED).getValue());
    }
}