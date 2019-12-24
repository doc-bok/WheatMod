package com.bokmcdok.wheat.sound;

import com.bokmcdok.wheat.WheatMod;
import com.bokmcdok.wheat.data.ModSoundManager;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(modid = WheatMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(WheatMod.MOD_ID)
public class ModSoundUtils {

    /**
     * Register the sounds used by the mod
     * @param event The event containing the sound event registry
     */
    @SubscribeEvent
    public static void registerSounds(RegistryEvent.Register<SoundEvent> event) {
        ModSoundManager soundManager = new ModSoundManager();
        soundManager.loadAssetEntries("sounds", ".ogg");
        event.getRegistry().registerAll(soundManager.getAllEntries().toArray(new SoundEvent[0]));
    }
}
