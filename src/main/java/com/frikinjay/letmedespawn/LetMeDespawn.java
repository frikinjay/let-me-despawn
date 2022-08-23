package com.frikinjay.letmedespawn;

import com.frikinjay.letmedespawn.core.CoreMod;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = LetMeDespawn.MOD_ID, clientSideOnly = true)
public class LetMeDespawn
{
    public static final String MOD_ID   = "letmedespawn";
    public static final String MOD_NAME = "Let Me Despawn";
    public static final Logger LOGGER   = LogManager.getLogger(LetMeDespawn.MOD_ID);

    @Mod.EventHandler
    public void
    onPreInit(final FMLPreInitializationEvent event)
    {
        if (!CoreMod.foundMixinFramework)
        {
            MinecraftForge.EVENT_BUS.register(new MissingMixinFrameworkHandler());
        }
    }
}
