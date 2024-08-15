package xyz.devvydont.smprpg.services;

import io.papermc.paper.persistence.PersistentDataViewHolder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Enemy;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.entity.Warden;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.Nullable;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.entity.base.EnemyEntity;
import xyz.devvydont.smprpg.entity.base.LeveledEntity;
import xyz.devvydont.smprpg.events.CustomChancedItemDropSuccessEvent;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemRarity;
import xyz.devvydont.smprpg.items.base.SMPItemBlueprint;
import xyz.devvydont.smprpg.util.crafting.ItemUtil;
import xyz.devvydont.smprpg.util.formatting.ComponentUtil;
import xyz.devvydont.smprpg.util.items.ChancedItemDrop;
import xyz.devvydont.smprpg.util.items.DropFireworkTask;
import xyz.devvydont.smprpg.util.items.LootDrop;
import xyz.devvydont.smprpg.util.persistence.UUIDPersistentDataType;

import java.util.*;

/**
 * In charge of managing item drops in the world.
 * - When a player dies, their items need to be protected and can only be picked up by them
 * - When an entity drops items, they drop items for whoever helped kill it so everyone gets items
 */
public class DropsService implements BaseService, Listener {

    public enum DropFlag {
        NULL,
        DEATH,
        LOOT
        ;

        public static DropFlag fromInt(int flag) {
            if (flag < 0 || flag > values().length)
                return NULL;
            return DropFlag.values()[flag];
        }
    }

    private final SMPRPG plugin;

    // The owner tag for a drop, drops cannot be picked up by players unless they own it
    private final NamespacedKey OWNER_UUID_KEY;

    // The flag that an owner tag drop has. 1 = Death, 2 = Drop
    private final NamespacedKey DROP_FLAG_KEY;

    public static final Map<ItemRarity, Team> rarityToTeam = new HashMap<>();

    public DropsService(SMPRPG plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.OWNER_UUID_KEY = new NamespacedKey(plugin, "drop-owner-uuid");
        this.DROP_FLAG_KEY = new NamespacedKey(plugin, "drop-flag");

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
        holder.getPersistentDataContainer().set(getItemOwnerKey(), UUIDPersistentDataType.INSTANCE, player.getUniqueId());
    }

