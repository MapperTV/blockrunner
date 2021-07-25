package tv.mapper.blockrunner.event;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import tv.mapper.blockrunner.BlockRunner;
import tv.mapper.blockrunner.config.ConfigChecker;
import tv.mapper.blockrunner.config.RunnerConfig;

@Mod.EventBusSubscriber(modid = BlockRunner.MODID)
public class RunnerEvent
{
    private static AttributeModifier blockAttributeA;
    private static AttributeModifier blockAttributeB;
    private static AttributeModifier blockAttributeC;
    private static AttributeModifier BlockAttributeD;
    private static AttributeModifier blockAttributeE;

    private static int attribute = 0;
    private static int previousAttribute = 0;
    private static boolean firstLaunch = true;

    public static void init()
    {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(RunnerEvent::configChange);
    }

    static void configChange(final ModConfigEvent.Reloading event)
    {
        ModConfig config = event.getConfig();

        List<Double> speeds = new ArrayList<Double>();

        speeds.add(config.getConfigData().get("config_a.configASpeed") == null ? 0.0d : (double)config.getConfigData().get(
            "config_a.configASpeed") > 1.0d ? 1.0d : (double)config.getConfigData().get("config_a.configASpeed") < -0.1d ? -0.1d : config.getConfigData().get("config_a.configASpeed"));
        speeds.add(config.getConfigData().get("config_b.configBSpeed") == null ? 0.0d : (double)config.getConfigData().get(
            "config_b.configBSpeed") > 1.0d ? 1.0d : (double)config.getConfigData().get("config_b.configBSpeed") < -0.1d ? -0.1d : config.getConfigData().get("config_b.configBSpeed"));
        speeds.add(config.getConfigData().get("config_c.configCSpeed") == null ? 0.0d : (double)config.getConfigData().get(
            "config_c.configCSpeed") > 1.0d ? 1.0d : (double)config.getConfigData().get("config_c.configCSpeed") < -0.1d ? -0.1d : config.getConfigData().get("config_c.configCSpeed"));
        speeds.add(config.getConfigData().get("config_d.configDSpeed") == null ? 0.0d : (double)config.getConfigData().get(
            "config_d.configDSpeed") > 1.0d ? 1.0d : (double)config.getConfigData().get("config_d.configDSpeed") < -0.1d ? -0.1d : config.getConfigData().get("config_d.configDSpeed"));
        speeds.add(config.getConfigData().get("config_e.configESpeed") == null ? 0.0d : (double)config.getConfigData().get(
            "config_e.configESpeed") > 1.0d ? 1.0d : (double)config.getConfigData().get("config_e.configESpeed") < -0.1d ? -0.1d : config.getConfigData().get("config_e.configESpeed"));

        if(BlockRunner.debug)
            BlockRunner.LOGGER.debug("New speed values: " + speeds.get(0) + ", " + speeds.get(1) + ", " + speeds.get(2) + ", " + speeds.get(3) + ", " + speeds.get(4));

        setAttributes(speeds.get(0), speeds.get(1), speeds.get(2), speeds.get(3), speeds.get(4));

        BlockRunner.LOGGER.info("Updated config changes!");
        ConfigChecker.check();
    }

    private static void setAttributes(double speedA, double speedB, double speedC, double speedD, double speedE)
    {
        blockAttributeA = new AttributeModifier(UUID.fromString("aed652ce-7d33-480e-89ca-dacf858d7596"), "Block Speed Modifier A", speedA, AttributeModifier.Operation.ADDITION);
        blockAttributeB = new AttributeModifier(UUID.fromString("1df40977-2b88-4f19-97c8-bcb2fe280054"), "Block Speed Modifier B", speedB, AttributeModifier.Operation.ADDITION);
        blockAttributeC = new AttributeModifier(UUID.fromString("181b23a1-4974-4832-bd10-41f38e8bbe10"), "Block Speed Modifier C", speedC, AttributeModifier.Operation.ADDITION);
        BlockAttributeD = new AttributeModifier(UUID.fromString("bfab8ac9-05ee-4ffe-89ff-218742d32cc2"), "Block Speed Modifier D", speedD, AttributeModifier.Operation.ADDITION);
        blockAttributeE = new AttributeModifier(UUID.fromString("29520a66-3e58-423a-ab0a-cad04e9f5e41"), "Block Speed Modifier E", speedE, AttributeModifier.Operation.ADDITION);
    }

