package tv.mapper.blockrunner.event;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.minecraft.block.Block;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
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
        // double speedA, speedB, speedC, speedD, speedE;

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

        // speeds.add(config.getConfigData().get("config_b.configBSpeed") != null ? config.getConfigData().get("config_b.configBSpeed") : 0.0d);
        // speeds.add(config.getConfigData().get("config_c.configCSpeed") != null ? config.getConfigData().get("config_c.configCSpeed") : 0.0d);
        // speeds.add(config.getConfigData().get("config_d.configDSpeed") != null ? config.getConfigData().get("config_d.configDSpeed") : 0.0d);
        // speeds.add(config.getConfigData().get("config_e.configESpeed") != null ? config.getConfigData().get("config_e.configESpeed") : 0.0d);

        // Restrict value to 1.0 max
        // speedA = speedA > 1.0 ? 1.0 : speedA;
        // speedB = speedB > 1.0 ? 1.0 : speedB;
        // speedC = speedC > 1.0 ? 1.0 : speedC;
        // speedD = speedD > 1.0 ? 1.0 : speedD;
        // speedE = speedE > 1.0 ? 1.0 : speedE;
        // Restrict value to -0.1 min
        // speedA = speedA < -0.1 ? -0.1 : speedA;
        // speedB = speedB < -0.1 ? -0.1 : speedB;
        // speedC = speedC < -0.1 ? -0.1 : speedC;
        // speedD = speedD < -0.1 ? -0.1 : speedD;
        // speedE = speedE < -0.1 ? -0.1 : speedE;

        if(BlockRunner.debug)
            BlockRunner.LOGGER.debug("New speed values: " + speeds.get(0) + ", " + speeds.get(1) + ", " + speeds.get(2) + ", " + speeds.get(3) + ", " + speeds.get(4));

        setAttributes(speeds.get(0), speeds.get(1), speeds.get(2), speeds.get(3), speeds.get(4));

        BlockRunner.LOGGER.info("Updated config changes!");
        ConfigChecker.check();
    }

    private static void setAttributes(double speedA, double speedB, double speedC, double speedD, double speedE)
    {
        roadAttributeA = new AttributeModifier(UUID.fromString("aed652ce-7d33-480e-89ca-dacf858d7596"), "Road Speed Modifier A", speedA, AttributeModifier.Operation.ADDITION);
        roadAttributeB = new AttributeModifier(UUID.fromString("1df40977-2b88-4f19-97c8-bcb2fe280054"), "Road Speed Modifier B", speedB, AttributeModifier.Operation.ADDITION);
        roadAttributeC = new AttributeModifier(UUID.fromString("181b23a1-4974-4832-bd10-41f38e8bbe10"), "Road Speed Modifier C", speedC, AttributeModifier.Operation.ADDITION);
        roadAttributeD = new AttributeModifier(UUID.fromString("bfab8ac9-05ee-4ffe-89ff-218742d32cc2"), "Road Speed Modifier D", speedD, AttributeModifier.Operation.ADDITION);
        roadAttributeE = new AttributeModifier(UUID.fromString("29520a66-3e58-423a-ab0a-cad04e9f5e41"), "Road Speed Modifier E", speedE, AttributeModifier.Operation.ADDITION);
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

        if(player instanceof PlayerEntity && !player.getEntityWorld().isRemote)
        {
            if(player.isOnGround())
            {
                Block block = player.getEntityWorld().getBlockState(new BlockPos(player.getPosX(), player.getBoundingBox().minY - (double)0.05F, player.getPosZ())).getBlock();
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