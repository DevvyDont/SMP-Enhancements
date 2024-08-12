package xyz.devvydont.smprpg.services;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.keys.EnchantmentKeys;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.minecraft.world.level.storage.loot.LootTable;
import org.bukkit.Registry;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentOffer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.world.LootGenerateEvent;
import org.bukkit.inventory.EnchantingInventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.loot.LootTables;
import org.bukkit.scheduler.BukkitScheduler;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.enchantments.CustomEnchantment;
import xyz.devvydont.smprpg.enchantments.calculator.EnchantmentCalculator;
import xyz.devvydont.smprpg.enchantments.definitions.*;
import xyz.devvydont.smprpg.enchantments.definitions.vanilla.overrides.*;
import xyz.devvydont.smprpg.enchantments.definitions.vanilla.UnknownEnchantment;
import xyz.devvydont.smprpg.enchantments.definitions.vanilla.unchanged.*;
import xyz.devvydont.smprpg.entity.LeveledPlayer;

import java.util.*;

public class EnchantmentService implements BaseService, Listener {

    // Vanilla overrides
    public final static CustomEnchantment AQUA_AFFINITY = new AquaAffinityEnchantment(EnchantmentKeys.AQUA_AFFINITY);
    public final static CustomEnchantment BANE_OF_ARTHROPODS = new BaneOfArthropodsEnchantment(EnchantmentKeys.BANE_OF_ARTHROPODS);
    public final static CustomEnchantment BLAST_PROTECTION = new BlastProtectionEnchantment(EnchantmentKeys.BLAST_PROTECTION);
    public final static CustomEnchantment BREACH = new BreachEnchantment(EnchantmentKeys.BREACH);
    public final static CustomEnchantment CHANNELING = new ChannelingEnchantment(EnchantmentKeys.CHANNELING);
    public final static CustomEnchantment BINDING_CURSE = new BindingCurseEnchantment(EnchantmentKeys.BINDING_CURSE);
    public final static CustomEnchantment VANISHING_CURSE = new VanishingCurseEnchantment(EnchantmentKeys.VANISHING_CURSE);
    public final static CustomEnchantment DENSITY = new DensityEnchantment(EnchantmentKeys.DENSITY);
    public final static CustomEnchantment DEPTH_STRIDER = new DepthStriderEnchantment(EnchantmentKeys.DEPTH_STRIDER);
    public final static CustomEnchantment EFFICIENCY = new EfficiencyEnchantment(EnchantmentKeys.EFFICIENCY);
    public final static CustomEnchantment FEATHER_FALLING = new FeatherFallingEnchantment(EnchantmentKeys.FEATHER_FALLING);
    public final static CustomEnchantment FIRE_ASPECT = new FireAspectEnchantment(EnchantmentKeys.FIRE_ASPECT);
    public final static CustomEnchantment FIRE_PROTECTION = new FireProtectionEnchantment(EnchantmentKeys.FIRE_PROTECTION);
    public final static CustomEnchantment FLAME = new FlameEnchantment(EnchantmentKeys.FLAME);
    public final static CustomEnchantment FORTUNE = new FortuneEnchantment(EnchantmentKeys.FORTUNE);
    public final static CustomEnchantment FROST_WALKER = new FrostWalkerEnchantment(EnchantmentKeys.FROST_WALKER);
    public final static CustomEnchantment IMPALING = new ImpalingEnchantment(EnchantmentKeys.IMPALING);
    public final static CustomEnchantment INFINITY = new InfinityEnchantment(EnchantmentKeys.INFINITY);
    public final static CustomEnchantment KNOCKBACK = new KnockbackEnchantment(EnchantmentKeys.KNOCKBACK);
    public final static CustomEnchantment LOOTING = new LootingEnchantment(EnchantmentKeys.LOOTING);
    public final static CustomEnchantment LOYALTY = new LoyaltyEnchantment(EnchantmentKeys.LOYALTY);
    public final static CustomEnchantment LUCK_OF_THE_SEA = new LuckOfTheSeaEnchantment(EnchantmentKeys.LUCK_OF_THE_SEA);
    public final static CustomEnchantment LURE = new LureEnchantment(EnchantmentKeys.LURE);
    public final static CustomEnchantment MENDING = new MendingEnchantment(EnchantmentKeys.MENDING);
    public final static CustomEnchantment MULTISHOT = new MultishotEnchantment(EnchantmentKeys.MULTISHOT);
    public final static CustomEnchantment PIERCING = new PiercingEnchantment(EnchantmentKeys.PIERCING);
    public final static CustomEnchantment POWER = new PowerEnchantment(EnchantmentKeys.POWER);
    public final static CustomEnchantment PROJECTILE_PROTECTION = new ProjectileProtectionEnchantment(EnchantmentKeys.PROJECTILE_PROTECTION);
    public final static CustomEnchantment PROTECTION = new ProtectionEnchantment(EnchantmentKeys.PROTECTION);
    public final static CustomEnchantment PUNCH = new PunchEnchantment(EnchantmentKeys.PUNCH);
    public final static CustomEnchantment QUICK_CHARGE = new QuickChargeEnchantment(EnchantmentKeys.QUICK_CHARGE);
    public final static CustomEnchantment RESPIRATION = new RespirationEnchantment(EnchantmentKeys.RESPIRATION);
    public final static CustomEnchantment RIPTIDE = new RiptideEnchantment(EnchantmentKeys.RIPTIDE);
    public final static CustomEnchantment SHARPNESS = new SharpnessEnchantment(EnchantmentKeys.SHARPNESS);
    public final static CustomEnchantment SILK_TOUCH = new SilkTouchEnchantment(EnchantmentKeys.SILK_TOUCH);
    public final static CustomEnchantment SMITE = new SmiteEnchantment(EnchantmentKeys.SMITE);
    public final static CustomEnchantment SOUL_SPEED = new SoulSpeedEnchantment(EnchantmentKeys.SOUL_SPEED);
    public final static CustomEnchantment SWEEPING_EDGE = new SweepingEdgeEnchantment(EnchantmentKeys.SWEEPING_EDGE);
    public final static CustomEnchantment SWIFT_SNEAK = new SwiftSneakEnchantment(EnchantmentKeys.SWIFT_SNEAK);
    public final static CustomEnchantment THORNS = new ThornsEnchantment(EnchantmentKeys.THORNS);
    public final static CustomEnchantment UNBREAKING = new UnbreakingEnchantment(EnchantmentKeys.UNBREAKING);
    public final static CustomEnchantment WIND_BURST = new WindBurstEnchantment(EnchantmentKeys.WIND_BURST);