    @SubscribeEvent
    public static void detectRoad(LivingEvent.LivingUpdateEvent event)
    {
        LivingEntity player = (LivingEntity)event.getEntity();

        if(firstLaunch)
        {
            setAttributes(RunnerConfig.ServerConfig.CONFIG_A_SPEED.get(), RunnerConfig.ServerConfig.CONFIG_B_SPEED.get(), RunnerConfig.ServerConfig.CONFIG_C_SPEED.get(),
                RunnerConfig.ServerConfig.CONFIG_D_SPEED.get(), RunnerConfig.ServerConfig.CONFIG_E_SPEED.get());
            firstLaunch = false;
        }

        if(player instanceof Player && !player.getCommandSenderWorld().isClientSide)
        {
            if(player.isOnGround())
            {
                Block block = player.getCommandSenderWorld().getBlockState(new BlockPos(player.getX(), player.getBoundingBox().minY - (double)0.05F, player.getZ())).getBlock();
                String playerBlock = block.getRegistryName().toString();

                previousAttribute = attribute;
                attribute = 0;

                List<String> blockTags = new ArrayList<String>();

                for(ResourceLocation s : block.getTags())
                    blockTags.add("#" + s);

                for(String entry : RunnerConfig.ServerConfig.CONFIG_A_BLOCKS.get())
                    if(entry.startsWith("#") && blockTags.contains(entry))
                    {
                        attribute = 1;
                        break;
                    }

                if(attribute < 1)
                {
                    for(String entry : RunnerConfig.ServerConfig.CONFIG_B_BLOCKS.get())
                        if(entry.startsWith("#") && blockTags.contains(entry))
                        {
                            attribute = 2;
                            break;
                        }

                    if(attribute < 2)
                    {
                        for(String entry : RunnerConfig.ServerConfig.CONFIG_C_BLOCKS.get())
                            if(entry.startsWith("#") && blockTags.contains(entry))
                            {
                                attribute = 3;
                                break;
                            }

                        if(attribute < 3)
                        {
                            for(String entry : RunnerConfig.ServerConfig.CONFIG_D_BLOCKS.get())
                                if(entry.startsWith("#") && blockTags.contains(entry))
                                {
                                    attribute = 4;
                                    break;
                                }

                            if(attribute < 4)
                                for(String entry : RunnerConfig.ServerConfig.CONFIG_E_BLOCKS.get())
                                    if(entry.startsWith("#") && blockTags.contains(entry))
                                    {
                                        attribute = 5;
                                        break;
                                    }
                        }
                    }
                }

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
                        BlockRunner.LOGGER.debug("Walking on block " + block + ", applying attribute n°" + attribute);

                    applyAttribute(previousAttribute, attribute, (Player)player);
                }
            }
        }
    }

    private static void resetAttribute(int previousAttribute, Player entity)
    {
        if(BlockRunner.debug)
            BlockRunner.LOGGER.debug("Previous: " + previousAttribute);

        switch(previousAttribute)
        {
            case 1:
                entity.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(blockAttributeA);
                break;
            case 2:
                entity.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(blockAttributeB);
                break;
            case 3:
                entity.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(blockAttributeC);
                break;
            case 4:
                entity.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(BlockAttributeD);
                break;
            case 5:
                entity.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(blockAttributeE);
                break;
            case 0:
            default:
                break;
        }
    }

    private static void applyAttribute(int previousAttribute, int attribute, Player entity)
    {
        resetAttribute(previousAttribute, entity);

        switch(attribute)
        {
            case 0:
                break;
            case 1:
                if(!entity.getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(blockAttributeA))
                    entity.getAttribute(Attributes.MOVEMENT_SPEED).addPermanentModifier(blockAttributeA);
                break;
            case 2:
                if(!entity.getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(blockAttributeB))
                    entity.getAttribute(Attributes.MOVEMENT_SPEED).addPermanentModifier(blockAttributeB);
                break;
            case 3:
                if(!entity.getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(blockAttributeC))
                    entity.getAttribute(Attributes.MOVEMENT_SPEED).addPermanentModifier(blockAttributeC);
                break;
            case 4:
                if(!entity.getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(BlockAttributeD))
                    entity.getAttribute(Attributes.MOVEMENT_SPEED).addPermanentModifier(BlockAttributeD);
                break;
            case 5:
                if(!entity.getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(blockAttributeE))
                    entity.getAttribute(Attributes.MOVEMENT_SPEED).addPermanentModifier(blockAttributeE);
                break;
            default:
                BlockRunner.LOGGER.error("Invalid attribute selected!");
                break;
        }

        if(BlockRunner.debug)
            BlockRunner.LOGGER.debug("New speed: " + entity.getAttribute(Attributes.MOVEMENT_SPEED).getValue());
    }
}