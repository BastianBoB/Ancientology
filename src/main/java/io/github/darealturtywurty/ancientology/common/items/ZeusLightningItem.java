package io.github.darealturtywurty.ancientology.common.items;

import io.github.darealturtywurty.ancientology.core.init.ParticleInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.Random;

public class ZeusLightningItem extends Item {

    public static final int RANGE = 50;

    public ZeusLightningItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity, int pTimeCharged) {
        super.releaseUsing(pStack, pLevel, pLivingEntity, pTimeCharged);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity) {
        if(pLivingEntity instanceof Player player) {
            BlockPos blockPos = targetBlockPos(player, RANGE);
            if(blockPos != null) {
                LightningBolt bolt = new LightningBolt(EntityType.LIGHTNING_BOLT, pLevel);
                bolt.setPos(new Vec3(blockPos.getX(), blockPos.getY(), blockPos.getZ()));
                pLevel.addFreshEntity(bolt);
            }
        }
        return super.finishUsingItem(pStack, pLevel, pLivingEntity);
    }

    @Override
    public void onUsingTick(ItemStack stack, LivingEntity livingEntity, int count) {
        super.onUsingTick(stack, livingEntity, count);
        if (livingEntity instanceof Player player) {
            BlockPos blockPos = targetBlockPos(player, RANGE);
            if (blockPos == null || !player.level.isClientSide) return;

            for (int i = 0; i < count; i++) {
                player.level.addParticle(ParticleInit.ASH_VELOCITY_PARTICLE.get(), blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5, 0, 0.075D, 0);
            }
            player.level.addParticle(ParticleTypes.LAVA, blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5, 0, 0, 0);

        }
    }

    @Nullable
    BlockPos targetBlockPos(Player player, int range) {
        Vec3 playerEye = player.getEyePosition();
        BlockHitResult blockHitResult = player.level.clip(new ClipContext(playerEye, playerEye.add(player.getLookAngle().multiply(range, range, range)), ClipContext.Block.OUTLINE, ClipContext.Fluid.ANY, null));
        if (blockHitResult.getType() == HitResult.Type.MISS) return null;

        return blockHitResult.getBlockPos().relative(blockHitResult.getDirection());
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand pHand) {
        player.startUsingItem(pHand);
        return InteractionResultHolder.consume(player.getItemInHand(pHand));
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.SPYGLASS;
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 60;
    }
}
