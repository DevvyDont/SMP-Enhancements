package xyz.devvydont.smprpg.services;

import io.papermc.paper.persistence.PersistentDataViewHolder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.Nullable;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.attribute.AttributeWrapper;
import xyz.devvydont.smprpg.entity.base.LeveledEntity;
import xyz.devvydont.smprpg.entity.interfaces.IDamageTrackable;
import xyz.devvydont.smprpg.entity.player.LeveledPlayer;
import xyz.devvydont.smprpg.events.CustomChancedItemDropSuccessEvent;
import xyz.devvydont.smprpg.events.CustomItemDropRollEvent;
import xyz.devvydont.smprpg.items.ItemRarity;
import xyz.devvydont.smprpg.items.base.SMPItemBlueprint;
import xyz.devvydont.smprpg.util.crafting.ItemUtil;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;
import xyz.devvydont.smprpg.util.items.DropFireworkTask;
import xyz.devvydont.smprpg.util.persistence.UUIDPersistentDataType;
import xyz.devvydont.smprpg.util.time.TickTime;

import java.util.*;

/**
 * In charge of managing item drops in the world.
 * - When a player dies, their items need to be protected and can only be picked up by them
 * - When an entity drops items, they drop items for whoever helped kill it so everyone gets items
 */
public class DropsService implements IService, Listener {

    public enum DropFlag {
        NULL,
        DEATH,
        LOOT,
        TELEKINESIS_FAIL
        ;

        public static DropFlag fromInt(int flag) {
            if (flag < 0 || flag > values().length)
                return NULL;
            return DropFlag.values()[flag];
        }
    }

    // How long items will last on the ground in seconds when marked as a drop.
    public static int COMMON_EXPIRE_SECONDS = 60 * 60;    // 1hr
    public static int UNCOMMON_EXPIRE_SECONDS = 90 * 60;  // 1.5hr
    public static int RARE_EXPIRE_SECONDS = 60 * 60 * 5;  // 5hr
    public static int EPIC_EXPIRE_SECONDS = 60 * 60 * 12; // 12hr
    public static int LEGENDARY_EXPIRE_SECONDS = 60 * 60 * 24; // 24hr

    /*
     * Helper method to determine how long an item should last based on its rarity
     */
    public static long getMillisecondsUntilExpiry(ItemRarity rarity) {
        return switch (rarity) {
            case COMMON -> COMMON_EXPIRE_SECONDS;
            case UNCOMMON -> UNCOMMON_EXPIRE_SECONDS;
            case RARE -> RARE_EXPIRE_SECONDS;
            case EPIC -> EPIC_EXPIRE_SECONDS;
            default -> LEGENDARY_EXPIRE_SECONDS;
        } * 1000L;
    }

    // The owner tag for a drop, drops cannot be picked up by players unless they own it
    private final NamespacedKey OWNER_UUID_KEY;
    private final NamespacedKey OWNER_NAME_KEY;

    // The flag that an owner tag drop has. 1 = Death, 2 = Drop
    private final NamespacedKey DROP_FLAG_KEY;

    // A timestamp on when this drop should expire from the world and disappear. Varying rarity items have different times.
    private final NamespacedKey DROP_EXPIRE_KEY;

    // A task that runs every second to check if an item should expire or not.
    private BukkitRunnable itemTimerTask = null;

    public static final Map<ItemRarity, Team> rarityToTeam = new HashMap<>();

    public DropsService() {
        var plugin = SMPRPG.getInstance();
        this.OWNER_UUID_KEY = new NamespacedKey(plugin, "drop-owner-uuid");
        this.OWNER_NAME_KEY = new NamespacedKey(plugin, "drop-owner-name");
        this.DROP_FLAG_KEY = new NamespacedKey(plugin, "drop-flag");
        this.DROP_EXPIRE_KEY = new NamespacedKey(plugin, "expiry");

        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        rarityToTeam.clear();
        for (ItemRarity rarity : ItemRarity.values()) {
            Team team;
            if (scoreboard.getTeam(rarity.name()) != null)
                team = scoreboard.getTeam(rarity.name());
            else
                team = scoreboard.registerNewTeam(rarity.name());

            assert team != null;
            team.color(rarity.color);
            rarityToTeam.put(rarity, team);
        }
    }

