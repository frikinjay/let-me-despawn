package com.frikinjay.letmedespawn.core;

import com.frikinjay.letmedespawn.LetMeDespawn;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

import javax.annotation.Nullable;
import java.util.Map;

/* NOTE:
 * This is heavily based on https://github.com/Fuzss/aquaacrobatics
 * Thanks to Fuzss for making their mod Public Domain
 */
@IFMLLoadingPlugin.MCVersion("1.12.2")
@IFMLLoadingPlugin.Name("Let Me Despawn")
public class CoreMod implements IFMLLoadingPlugin
{
    public static boolean foundMixinFramework;

    @Override
    public String[]
    getASMTransformerClass()
    {
        return new String[0];
    }

    @Override
    public String
    getModContainerClass()
    {
        return null;
    }

    @Nullable
    @Override
    public String
    getSetupClass()
    {
        Class<?> mixinClass = null;

        try
        {
            mixinClass = Class.forName("org.spongepowered.asm.launch.MixinTweaker");
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }

        String result = null;

        if (mixinClass != null)
        {
            foundMixinFramework = true;

            result = CoreModMixinInterface.class.getName();

            LetMeDespawn.LOGGER.info("Found valid Mixin framework! Proceeding to load.");
        }
        else
        {
            LetMeDespawn.LOGGER.info("No valid Mixin framework found! Loading not possible.");
        }

        return result;
    }

    @Override
    public void
    injectData(Map<String, Object> data)
    {
    }

    @Override
    public String
    getAccessTransformerClass()
    {
        return null;
    }
}
