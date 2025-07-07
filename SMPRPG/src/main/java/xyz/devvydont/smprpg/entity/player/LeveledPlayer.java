package xyz.devvydont.smprpg.entity.player;

import com.destroystokyo.paper.event.entity.EntityAddToWorldEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.attribute.AttributeWrapper;
import xyz.devvydont.smprpg.entity.base.LeveledEntity;
import xyz.devvydont.smprpg.entity.components.EntityConfiguration;
import xyz.devvydont.smprpg.items.interfaces.IAttributeItem;
import xyz.devvydont.smprpg.services.*;
import xyz.devvydont.smprpg.skills.SkillInstance;
import xyz.devvydont.smprpg.skills.SkillType;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

import java.util.Collection;
import java.util.List;

public class LeveledPlayer extends LeveledEntity<Player> implements Listener {

    public static int MANA_REGENERATE_FREQUENCY = 10;

    // Used as a shortcut for skill modification
    private final SkillInstance combatSkill;
    private final SkillInstance miningSkill;
    private final SkillInstance fishingSkill;
    private final SkillInstance farmingSkill;
    private final SkillInstance woodcuttingSkill;
    private final SkillInstance magicSkill;

    private BukkitTask _manaRegenerateTask;
    private double _mana = 0;

    public LeveledPlayer(SMPRPG plugin, Player entity) {
        super(entity);

        var skillService = SMPRPG.getService(SkillService.class);
        // Skill shortcuts
        this.combatSkill = skillService.getNewSkillInstance(entity, SkillType.COMBAT);
        this.miningSkill = skillService.getNewSkillInstance(entity, SkillType.MINING);
        this.fishingSkill = skillService.getNewSkillInstance(entity, SkillType.FISHING);
        this.farmingSkill = skillService.getNewSkillInstance(entity, SkillType.FARMING);
        this.woodcuttingSkill = skillService.getNewSkillInstance(entity, SkillType.WOODCUTTING);
        this.magicSkill = skillService.getNewSkillInstance(entity, SkillType.MAGIC);

        this._config = EntityConfiguration.PLAYER;
    }

    @Override
    public void setup() {
        super.setup();
        startManaTask();
    }

    public void regenerateMana() {
        var max = getMaxMana();
        this._mana += getMaxMana() / 100;
        this._mana = Math.min(Math.max(0, _mana), max);
    }

    public double getMana() {
        return _mana;
    }

    public double getMaxMana() {
        var mana = SMPRPG.getService(AttributeService.class).getOrCreateAttribute(_entity, AttributeWrapper.INTELLIGENCE);
        return mana.getValue();
    }

    public void useMana(int cost) {
        this._mana -= cost;
        this._mana = Math.max(0, this._mana);
    }

    public ProfileDifficulty getDifficulty() {
        return SMPRPG.getService(DifficultyService.class).getDifficulty(getPlayer());
    }

    public SkillInstance getCombatSkill() {
        return combatSkill;
    }

    public SkillInstance getMiningSkill() {
        return miningSkill;
    }

    public SkillInstance getFishingSkill() {
        return fishingSkill;
    }

    public SkillInstance getFarmingSkill() {
        return farmingSkill;
    }

    public SkillInstance getWoodcuttingSkill() {
        return woodcuttingSkill;
    }

    public SkillInstance getMagicSkill() {
        return magicSkill;
    }

    public Collection<SkillInstance> getSkills() {
        return List.of(
                getCombatSkill(),
                getMiningSkill(),
                getFishingSkill(),
                getFarmingSkill(),
                getWoodcuttingSkill(),
                getMagicSkill()
        );
    }

    public double getAverageSkillLevel() {
        double sum = 0;
        for (SkillInstance skill : getSkills())
            sum += skill.getLevel();
        return sum / getSkills().size();
    }

    @Override
    public int getInvincibilityTicks() {
        return 10;
    }

    @Override
    public String getClassKey() {
        return "player";
    }

    @Override
    public EntityType getDefaultEntityType() {
        return EntityType.PLAYER;
    }

    @Override
    public String getEntityName() {
        return _entity.getName();
    }

    @Override
    public int getLevel() {

        // If we haven't fully initialized yet we cannot get a good calculation yet
        if (getCombatSkill() == null)
            return 0;

        // Using average level of gear and skills on this player determine how strong they are
        int factor = 0;
        double total = 0;

        // First skills
        for (SkillInstance skill : getSkills())
            total += skill.getLevel();
        factor += getSkills().size();

        // Now gear
        Player p = getPlayer();
        ItemStack[] gear = {p.getInventory().getHelmet(), p.getInventory().getChestplate(),
                p.getInventory().getLeggings(), p.getInventory().getBoots(), p.getInventory().getItemInMainHand(),
                p.getInventory().getItemInOffHand()};

        for (ItemStack item : gear) {

            if (item == null || item.getType().equals(Material.AIR))
                continue;

            var blueprint = SMPRPG.getService(ItemService.class).getBlueprint(item);
            if (!(blueprint instanceof IAttributeItem attributable))
                continue;

            total += attributable.getPowerRating();
            factor += 1;
        }

        // The factor cannot be any less than all the skills we have + 4 pieces of armor (exclude holding nothing in our hands)
        factor = Math.max(factor, getSkills().size() + 4);
        return (int) (total / factor);
    }