    public Team getTeam(ItemRarity rarity) {
        return rarityToTeam.get(rarity);
    }

    public NamespacedKey getItemOwnerKey() {
        return OWNER_UUID_KEY;
    }

    public NamespacedKey getDropFlagKey() {
        return DROP_FLAG_KEY;
    }

    /**
     * Marks this PDC as owned by a player
     *
     * @param holder
     * @param player
     */
    public void setOwner(PersistentDataHolder holder, Player player) {
        holder.getPersistentDataContainer().set(OWNER_NAME_KEY, PersistentDataType.STRING, player.getName());
        holder.getPersistentDataContainer().set(getItemOwnerKey(), UUIDPersistentDataType.INSTANCE, player.getUniqueId());
    }

    /**
     * Untags this item as being owned by someone
     *
     * @param holder
     */
    public void removeOwner(PersistentDataHolder holder) {
        holder.getPersistentDataContainer().remove(OWNER_NAME_KEY);
        holder.getPersistentDataContainer().remove(getItemOwnerKey());
    }

    /**
     * Gets the UUID of the owner of this PDC. If no owner, null is returned
     *
     * @param holder
     * @return
     */
    @Nullable
    public UUID getOwner(PersistentDataViewHolder holder) {
        return holder.getPersistentDataContainer().get(getItemOwnerKey(), UUIDPersistentDataType.INSTANCE);
    }

    /**
     * Gets the String name of the owner of this PDC. If no owner, null is returned
     *
     * @param holder
     * @return
     */
    @Nullable
    public String getOwnerName(PersistentDataViewHolder holder) {
        return holder.getPersistentDataContainer().get(OWNER_NAME_KEY, PersistentDataType.STRING);
    }

    /**
     * Determines if this PDC contains the owner field
     *
     * @param holder
     * @return
     */
    public boolean hasOwner(PersistentDataViewHolder holder) {
        return holder.getPersistentDataContainer().has(getItemOwnerKey());
    }

    public DropFlag getFlag(PersistentDataViewHolder holder) {
        int rawFlag = holder.getPersistentDataContainer().getOrDefault(getDropFlagKey(), PersistentDataType.INTEGER, 0);
        return DropFlag.fromInt(rawFlag);
    }

    public void setFlag(PersistentDataHolder holder, DropFlag flag) {
        holder.getPersistentDataContainer().set(getDropFlagKey(), PersistentDataType.INTEGER, flag.ordinal());
    }

    /*
     * Checks if a PDC has an expiry timestamp set.
     */
    public boolean hasExpiryTimestamp(PersistentDataViewHolder holder) {
        return holder.getPersistentDataContainer().has(DROP_EXPIRE_KEY, PersistentDataType.LONG);
    }

    /*
     * Gets the expiry timestamp set on an item using System.currentTimeMillis()
     */
    public long getExpiryTimestamp(PersistentDataViewHolder holder) {
        return holder.getPersistentDataContainer().getOrDefault(DROP_EXPIRE_KEY, PersistentDataType.LONG, 0L);
    }

    /*
     * Flags a PDC holder to have an expiry timestamp at a certain timestamp using System.currentTimeMillis()
     */
    public void setExpiryTimestamp(PersistentDataHolder holder, long timestamp) {
        holder.getPersistentDataContainer().set(DROP_EXPIRE_KEY, PersistentDataType.LONG, timestamp);
    }

    /*
     * Removes the expiry field from an item. It is not needed anymore once it is picked up by a player.
     */
    public void removeExpiryTimestamp(PersistentDataHolder holder) {
        holder.getPersistentDataContainer().remove(DROP_EXPIRE_KEY);
    }

    public void removeFlag(PersistentDataHolder holder) {
        holder.getPersistentDataContainer().remove(getDropFlagKey());
    }