    /**
     * Untags this item as being owned by someone
     *
     * @param holder
     */
    public void removeOwner(PersistentDataHolder holder) {
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

    public void removeFlag(PersistentDataHolder holder) {
        holder.getPersistentDataContainer().remove(getDropFlagKey());
    }

    public void removeAllTags(PersistentDataHolder holder) {
        removeOwner(holder);
        removeFlag(holder);
    }

    public void removeAllTags(ItemStack itemStack) {
        itemStack.editMeta(this::removeAllTags);
    }

    @Override
    public boolean setup() {
        return true;
    }

    @Override
    public void cleanup() {

    }

    @Override
    public boolean required() {
        return true;
    }

    /**
     * When a player dies, mark all their items as being owned by them
     *
     * @param event
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerDeath(PlayerDeathEvent event) {

        // Go through all the drops on the player and tag it as being owned
        for (ItemStack drop : event.getDrops()) {
            drop.editMeta(meta -> {
                setOwner(meta, event.getPlayer());
                setFlag(meta, DropFlag.DEATH);
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
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onItemSpawn(ItemSpawnEvent event) {

        // Set the rarity glow of the item
        event.getEntity().setGlowing(true);
        ItemStack item = event.getEntity().getItemStack();
        ItemRarity rarity = plugin.getItemService().getBlueprint(item).getRarity(item);
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
        event.getEntity().customName(name.append(ComponentUtil.getColoredComponent(" (" + p.getName() + ")", NamedTextColor.DARK_GRAY)));
        event.getEntity().setCanMobPickup(false);
        event.getEntity().setUnlimitedLifetime(true);
        event.getEntity().setInvulnerable(true);

        // If this is a drop and the rarity is above rare, add the firework task
        if (getFlag(item).equals(DropFlag.LOOT) && rarity.ordinal() >= ItemRarity.RARE.ordinal())
            DropFireworkTask.start(event.getEntity());

        // Now that we successfully transferred ItemStack -> Item entity data, we can clear the flags on the itemstack
        removeAllTags(event.getEntity().getItemStack());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onFireworkDamageFromDrop(EntityDamageEvent event) {

        // We only care about fireworks
        if (!event.getDamageSource().getDamageType().equals(DamageType.FIREWORKS))
            return;

        Firework firework = (Firework) event.getDamageSource().getDirectEntity();
        if (firework == null)
            return;

        // Custom fireworks don't do damage
        if (firework.getEntitySpawnReason() != CreatureSpawnEvent.SpawnReason.CUSTOM)
            return;

        event.setDamage(0);
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onDropCoins(EntityDeathEvent event) {

        // Add some coins to the drop depending on the level of entity that was killed if the mob was hostile
        if (!(event.getEntity() instanceof Enemy))
            return;

        // Only a select amount of mobs will drop coins
        if (Math.random() < .25)
            return;

        LeveledEntity leveled = SMPRPG.getInstance().getEntityService().getEntityInstance(event.getEntity());

        // Some chance to add more money
        if (Math.random() < .33)
            ItemUtil.getOptimalCoinStack(plugin.getItemService(), leveled.getLevel());

    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onEntityHasDrops(EntityDeathEvent event) {

        LeveledEntity entity = plugin.getEntityService().getEntityInstance(event.getEntity());

        // Clear the drops from the vanilla roll if desired
        if (!entity.hasVanillaDrops())
            event.getDrops().clear();

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
        if (entity instanceof EnemyEntity enemy)
            for (Map.Entry<Player, Integer> entry : enemy.getPlayerDamageTracker().entrySet())
                // Add this player damage to max hp ratio
                involvedPlayers.put(entry.getKey(), Math.min(entry.getValue() / enemy.getMaxHp(), 1.0));

        // Loop through every involved player
        for (Map.Entry<Player, Double> entry : involvedPlayers.entrySet()) {

            Player player = entry.getKey();
            Double damageRatio = entry.getValue();

            // If an entity does at least some % damage to an entity, they should get full credit for drops
            damageRatio /= entity.getDamageRatioRequirement();
            damageRatio = Math.min(damageRatio, 1.0);

            // Loop through all the droppable items from the entity
            for (LootDrop drop : entity.getItemDrops()) {

                // Test for items to drop
                Collection<ItemStack> roll = drop.roll(player, player.getInventory().getItemInMainHand(), damageRatio);
                // If we didn't roll anything skip
                if (roll == null || roll.isEmpty())
                    continue;

                // Tag all the drops as loot drops
                for (ItemStack item : roll) {
                    item.editMeta(meta -> {
                        setOwner(meta, player);
                        setFlag(meta, DropFlag.LOOT);
                    });
                }

                // Extend the list of items
                event.getDrops().addAll(roll);
            }


        }

    }

    @EventHandler
    public void onRareDropObtained(CustomChancedItemDropSuccessEvent event) {

        SMPItemBlueprint blueprint = plugin.getItemService().getBlueprint(event.getItem());
        ItemRarity rarityOfDrop = blueprint.getRarity(event.getItem());
        Component prefix = ComponentUtil.getAlertMessage(Component.text(rarityOfDrop.name() + " DROP!!! ").decorate(TextDecoration.BOLD), NamedTextColor.YELLOW, rarityOfDrop.color);
        Component player = ComponentUtil.getColoredComponent(event.getPlayer().getName(), NamedTextColor.AQUA);
        Component item = event.getItem().displayName().hoverEvent(event.getItem().asHoverEvent());
        Component suffix = ComponentUtil.getDefaultText(" found ").append(item).append(ComponentUtil.getDefaultText(" from ")).append(event.getSource().getAsComponent()).append(ComponentUtil.getDefaultText("!"));
        Component chance = ComponentUtil.getColoredComponent(" (" + event.getFormattedChance() + ")", NamedTextColor.DARK_GRAY);
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
        Component message = prefix.append(ComponentUtil.getDefaultText("You")).append(suffix).append(chance);
        event.getPlayer().sendMessage(message);
        event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ENTITY_CHICKEN_EGG, 1, 1);
    }

}
