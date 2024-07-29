package xyz.devvydont.smprpg;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.devvydont.smprpg.config.ConfigManager;
import xyz.devvydont.smprpg.services.*;
import xyz.devvydont.smprpg.listeners.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO:
 * - Implement POWER enchantment for bows in listeners.DamageOverrideListener
 * - Fix issue with bow damage stacking for normal melee damage/dual wielding bows
 */

public final class SMPRPG extends JavaPlugin implements Listener {

    public static SMPRPG INSTANCE;

    public static SMPRPG getInstance() {
        return INSTANCE;
    }

    EconomyService economyService;
    ChatService chatService;
    ItemService itemService;
    EnchantmentService enchantmentService;
    EntityService entityService;
    SkillService skillService;
    DropsService dropsService;
    UnstableListenersService unstableListenersService;

    List<BaseService> services;

    public EconomyService getEconomyService() {
        return economyService;
    }

    public ChatService getChatService() {
        return chatService;
    }

    public ItemService getItemService() {
        return itemService;
    }

    public EnchantmentService getEnchantmentService() {
        return enchantmentService;
    }

    public EntityService getEntityService() {
        return entityService;
    }

    public SkillService getSkillService() {
        return skillService;
    }

    public DropsService getDropsService() {
        return dropsService;
    }

    public UnstableListenersService getUnstableListenersService() {
        return unstableListenersService;
    }

    public void checkServerSettings() {
        YamlConfiguration spigotConfig = getServer().spigot().getSpigotConfig();
        spigotConfig.set("settings.attribute.maxHealth.max", 999999999.0);
        spigotConfig.set("settings.attribute.attackDamage.max", 999999999.0);

        try {
            spigotConfig.save(getServer().getWorldContainer().getAbsolutePath() + "/spigot.yml");
        } catch (IOException e) {
            getLogger().severe("Failed to update spigot configuration to support high attack/health values.");
            e.printStackTrace();
        }
    }

    @Override
    public void onEnable() {

        INSTANCE = this;

        ConfigManager.init();  // Enable config.yml defaults
        checkServerSettings();  // Make sure HP and DMG values are good

        services = new ArrayList<>();

        economyService = new EconomyService(this);
        registerService(economyService);

        chatService = new ChatService(this);
        registerService(chatService);

        itemService = new ItemService(this);
        registerService(itemService);

        enchantmentService = new EnchantmentService();
        registerService(enchantmentService);

        entityService = new EntityService(this);
        registerService(entityService);

        skillService = new SkillService(this);
        registerService(skillService);

        dropsService = new DropsService(this);
        registerService(dropsService);

        unstableListenersService =  new UnstableListenersService(this);
        registerService(unstableListenersService);

        getServer().getPluginManager().registerEvents(this, this);

        new DamageOverrideListener(this);
        new EnvironmentalDamageListener(this);
        new AbsorptionDamageFix(this);
        new HealthRegenerationListener(this);
        new HealthScaleListener(this);
        new AnvilEnchantmentCombinationFixListener(this);

        new StructureEntitySpawnListener(this);
    }

    public void registerService(BaseService service) {
        boolean success = service.setup();

        // Did this service fail to start and the plugin requires it?
        if (!success && service.required())
            getServer().getPluginManager().disablePlugin(this);

        services.add(service);
    }

    @Override
    public void onDisable() {

        for (BaseService service : services)
            service.cleanup();

    }

}
