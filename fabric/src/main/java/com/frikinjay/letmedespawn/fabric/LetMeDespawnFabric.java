package com.frikinjay.letmedespawn.fabric;

import com.frikinjay.letmedespawn.LetMeDespawn;
import net.fabricmc.api.ModInitializer;

public final class LetMeDespawnFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        // Run our common setup.
        LetMeDespawn.init();
    }
}
