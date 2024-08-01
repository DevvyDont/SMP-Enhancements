package xyz.devvydont.smprpg.entity;

import com.destroystokyo.paper.event.entity.EntityAddToWorldEvent;
import com.destroystokyo.paper.event.entity.EntityRemoveFromWorldEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDismountEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.joml.Vector3f;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.entity.base.LeveledEntity;
import xyz.devvydont.smprpg.items.interfaces.Attributeable;
import xyz.devvydont.smprpg.items.base.SMPItemBlueprint;
import xyz.devvydont.smprpg.skills.SkillInstance;
import xyz.devvydont.smprpg.skills.SkillType;
import xyz.devvydont.smprpg.util.formatting.ChatUtil;
import xyz.devvydont.smprpg.util.formatting.PlayerChatInformation;
import xyz.devvydont.smprpg.util.formatting.Symbols;
import xyz.devvydont.smprpg.util.world.TransformationUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class LeveledPlayer extends LeveledEntity implements Listener {

    private TextDisplay info;

    private int seed;

    // Used as a shortcut for skill modification
    private final SkillInstance combatSkill;
    private final SkillInstance miningSkill;
    private final SkillInstance fishingSkill;
    private final SkillInstance farmingSkill;
    private final SkillInstance woodcuttingSkill;
    private final SkillInstance magicSkill;

    public LeveledPlayer(SMPRPG plugin, Player entity) {
        super(plugin, entity);

        // Skill shortcuts
        this.combatSkill = plugin.getSkillService().getNewSkillInstance(entity, SkillType.COMBAT);
        this.miningSkill = plugin.getSkillService().getNewSkillInstance(entity, SkillType.MINING);
        this.fishingSkill = plugin.getSkillService().getNewSkillInstance(entity, SkillType.FISHING);
        this.farmingSkill = plugin.getSkillService().getNewSkillInstance(entity, SkillType.FARMING);
        this.woodcuttingSkill = plugin.getSkillService().getNewSkillInstance(entity, SkillType.WOODCUTTING);
        this.magicSkill = plugin.getSkillService().getNewSkillInstance(entity, SkillType.MAGIC);

        shuffleSeed();
    }


    /**
     * Used for various random elements, such as the enchanting table
     *
     * @return
     */
    public int getSeed() {
        return seed;
    }

    public void shuffleSeed() {
        seed = new Random().nextInt();
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
    public String getClassKey() {
        return "player";
    }

    @Override
    public EntityType getDefaultEntityType() {
        return EntityType.PLAYER;
    }

    @Override
    public String getDefaultName() {
        return entity.getName();
    }

    @Override
    public int getDefaultLevel() {
        return getLevel();
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

            SMPItemBlueprint blueprint = plugin.getItemService().getBlueprint(item);
            if (!(blueprint instanceof Attributeable attributeable))
                continue;

            total += attributeable.getPowerRating();
            factor += 1;
        }

        // The factor cannot be any less than all the skills we have + 4 pieces of armor (exclude holding nothing in our hands)
        factor = Math.max(factor, getSkills().size() + 4);
        return (int) (total / factor);
    }

    /**
     * The amount of base health a player has
     *
     * @return
     */
    public int getBaseHealth() {
        return 100;
    }

    /**
     * The amount of hearts to render for the player, certain aspects can increase this
     *
     * @return
     */
    public int getHealthScale() {

        // When someone achieves max HP of 1000, they should have 3 rows of hearts (displays as 60 hp)
        // When someone is at starting HP of 100, they should only have 5 hearts (displays as 10 hp)

        // If we are under 200 HP, we achieve 1 scale point for every 10 HP (200HP = 20 scale points)
        int scale = 10;
        if (getMaxHp() < 200)
            scale = (int) (getMaxHp() / 10);

        // If we are under 500 HP, we achieve 1 scale point for every 15 HP (500HP = 40 scale points)
        else if (getMaxHp() < 500)
            scale = (int) (20 + ((getMaxHp() - 200) / 15));

        // If we are at anything else, we achieve 1 scale point for every 25 HP (1000HP = 60 scale points)
        else
            scale = (int) (40 + ((getMaxHp() - 500) / 25));

        // Never allow half hearts to show, only full hearts
        if (scale % 2 != 0)
            scale++;
        return Math.min(Math.max(10, scale), 60);
    }

    /**
     * The current value of HP this player's half of heart is HP wise
     * This amount of HP is used a lot for damage such as fall damage, burning, and regeneration values
     *
     * @return
     */
    @Override
    public double getHalfHeartValue() {
        return getMaxHp() / getHealthScale();
    }

    @Override
    public void updateAttributes() {

        // Update max health to 100 while maintaining their current HP
        double percent = getHealthPercentage();
        updateBaseAttribute(Attribute.GENERIC_MAX_HEALTH, getBaseHealth());
        // Set knockback resistance attributes so that percentages work correctly
        updateBaseAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE, .01);
        updateBaseAttribute(Attribute.GENERIC_EXPLOSION_KNOCKBACK_RESISTANCE, .01);
        updateBaseAttribute(Attribute.PLAYER_SWEEPING_DAMAGE_RATIO, .01);
        setHealthPercentage(percent);

        // Make sure we aren't overloading their UI with hearts
        getPlayer().setHealthScale(getHealthScale());
        getPlayer().setHealthScaled(true);
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
        PlayerChatInformation chatInformation = plugin.getChatService().getPlayerInfo(getPlayer());
        Component newPrefix = ChatUtil.getBracketedPowerComponent(getLevel())
                .append(Component.text(" " + ChatColor.translateAlternateColorCodes('&', chatInformation.prefix())));
        team.prefix(newPrefix);
        team.suffix(Component.text(ChatColor.translateAlternateColorCodes('&', chatInformation.suffix())));
    }

    @Override
    public void cleanup() {
        super.cleanup();
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

    public Player getPlayer() {
        return (Player) entity;
    }

    @Override
    public double calculateBaseAttackDamage() {
        return 1.0;
    }

    @Override
    public boolean hasVanillaDrops() {
        return true;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityAddToWorld(EntityAddToWorldEvent event) {

        if (event.getEntity().equals(entity))
            updateNametag();

    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent event) {

        if (!event.getPlayer().equals(getPlayer()))
            return;

        updateNametag();
    }

}
