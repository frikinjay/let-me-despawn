package com.frikinjay.letmedespawn.config;

import com.frikinjay.almanac.Almanac;
import com.frikinjay.letmedespawn.LetMeDespawn;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LetMeDespawnConfig {
    private Set<String> mobNames = new HashSet<>(List.of("corpse:corpse"));

    public Set<String> getMobNames() {
        return mobNames;
    }

    public void addMobName(String mobName) {
        mobNames.add(mobName);
        save();
    }

    public void removeMobName(String mobName) {
        mobNames.remove(mobName);
        save();
    }

    public static LetMeDespawnConfig load() {
        return Almanac.loadConfig(LetMeDespawn.CONFIG_FILE, LetMeDespawnConfig.class);
    }

    public void save() {
        Almanac.saveConfig(LetMeDespawn.CONFIG_FILE, this);
    }
}