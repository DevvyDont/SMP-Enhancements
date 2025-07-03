package xyz.devvydont.smprpg.services;

import com.destroystokyo.paper.event.player.PlayerPickupExperienceEvent;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.attribute.AttributeWrapper;
import xyz.devvydont.smprpg.entity.player.ProfileDifficulty;
import xyz.devvydont.smprpg.events.skills.SkillExperienceGainEvent;
import xyz.devvydont.smprpg.gui.player.MenuDifficultyChooser;

public class DifficultyService implements IService, Listener {

    public final static NamespacedKey DIFFICULTY_MODIFIER_KEY = new NamespacedKey(SMPRPG.getInstance(), "difficulty_modifier");

    /**
     * Given a difficulty, determine the skill experience multiplier.
     * @param difficulty The difficulty a player is on.
     * @return The multiplier of skill experience they gain.
     */
    public static float getSkillExperienceMultiplier(ProfileDifficulty difficulty) {
        return switch (difficulty) {
            case EASY -> 1.25f;
            case HARD -> 0.75f;
            default -> 1.0f;
        };
    }

    /**
     * Given a difficulty, determine the incoming damage multiplier.
     * @param difficulty The difficulty a player is on.
     * @return The multiplier of incoming damage they receive.
     */
    public static float getDamageMultiplier(ProfileDifficulty difficulty) {
        return switch (difficulty) {
            case EASY -> .5f;
            case HARD -> 2f;
            default -> 1.0f;
        };
    }

    /**
     * Given a difficulty, determine the drop chance multiplier.
     * @param difficulty The difficulty a player is on.
     * @return The multiplier of drop rate chance they receive.
     */
    public static float getDropRateChanceMultiplier(ProfileDifficulty difficulty) {
        return switch (difficulty) {
            case EASY -> .5f;
            case HARD -> 2f;
            default -> 1.0f;
        };
    }

    private final NamespacedKey difficultyKey;

    public DifficultyService() {
        this.difficultyKey = new NamespacedKey(SMPRPG.getInstance(), "profile_type");
    }

    @Override
    public boolean setup() {
        SMPRPG.getInstance().getServer().getPluginManager().registerEvents(this, SMPRPG.getInstance());
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
     * Queries for the difficulty that this player is currently playing on.
     * @param player The player to check difficulty for.
     * @return The ProfileDifficulty enum that they are currently set to.
     */
    @NotNull
    public ProfileDifficulty getDifficulty(Player player) {
        return player.getPersistentDataContainer().getOrDefault(difficultyKey, ProfileDifficulty.ADAPTER, ProfileDifficulty.NOT_CHOSEN);
    }

    @NotNull
    public ProfileDifficulty getDifficulty(OfflinePlayer player) {
        return player.getPersistentDataContainer().getOrDefault(difficultyKey, ProfileDifficulty.ADAPTER, ProfileDifficulty.NOT_CHOSEN);
    }

    /**
     * Sets the difficulty of a player. Also handles any necessary side effects that a difficulty change would require.
     * @param player The player to set a difficulty for.
     * @param difficulty The difficulty to set.
     */
    public void setDifficulty(Player player, ProfileDifficulty difficulty) {

        // Don't do anything if the difficulty is not changing.
        var oldDifficulty = getDifficulty(player);
        if (oldDifficulty.equals(difficulty))
            return;

        // Store the difficulty on the player.
        player.getPersistentDataContainer().set(difficultyKey, ProfileDifficulty.ADAPTER, difficulty);
        var playerWrapper = SMPRPG.getInstance().getEntityService().getPlayerInstance(player);

        // Set the state of the player necessary for this difficulty.
        // For now, we just have to make sure their stats are sanity checked, as everything else is dynamically handled.
        applyDifficultyModifiers(player, difficulty);
        SMPRPG.getInstance().getSkillService().syncSkillAttributes(playerWrapper);
        playerWrapper.setConfiguration(playerWrapper.getDefaultConfiguration());
    }

    /**
     * Removes all difficulty related attribute modifiers, and adds new ones based on the difficulty they are on.
     * @param player The player to tweak attributes of.
     * @param difficulty The difficulty they want modifiers for.
     */
    public void applyDifficultyModifiers(Player player, ProfileDifficulty difficulty) {

        // As of now, the only global difficulty modifier is luck. First remove it.
        var luck = AttributeService.getInstance().getOrCreateAttribute(player, AttributeWrapper.LUCK);
        luck.removeModifier(DIFFICULTY_MODIFIER_KEY);

        // Apply a luck modifier based on the difficulty.
        luck.addModifier(new AttributeModifier(DIFFICULTY_MODIFIER_KEY, getDropRateChanceMultiplier(difficulty), AttributeModifier.Operation.MULTIPLY_SCALAR_1, EquipmentSlotGroup.ANY));
        luck.save(player, AttributeWrapper.LUCK);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void __onPlayerJoin(PlayerJoinEvent event) {

        // When a player joins, we need to make sure they have a difficulty selected so they can play.
        var difficulty = getDifficulty(event.getPlayer());
        if (!difficulty.equals(ProfileDifficulty.NOT_CHOSEN)) {
            applyDifficultyModifiers(event.getPlayer(), difficulty);
            return;
        }
        
        // They haven't chosen! Open the interface.
        var gui = new MenuDifficultyChooser(event.getPlayer());
        gui.openMenu();
        gui.lock();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void __onPlayerEarnSkillExperience(SkillExperienceGainEvent event) {

        // Ignore experience gained from commands, otherwise the skill set command will behave wonky.
        if (event.getSource().equals(SkillExperienceGainEvent.ExperienceSource.COMMANDS))
            return;

        var multiplier = getSkillExperienceMultiplier(getDifficulty(event.getPlayer()));
        event.multiplyExperienceEarned(multiplier);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    private void __onPlayerEarnMinecraftExperience(PlayerPickupExperienceEvent event) {
        if (getDifficulty(event.getPlayer()).equals(ProfileDifficulty.HARD))
            event.getExperienceOrb().setExperience((int) (event.getExperienceOrb().getExperience() * 1.5));
    }
}