    public void removeAllTags(PersistentDataHolder holder) {
        removeOwner(holder);
        removeFlag(holder);
        removeExpiryTimestamp(holder);
    }

    public void removeAllTags(ItemStack itemStack) {
        itemStack.editMeta(this::removeAllTags);
    }

    private String stringifyTime(long seconds) {

        if (seconds < 60)
            return seconds + "s";

        if (seconds < 3600)
            return seconds / 60 + "m";

        return seconds / 3600 + "h";
    }

    @Override
    public void setup() throws RuntimeException {
        var plugin = SMPRPG.getInstance();
        itemTimerTask = new BukkitRunnable() {
            @Override
            public void run() {
                long now = System.currentTimeMillis();
                for (World world : Bukkit.getWorlds()) {
                    for (Item item : world.getEntitiesByClass(Item.class)) {

                        // If this item doesn't have the expiry tag, we can't do anything with it
                        if (!hasExpiryTimestamp(item))
                            continue;

                        // This item has an expiry time, see if it has expired, if it has then remove it
                        long expiresAt = getExpiryTimestamp(item);
                        if (expiresAt < now) {
                            item.remove();
                            continue;
                        }

                        // If it hasn't expired yet, update the name of the item
                        String rawName = getOwnerName(item);
                        if (rawName == null)
                            rawName = "???";

                        Component name = ComponentUtils.create(" (" + rawName + ")", NamedTextColor.DARK_GRAY);
                        String timeleft = stringifyTime((expiresAt - now) / 1000);
                        Component time = ComponentUtils.create(" (" + timeleft + ")", NamedTextColor.DARK_GRAY);
                        Component itemName = SMPRPG.getService(ItemService.class).getBlueprint(item.getItemStack()).getNameComponent(item.getItemStack());
                        item.customName(time.append(itemName).append(name.decoration(TextDecoration.OBFUSCATED, false)));
                    }
                }
            }
        };
        itemTimerTask.runTaskTimer(plugin, 0, TickTime.seconds(1));
    }

    @Override
    public void cleanup() {
        if (itemTimerTask != null)
            itemTimerTask.cancel();

        itemTimerTask = null;
    }

    /**
     * When players roll for a drop, consider their luck stat as a factor for an item
     *
     * @param event
     */
    @EventHandler
    private void __onConsiderLuckRollForGear(CustomItemDropRollEvent event) {

        var luck = AttributeService.getInstance().getAttribute(event.getPlayer(), AttributeWrapper.LUCK);

        if (luck == null)
            return;

        // Divide the luck in such a way that 100 luck means there's no effect.
        var multiplier = luck.getValue() / 100.0;
        event.setChance(event.getChance() * multiplier);
    }

