package io.github.darealturtywurty.ancientology;

import io.github.darealturtywurty.ancientology.core.init.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import software.bernie.example.GeckoLibMod;
import software.bernie.geckolib3.GeckoLib;

@Mod(Ancientology.MODID)
public class Ancientology {
    public static final String MODID = "ancientology";

    public static final CreativeModeTab TAB = new CreativeModeTab(MODID) {
        @Override
        public ItemStack makeIcon() {
            return ItemInit.FLINT_AXE.get().getDefaultInstance();
        }
    };

    public Ancientology() {
        if (FMLEnvironment.dist.isClient()) {
            GeckoLibMod.DISABLE_IN_DEV = true;
            GeckoLib.initialize();
        }

        final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        BlockInit.BLOCKS.register(bus);
        BlockEntityInit.BLOCK_ENTITIES.register(bus);
        ItemInit.ITEMS.register(bus);
        RecipeInit.RECIPES.register(bus);
        EntityInit.ENTITIES.register(bus);
        MobEffectInit.MOB_EFFECTS.register(bus);
        ParticleInit.PARTICLE_TYPES.register(bus);
    }

    public static ResourceLocation rl(final String name) {
        return new ResourceLocation(MODID, name);
    }
}
