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
import tv.mapper.blockrunner.BlockRunner;
import tv.mapper.blockrunner.config.RunnerConfig;

@Mod.EventBusSubscriber
public class RunnerEvent
{
    private static AttributeModifier roadAttributeA = new AttributeModifier(UUID.fromString(
        "aed652ce-7d33-480e-89ca-dacf858d7596"), "Road Speed Modifier A", RunnerConfig.CommonConfig.CONFIG_A_SPEED.get(), AttributeModifier.Operation.MULTIPLY_TOTAL);
    private static AttributeModifier roadAttributeB = new AttributeModifier(UUID.fromString(
        "1df40977-2b88-4f19-97c8-bcb2fe280054"), "Road Speed Modifier B", RunnerConfig.CommonConfig.CONFIG_B_SPEED.get(), AttributeModifier.Operation.MULTIPLY_TOTAL);
    private static AttributeModifier roadAttributeC = new AttributeModifier(UUID.fromString(
        "181b23a1-4974-4832-bd10-41f38e8bbe10"), "Road Speed Modifier C", RunnerConfig.CommonConfig.CONFIG_C_SPEED.get(), AttributeModifier.Operation.MULTIPLY_TOTAL);
    private static AttributeModifier roadAttributeD = new AttributeModifier(UUID.fromString(
        "bfab8ac9-05ee-4ffe-89ff-218742d32cc2"), "Road Speed Modifier D", RunnerConfig.CommonConfig.CONFIG_D_SPEED.get(), AttributeModifier.Operation.MULTIPLY_TOTAL);
    private static AttributeModifier roadAttributeE = new AttributeModifier(UUID.fromString(
        "29520a66-3e58-423a-ab0a-cad04e9f5e41"), "Road Speed Modifier E", RunnerConfig.CommonConfig.CONFIG_E_SPEED.get(), AttributeModifier.Operation.MULTIPLY_TOTAL);

    private static int attribute = 0;
    private static int previousAttribute = 0;

    @SubscribeEvent
    public static void detectRoad(LivingEvent.LivingUpdateEvent event)
    {
        LivingEntity player = (LivingEntity)event.getEntity();

        if(player instanceof PlayerEntity && !player.getEntityWorld().isRemote)
        {
            if(player.isOnGround())
            {
                Block block = player.getEntityWorld().getBlockState(new BlockPos(player.getPosX(), player.getBoundingBox().minY - (double)0.05F, player.getPosZ())).getBlock();
                String playerBlock = block.getRegistryName().toString();

                previousAttribute = attribute;
                attribute = 0;

                if(RunnerConfig.CommonConfig.CONFIG_A_BLOCKS.get().contains(playerBlock) && RunnerConfig.CommonConfig.CONFIG_A_ENABLE.get())
                    attribute = 1;
                else if(RunnerConfig.CommonConfig.CONFIG_B_BLOCKS.get().contains(playerBlock) && RunnerConfig.CommonConfig.CONFIG_B_ENABLE.get())
                    attribute = 2;
                else if(RunnerConfig.CommonConfig.CONFIG_C_BLOCKS.get().contains(playerBlock) && RunnerConfig.CommonConfig.CONFIG_C_ENABLE.get())
                    attribute = 3;
                else if(RunnerConfig.CommonConfig.CONFIG_D_BLOCKS.get().contains(playerBlock) && RunnerConfig.CommonConfig.CONFIG_D_ENABLE.get())
                    attribute = 4;
                else if(RunnerConfig.CommonConfig.CONFIG_E_BLOCKS.get().contains(playerBlock) && RunnerConfig.CommonConfig.CONFIG_E_ENABLE.get())
                    attribute = 5;

                if(attribute >= 0 && attribute <= 5 && attribute != previousAttribute)
                    applyAttribute(previousAttribute, attribute, (PlayerEntity)player);
            }
        }
    }

    private static void resetAttribute(int previousAttribute, PlayerEntity entity)
    {
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
    }
}