    // Custom enchantments
    public final static CustomEnchantment KEEPING_BLESSING = new KeepingBlessing("keeping");
    public final static CustomEnchantment FORTUITY = new FortuityEnchantment("fortuity");
    public final static CustomEnchantment HEARTY = new HeartyEnchantment("hearty");
    public final static CustomEnchantment SPEEDSTER = new SpeedsterEnchantment("speedster");
    public final static CustomEnchantment LEECH = new LeechEnchantment("leech");
    public final static CustomEnchantment TRACING = new BossTracingEnchantment("tracing");
    public final static CustomEnchantment BLESSED = new BlessedEnchantment("blessed");
    public final static CustomEnchantment STABILIZED = new StabilizedEnchantment("stabilized");
    public final static CustomEnchantment PROFICIENT = new ProficientEnchantment("proficient");
    public final static CustomEnchantment CLIMBING = new ClimbingEnchantment("climbing");
    public final static CustomEnchantment SERRATED = new SerratedEnchantment("serrated");

    public final static CustomEnchantment[] CUSTOM_ENCHANTMENTS = {

            // Blessings
            KEEPING_BLESSING,

            // Curses
            BINDING_CURSE,
            VANISHING_CURSE,

            // Important enchantments (display first)
            SHARPNESS,
            POWER,
            SMITE,
            BANE_OF_ARTHROPODS,
            SERRATED,
            EFFICIENCY,

            BLESSED,
            STABILIZED,

            AQUA_AFFINITY,
            BLAST_PROTECTION,
            BREACH,
            CHANNELING,
            DENSITY,
            DEPTH_STRIDER,
            FEATHER_FALLING,
            FIRE_ASPECT,
            FIRE_PROTECTION,
            FLAME,
            FORTUNE,
            FROST_WALKER,
            IMPALING,
            INFINITY,
            KNOCKBACK,
            LOOTING,
            LOYALTY,
            LUCK_OF_THE_SEA,
            LURE,
            MULTISHOT,
            PIERCING,
            PROJECTILE_PROTECTION,
            PROTECTION,
            PUNCH,
            QUICK_CHARGE,
            RESPIRATION,
            RIPTIDE,
            SILK_TOUCH,
            SOUL_SPEED,
            SWEEPING_EDGE,
            SWIFT_SNEAK,
            THORNS,
            UNBREAKING,
            WIND_BURST,
            MENDING,

            CLIMBING,
            FORTUITY,
            HEARTY,
            LEECH,
            PROFICIENT,
            SPEEDSTER,
            TRACING,
    };

