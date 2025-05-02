package xyz.devvydont.smprpg.services;

import com.destroystokyo.paper.event.inventory.PrepareResultEvent;
import io.papermc.paper.datacomponent.DataComponentTypes;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ItemMergeEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.world.LootGenerateEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.SMPItemQuery;
import xyz.devvydont.smprpg.items.base.*;
import xyz.devvydont.smprpg.items.blueprints.resources.VanillaResource;
import xyz.devvydont.smprpg.items.blueprints.vanilla.*;
import xyz.devvydont.smprpg.items.interfaces.*;
import xyz.devvydont.smprpg.items.listeners.ExperienceBottleListener;
import xyz.devvydont.smprpg.items.listeners.ShieldBlockingListener;
import xyz.devvydont.smprpg.reforge.ReforgeBase;
import xyz.devvydont.smprpg.reforge.ReforgeType;
import xyz.devvydont.smprpg.util.attributes.AttributeUtil;
import xyz.devvydont.smprpg.util.crafting.ItemUtil;
import xyz.devvydont.smprpg.util.crafting.MaterialWrapper;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;
import xyz.devvydont.smprpg.util.formatting.MinecraftStringUtils;
import xyz.devvydont.smprpg.util.formatting.Symbols;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class ItemService implements BaseService, Listener {

    // Integer to tag items with whenever we update them to prevent unnecessary work
    public static final int VERSION = 1;
    public static final int VERSION_NO_UPDATE = -1;

    // Shortcut methods to do very common operations much less verbosely. This instance should always be a singleton
    // so static method calls like this are designed to be safe.

    /**
     * Given a custom item type enum, retrieve a default ItemStack of the custom item type.
     *
     * @param type The type of item you want
     * @return an ItemStack instance freshly generated of the type desired
     */
    public static ItemStack generate(CustomItemType type) {
        return SMPRPG.getInstance().getItemService().getCustomItem(type);
    }

    /**
     * Given a Bukkit material, return a vanilla instanced ItemStack with properly updated metadata.
     *
     * @param material The Minecraft material to retrieve
     * @return An ItemStack instance freshly generated of the vanilla material desired
     */
    public static ItemStack generate(Material material) {
        return SMPRPG.getInstance().getItemService().getCustomItem(material);
    }

    /**
     * Given an item, retrieve its blueprint.
     * @param item The item to get a blueprint of.
     * @return The blueprint.
     */
    public static SMPItemBlueprint blueprint(ItemStack item) {
        return SMPRPG.getInstance().getItemService().getBlueprint(item);
    }

    // End shortcut static methods

    SMPRPG plugin;
    public final NamespacedKey ITEM_VERSION_KEY;
    public final NamespacedKey ITEM_TYPE_KEY;
    public final NamespacedKey REFORGE_TYPE_KEY;

    private final Map<Material, VanillaItemBlueprint> vanillaBlueprintResolver;

    private final Map<CustomItemType, SMPItemBlueprint> blueprints;
    private final Map<String, CustomItemType> keyMappings;

    private final Map<String, ReforgeBase> reforges;

    private final List<Recipe> registeredRecipes;

    private final List<Listener> listeners;

    private final Map<Material, List<NamespacedKey>> materialToRecipeUnlocks = new HashMap<>();
    private final Map<CustomItemType, List<NamespacedKey>> customItemToRecipeUnlocks = new HashMap<>();

    public ItemService(SMPRPG plugin) {
        this.plugin = plugin;
        ITEM_VERSION_KEY = new NamespacedKey(plugin, "item-version");
        ITEM_TYPE_KEY = new NamespacedKey(plugin, "item-type");
        REFORGE_TYPE_KEY = new NamespacedKey(plugin, "reforge");

        vanillaBlueprintResolver = new HashMap<>();
        blueprints = new HashMap<>();
        keyMappings = new HashMap<>();
        reforges = new HashMap<>();
        listeners = new ArrayList<>();

        registeredRecipes = new ArrayList<>();

        registerListeners();
    }

    /*
     * Registers listeners associated with custom items. This is for things such as abilities, shield blocking, etc.
     */
    private void registerListeners() {
        cleanupListeners();

        listeners.add(new ShieldBlockingListener());
        listeners.add(new ExperienceBottleListener());

        // Register all the listeners.
        for (Listener listener : listeners)
            plugin.getServer().getPluginManager().registerEvents(listener, plugin);
    }

    /*
     * Tells all the listeners to stop functioning
     */
    private void cleanupListeners() {
        for (Listener listener : listeners)
            HandlerList.unregisterAll(listener);
        listeners.clear();
    }

    /**
     * Used to count recipes registered on the server, mostly used for logging when this service starts
     *
     * @return How many recipes are registered.
     */
    private int countRecipes() {
        int n = 0;
        Iterator<Recipe> recipeIterator = plugin.getServer().recipeIterator();
        while (recipeIterator.hasNext()) {
            n++;
            recipeIterator.next();
        }
        return n;
    }

    @Override
    public boolean setup() {
        plugin.getLogger().info("Setting up Item Service");
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        registerReforges();
        plugin.getLogger().info(String.format("Successfully registered %d reforges", reforges.size()));

        int recipeCount = countRecipes();
        registerCustomItems();
        plugin.getLogger().info(String.format("Successfully associated %d vanilla materials with blueprints", vanillaBlueprintResolver.size()));
        plugin.getLogger().info(String.format("Successfully registered %d custom item blueprints", blueprints.size()));
        int postCustomRegisteredRecipeCount = countRecipes();
        plugin.getLogger().info(String.format("Successfully registered %d custom crafting recipes", postCustomRegisteredRecipeCount-recipeCount));

        int preCompressionRecipeCount = countRecipes();
        registerCompressionCraftingChains();
        int postCompressionRecipeCount = countRecipes();
        plugin.getLogger().info(String.format("Successfully registered %d compression recipes", postCompressionRecipeCount-preCompressionRecipeCount));

        Bukkit.updateRecipes();
        return true;
    }

    @Override
    public void cleanup() {

        plugin.getLogger().info("Cleaning up ItemService");

        // Unregister all the custom recipes.
        for (SMPItemBlueprint blueprint : blueprints.values()) {
            if (blueprint instanceof ICraftable)
                plugin.getServer().removeRecipe(((ICraftable) blueprint).getRecipeKey());

            if (blueprint instanceof Compressable)
                for (NamespacedKey key : ((Compressable) blueprint).getAllRecipeKeys())
                    plugin.getServer().removeRecipe(key);
        }

        // Tell all the listeners to stop doing things.
        cleanupListeners();
    }

    @Override
    public boolean required() {
        return false;
    }

    private void registerCustomItems() {

        registerVanillaMaterialResolver(Material.SHEARS, ItemShears.class);

        registerVanillaMaterialResolver(Material.WOODEN_SWORD, ItemSword.class);
        registerVanillaMaterialResolver(Material.STONE_SWORD, ItemSword.class);
        registerVanillaMaterialResolver(Material.GOLDEN_SWORD, ItemSword.class);
        registerVanillaMaterialResolver(Material.IRON_SWORD, ItemSword.class);
        registerVanillaMaterialResolver(Material.DIAMOND_SWORD, ItemSword.class);
        registerVanillaMaterialResolver(Material.NETHERITE_SWORD, ItemSword.class);
        registerVanillaMaterialResolver(Material.SHIELD, ItemShield.class);

        registerVanillaMaterialResolver(Material.TRIDENT, ItemSword.class);

        registerVanillaMaterialResolver(Material.WOODEN_AXE, ItemAxe.class);
        registerVanillaMaterialResolver(Material.STONE_AXE, ItemAxe.class);
        registerVanillaMaterialResolver(Material.GOLDEN_AXE, ItemAxe.class);
        registerVanillaMaterialResolver(Material.IRON_AXE, ItemAxe.class);
        registerVanillaMaterialResolver(Material.DIAMOND_AXE, ItemAxe.class);
        registerVanillaMaterialResolver(Material.NETHERITE_AXE, ItemAxe.class);

        registerVanillaMaterialResolver(Material.BOW, ItemBow.class);
        registerVanillaMaterialResolver(Material.CROSSBOW, ItemCrossbow.class);

        registerVanillaMaterialResolver(Material.WOODEN_PICKAXE, ItemPickaxe.class);
        registerVanillaMaterialResolver(Material.STONE_PICKAXE, ItemPickaxe.class);
        registerVanillaMaterialResolver(Material.GOLDEN_PICKAXE, ItemPickaxe.class);
        registerVanillaMaterialResolver(Material.IRON_PICKAXE, ItemPickaxe.class);
        registerVanillaMaterialResolver(Material.DIAMOND_PICKAXE, ItemPickaxe.class);
        registerVanillaMaterialResolver(Material.NETHERITE_PICKAXE, ItemPickaxe.class);

        registerVanillaMaterialResolver(Material.WOODEN_SHOVEL, ItemShovel.class);
        registerVanillaMaterialResolver(Material.STONE_SHOVEL, ItemShovel.class);
        registerVanillaMaterialResolver(Material.GOLDEN_SHOVEL, ItemShovel.class);
        registerVanillaMaterialResolver(Material.IRON_SHOVEL, ItemShovel.class);
        registerVanillaMaterialResolver(Material.DIAMOND_SHOVEL, ItemShovel.class);
        registerVanillaMaterialResolver(Material.NETHERITE_SHOVEL, ItemShovel.class);

        registerVanillaMaterialResolver(Material.WOODEN_HOE, ItemHoe.class);
        registerVanillaMaterialResolver(Material.STONE_HOE, ItemHoe.class);
        registerVanillaMaterialResolver(Material.GOLDEN_HOE, ItemHoe.class);
        registerVanillaMaterialResolver(Material.IRON_HOE, ItemHoe.class);
        registerVanillaMaterialResolver(Material.DIAMOND_HOE, ItemHoe.class);
        registerVanillaMaterialResolver(Material.NETHERITE_HOE, ItemHoe.class);

        registerVanillaMaterialResolver(Material.LEATHER_HELMET, ItemArmor.class);
        registerVanillaMaterialResolver(Material.LEATHER_CHESTPLATE, ItemArmor.class);
        registerVanillaMaterialResolver(Material.LEATHER_LEGGINGS, ItemArmor.class);
        registerVanillaMaterialResolver(Material.LEATHER_BOOTS, ItemArmor.class);

        registerVanillaMaterialResolver(Material.CHAINMAIL_HELMET, ItemArmor.class);
        registerVanillaMaterialResolver(Material.CHAINMAIL_CHESTPLATE, ItemArmor.class);
        registerVanillaMaterialResolver(Material.CHAINMAIL_LEGGINGS, ItemArmor.class);
        registerVanillaMaterialResolver(Material.CHAINMAIL_BOOTS, ItemArmor.class);

        registerVanillaMaterialResolver(Material.GOLDEN_HELMET, ItemArmor.class);
        registerVanillaMaterialResolver(Material.GOLDEN_CHESTPLATE, ItemArmor.class);
        registerVanillaMaterialResolver(Material.GOLDEN_LEGGINGS, ItemArmor.class);
        registerVanillaMaterialResolver(Material.GOLDEN_BOOTS, ItemArmor.class);

        registerVanillaMaterialResolver(Material.IRON_HELMET, ItemArmor.class);
        registerVanillaMaterialResolver(Material.IRON_CHESTPLATE, ItemArmor.class);
        registerVanillaMaterialResolver(Material.IRON_LEGGINGS, ItemArmor.class);
        registerVanillaMaterialResolver(Material.IRON_BOOTS, ItemArmor.class);

        registerVanillaMaterialResolver(Material.DIAMOND_HELMET, ItemArmor.class);
        registerVanillaMaterialResolver(Material.DIAMOND_CHESTPLATE, ItemArmor.class);
        registerVanillaMaterialResolver(Material.DIAMOND_LEGGINGS, ItemArmor.class);
        registerVanillaMaterialResolver(Material.DIAMOND_BOOTS, ItemArmor.class);

        registerVanillaMaterialResolver(Material.NETHERITE_HELMET, ItemArmor.class);
        registerVanillaMaterialResolver(Material.NETHERITE_CHESTPLATE, ItemArmor.class);
        registerVanillaMaterialResolver(Material.NETHERITE_LEGGINGS, ItemArmor.class);
        registerVanillaMaterialResolver(Material.NETHERITE_BOOTS, ItemArmor.class);

        registerVanillaMaterialResolver(Material.ELYTRA, ItemArmor.class);
        registerVanillaMaterialResolver(Material.TURTLE_HELMET, ItemArmor.class);
        registerVanillaMaterialResolver(Material.LEATHER_HORSE_ARMOR, ItemArmor.class);
        registerVanillaMaterialResolver(Material.GOLDEN_HORSE_ARMOR, ItemArmor.class);
        registerVanillaMaterialResolver(Material.IRON_HORSE_ARMOR, ItemArmor.class);
        registerVanillaMaterialResolver(Material.DIAMOND_HORSE_ARMOR, ItemArmor.class);
        registerVanillaMaterialResolver(Material.WOLF_ARMOR, ItemArmor.class);

        registerVanillaMaterialResolver(Material.FISHING_ROD, ItemFishingRod.class);

        registerVanillaMaterialResolver(Material.MACE, ItemMace.class);

        registerVanillaMaterialResolver(Material.ENCHANTED_BOOK, ItemEnchantedBook.class);

        // Register vanilla items that should have a sell price.
        for (Map.Entry<Material, Integer> entry : VanillaResource.getMaterialWorthMap().entrySet())
            registerVanillaMaterialResolver(entry.getKey(), VanillaResource.class);

        // Loop through all the custom items and use reflection to register a handler
        for (CustomItemType customItemType : CustomItemType.values()) {

            CustomItemBlueprint blueprint;
            try {
                blueprint = customItemType.getHandler().getConstructor(ItemService.class, CustomItemType.class).newInstance(this, customItemType);
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
                plugin.getLogger().severe("Failed to register custom item: " + customItemType + e.getMessage());
                continue;
            }

            registerCustomItem(blueprint);
        }

        // Now go back through and register item recipes since every item is generated
        for (SMPItemBlueprint blueprint : getCustomBlueprints()) {
            if (blueprint instanceof ICraftable craftable) {

                // Only register it if it is not registered already
                if (plugin.getServer().getRecipe(craftable.getRecipeKey()) == null)
                    plugin.getServer().addRecipe(craftable.getCustomRecipe());

                registeredRecipes.add(craftable.getCustomRecipe());
            }

        }

        // Go back through all items and find recipe links, kind of ugly but this will save us computation time
        for (SMPItemBlueprint blueprint : getCustomBlueprints()) {

            // If a blueprint is compressable, then the first material in the chain will unlock all the recipes.
            if (blueprint instanceof Compressable compressable) {

                MaterialWrapper firstElement = compressable.getCompressionFlow().getFirst().getMaterial();

                if (firstElement.isCustom()) {

                    List<NamespacedKey> recipes = customItemToRecipeUnlocks.getOrDefault(firstElement.getCustom(), new ArrayList<>());
                    recipes.addAll(compressable.getAllRecipeKeys());
                    customItemToRecipeUnlocks.put(firstElement.getCustom(), recipes);

                } else {

                    List<NamespacedKey> recipes = materialToRecipeUnlocks.getOrDefault(firstElement.getVanilla(), new ArrayList<>());
                    recipes.addAll(compressable.getAllRecipeKeys());
                    materialToRecipeUnlocks.put(firstElement.getVanilla(), recipes);

                }

            }

            if (blueprint instanceof ICraftable craftable) {
                for (ItemStack unlockedBy : craftable.unlockedBy()) {
                    SMPItemBlueprint unlockBlueprint = getBlueprint(unlockedBy);
                    if (unlockBlueprint instanceof CustomItemBlueprint custom) {
                        List<NamespacedKey> recipes = customItemToRecipeUnlocks.getOrDefault(custom.getCustomItemType(), new ArrayList<>());
                        recipes.add(craftable.getRecipeKey());
                        customItemToRecipeUnlocks.put(custom.getCustomItemType(), recipes);
                    } else if (unlockBlueprint instanceof  VanillaItemBlueprint vanilla) {
                        List<NamespacedKey> recipes = materialToRecipeUnlocks.getOrDefault(vanilla.getMaterial(), new ArrayList<>());
                        recipes.add(craftable.getRecipeKey());
                        materialToRecipeUnlocks.put(vanilla.getMaterial(), recipes);
                    }

                }
            }
        }


    }

    private void registerReforges() {
        for (ReforgeType reforgeType : ReforgeType.values()) {
            ReforgeBase handler = reforgeType.createHandler();
            if (handler instanceof Listener)
                plugin.getServer().getPluginManager().registerEvents((Listener) handler, plugin);
            reforges.put(reforgeType.key(), handler);
        }
    }

    private VanillaItemBlueprint registerVanillaMaterialResolver(Material material, Class<? extends VanillaItemBlueprint> wrapper) {
        plugin.getLogger().finest(String.format("Assigned vanilla material %s with wrapper class %s", material.name(), wrapper.getName()));

        if (vanillaBlueprintResolver.containsKey(material))
            throw new IllegalStateException("Already registered material " + material + " with wrapper " + vanillaBlueprintResolver.get(material).getClass().getName());

        // Use reflection to create an instance of the wrapper.
        VanillaItemBlueprint instance;
        try {
            instance = wrapper.getConstructor(ItemService.class, Material.class).newInstance(this, material);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            plugin.getLogger().severe("Failed to instantiate vanilla material handler " + material + " - " + e.getMessage());
            return null;
        }

        vanillaBlueprintResolver.put(material, instance);
        if (instance instanceof Listener listener)
            plugin.getServer().getPluginManager().registerEvents(listener, plugin);
        return instance;
    }

    private void registerCustomItem(CustomItemBlueprint blueprint) {
        plugin.getLogger().finest(String.format("Registering custom item %s {key=%s,model=%d}", blueprint.getCustomItemType().name, blueprint.getCustomItemType().getKey(), blueprint.getCustomItemType().getModelData()));

        blueprints.put(blueprint.getCustomItemType(), blueprint);
        keyMappings.put(blueprint.getCustomItemType().getKey(), blueprint.getCustomItemType());

        // If this blueprint needs to hook into events register them.
        if (blueprint instanceof Listener)
            plugin.getServer().getPluginManager().registerEvents((Listener) blueprint, plugin);
    }

    /**
     * Loops through every custom blueprint and checks if its a "compression chain" class.
     * These classes contain many recipes to allow compression and decompression of a family of items.
     * Duplicate recipes may attempt to be registered, but that is checked for in the class.
     */
    private void registerCompressionCraftingChains() {

        for (SMPItemBlueprint blueprint : blueprints.values()) {
            if (blueprint instanceof Compressable compressable) {
                registeredRecipes.addAll(compressable.registerCompressionChain());

                // todo: make a setting for this
                // Also do this the other way around to allow decompression
                registeredRecipes.addAll(compressable.registerDecompressionChain());
            }
        }

    }

    public int getItemVersion(ItemStack item) {
        return item.getPersistentDataContainer().getOrDefault(ITEM_VERSION_KEY, PersistentDataType.INTEGER, 0);
    }

    public void setItemVersion(ItemStack item, int version) {
        item.editMeta(meta -> {
            meta.getPersistentDataContainer().set(ITEM_VERSION_KEY, PersistentDataType.INTEGER, version);
        });
    }

    /**
     * Some items don't need to be updated, for example GUI items, weird custom items, and probably debug items
     *
     * @param itemStack The item that should or should not listen for updates.
     * @return true if it should not be updated ever.
     */
    public boolean shouldIgnoreMetaUpdate(ItemStack itemStack) {
        return getItemVersion(itemStack) == VERSION_NO_UPDATE;
    }

    public void setIgnoreMetaUpdate(ItemStack itemStack) {
        setItemVersion(itemStack, VERSION_NO_UPDATE);
    }

    /**
     * Attempt to extract a custom item key from the given item meta. null if it is not a custom item from this plugin.
     *
     * @param meta The ItemMeta of the item.
     * @return The unique identifier for the item type.
     */
    @Nullable
    public String getItemKey(ItemMeta meta) {
        if (meta == null)
            return null;

        String key = meta.getPersistentDataContainer().getOrDefault(ITEM_TYPE_KEY, PersistentDataType.STRING, "");
        if (key.isEmpty())
            return null;

        return key;
    }

    /**
     * Attempt to extract a custom item key from the given item stack. null if it is not a custom item from this plugin.
     *
     * @param itemStack The item.
     * @return The unique identifier for the item type.
     */
    @Nullable
    public String getItemKey(ItemStack itemStack) {

        if (itemStack == null)
            return null;

        if (itemStack.getItemMeta() == null)
            return null;

        if (!itemStack.hasItemMeta())
            return null;

        return getItemKey(itemStack.getItemMeta());
    }

    /**
     * Given a string representation of a key that should be stored for a custom item, attempt to retrieve it.
     * If null is returned, that means the key is not registered.
     *
     * @param key The unique identifier from getItemKey().
     * @return The CustomItemType enum associated with the unique identifier.
     */
    @Nullable
    public CustomItemType getItemTypeFromKey(String key) {
        return keyMappings.getOrDefault(key, null);
    }

    /**
     * Given an item stack, attempt to determine its custom item type based on the key stored in item meta.
     * If null is returned, that means this item is not a custom item and is vanilla.
     *
     * @param itemStack The item to check.
     * @return The CustomItemType enum from the item stack, if it is associated with one. Returns null if not.
     */
    @Nullable
    public CustomItemType getItemTypeFromItemStack(ItemStack itemStack) {

        String key = getItemKey(itemStack);
        if (key == null)
            return null;

        return getItemTypeFromKey(key);
    }

    /**
     * Given item meta, attempt to determine its custom item type based on the key stored in item meta.
     * If null is returned, that means this item is not a custom item and is vanilla.
     *
     * @param meta The ItemMeta of an item.
     * @return The CustomItemType associated with this meta, if it is associated with one. Otherwise, returns null.
     */
    @Nullable
    public CustomItemType getItemTypeFromMeta(ItemMeta meta) {

        String key = getItemKey(meta);
        if (key == null)
            return null;

        return getItemTypeFromKey(key);
    }

    /**
     * Given a custom item type, retreive the blueprint stored for it. We can guarantee that the blueprint
     * queried is going to be a custom item blueprint.
     *
     * @param type The CustomItemType to retrieve a blueprint for.
     * @return The item wrapper/handler for the given type of item.
     */
    public CustomItemBlueprint getBlueprint(CustomItemType type) {
        return (CustomItemBlueprint) blueprints.get(type);
    }

    /**
     * Returns a collection of all blueprints that are registered as "custom items"
     *
     * @return Returns all the items that are considered custom.
     */
    public Collection<SMPItemBlueprint> getCustomBlueprints() {
        return blueprints.values();
    }

    /**
     * Completely ignores any meta stored on an item. Return a vanilla blueprint for a material.
     * If the material resolver detects a certain class to handle this material, use that.
     * If nothing is found, use the default vanilla item blueprint.
     * Can throw a lot of exceptions, but as long as everything is defined correctly this will never happen
     *
     * @param item The item that we are querying a blueprint for.
     * @return The vanilla item blueprint wrapper to interact with this item.
     */
    public VanillaItemBlueprint getVanillaBlueprint(ItemStack item) {

        // If we contain the key already for this handler, just return it.
        if (vanillaBlueprintResolver.containsKey(item.getType()))
            return vanillaBlueprintResolver.get(item.getType());

        // If we don't contain the key for what we desire, simply just create a new one and register it.
        var handler = registerVanillaMaterialResolver(item.getType(), VanillaItemBlueprint.class);

        // If this went wrong, then just return a new instance. Not a big deal.
        if (handler == null)
            handler = new VanillaItemBlueprint(this, item.getType());

        return handler;
    }

    /**
     * Given an itemstack, retrieve a blueprint that can interact with the item.
     * When an item is custom, the defined custom item blueprint is returned.
     * When an item is vanilla, a fresh transient VanillaItemBlueprint instance is returned.
     * This instance is unique to the item stack and allows modifications similar to what the custom
     * item blueprints do.
     *
     * @param itemStack The item whose blueprint is desired.
     * @return The blueprint of this item.
     */
    @NotNull
    public SMPItemBlueprint getBlueprint(@NotNull ItemStack itemStack) {

        // Retrieve the custom item identifier. If it is null, return a brand new VanillaItemBlueprint.
        String key = getItemKey(itemStack);
        if (key == null)
            return getVanillaBlueprint(itemStack);

        // Retrieve the custom item type. If this is also null, that means this key was not valid. This item is legacy
        CustomItemType type = getItemTypeFromKey(key);
        if (type == null)
            return getBlueprint(CustomItemType.LEGACY_ITEM);

        return getBlueprint(type);
    }

    /**
     * Use to generate a new ItemStack using a certain custom item enum
     *
     * @param type The type of item desired.
     * @return A newly generated item of the desired type.
     */
    public ItemStack getCustomItem(CustomItemType type) {
        return getBlueprint(type).generate();
    }

    /**
     * Get a vanilla item and make sure it is up to the standard of our SMP items.
     *
     * @param material The vanilla material desired.
     * @return The fully updated and converted vanilla item.
     */
    public ItemStack getCustomItem(Material material) {
        ItemStack item = new ItemStack(material);
        ensureItemStackUpdated(item);
        return item;
    }

    /**
     * Use to generate a new ItemStack using the unique identifier for a custom item. can be null if invalid key given.
     *
     * @param key The unique identifier of a desired item.
     * @return The updated and newly generated item.
     */
    @Nullable
    public ItemStack getCustomItem(String key) {

        // Check if the key given is valid
        CustomItemType type = getItemTypeFromKey(key);
        if (type == null)
            return null;

        // Make a new item
        return getBlueprint(type).generate();
    }

    /**
     * Returns a collection of all registered reforges for the server
     *
     * @return All registered reforges.
     */
    public Collection<ReforgeBase> getReforges() {
        return reforges.values();
    }

    public ReforgeBase getReforge(ReforgeType type) {
        return reforges.get(type.key());
    }

    /**
     * Get the reforge that is contained on the item. Null if not reforged.
     *
     * @param meta The meta of the reforged item.
     * @return The reforge instance on this item.
     */
    @Nullable
    public ReforgeBase getReforge(ItemMeta meta) {
        if (!meta.getPersistentDataContainer().has(REFORGE_TYPE_KEY))
            return null;

        String appliedReforgeKey = meta.getPersistentDataContainer().get(REFORGE_TYPE_KEY, PersistentDataType.STRING);
        if (!reforges.containsKey(appliedReforgeKey))
            return null;

        return reforges.get(appliedReforgeKey);
    }

    @Nullable
    public ReforgeBase getReforge(ItemStack item) {

        if (item == null || item.getType().equals(Material.AIR) || !item.hasItemMeta())
            return null;

        return getReforge(item.getItemMeta());
    }

    public boolean hasReforge(ItemMeta meta) {
        return getReforge(meta) != null;
    }

    /**
     * Given an ItemStack, return the information about this item. Useful if you need to do multiple checks
     * and need blueprint shortcuts since SMPItemQuery contains the blueprint as well.
     *
     * @param itemStack The ItemStack to query.
     * @return A record containing information about the item.
     */
    public SMPItemQuery getItemInformation(ItemStack itemStack) {

        SMPItemBlueprint blueprint = getBlueprint(itemStack);
        SMPItemQuery.ItemType type = blueprint instanceof CustomItemBlueprint ? SMPItemQuery.ItemType.CUSTOM : SMPItemQuery.ItemType.VANILLA;

        return new SMPItemQuery(type, blueprint);
    }

    /**
     * Returns a collection of recipes that our plugin has registered.
     *
     * @return All custom recipes on the server.
     */
    public Collection<Recipe> getCustomRecipes() {
        return registeredRecipes;
    }

    /**
     * Given an ItemStack, generate a list of components that is to be used for the lore (tooltip) on the item.
     * This can not only be used for actually setting ItemStack lore, but also in other circumstances such as
     * generating a hoverable chat component or buttons on a GUI.
     * This method consists of basically a disgusting amount of isinstance checks for all item interface types in
     * items/interfaces where we decide how we want to render all the attributes/components an item can have.
     * While I am usually against large methods, it makes sense to have this gross logic all in one place, and it allows
     * items to be very customizable and modular while also having a set order for how components should display.
     *
     * @param itemStack The item to render lore for.
     * @return A list of components describing the item.
     */
    public List<Component> renderItemStackLore(ItemStack itemStack) {


        // First, we need to extract the blueprint of this item so we know how to display it.
        SMPItemBlueprint blueprint = getBlueprint(itemStack);
        ItemMeta meta = itemStack.getItemMeta();

        // The lore that we are going to return at the end.
        List<Component> lore = new ArrayList<>();

        // Check for stats that the item will apply if equipped.
        if (blueprint instanceof IAttributeItem attributeable) {
            int power = attributeable.getPowerRating() + AttributeUtil.getPowerBonus(meta);
            lore.add(ComponentUtils.create("Power Rating: ").append(ComponentUtils.create(Symbols.POWER + power, NamedTextColor.YELLOW)));
            lore.add(ComponentUtils.EMPTY);
            lore.addAll(AttributeUtil.getAttributeLore(attributeable, meta));
            lore.add(ComponentUtils.create("Slot: " + attributeable.getActiveSlot().toString().toLowerCase(), NamedTextColor.DARK_GRAY));
        }

        // Check for a description.
        if (blueprint instanceof IHeaderDescribable describable) {
            lore.add(ComponentUtils.EMPTY);
            lore.addAll(describable.getHeader(itemStack));
        }

        // Check if this is a reforge applicator.
        if (blueprint instanceof ReforgeApplicator applicator) {
            lore.add(ComponentUtils.EMPTY);
            lore.addAll(applicator.getReforgeInformation());
        }

        // If this item is a shield add the shield stats
        if (blueprint instanceof IShield shieldable) {
            lore.add(ComponentUtils.EMPTY);
            lore.add(ComponentUtils.create("Blocking Resistance: ").append(ComponentUtils.create("-" + (int)(shieldable.getDamageBlockingPercent() * 100) + "%", NamedTextColor.GREEN)));
            lore.add(ComponentUtils.create("Blocking Delay: ").append(ComponentUtils.create("+" + (shieldable.getShieldDelay() / 20.0) + "s", NamedTextColor.RED)));
        }

        if (blueprint instanceof IMace mace) {
            lore.add(ComponentUtils.EMPTY);
            lore.add(ComponentUtils.merge(
                    ComponentUtils.create("Velocity Efficiency: "),
                    ComponentUtils.create(((int)(mace.getVelocityMultiplier() * 100)) + "%", NamedTextColor.GREEN)
            ));
        }

        // First, enchants. Are we not forcing glow? Only display enchants when we are not forcing glow (and have some).
        if (!itemStack.getEnchantments().isEmpty())
            lore.addAll(blueprint.getEnchantsComponent(itemStack));

        // If this item holds experience
        if (blueprint instanceof ExperienceThrowable holder) {
            lore.add(ComponentUtils.EMPTY);
            lore.add(ComponentUtils.create("Stored Experience: ").append(ComponentUtils.create(MinecraftStringUtils.formatNumber(holder.getExperience()) + "XP", NamedTextColor.GREEN)));
        }

        // Does this item have consumable properties? First, check for edibility since it is more specific.
        if (blueprint instanceof IEdible edible) {
            lore.add(ComponentUtils.EMPTY);
            lore.addAll(IEdible.generateEdibilityComponent(edible));
        } else if (blueprint instanceof IConsumable consumable) {
            lore.add(ComponentUtils.EMPTY);
            lore.addAll(IConsumable.generateConsumabilityComponent(consumable));
        }

        // Temp hack, get food and consumable properties for vanilla items.
        // Keeping this here until we get all vanilla items on the same page as custom ones.
        if (!blueprint.isCustom() && itemStack.getData(DataComponentTypes.FOOD) != null) {
            var foodData = itemStack.getData(DataComponentTypes.FOOD);
            var consumableData = itemStack.getData(DataComponentTypes.CONSUMABLE);
            lore.add(ComponentUtils.EMPTY);
            lore.addAll(IEdible.generateEdibilityComponent(IEdible.fromVanillaData(foodData, consumableData)));
        } else if (!blueprint.isCustom() && itemStack.getData(DataComponentTypes.CONSUMABLE) != null) {
            var consumableData = itemStack.getData(DataComponentTypes.CONSUMABLE);
            lore.add(ComponentUtils.EMPTY);
            lore.addAll(IConsumable.generateConsumabilityComponent(() -> consumableData));
        }

        // Is this item compressed?
        if (blueprint instanceof Compressable compressable) {
            Component material = compressable.getCompressionFlow().getFirst().getMaterial().component().decoration(TextDecoration.BOLD, true);
            lore.add(ComponentUtils.create("An ultra compressed"));
            lore.add(ComponentUtils.create("collection of ").append(material));
            lore.add(ComponentUtils.EMPTY);
            lore.add(ComponentUtils.create("(1x)  Uncompressed amount: ", NamedTextColor.DARK_GRAY).append(ComponentUtils.create(MinecraftStringUtils.formatNumber(compressable.getCompressedAmount()), NamedTextColor.DARK_GRAY, TextDecoration.BOLD)));
            lore.add(ComponentUtils.create("(64x) Uncompressed amount: ", NamedTextColor.DARK_GRAY).append(ComponentUtils.create(MinecraftStringUtils.formatNumber(compressable.getCompressedAmount() * 64L), NamedTextColor.DARK_GRAY, TextDecoration.BOLD)));
        }

        // Is this item reforged?
        if (blueprint.isReforged(itemStack)) {
            lore.add(ComponentUtils.EMPTY);
            lore.addAll(blueprint.getReforgeComponent(itemStack));
        }

        // Footer description (if present)
        if (blueprint instanceof IFooterDescribable describable) {
            lore.add(ComponentUtils.EMPTY);
            lore.addAll(describable.getFooter(itemStack));
        }

        // Durability if the item has it
        if (meta instanceof Damageable damageable && damageable.hasMaxDamage() && !meta.isUnbreakable() && !(blueprint instanceof ChargedItemBlueprint)) {
            lore.add(ComponentUtils.EMPTY);
            lore.add(
                    ComponentUtils.create("Durability: ")
                            .append(ComponentUtils.create(MinecraftStringUtils.formatNumber(damageable.getMaxDamage()-damageable.getDamage()), NamedTextColor.RED))
                            .append(ComponentUtils.create("/" + MinecraftStringUtils.formatNumber(damageable.getMaxDamage()), NamedTextColor.DARK_GRAY))
            );
        }

        // Damage resistant?
        if (meta.hasDamageResistant()) {
            var resistance = meta.getDamageResistant();
            if (resistance != null)
                lore.add(ComponentUtils.create(Symbols.FIRE + resistance.getKey().asMinimalString(), NamedTextColor.GOLD));
        }

        // Now, value and rarity
        lore.add(ComponentUtils.EMPTY);
        if (blueprint instanceof ISellable sellable) {
            int value = sellable.getWorth(itemStack.asOne());
            if (value > 0)
                lore.add(ComponentUtils.create("Sell Value: ").append(ComponentUtils.create(EconomyService.formatMoney(value), NamedTextColor.GOLD)));
        }
        lore.add(blueprint.getRarity(itemStack).applyDecoration(ComponentUtils.create(blueprint.getRarity(itemStack).name() + " " + blueprint.getItemClassification().name().replace("_", " "))).decoration(TextDecoration.BOLD, true).color(blueprint.getRarity(itemStack).color));
        lore.add(ComponentUtils.create(blueprint.getCustomModelDataIdentifier(), NamedTextColor.DARK_GRAY));
        return ComponentUtils.cleanItalics(lore);
    }

    /**
     * Given an item stack, ensure that this item stack is up to date. First makes sure that it contains its
     * initial needed item meta (lore, attributes, etc.) and also determines if updates need to be applied to it.
     *
     * @param itemStack The item to update.
     * @return The newly updated item. This isn't necessary to use, as it should reference the same item as passed in.
     */
    public ItemStack ensureItemStackUpdated(ItemStack itemStack) {

        if (itemStack == null)
            return null;

        if (itemStack.getType().equals(Material.AIR))
            return itemStack;

        if (shouldIgnoreMetaUpdate(itemStack))
            return itemStack;

        // For now just force update all the time, maybe in the future we can have item versioning
        SMPItemBlueprint blueprint = getBlueprint(itemStack);
        blueprint.updateMeta(itemStack);
        return itemStack;
    }

    private void discoverRecipesForItem(Player player, ItemStack item) {
        SMPItemBlueprint blueprint = getBlueprint(item);

        // If this blueprint is a compression member, discover the recipes for this item
        if (blueprint instanceof Compressable compressable) {
            player.discoverRecipes(compressable.getAllRecipeKeys());
        }

        // If this is a vanilla item, see if recipes are discovered by the material
        if (blueprint instanceof VanillaItemBlueprint vanilla)
            if (materialToRecipeUnlocks.containsKey(vanilla.getMaterial()))
                player.discoverRecipes(materialToRecipeUnlocks.get(vanilla.getMaterial()));

        // If this is a custom item, see if recipes are discovered by the type
        if (blueprint instanceof CustomItemBlueprint custom)
            if (customItemToRecipeUnlocks.containsKey(custom.getCustomItemType()))
                player.discoverRecipes(customItemToRecipeUnlocks.get(custom.getCustomItemType()));
    }

    /**
     * When generating loot, update all the items
     */
    @EventHandler
    private void __onGenerateLoot(LootGenerateEvent event) {

        // Loop through all the loot and fix it
        List<ItemStack> fixed = new ArrayList<>();
        for (ItemStack item : event.getLoot())
            fixed.add(ensureItemStackUpdated(item));
        event.setLoot(fixed);

    }

    /**
     * Makes sure that item meta is set correctly when being brought into this world
     */
    @EventHandler
    private void __onMiscResult(PrepareResultEvent event) {

        // If the item doesn't exist don't do anything
        if (event.getResult() == null)
            return;

        // If the item is just air don't do anything
        if (event.getResult().getType().equals(Material.AIR))
            return;

        ensureItemStackUpdated(event.getResult());
    }

    @EventHandler
    private void __onRequestVillagerTrades(PlayerInteractEntityEvent event) {

        // Is the entity being interacted with a villager?
        if (!(event.getRightClicked() instanceof Villager villager))
            return;

        // Loop through all their trades and ensure their items are up to date.
        List<MerchantRecipe> recipes = new ArrayList<>();
        for (MerchantRecipe trade : villager.getRecipes()) {

            // Construct a new recipe for this villager copying the attributes from the old one
            ItemStack newTradeResult = ItemUtil.checkVillagerItem(this, ensureItemStackUpdated(trade.getResult()));
            MerchantRecipe fixedRecipe = new MerchantRecipe(newTradeResult, trade.getMaxUses());

            // Copy over attributes
            fixedRecipe.setUses(trade.getUses());
            fixedRecipe.setExperienceReward(trade.hasExperienceReward());
            fixedRecipe.setVillagerExperience(trade.getVillagerExperience());
            fixedRecipe.setPriceMultiplier(trade.getPriceMultiplier());
            fixedRecipe.setSpecialPrice(trade.getSpecialPrice());
            fixedRecipe.setDemand(trade.getDemand());
            fixedRecipe.setIgnoreDiscounts(trade.shouldIgnoreDiscounts());

            // Fix the ingredients
            List<ItemStack> fixedIngredients = new ArrayList<>();
            for (ItemStack ingredient : trade.getIngredients())
                fixedIngredients.add(ItemUtil.checkVillagerItem(this, ensureItemStackUpdated(ingredient)));
            fixedRecipe.setIngredients(fixedIngredients);

            recipes.add(fixedRecipe);
        }
        villager.setRecipes(recipes);
    }

    @EventHandler
    private void __onCraftingResult(PrepareItemCraftEvent event) {

        // If we aren't crafting a recipe don't do anything
        if (event.getInventory().getResult() == null)
            return;

        // If the item is just air don't do anything
        if (event.getInventory().getResult().getType().equals(Material.AIR))
            return;

        ensureItemStackUpdated(event.getInventory().getResult());
    }

    /**
     * Don't ever let custom items be smith-ed into something else in a smithing table.
     * ALSO
     * We need to handle the rare event that someone is upgrading their gear to netherite.
     * We do not want to keep the old diamond name. But we also don't want to override a custom name. BLEH
     */
    @EventHandler
    private void __onSmithingPrepareResult(PrepareSmithingEvent event) {

        ItemStack input = event.getInventory().getInputEquipment();
        // Nothing in input means we don't care
        if (input == null || input.getType().equals(Material.AIR))
            return;

        // Nothing in output means we don't care
        ItemStack output = event.getResult();
        if (output == null || output.getType().equals(Material.AIR))
            return;

        SMPItemBlueprint inputBlueprint = getBlueprint(input);
        SMPItemBlueprint outputBlueprint = getBlueprint(output);

        // Don't allow custom items to turn into vanilla ones. If we have a custom input and a vanilla output, something
        // is not right.
        if (inputBlueprint instanceof CustomItemBlueprint && outputBlueprint instanceof VanillaItemBlueprint) {
            event.setResult(null);
            return;
        }

        // Don't allow custom items to be an input
        if (inputBlueprint instanceof CustomItemBlueprint) {
            event.setResult(null);
            return;
        }

        // If we have a dummy item as a result, set the result to nothing, this is so that custom smithing recipes can work properly
        if (outputBlueprint instanceof CustomItemBlueprint custom && custom.getCustomItemType().equals(CustomItemType.DUMMY_SMITHING_RESULT)) {
            event.setResult(null);
            return;
        }

        ensureItemStackUpdated(output);
        event.setResult(output);
    }

    /**
     * Called when a custom item entity spawns in the world. Add a title to it so people know it's custom
     */
    @EventHandler
    private void __onCustomItemSpawn(ItemSpawnEvent event) {

        SMPItemBlueprint blueprint = getBlueprint(event.getEntity().getItemStack());
        ensureItemStackUpdated(event.getEntity().getItemStack());

        Component quantity = ComponentUtils.EMPTY;
        if (event.getEntity().getItemStack().getAmount() > 1)
            quantity = ComponentUtils.create(String.format("%dx ", event.getEntity().getItemStack().getAmount()));

        event.getEntity().customName(quantity.append(blueprint.getNameComponent(event.getEntity().getItemStack())));
        event.getEntity().setCustomNameVisible(true);
    }

    /**
     * When custom items merge update the name to reflect the new total
     */
    @EventHandler
    private void __onCustomItemMerge(ItemMergeEvent event) {

        var blueprint = getBlueprint(event.getEntity().getItemStack());
        int newTotal = event.getEntity().getItemStack().getAmount() + event.getTarget().getItemStack().getAmount();

        var quantity = ComponentUtils.EMPTY;
        if (newTotal > 1)
            quantity = ComponentUtils.create(String.format("%dx ", newTotal));

        event.getTarget().customName(quantity.append(blueprint.getNameComponent(event.getEntity().getItemStack())));
        event.getTarget().setCustomNameVisible(true);
    }

    /**
     * Never ever allow custom items to translate into vanilla items unless we make a recipe ourselves.
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void __onAttemptCraft(PrepareItemCraftEvent event) {

        // No recipe :p
        if (event.getRecipe() == null)
            return;

        boolean craftingWithCustomItems = false;
        for (ItemStack input : event.getInventory().getMatrix()) {

            if (input == null)
                continue;

            if (this.getItemInformation(input).isCustom()) {
                craftingWithCustomItems = true;
                break;
            }
        }

        // If we are purely doing a vanilla crafting interaction, ignore :3
        if (!craftingWithCustomItems)
            return;

        if (!(event.getRecipe() instanceof Keyed recipe))
            return;

        // If we are dealing with a recipe that is not in vanilla minecraft, ignore
        if (!recipe.getKey().getNamespace().equals(NamespacedKey.MINECRAFT))
            return;

        // So now we know we are using a vanilla recipe, and custom items are in the input.
        // This is probably undesirable as custom items yielding vanilla items should be defined by us.
        event.getInventory().setResult(null);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void __onEnchant(EnchantItemEvent e) {

        if (e.isCancelled())
            return;

        new BukkitRunnable() {
            @Override
            public void run() {
                ensureItemStackUpdated(e.getItem());
            }
        }.runTaskLater(plugin, 0L);


    }

    /**
     * Schedules item update for next tick so we can catch things like interacting with buckets
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onInteract(PlayerInteractEvent event) {
        new BukkitRunnable() {
            @Override
            public void run() {
                ensureItemStackUpdated(event.getPlayer().getInventory().getItemInMainHand());
                ensureItemStackUpdated(event.getPlayer().getInventory().getItemInOffHand());
            }
        }.runTaskLater(plugin, 0L);
    }

    /**
     * Mainly used for keeping durability in sync when breaking blocks
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onBreakBlock(BlockBreakEvent event) {
        if (event.isCancelled())
            return;

        new BukkitRunnable() {
            @Override
            public void run() {
                ensureItemStackUpdated(event.getPlayer().getInventory().getItemInMainHand());
            }
        }.runTaskLater(plugin, 0L);
    }

    @EventHandler
    private void __onArmorTakeDamage(EntityDamageEvent event) {

        if (!(event.getEntity() instanceof LivingEntity living))
            return;

        if (living.getEquipment() == null)
            return;

        ItemStack[] gear = {living.getEquipment().getHelmet(), living.getEquipment().getChestplate(),
                living.getEquipment().getLeggings(), living.getEquipment().getBoots()};

        new BukkitRunnable() {
            @Override
            public void run() {
                for (ItemStack item : gear)
                    ensureItemStackUpdated(item);
            }
        }.runTaskLater(plugin, 0L);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    private void __onClickItem(InventoryClickEvent event) {

        if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR)
            return;

        if (event.getClickedInventory() == null)
            return;

        if (event.getClickedInventory().getType().equals(InventoryType.CHEST))
            return;

        ensureItemStackUpdated(event.getCurrentItem());
        discoverRecipesForItem((Player) event.getWhoClicked(), event.getCurrentItem());
    }


    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    private void __onPickupItem(PlayerAttemptPickupItemEvent event) {

        if (!event.getFlyAtPlayer())
            return;

        discoverRecipesForItem(event.getPlayer(), event.getItem().getItemStack());
    }

    @EventHandler
    private void __onPlayerDamageItem(PlayerItemDamageEvent event) {

        // Durability changes are always 1
        if (event.getDamage() > 0)
            event.setDamage(1);

    }

    @EventHandler
    private void __onPlaceCustomItem(BlockPlaceEvent event) {

        ItemStack item = event.getItemInHand();
        SMPItemBlueprint blueprint = getBlueprint(item);

        // If this item is a custom item, don't allow it to be placed!!!
        if (blueprint.isCustom())
            event.setCancelled(true);
    }

    /*
     * We never want to allow players to cook custom items.
     */
    @EventHandler(priority = EventPriority.LOWEST)
    private void __onSmelt(FurnaceSmeltEvent event) {

        // Custom item? Make it so it never cooks
        if (getBlueprint(event.getSource()).isCustom())
            event.setCancelled(true);
    }

    /*
     * We never want to allow players to cook custom items.
     */
    @EventHandler
    private void __onSmeltCustomItem(FurnaceStartSmeltEvent event) {

        // Custom item? Make it so it never cooks
        if (getBlueprint(event.getSource()).isCustom())
            event.setTotalCookTime(999_999);

    }


}
