package xyz.devvydont.smprpg;

import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.devvydont.smprpg.config.ConfigManager;
import xyz.devvydont.smprpg.effects.services.SpecialEffectService;
import xyz.devvydont.smprpg.listeners.*;
import xyz.devvydont.smprpg.loot.LootListener;
import xyz.devvydont.smprpg.services.*;
import xyz.devvydont.smprpg.util.animations.AnimationService;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

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

    AttributeService attributeService;
    EntityDamageCalculatorService entityDamageCalculatorService;
    EconomyService economyService;
    ChatService chatService;
    ItemService itemService;
    EnchantmentService enchantmentService;
    EntityService entityService;
    DifficultyService difficultyService;
    SpecialEffectService specialEffectsService;
    SkillService skillService;
    DropsService dropsService;
    ActionBarService actionBarService;
    UnstableListenersService unstableListenersService;
    AnimationService animationService;

    List<IService> services;

    public static void broadcastToOperators(TextComponent alert) {
        Bukkit.getLogger().warning(alert.content());
        for (var player : Bukkit.getOnlinePlayers())
            if (player.isOp() || player.permissionValue("smprpg.receiveopmessages").toBooleanOrElse(false))
                player.sendMessage(ComponentUtils.alert(ComponentUtils.create("OP MSG", NamedTextColor.DARK_RED), alert));
    }

    public static void broadcastToOperatorsCausedBy(Player player, TextComponent alert) {
        Bukkit.getLogger().warning(alert.content());
        for (var op : Bukkit.getOnlinePlayers())
            if (op.isOp() || op.permissionValue("smprpg.receiveopmessages").toBooleanOrElse(false)) {
                op.sendMessage(ComponentUtils.alert(ComponentUtils.create("OP MSG", NamedTextColor.DARK_RED), ComponentUtils.create("(Caused by " + player.getName() +  ") ", NamedTextColor.RED).append(alert)));
            }
    }

    public AttributeService getAttributeService() {
        return attributeService;
    }

    public EntityDamageCalculatorService getEntityDamageCalculatorService() {
        return entityDamageCalculatorService;
    }

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

    public DifficultyService getDifficultyService() {
        return difficultyService;
    }

    public SpecialEffectService getSpecialEffectsService() {
        return specialEffectsService;
    }

    public SkillService getSkillService() {
        return skillService;
    }

    public DropsService getDropsService() {
        return dropsService;
    }

    public ActionBarService getActionBarService() {
        return actionBarService;
    }

    public UnstableListenersService getUnstableListenersService() {
        return unstableListenersService;
    }

    public AnimationService getAnimationService() {
        return animationService;
    }

    @Override
    public void onEnable() {

        INSTANCE = this;

        ConfigManager.init();  // Enable config.yml defaults

        services = new ArrayList<>();

        enchantmentService = new EnchantmentService();
        registerService(enchantmentService);

        economyService = new EconomyService(this);
        registerService(economyService);

        chatService = new ChatService(this);
        registerService(chatService);

        attributeService = new AttributeService();
        registerService(attributeService);

        entityDamageCalculatorService = new EntityDamageCalculatorService(this);
        registerService(entityDamageCalculatorService);

        itemService = new ItemService(this);
        registerService(itemService);

        entityService = new EntityService(this);
        registerService(entityService);

        difficultyService = new DifficultyService();
        registerService(difficultyService);

        specialEffectsService = new SpecialEffectService(this);
        registerService(specialEffectsService);

        skillService = new SkillService(this);
        registerService(skillService);

        dropsService = new DropsService(this);
        registerService(dropsService);

        actionBarService = new ActionBarService(this);
        registerService(actionBarService);

        unstableListenersService =  new UnstableListenersService(this);
        registerService(unstableListenersService);

        animationService =  new AnimationService(this);

        getServer().getPluginManager().registerEvents(this, this);

        new EnvironmentalDamageListener(this);
        new AbsorptionDamageFix(this);
        new HealthRegenerationListener(this);
        new HealthScaleListener(this);
        new AnvilEnchantmentCombinationFixListener(this);
        new DimensionPortalLockingListener(this);
        new PvPListener();

        new StructureEntitySpawnListener(this);
        new LootListener(this);
    }

    public void registerService(IService service) {
        boolean success = service.setup();

        // Did this service fail to start and the plugin requires it?
        if (!success && service.required())
            getServer().getPluginManager().disablePlugin(this);

        services.add(service);
    }

    @Override
    public void onDisable() {

        for (IService service : services)
            service.cleanup();

    }

}