    public final Map<Enchantment, CustomEnchantment> enchantments = new HashMap<>();

    @Override
    public boolean setup() {

        SMPRPG.getInstance().getServer().getPluginManager().registerEvents(this, SMPRPG.getInstance());

        for (CustomEnchantment enchantment : CUSTOM_ENCHANTMENTS) {
            enchantments.put(getEnchantment(enchantment), enchantment);

            if (enchantment instanceof Listener)
                SMPRPG.getInstance().getServer().getPluginManager().registerEvents((Listener) enchantment, SMPRPG.getInstance());
        }

        // Debug print out the level unlocks of all the enchants
        List<CustomEnchantment> sortedByUnlock = new ArrayList<>(enchantments.values());
        sortedByUnlock.sort(Comparator.comparingInt(CustomEnchantment::getSkillRequirement));
        for (CustomEnchantment e : sortedByUnlock)
            SMPRPG.getInstance().getLogger().fine(PlainTextComponentSerializer.plainText().serialize(e.getDisplayName()) + ": " + e.getSkillRequirement());

        return true;
    }

    @Override
    public void cleanup() {

    }

    @Override
    public boolean required() {
        return true;
    }

    private Registry<Enchantment> getEnchantmentRegistry() {
        return RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT);
    }

    /**
     * Given an enchantment key, return the vanilla representation of the enchantment
     *
     * @param key
     * @return
     */
    public Enchantment getEnchantment(TypedKey<Enchantment> key) {
        return getEnchantmentRegistry().getOrThrow(key);
    }

    /**
     * Given a custom enchant wrapper, return the vanilla representation of the enchantment
     *
     * @param enchantment
     * @return
     */
    public Enchantment getEnchantment(CustomEnchantment enchantment) {
        return getEnchantment(TypedKey.create(RegistryKey.ENCHANTMENT, enchantment.getKey()));
    }

    /**
     * Given a vanilla enchantment, return the custom wrapped version of the enchant
     *
     * @param enchantment
     * @return
     */
    public CustomEnchantment getEnchantment(Enchantment enchantment) {
        if (!enchantments.containsKey(enchantment))
            return new UnknownEnchantment(TypedKey.create(RegistryKey.ENCHANTMENT, enchantment.getKey()));
        return enchantments.get(enchantment);
    }

    /**
     * Returns all registered custom enchantments
     *
     * @return
     */
    public Collection<CustomEnchantment> getCustomEnchantments() {
        return enchantments.values();
    }

    /**
     * Given an item, return a collection of custom enchantments stored on the item
     *
     * @param meta
     * @return
     */
    public Collection<CustomEnchantment> getCustomEnchantments(ItemMeta meta) {
        return getCustomEnchantments(meta.getEnchants());
    }

    /**
     * Returns a collection of enchantments sorted by the order that they are defined above
     *
     * @param enchants
     * @return
     */
    public Collection<CustomEnchantment> getCustomEnchantments(Map<Enchantment, Integer> enchants) {


        List<CustomEnchantment> enchantList = new ArrayList<>();

        // Loop through every enchant registered
        for (CustomEnchantment enchantment : CUSTOM_ENCHANTMENTS)
            // If the enchantment map on the item contains this enchantment, add it and its level to the list to return later
            if (enchants.containsKey(enchantment.getEnchantment()))
                enchantList.add(enchantment.build(enchants.get(enchantment.getEnchantment())));

        return enchantList;
    }

    public EnchantmentCalculator getCalculator(ItemStack item, int bookshelfBonus, int magicLevel, int seed) {
        return new EnchantmentCalculator(item, bookshelfBonus, magicLevel, seed);
    }

    // Caches calculator queries so that the EnchantItemEvent can use results from PrepareItemEnchantEvent
    public static Map<UUID, Map<EnchantmentCalculator.EnchantmentSlot, List<EnchantmentOffer>>> calculatorCache = new HashMap<>();

    /**
     * When opening an enchanting GUI, the lapis lazuli slot should always be filled.
     *
     * @param event
     */
    @EventHandler
    public void onOpenEnchantInterface(InventoryOpenEvent event) {

        if (!(event.getInventory() instanceof EnchantingInventory inventory))
            return;

        inventory.setSecondary(ItemType.LAPIS_LAZULI.createItemStack(64));
    }

    /**
     * When closing an enchanting GUI, get rid of the lapis lazuli so we don't duplicate it
     *
     * @param event
     */
    @EventHandler
    public void onCloseEnchantInterface(InventoryCloseEvent event) {

        if (!(event.getInventory() instanceof EnchantingInventory inventory))
            return;

        inventory.setSecondary(null);
    }

    /**
     * When clicking the lapis in the enchant GUI, don't allow any interactions with it
     *
     * @param event
     */
    @EventHandler
    public void onClickLapisEnchantInterface(InventoryClickEvent event) {

        if (event.getClickedInventory() == null || !event.getClickedInventory().getType().equals(InventoryType.ENCHANTING))
            return;

        if (event.getSlot() != 1)
            return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onEnchantPrepare(PrepareItemEnchantEvent event) {

        LeveledPlayer player = SMPRPG.getInstance().getEntityService().getPlayerInstance(event.getEnchanter());
        EnchantmentCalculator calculator = getCalculator(event.getItem(), event.getEnchantmentBonus(), player.getMagicSkill().getLevel(), player.getSeed());

        // Calculate enchantments to give and update costs and previews
        Map<EnchantmentCalculator.EnchantmentSlot, List<EnchantmentOffer>> offers = calculator.calculate();
        calculatorCache.put(event.getEnchanter().getUniqueId(), offers);

        for (Map.Entry<EnchantmentCalculator.EnchantmentSlot, List<EnchantmentOffer>> entry : offers.entrySet())
            event.getOffers()[entry.getKey().ordinal()] = !entry.getValue().isEmpty() ? entry.getValue().getFirst() : null;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onEnchant(EnchantItemEvent event) {

        Map<EnchantmentCalculator.EnchantmentSlot, List<EnchantmentOffer>> allOffers = calculatorCache.get(event.getEnchanter().getUniqueId());
        if (allOffers == null)
            return;

        List<EnchantmentOffer> clickedOffers = allOffers.get(EnchantmentCalculator.EnchantmentSlot.fromButton(event.whichButton()));
        event.getEnchantsToAdd().clear();
        for (EnchantmentOffer offer : clickedOffers)
            event.getEnchantsToAdd().put(offer.getEnchantment(), offer.getEnchantmentLevel());

        SMPRPG.getInstance().getEntityService().getPlayerInstance(event.getEnchanter()).shuffleSeed();

        BukkitScheduler scheduler = SMPRPG.getInstance().getServer().getScheduler();
        scheduler.runTaskLater(SMPRPG.getInstance(), () -> {
           InventoryView view = event.getEnchanter().getOpenInventory();
           if (!(view.getTopInventory() instanceof EnchantingInventory inv))
            return;
           inv.setSecondary(ItemType.LAPIS_LAZULI.createItemStack(64));
           ItemStack result = inv.getItem();
           SMPRPG.getInstance().getItemService().ensureItemStackUpdated(result);
           inv.setItem(result);
        }, 0L);
    }

}
