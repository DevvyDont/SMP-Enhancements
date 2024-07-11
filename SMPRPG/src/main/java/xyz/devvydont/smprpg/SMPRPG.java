package xyz.devvydont.smprpg;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.set.RegistryKeySet;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minecraft.core.component.DataComponentType;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.inventory.components.CraftToolComponent;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntitySnapshot;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.CraftingRecipe;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.FoodComponent;
import org.bukkit.inventory.meta.components.ToolComponent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import xyz.devvydont.smprpg.commands.economy.CommandBalance;
import xyz.devvydont.smprpg.commands.economy.CommandBalanceTop;
import xyz.devvydont.smprpg.commands.economy.CommandDeposit;
import xyz.devvydont.smprpg.commands.economy.CommandWithdrawal;
import xyz.devvydont.smprpg.commands.items.CommandGiveItem;
import xyz.devvydont.smprpg.commands.player.CommandStatistics;
import xyz.devvydont.smprpg.enchantments.EnchantmentService;
import xyz.devvydont.smprpg.listeners.*;
import xyz.devvydont.smprpg.services.EntityService;
import xyz.devvydont.smprpg.services.BaseService;
import xyz.devvydont.smprpg.services.ChatService;
import xyz.devvydont.smprpg.services.EconomyService;
import xyz.devvydont.smprpg.services.ItemService;

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

        checkServerSettings();

        INSTANCE = this;

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

        getServer().getPluginManager().registerEvents(this, this);

        new DamageOverrideListener(this);
        new EnvironmentalDamageListener(this);
        new AbsorptionDamageFix(this);
        new HealthRegenerationListener(this);

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

    // DEBUG EVENTS
    @EventHandler
    public void onJump(PlayerJumpEvent event) {

//        ItemStack pick = new ItemStack(Material.DIAMOND_SWORD);
//        ItemMeta meta = pick.getItemMeta();
//        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(new NamespacedKey("test", "test2"), 5, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.HAND));
//        pick.setItemMeta(meta);
//        event.getPlayer().getInventory().addItem(pick);
//
//        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
//        if (!item.hasItemMeta())
//            return;
    }

}
