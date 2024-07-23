package xyz.devvydont.smprpg.entity;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.entity.base.LeveledEntity;
import xyz.devvydont.smprpg.items.interfaces.Attributeable;
import xyz.devvydont.smprpg.items.base.SMPItemBlueprint;
import xyz.devvydont.smprpg.skills.SkillInstance;
import xyz.devvydont.smprpg.skills.SkillType;
import xyz.devvydont.smprpg.util.formatting.Symbols;

import java.util.Collection;
import java.util.List;
import java.util.Random;

public class LeveledPlayer extends LeveledEntity {

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

        plugin.getSkillService().syncSkillAttributes(this);

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

        // When someone achieves max HP of 1000, they should have 4 rows of hearts (displays as 80 hp)
        // When someone is at starting HP of 100, they should only have 5 hearts (displays as 10 hp)

        // Divide by 10 and round up to the nearest whole number, and if the number isn't even increase it by another
        // 1 so that we don't ever display half hearts as max hp
        int scale = 10;

        // For every 10 HP we have above 100, we add another health scale point
        scale += (int) (Math.max(0, getMaxHp()-100) / 10.0);
        if (scale % 2 != 0)
            scale++;
        return Math.min(Math.max(10, scale), 80);
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
        setHealthPercentage(percent);

        // Make sure we aren't overloading their UI with hearts
        getPlayer().setHealthScale(getHealthScale());
        getPlayer().setHealthScaled(true);
    }

    @Override
    public void updateNametag() {

        // Players will use the text display thingies
        if (!hasSecondaryNametag())
            spawnSecondaryNametag();

        int hp;
        if (getHp() <= 0)
            hp = 0;
        else if (getHp() > 0 && getHp() < 1)
            hp = 1;
        else
            hp = (int)getHp();
        int maxHp = (int)Math.round(getMaxHp());

        TextColor hpTextColor = getChatColorFromHealth(getHp(), maxHp);
        info.text(Component.text("[").color(NamedTextColor.GRAY)
                .append(Component.text(Symbols.POWER + getLevel()).color(NamedTextColor.YELLOW))
                .append(Component.text("]").color(NamedTextColor.GRAY))
                .append(Component.text(" " + hp).color(hpTextColor))
                .append(Component.text("/").color(NamedTextColor.GRAY))
                .append(Component.text(maxHp).color(NamedTextColor.GREEN))
                .append(Component.text(Symbols.HEART).color(NamedTextColor.DARK_RED)));
    }

    @Override
    public void cleanup() {
        super.cleanup();
        if (hasSecondaryNametag())
            killSecondaryNametag();
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

    public TextDisplay spawnSecondaryNametag() {

        // Kill the old one if it exists
        killSecondaryNametag();

        // Spawn a new TextDisplay and mount it on the player
        info = entity.getWorld().spawn(entity.getLocation(), TextDisplay.class, e -> {
            e.setSeeThrough(true);
            e.setPersistent(false);
            e.setBillboard(Display.Billboard.CENTER);
        });
        entity.addPassenger(info);
        return info;
    }

    public void killSecondaryNametag() {
        if (!hasSecondaryNametag()) {
            info = null;
            return;
        }

        info.remove();
        info = null;
    }

    public boolean hasSecondaryNametag() {
        return info != null && info.isValid();
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
}
