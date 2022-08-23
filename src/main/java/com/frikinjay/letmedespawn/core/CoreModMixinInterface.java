package com.frikinjay.letmedespawn.core;

import com.frikinjay.letmedespawn.LetMeDespawn;
import net.minecraftforge.fml.relauncher.IFMLCallHook;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;

import java.util.Map;

/* NOTE:
 * This is heavily based on https://github.com/Fuzss/aquaacrobatics
 * Thanks to Fuzss for making their mod Public Domain
 */
public class CoreModMixinInterface implements IFMLCallHook
{
    @Override
    public void
    injectData(Map<String, Object> map)
    {
    }

    @Override
    public Void
    call() throws Exception
    {
        MixinBootstrap.init();
        Mixins.addConfiguration("mixins." + LetMeDespawn.MOD_ID + ".json");
        MixinEnvironment.getDefaultEnvironment().setObfuscationContext("searge");

        return null;
    }
}