    /**
     * Calculates the number of half-hearts to render for the player, based on max HP.
     * Display rules:
     * - 100 HP = 1 full row = 20 half-hearts
     * - 1000 HP = 2 full rows = 40 half-hearts
     * - 2500 HP = 3 full rows = 60 half-hearts (cap)
     * The scale is calculated in tiers:
     * - Below 100 HP: 1 half-heart per 5 HP
     * - Between 100–1000 HP: 1 half-heart per 40 HP above 200 (starts from 20)
     * - Above 1000 HP: 1 half-heart per 75 HP above 1000 (starts from 40)
     * Always rounded up to the nearest even number (Minecraft only displays full hearts).
     * @return The health scale (number of half-hearts) to display to the client (min 2, max 60).
     */
    public int getHealthScale() {
        float hp = (float) getMaxHp();
        int scale;

        if (hp < 100) {
            scale = Math.round(hp / 5f); // 20 at 100 HP
        } else if (hp < 1000) {
            scale = 20 + Math.round((hp - 100) / 40f); // 40 at 1000 HP
        } else {
            scale = 40 + Math.round((hp - 1000) / 75f); // 60 at 2500 HP
        }

        // Round up to nearest even number to avoid half-hearts
        if (scale % 2 != 0)
            scale++;

        return Math.min(Math.max(2, scale), 60);
    }

    /**
     * The current value of HP this player's half of heart is HP wise
     * This amount of HP is used a lot for damage such as fall damage, burning, and regeneration values
     * @return The amount of HP this player's half heart is currently worth.
     */
    @Override
    public double getHalfHeartValue() {
        return getMaxHp() / getHealthScale();
    }

    @Override
    public void updateAttributes() {

        // Update max health to 100 while maintaining their current HP
        double percent = getHealthPercentage();
        updateBaseAttribute(AttributeWrapper.HEALTH, this._config.getBaseHealth());

        if (percent > .01)
            setHealthPercentage(percent);

        // Set misc default base attributes that players should have
        updateBaseAttribute(AttributeWrapper.STRENGTH, this._config.getBaseDamage());
        updateBaseAttribute(AttributeWrapper.REGENERATION, getDifficulty() == ProfileDifficulty.HARD ? 50 : 100);
        updateBaseAttribute(AttributeWrapper.INTELLIGENCE, getDifficulty() == ProfileDifficulty.HARD ? 50 : 100);
        updateBaseAttribute(AttributeWrapper.LUCK, 100);
        updateBaseAttribute(AttributeWrapper.DEFENSE, 0);
        updateBaseAttribute(AttributeWrapper.CRITICAL_CHANCE, 0);
        updateBaseAttribute(AttributeWrapper.CRITICAL_DAMAGE, getDifficulty() == ProfileDifficulty.HARD ? 25 : 50);

        updateBaseAttribute(AttributeWrapper.MINING_FORTUNE, 0);
        updateBaseAttribute(AttributeWrapper.FARMING_FORTUNE, 0);
        updateBaseAttribute(AttributeWrapper.WOODCUTTING_FORTUNE, 0);

        updateBaseAttribute(AttributeWrapper.FISHING_RATING, 0);
        updateBaseAttribute(AttributeWrapper.FISHING_CREATURE_CHANCE, 0);
        updateBaseAttribute(AttributeWrapper.FISHING_TREASURE_CHANCE, 0);



        // Make sure we aren't overloading their UI with hearts
        getPlayer().setHealthScale(getHealthScale());
        getPlayer().setHealthScaled(true);

        // Make them only start with a fraction of their mana, to prevent abusing mana restoration from re-logging.
        this._mana = getMaxMana() / 5;
    }

    private Team getNametagTeam() {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        Player player = getPlayer();
        String teamKey = player.getUniqueId().toString();
        Team team = scoreboard.getTeam(teamKey);
        if (team == null)
            team = scoreboard.registerNewTeam(teamKey);
        team.addPlayer(player);
        return team;
    }

    @Override
    public void updateNametag() {
        Team team = getNametagTeam();
        var chatInformation = SMPRPG.getService(ChatService.class).getPlayerInfo(getPlayer());
        Component newPrefix = ComponentUtils.powerLevelPrefix(getLevel()).append(ComponentUtils.SPACE);
        team.prefix(newPrefix);
        if (!chatInformation.prefix().isEmpty())
            team.suffix(Component.text(" " + chatInformation.prefix().stripTrailing(), NamedTextColor.WHITE));
        else
            team.suffix(null);
        team.color(NamedTextColor.nearestTo(getDifficulty().Color));
    }

    @Override
    public void cleanup() {
        super.cleanup();
        killManaTask();
    }

    @Override
    public void setLevel(int level) {
        // Does nothing
    }

    @Override
    public void resetLevel() {
        // Does nothing
    }

    @Override
    public void dimNametag() {
        // Does nothing
    }

    @Override
    public void brightenNametag() {
        // does nothing
    }

    @Override
    public EntityConfiguration getDefaultConfiguration() {
        var difficulty = getDifficulty();
        return EntityConfiguration.builder()
                .withLevel(0)
                .withHealth(difficulty == ProfileDifficulty.HARD ? 50 : 100)
                .withDamage(difficulty == ProfileDifficulty.HARD ? 2 : 5)
                .build();
    }

    public Player getPlayer() {
        return _entity;
    }

    @Override
    public boolean hasVanillaDrops() {
        return true;
    }

    private void killManaTask() {
        if (_manaRegenerateTask != null)
            _manaRegenerateTask.cancel();
        _manaRegenerateTask = null;
    }

    private void startManaTask() {
        killManaTask();
        _manaRegenerateTask = Bukkit.getScheduler().runTaskTimer(SMPRPG.getInstance(), this::regenerateMana, 0, MANA_REGENERATE_FREQUENCY);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    private void __onEntityAddToWorld(EntityAddToWorldEvent event) {

        if (event.getEntity().equals(_entity))
            updateNametag();

    }

    @EventHandler(priority = EventPriority.MONITOR)
    private void __onJoin(PlayerJoinEvent event) {

        if (!event.getPlayer().equals(getPlayer()))
            return;

        updateNametag();
    }
}
