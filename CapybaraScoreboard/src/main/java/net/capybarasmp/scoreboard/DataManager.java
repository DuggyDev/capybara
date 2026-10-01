package net.capybarasmp.scoreboard;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class DataManager {

    private final CapybaraScoreboard plugin;
    private final File file;
    private final Map<UUID, PlayerData> cache = new HashMap<>();

    public DataManager(CapybaraScoreboard plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "data.yml");
    }

    public void load() {
        cache.clear();
        if (!file.exists()) return;
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
        ConfigurationSection players = yaml.getConfigurationSection("players");
        if (players == null) return;
        for (String key : players.getKeys(false)) {
            try {
                UUID id = UUID.fromString(key);
                ConfigurationSection s = players.getConfigurationSection(key);
                if (s == null) continue;
                PlayerData d = new PlayerData(s.getInt("lives", plugin.getConfig().getInt("start-lives", 3)));
                d.kills = s.getInt("kills", 0);
                d.deaths = s.getInt("deaths", 0);
                d.playtimeSeconds = s.getLong("playtime", 0L);
                cache.put(id, d);
            } catch (IllegalArgumentException ignored) {
                // skip invalid UUID keys
            }
        }
    }

    public PlayerData get(UUID id) {
        return cache.computeIfAbsent(id, k -> new PlayerData(plugin.getConfig().getInt("start-lives", 3)));
    }

    public void save() {
        YamlConfiguration yaml = new YamlConfiguration();
        for (Map.Entry<UUID, PlayerData> e : cache.entrySet()) {
            String base = "players." + e.getKey();
            PlayerData d = e.getValue();
            yaml.set(base + ".kills", d.kills);
            yaml.set(base + ".deaths", d.deaths);
            yaml.set(base + ".playtime", d.playtimeSeconds);
            yaml.set(base + ".lives", d.lives);
        }
        try {
            plugin.getDataFolder().mkdirs();
            yaml.save(file);
        } catch (IOException ex) {
            plugin.getLogger().severe("Could not save data.yml: " + ex.getMessage());
        }
    }
}
