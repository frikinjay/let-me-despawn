package com.frikinjay.almanac;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;


public class Almanac {

    public static boolean pickedItems = true;
    public static final String MOD_ID = "almanac";

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private static final List<Consumer<CommandDispatcher<CommandSourceStack>>> COMMAND_REGISTRATIONS = new ArrayList<>();
    private static final Map<String, ConfigEntry<?>> CONFIG_ENTRIES = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public static <T> void addConfigChangeListener(File configFile, Consumer<T> listener) {
        ConfigEntry<T> entry = (ConfigEntry<T>) CONFIG_ENTRIES.get(configFile.getName());
        if (entry != null) {
            entry.addListener(listener);
        }
    }

    public static void addCommandRegistration(Consumer<CommandDispatcher<CommandSourceStack>> registration) {
        COMMAND_REGISTRATIONS.add(registration);
    }

    public static <T> T loadConfig(File configFile, Class<T> configClass) {
        T config = loadConfigFromFile(configFile, configClass);
        if (config != null) {
            CONFIG_ENTRIES.put(configFile.getName(), new ConfigEntry<>(config, configClass, configFile));
        }
        return config;
    }

    public static <T> T loadConfigFromFile(File configFile, Class<T> configClass) {
        if (configFile.exists()) {
            try (FileReader reader = new FileReader(configFile)) {
                return GSON.fromJson(reader, configClass);
            } catch (IOException e) {
                LOGGER.error("Failed to load config", e);
            }
        }
        return createDefaultConfig(configClass);
    }

    private static <T> T createDefaultConfig(Class<T> configClass) {
        try {
            return configClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            LOGGER.error("Failed to create default config", e);
            return null;
        }
    }

    public static void saveConfig(File configFile, Object config) {
        try (FileWriter writer = new FileWriter(configFile)) {
            GSON.toJson(config, writer);
        } catch (IOException e) {
            LOGGER.error("Failed to save config", e);
        }
    }

    public static boolean matchesStackedName(String customName, net.minecraft.world.entity.Entity entity) {
        return java.util.regex.Pattern.compile(java.util.regex.Pattern.quote(getLocalizedEntityName(entity.getType()).getString()) + " x\\d+").matcher(customName).find();
    }

    public static net.minecraft.network.chat.Component getLocalizedEntityName(net.minecraft.world.entity.EntityType<?> entityType) {
        String translationKey = entityType.getDescriptionId();
        return net.minecraft.network.chat.Component.translatable(translationKey);
    }

    private static class ConfigEntry<T> {
        private T config;
        private final Class<T> configClass;
        private final File configFile;
        private final List<Consumer<T>> listeners = new ArrayList<>();

        public ConfigEntry(T config, Class<T> configClass, File configFile) {
            this.config = config;
            this.configClass = configClass;
            this.configFile = configFile;
        }

        public void addListener(Consumer<T> listener) {
            listeners.add(listener);
        }

        public Class<T> getConfigClass() {
            return configClass;
        }

        public File getConfigFile() {
            return configFile;
        }

    }
}