    /**
     * When a player dies, mark all their items as being owned by them
     *
     * @param event
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    private void __onPlayerDeath(PlayerDeathEvent event) {

        // Go through all the drops on the player and tag it as being owned
        for (ItemStack drop : event.getDrops()) {
            drop.editMeta(meta -> {
                ItemRarity rarity = SMPRPG.getService(ItemService.class).getBlueprint(drop).getRarity(drop);
                setOwner(meta, event.getPlayer());
                setFlag(meta, DropFlag.DEATH);
                setExpiryTimestamp(meta, System.currentTimeMillis() + getMillisecondsUntilExpiry(rarity));
            });
        }
    }

    /**
     * When an item spawns into the world, check if it is owned by someone
     * and transfer the PDC value over to the item entity
     * Also add rarity glow to the item
     *
     * @param event
     */
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    private void __onItemSpawn(ItemSpawnEvent event) {

        // Set the rarity glow of the item
        event.getEntity().setGlowing(true);
        ItemStack item = event.getEntity().getItemStack();
        ItemRarity rarity = SMPRPG.getService(ItemService.class).getBlueprint(item).getRarity(item);
        getTeam(rarity).addEntity(event.getEntity());

        // Set who owns the item based on previous tag setting
        UUID owner = getOwner(event.getEntity().getItemStack());

        // No owner means we do nothing
        if (owner == null)
            return;

        // Tag the end of the display name to display the owner's name
        Player p = Bukkit.getPlayer(owner);
        Component name = event.getEntity().customName();
        if (p == null || name == null)
            return;

        // Transfer ownership to the item entity, add their name to it, and make it unbreakable
        event.getEntity().setOwner(owner);
        event.getEntity().customName(name.append(ComponentUtils.create(" (" + p.getName() + ")", NamedTextColor.DARK_GRAY)));
        event.getEntity().setCanMobPickup(false);
        event.getEntity().setInvulnerable(true);

        // Items expire when we tell them to, so let bukkit never decide for us
        event.getEntity().setUnlimitedLifetime(true);
        setExpiryTimestamp(event.getEntity(), getExpiryTimestamp(event.getEntity().getItemStack()));
        setOwner(event.getEntity(), p);

        // If this is a drop and the rarity is above rare, add the firework task
        if (getFlag(item).equals(DropFlag.LOOT) && rarity.ordinal() >= ItemRarity.RARE.ordinal())
            DropFireworkTask.start(event.getEntity());

        // Now that we successfully transferred ItemStack -> Item entity data, we can clear the flags on the itemstack
        removeAllTags(event.getEntity().getItemStack());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private void __onFireworkDamageFromDrop(EntityDamageEvent event) {

        // We only care about fireworks
        if (!event.getDamageSource().getDamageType().equals(DamageType.FIREWORKS))
            return;

        Firework firework = (Firework) event.getDamageSource().getDirectEntity();
        if (firework == null)
            return;

        // Custom fireworks don't do damage
        if (firework.getEntitySpawnReason() != CreatureSpawnEvent.SpawnReason.CUSTOM)
            return;

        event.setDamage(EntityDamageEvent.DamageModifier.BASE, 0);
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    private void __onEntityHasDrops(EntityDeathEvent event) {

        LeveledEntity<?> entity = SMPRPG.getService(EntityService.class).getEntityInstance(event.getEntity());

        // Clear the drops from the vanilla roll if desired
        if (!entity.hasVanillaDrops())
            event.getDrops().clear();

        // Set experience dropped to the level of the entity if it is not a player
        if (!(entity instanceof LeveledPlayer))
            event.setDroppedExp(entity.getMinecraftExperienceDropped());

        // Drop override?
        if (entity.getItemDrops() == null)
            return;

        // Is there a killer involved?
        Player killer = event.getEntity().getKiller();
        if (killer == null)
            return;

        // Loop through all players that helped kill this entity and did at least some meaningful damage
        Map<Player, Double> involvedPlayers = new HashMap<>();
        involvedPlayers.put(killer, 1.0);  // Ensure killer at least gets credit for the kill

        // If this entity has a damage map go through all participants and add them to the involved players
        if (entity instanceof IDamageTrackable trackable)
            for (var entry : trackable.getDamageTracker().getPlayerDamageTracker().entrySet())
                // Add this player damage to max hp ratio
                involvedPlayers.put(entry.getKey(), Math.min(entry.getValue() / entity.getMaxHp(), 1.0));

        // Loop through every involved player
        for (Map.Entry<Player, Double> entry : involvedPlayers.entrySet()) {

            Player player = entry.getKey();
            Double damageRatio = entry.getValue();

            // If an entity does at least some % damage to an entity, they should get full credit for drops
            damageRatio /= entity.getDamageRatioRequirement();
            damageRatio = Math.min(damageRatio, 1.0);

            // Now test for coins
            // Some chance to add more money
            if (Math.random() < .2)
                event.getDrops().addAll(ItemUtil.getOptimalCoinStacks(SMPRPG.getService(ItemService.class), (int) (entity.getLevel() * (Math.random() * 3))));

            // Loop through all the droppable items from the entity
            for (var drop : entity.getItemDrops()) {

                List<ItemStack> allInvolvedPlayersDrops = new ArrayList<>();

                // Test for items to drop
                Collection<ItemStack> roll = drop.roll(player, player.getInventory().getItemInMainHand(), damageRatio);
                if (roll != null)
                    allInvolvedPlayersDrops.addAll(roll);

                // If we didn't roll anything skip
                if (allInvolvedPlayersDrops.isEmpty())
                    continue;

                // Tag all the drops as loot drops
                for (ItemStack item : allInvolvedPlayersDrops) {
                    item.editMeta(meta -> {
                        ItemRarity rarity = SMPRPG.getService(ItemService.class).getBlueprint(item).getRarity(item);
                        setOwner(meta, player);
                        setFlag(meta, DropFlag.LOOT);
                        setExpiryTimestamp(meta, System.currentTimeMillis() + getMillisecondsUntilExpiry(rarity));
                    });
                }

                // Extend the list of items
                event.getDrops().addAll(allInvolvedPlayersDrops);
            }


        }

    }

    /*
     * When a player breaks a block and causes items to drop, mark it as loot for the player so they own it.
     */
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    private void __onBlockDroppedItemEvent(BlockDropItemEvent event) {

        // Tag all the drops as loot drops
        for (Item itemEntity : event.getItems()) {
            ItemStack item = itemEntity.getItemStack();
            SMPRPG.getService(ItemService.class).ensureItemStackUpdated(item);
            item.editMeta(meta -> {
                ItemRarity rarity = SMPRPG.getService(ItemService.class).getBlueprint(item).getRarity(item);
                setOwner(meta, event.getPlayer());
                setFlag(meta, DropFlag.LOOT);
                setExpiryTimestamp(meta, System.currentTimeMillis() + getMillisecondsUntilExpiry(rarity));
            });
            itemEntity.setItemStack(item);
        }

    }

    @EventHandler
    private void __onRareDropObtained(CustomChancedItemDropSuccessEvent event) {

        SMPItemBlueprint blueprint = SMPRPG.getService(ItemService.class).getBlueprint(event.getItem());
        ItemRarity rarityOfDrop = blueprint.getRarity(event.getItem());
        Component prefix = ComponentUtils.alert(
                ComponentUtils.create(rarityOfDrop.name() + " DROP!!! ", rarityOfDrop.color, TextDecoration.BOLD),
                NamedTextColor.YELLOW
        );

        Component player = SMPRPG.getService(ChatService.class).getPlayerDisplay(event.getPlayer());
        Component item = event.getItem().displayName().hoverEvent(event.getItem().asHoverEvent());
        Component suffix = ComponentUtils.create(" found ").append(item).append(ComponentUtils.create(" from ")).append(event.getSource().getAsComponent()).append(ComponentUtils.create("!"));
        Component chance = ComponentUtils.create(" (" + event.getFormattedChance() + ")", NamedTextColor.DARK_GRAY);
        boolean broadcastServer = rarityOfDrop.ordinal() >= ItemRarity.LEGENDARY.ordinal();
        if (event.getChance() < rarityOfDrop.ordinal() * rarityOfDrop.ordinal() / 100.0)
            broadcastServer = true;

        // We have 3 levels of "rare drop" obtaining based on the chance.
        // If the drop is worth broadcasting to the entire server...
        if (broadcastServer) {
            Component message = prefix.append(player).append(suffix).append(chance);
            Bukkit.broadcast(message);
            for (Player p : Bukkit.getOnlinePlayers())
                p.playSound(p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1, 1);
            return;
        }

        boolean tellPlayer = rarityOfDrop.ordinal() >= ItemRarity.RARE.ordinal();
        if (event.getChance() < rarityOfDrop.ordinal() * rarityOfDrop.ordinal() / 25.0)
            tellPlayer = true;

        if (!tellPlayer)
            return;

        // Just show the message to the player since it's not THAT crazy
        Component message = prefix.append(ComponentUtils.create("You")).append(suffix).append(chance);
        event.getPlayer().sendMessage(message);
        event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ENTITY_CHICKEN_EGG, 1, 1);
    }

}
