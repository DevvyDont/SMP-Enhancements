package xyz.devvydont.smprpg.services;

import com.destroystokyo.paper.event.inventory.PrepareResultEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
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
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.items.*;
import xyz.devvydont.smprpg.items.ItemRarity;
import xyz.devvydont.smprpg.items.base.CustomItemBlueprint;
import xyz.devvydont.smprpg.items.base.SMPItemBlueprint;
import xyz.devvydont.smprpg.items.base.VanillaItemBlueprint;
import xyz.devvydont.smprpg.items.blueprints.vanilla.*;
import xyz.devvydont.smprpg.items.interfaces.*;
import xyz.devvydont.smprpg.items.reforges.ReforgeBase;
import xyz.devvydont.smprpg.items.reforges.ReforgeType;
import xyz.devvydont.smprpg.items.reforges.definitions.ReforgeAccelerated;
import xyz.devvydont.smprpg.items.reforges.interfaces.*;
import xyz.devvydont.smprpg.util.crafting.ItemUtil;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class ItemService implements BaseService, Listener {

    // Integer to tag items with whenever we update them to prevent unnecessary work
    public static final int VERSION = 1;

    public static final int VERSION_NO_UPDATE = -1;

    SMPRPG plugin;
    public final NamespacedKey ITEM_VERSION_KEY;
    public final NamespacedKey ITEM_TYPE_KEY;
    public final NamespacedKey REFORGE_TYPE_KEY;

    private final Map<Material, Class<? extends VanillaItemBlueprint>> vanillaBlueprintResolver;

    private final Map<CustomItemType, SMPItemBlueprint> blueprints;
    private final Map<String, CustomItemType> keyMappings;

    private final Map<ReforgeType, ReforgeBase> reforges;

    private final List<CraftingRecipe> registeredRecipes;

    public ItemService(SMPRPG plugin) {
        this.plugin = plugin;
        ITEM_VERSION_KEY = new NamespacedKey(plugin, "item-version");
        ITEM_TYPE_KEY = new NamespacedKey(plugin, "item-type");
        REFORGE_TYPE_KEY = new NamespacedKey(plugin, "reforge");

        vanillaBlueprintResolver = new HashMap<>();
        blueprints = new HashMap<>();
        keyMappings = new HashMap<>();
        reforges = new HashMap<>();

        registeredRecipes = new ArrayList<>();
    }

    /**
     * Used to count recipes registered on the server, mostly used for logging when this service starts
     *
     * @return
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
        int recipeCount = countRecipes();
        registerCustomItems();
        plugin.getLogger().info(String.format("Successfully associated %d vanilla materials with blueprints", vanillaBlueprintResolver.size()));
        plugin.getLogger().info(String.format("Successfully registered %d custom item blueprints", blueprints.size()));
        int postCustomRegisteredRecipeCount = countRecipes();
        plugin.getLogger().info(String.format("Successfully registered %d custom crafting recipes", postCustomRegisteredRecipeCount-recipeCount));

        registerReforges();
        plugin.getLogger().info(String.format("Successfully registered %d reforges", reforges.size()));

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
            if (blueprint instanceof Craftable)
                plugin.getServer().removeRecipe(((Craftable) blueprint).getRecipeKey());

            if (blueprint instanceof Compressable)
                for (NamespacedKey key : ((Compressable) blueprint).getAllRecipeKeys())
                    plugin.getServer().removeRecipe(key);
        }
    }

    @Override
    public boolean required() {
        return false;
    }

    private void registerCustomItems() {

        registerVanillaMaterialResolver(Material.WOODEN_SWORD, ItemSword.class);
        registerVanillaMaterialResolver(Material.STONE_SWORD, ItemSword.class);
        registerVanillaMaterialResolver(Material.GOLDEN_SWORD, ItemSword.class);
        registerVanillaMaterialResolver(Material.IRON_SWORD, ItemSword.class);
        registerVanillaMaterialResolver(Material.DIAMOND_SWORD, ItemSword.class);
        registerVanillaMaterialResolver(Material.NETHERITE_SWORD, ItemSword.class);

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

        registerVanillaMaterialResolver(Material.MACE, ItemMace.class);

        // Loop through all the custom items and use reflection to register a handler
        for (CustomItemType customItemType : CustomItemType.values()) {

            CustomItemBlueprint blueprint;
            try {
                blueprint = customItemType.getHandler().getConstructor(ItemService.class, CustomItemType.class).newInstance(this, customItemType);
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
                plugin.getLogger().severe("Failed to register custom item: " + customItemType);
                throw new RuntimeException(e);
            }

            registerCustomItem(blueprint);
        }

        // Now go back through and register item recipes since every item is generated
        for (SMPItemBlueprint blueprint : getCustomBlueprints()) {
            if (blueprint instanceof Craftable craftable) {

                // Only register it if it is not registered already
                if (plugin.getServer().getRecipe(craftable.getRecipeKey()) == null)
                    plugin.getServer().addRecipe(craftable.getCustomRecipe());

                registeredRecipes.add(craftable.getCustomRecipe());
            }
        }

        // Go back through all items and find recipe links, kind of ugly but this will save us computation time
        for (SMPItemBlueprint blueprint : getCustomBlueprints()) {
            if (blueprint instanceof Craftable craftable) {
                for (ItemStack unlockedBy : craftable.unlockedBy()) {
                    SMPItemBlueprint unlockBlueprint = getBlueprint(unlockedBy);
                    if (unlockBlueprint instanceof CustomItemBlueprint custom) {
                        List<NamespacedKey> recipes = customItemToRecipeUnlocks.getOrDefault(custom.getCustomItemType(), new ArrayList<>());
                        recipes.add(craftable.getRecipeKey());
                        customItemToRecipeUnlocks.put(custom.getCustomItemType(), recipes);
                    } else if (unlockBlueprint instanceof  VanillaItemBlueprint vanilla) {
                        List<NamespacedKey> recipes = materialToRecipeUnlocks.getOrDefault(vanilla.getItem().getType(), new ArrayList<>());
                        recipes.add(craftable.getRecipeKey());
                        materialToRecipeUnlocks.put(vanilla.getItem().getType(), recipes);
                    }

                }
            }
        }


    }

    private void registerReforges() {
        registerReforge(new ReforgeAccelerated(this));
    }

    private void registerVanillaMaterialResolver(Material material, Class<? extends VanillaItemBlueprint> wrapper) {
        plugin.getLogger().finest(String.format("Assigned vanilla material %s with wrapper class %s", material.name(), wrapper.getName()));
        vanillaBlueprintResolver.put(material, wrapper);
    }

    private void registerCustomItem(CustomItemBlueprint blueprint) {
        plugin.getLogger().finest(String.format("Registering custom item %s {key=%s,model=%d}", blueprint.getCustomItemType().name, blueprint.getCustomItemType().getKey(), blueprint.getCustomItemType().getModelData()));

        blueprints.put(blueprint.getCustomItemType(), blueprint);
        keyMappings.put(blueprint.getCustomItemType().getKey(), blueprint.getCustomItemType());

        // If this blueprint needs to hook into events register them.
        if (blueprint instanceof Listener)
            plugin.getServer().getPluginManager().registerEvents((Listener) blueprint, plugin);
    }

    private void registerReforge(ReforgeBase reforge) {
        plugin.getLogger().finest(String.format("Registering reforge %s", reforge.getReforgeType()));
        reforges.put(reforge.getReforgeType(), reforge);
    }

    /**
     * Loops through every custom blueprint and checks if its a "compression chain" class.
     * These classes contain many recipes to allow compression and decompression of a family of items.
     * Duplicate recipes may attempt to be registered, but that is checked for in the class.
     */
    private void registerCompressionCraftingChains() {

        for (SMPItemBlueprint blueprint : blueprints.values()) {
            if (blueprint instanceof Compressable) {
                Compressable compressable = (Compressable) blueprint;
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
     * @param itemStack
     * @return
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
     * @param meta
     * @return
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
     * @param itemStack
     * @return
     */
    @Nullable
    public String getItemKey(ItemStack itemStack) {

        if (itemStack == null)
            return null;

        return getItemKey(itemStack.getItemMeta());
    }

    /**
     * Given a string representation of a key that should be stored for a custom item, attempt to retrieve it.
     * If null is returned, that means the key is not registered.
     *
     * @param key
     * @return
     */
    @Nullable
    public CustomItemType getItemTypeFromKey(String key) {
        return keyMappings.getOrDefault(key, null);
    }

    /**
     * Given an item stack, attempt to determine its custom item type based on the key stored in item meta.
     * If null is returned, that means this item is not a custom item and is vanilla.
     *
     * @param itemStack
     * @return
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
     * @param meta
     * @return
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
     * @param type
     * @return
     */
    public CustomItemBlueprint getBlueprint(CustomItemType type) {
        return (CustomItemBlueprint) blueprints.get(type);
    }

    /**
     * Returns a collection of all blueprints that are registered as "custom items"
     *
     * @return
     */
    public Collection<SMPItemBlueprint> getCustomBlueprints() {
        return blueprints.values();
    }

    /**
     * Completely ignores any meta stored on an item. Return a vanilla blueprint for a material.
     * If the material resolver detects a certain class to handle this material, use that.
     * If nothing is found, use the default vanilla item blueprint.
     *
     * Can throw a lot of exceptions, but as long as everything is defined correctly this will never happen
     *
     * @param item
     * @return
     */
    public VanillaItemBlueprint getVanillaBlueprint(ItemStack item) {

        if (!vanillaBlueprintResolver.containsKey(item.getType()))
            return new VanillaItemBlueprint(this, item);

        // Perform reflection hacks to instantiate a new blueprint using the handler assigned
        Class<? extends VanillaItemBlueprint> wrapper = vanillaBlueprintResolver.get(item.getType());

        // Attempt to instantiate the new instance, if this fails failsafe back to a normal vanilla item.
        // In the event this does fail, write out an error to the server because this is really bad
        try {
            return wrapper.getConstructor(ItemService.class, ItemStack.class).newInstance(this, item);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException exception) {
            plugin.getLogger().severe(String.format("Failed to create new %s instance for item of type %s. Is the class implemented correctly?", wrapper.getName(), item.getType().name()));
            return new VanillaItemBlueprint(this, item);
        }
    }

    /**
     * Given an itemstack, retrieve a blueprint that can interact with the item.
     *
     * When an item is custom, the defined custom item blueprint is returned.
     *
     * When an item is vanilla, a fresh transient VanillaItemBlueprint instance is returned.
     * This instance is unique to the item stack and allows modifications similar to what the custom
     * item blueprints do.
     *
     * @param itemStack
     * @return
     */
    @NotNull
    public SMPItemBlueprint getBlueprint(@NotNull ItemStack itemStack) {

        // Retrieve the custom item identifier. If it is null, return a brand new VanillaItemBlueprint.
        String key = getItemKey(itemStack);
        if (key == null)
            return getVanillaBlueprint(itemStack);

        // Retreive the custom item type. If this is also null, that means this key was not valid. Assume vanilla.
        CustomItemType type = getItemTypeFromKey(key);
        if (type == null)
            return getVanillaBlueprint(itemStack);

        return getBlueprint(type);
    }

    /**
     * Use to generate a new itemstack using a certain custom item enum
     *
     * @param type
     * @return
     */
    public ItemStack getCustomItem(CustomItemType type) {
        return getBlueprint(type).generate();
    }

    /**
     * Get a vanilla item and make sure it is up to the standard of our SMP items.
     *
     * @param material
     * @return
     */
    public ItemStack getCustomItem(Material material) {
        ItemStack item = new ItemStack(material);
        ensureItemStackUpdated(item);
        return item;
    }

    /**
     * Use to generate a new itemstack using the unique identifier for a custom item. can be null if invalid key given.
     *
     * @param key
     * @return
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
     * @return
     */
    public Collection<ReforgeBase> getReforges() {
        return reforges.values();
    }

    /**
     * Returns a collection of all registered reforges for the server by reforge type enum
     *
     * @return
     */
    public Collection<ReforgeType> getRegisteredReforgeTypes() {
        return reforges.keySet();
    }

    public ReforgeBase getReforge(ReforgeType type) {
        return reforges.get(type);
    }

    /**
     * Given an equipment type, return the modifiers that are assigned to a specific reforge's equipment type.
     * It is possible that this will return an empty listen under the circumstance a reforge type was provided that
     * is not compatible with a specific equipment type. If this is the case, a warning message will be printed in
     * the console as this is most likely a developer error.
     *
     * @param reforgeType
     * @param rarity
     * @param itemClassification
     * @return
     */
    public Map<Attribute, AttributeModifier> getModifiersForReforge(ReforgeType reforgeType, ItemRarity rarity, ItemClassification itemClassification) {

        if (!getRegisteredReforgeTypes().contains(reforgeType))
            throw new IllegalArgumentException("Reforge " + reforgeType + " was not registered");

        ReforgeBase reforge = getReforge(reforgeType);

        // Essentially analyze the equipment type we received and see if there's valid attributes for it

        // If this type is a sword and we have a melee reforge
        if (itemClassification.equals(ItemClassification.SWORD) && reforge instanceof MeleeReforgeable)
            return ((MeleeReforgeable) reforge).getMeleeModifiers(rarity);

        // If this type is a bow and we have a ranged reforge
        if (itemClassification.equals(ItemClassification.BOW) && reforge instanceof RangedReforgeable)
            return ((RangedReforgeable) reforge).getRangedModifiers(rarity);

        // If this item is an axe and we have a ranged reforge
        if (itemClassification.equals(ItemClassification.AXE) && reforge instanceof MeleeReforgeable)
            return ((MeleeReforgeable) reforge).getMeleeModifiers(rarity);

        // If this item is armor and we have a wearable reforge
        if (itemClassification.isArmor() && reforge instanceof ArmorReforgeable)
            return ((ArmorReforgeable) reforge).getArmorModifiers(rarity);

        // If this item is something that is held and we have a holding reforge
        if (itemClassification.equals(ItemClassification.EQUIPMENT) && reforge instanceof HoldingReforgeable)
            return ((HoldingReforgeable) reforge).getHeldModifiers(rarity);

        // If this item harvests (shovel, pickaxe, hoe) and we have a harvesting reforge
        if (itemClassification.equals(ItemClassification.TOOL) && reforge instanceof HarvestReforgeable)
            return ((HarvestReforgeable) reforge).getHarvesterModifiers(rarity);

        // If we made it past this point, we prob got an invalid combination
        plugin.getLogger().severe(String.format("Detected impossible reforge combination! REFORGE %s -> SLOT %s", reforgeType, itemClassification));
        return Map.of();
    }

    /**
     * Given an ItemStack, return the information about this item. Useful if you need to need to do multiple checks
     * and need blueprint shortcuts since SMPItemQuery contains the blueprint as well.
     *
     * @param itemStack
     * @return
     */
    public SMPItemQuery getItemInformation(ItemStack itemStack) {

        SMPItemBlueprint blueprint = getBlueprint(itemStack);
        SMPItemQuery.ItemType type = blueprint instanceof CustomItemBlueprint ? SMPItemQuery.ItemType.CUSTOM : SMPItemQuery.ItemType.VANILLA;

        return new SMPItemQuery(type, blueprint);
    }

    /**
     * Returns a collection of recipes that our plugin has registered.
     *
     * @return
     */
    public Collection<CraftingRecipe> getCustomRecipes() {
        return registeredRecipes;
    }

    /**
     * Given an item stack, ensure that this item stack is up to date. First makes sure that it contains its
     * initial needed item meta (lore, attributes, etc.) and also determines if updates need to be applied to it.
     *
     * @param itemStack
     * @return
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

    /**
     * When generating loot, update all the items
     *
     * @param event
     */
    @EventHandler
    public void onGenerateLoot(LootGenerateEvent event) {

        // Loop through all the loot and fix it
        List<ItemStack> fixed = new ArrayList<>();
        for (ItemStack item : event.getLoot())
            fixed.add(ensureItemStackUpdated(item));
        event.setLoot(fixed);

    }

    /**
     * Makes sure that item meta is set correctly when being brought into this world
     *
     * @param event
     */
    @EventHandler
    public void onMiscResult(PrepareResultEvent event) {

        // If the item doesn't exist don't do anything
        if (event.getResult() == null)
            return;

        // If the item is just air don't do anything
        if (event.getResult().getType().equals(Material.AIR))
            return;

        ensureItemStackUpdated(event.getResult());
    }

    @EventHandler
    public void onRequestVillagerTrades(PlayerInteractEntityEvent event) {

        // Is the entity being interacted with a villager?
        if (!(event.getRightClicked() instanceof Villager))
            return;

        Villager villager = (Villager) event.getRightClicked();

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
    public void onCraftingResult(PrepareItemCraftEvent event) {

        // If we aren't crafting a recipe don't do anything
        if (event.getInventory().getResult() == null)
            return;

        // If the item is just air don't do anything
        if (event.getInventory().getResult().getType().equals(Material.AIR))
            return;

        ensureItemStackUpdated(event.getInventory().getResult());
    }

    /**
     * Don't ever let custom items be smithed into something else in a smithing table.
     *
     * ALSO
     *
     * We need to handle the rare event that someone is upgrading their gear to netherite.
     * We do not want to keep the old diamond name. But we also don't want to override a custom name. BLEH
     *
     * @param event
     */
    @EventHandler
    public void onSmithingPrepareResult(PrepareSmithingEvent event) {

        ItemStack input = event.getInventory().getInputEquipment();
        // Nothing in input means we don't care
        if (input == null || input.getType().equals(Material.AIR))
            return;

        SMPItemBlueprint inputBlueprint = getBlueprint(input);

        // Do we have a custom item in the input slot? If so, make sure result is empty and return
        if (inputBlueprint.isCustom()) {
            event.setResult(null);
            return;
        }

        // Skip anything that isn't diamond
        if (!ItemUtil.getDiamondEquipment().contains(input.getType()))
            return;

        // Skip if nothing is in the output
        ItemStack output = event.getResult();
        if (output == null || output.getType().equals(Material.AIR))
            return;

        ensureItemStackUpdated(output);

        // Skip if netherite is not in the output
        if (!ItemUtil.getNetheriteEquipment().contains(output.getType()))
            return;

        // Skip this item if it doesn't have a display name (won't screw it up)
        ItemMeta outputMeta = output.getItemMeta();
        if (outputMeta.displayName() == null)
            return;

        TextReplacementConfig replacer = TextReplacementConfig.builder().matchLiteral("Diamond").replacement("Netherite").build();
        outputMeta.displayName(outputMeta.displayName().replaceText(replacer));
        output.setItemMeta(outputMeta);
        event.setResult(output);
    }

    /**
     * Called when a custom item entity spawns in the world. Add a title to it so people know it's custom
     *
     * @param event
     */
    @EventHandler
    public void onCustomItemSpawn(ItemSpawnEvent event) {

        SMPItemBlueprint blueprint = getBlueprint(event.getEntity().getItemStack());
        ensureItemStackUpdated(event.getEntity().getItemStack());

        Component quantity = Component.empty();
        if (event.getEntity().getItemStack().getAmount() > 1)
            quantity = Component.text(String.format("%dx ", event.getEntity().getItemStack().getAmount())).color(NamedTextColor.GRAY);

        event.getEntity().customName(quantity.append(blueprint.getNameComponent(event.getEntity().getItemStack().getItemMeta())));
        event.getEntity().setCustomNameVisible(true);
    }

    /**
     * When custom items merge update the name to reflect the new total
     *
     * @param event
     */
    @EventHandler
    public void onCustomItemMerge(ItemMergeEvent event) {

        // Ignore vanilla items
        SMPItemBlueprint blueprint = getBlueprint(event.getEntity().getItemStack());
//        if (!blueprint.isCustom())
//            return;

        int newTotal = event.getEntity().getItemStack().getAmount() + event.getTarget().getItemStack().getAmount();

        Component quantity = Component.empty();
        if (newTotal > 1)
            quantity = Component.text(String.format("%dx ", newTotal)).color(NamedTextColor.GRAY);

        event.getTarget().customName(quantity.append(blueprint.getNameComponent(event.getEntity().getItemStack().getItemMeta())));
        event.getTarget().setCustomNameVisible(true);
    }

    /**
     * Never ever ever allow custom items to translate into vanilla items unless we make a recipe ourselves.
     *
     * @param event
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onAttemptCraft(PrepareItemCraftEvent event) {

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

        if (!(event.getRecipe() instanceof Keyed))
            return;

        Keyed recipe = (Keyed) event.getRecipe();

        // If we are dealing with a recipe that is not in vanilla minecraft, ignore
        if (!recipe.getKey().getNamespace().equals(NamespacedKey.MINECRAFT))
            return;

        // So now we know we are using a vanilla recipe, and custom items are in the input.
        // This is probably undesirable as custom items yielding vanilla items should be defined by us.
        event.getInventory().setResult(null);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEnchant(EnchantItemEvent e) {

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
     *
     * @param event
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
     *
     * @param event
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
    public void onArmorTakeDamage(EntityDamageEvent event) {

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

    private final Map<Material, List<NamespacedKey>> materialToRecipeUnlocks = new HashMap<>();
    private final Map<CustomItemType, List<NamespacedKey>> customItemToRecipeUnlocks = new HashMap<>();

    private void discoverRecipesForItem(Player player, ItemStack item) {
        SMPItemBlueprint blueprint = getBlueprint(item);

        // If this blueprint is a compression member, discover the recipes for this item
        if (blueprint instanceof Compressable compressable) {
            player.discoverRecipes(compressable.getAllRecipeKeys());
        }

        // If this is a vanilla item, see if recipes are discovered by the material
        if (blueprint instanceof VanillaItemBlueprint vanilla)
            if (materialToRecipeUnlocks.containsKey(vanilla.getItem().getType()))
                player.discoverRecipes(materialToRecipeUnlocks.get(vanilla.getItem().getType()));

        // If this is a custom item, see if recipes are discovered by the type
        if (blueprint instanceof CustomItemBlueprint custom)
            if (customItemToRecipeUnlocks.containsKey(custom.getCustomItemType()))
                player.discoverRecipes(customItemToRecipeUnlocks.get(custom.getCustomItemType()));
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onClickItem(InventoryClickEvent event) {

        if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR)
            return;

        discoverRecipesForItem((Player) event.getWhoClicked(), event.getCurrentItem());
    }


    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPickupItem(PlayerAttemptPickupItemEvent event) {

        if (!event.getFlyAtPlayer())
            return;

        discoverRecipesForItem(event.getPlayer(), event.getItem().getItemStack());
    }

    @EventHandler
    public void onPlayerDamageItem(PlayerItemDamageEvent event) {

        // Durability changes are always 1
        if (event.getDamage() > 0)
            event.setDamage(1);

    }


}
