import enums.ArmorPiece;
import enums.ItemRarity;
import enums.StatSource;

import java.util.Scanner;

/**
 * A utility application to aid in the creation and modification of items, stats, and attributes of entities.
 * This code is explicitly a developer utility and does not affect the project in any way.
 */
public class StatCalculator {

    private enum Operation {
        SET_LEVEL("l", "Set the calculator level"),
        CREATE_ARMOR("armor", "Create armor"),
        CREATE_SWORD("sword", "Create sword"),
        CREATE_AXE("axe", "Create axe"),
        DUMP_PLAYER_EXPECTATIONS("player", "Dump player expectations"),
        DUMP_DPS_TABLE("dps", "Dump DPS Table"),
        DUMP_HP_TABLE("hp", "Dump HP/DEF Table"),
        DUMP_SKILLS_TABLE("skills", "Dump Skills Table"),
        QUIT("q", "Quit"),
        ;

        public final String Key;
        public final String Description;

        Operation(String key, String description) {
            Key = key;
            this.Description = description;
        }

        public static Operation get(String key) {
            for (Operation op : Operation.values()) {
                if (op.Key.equalsIgnoreCase(key)) {
                    return op;
                }
            }
            return null;
        }
    }

    /**
     * How many times should an enemy hit a player in order to kill them assuming they are equal in power and
     * are not receiving any sort of health regeneration?
     * Higher values will allow players to survive easier, while lower values will make enemies more lethal.
     */
    private final static double ENEMY_HITS_TO_KILL_PLAYER = 8.0;

    /**
     * How many times should a player have to hit an enemy in order to kill it assuming they are equal in power and
     * keeping up with their stats and all mechanics? These hits will also assume they are not landing critical hits,
     * meaning that this number will most likely *feel* like it's actually set lower, since optimal players will
     * typically maximize the number of crits they are landing.
     */
    private final static double PLAYER_HITS_TO_KILL_ENEMY = 3.75;

    /**
     * A modifier for how much damage should be applied to swords. Since swords have a relatively quick attack speed,
     * full charge hits can happen multiple times a second meaning that DPS thresholds can be hit faster with
     * multiple hits.
     * Increase this to make swords more powerful.
     */
    private final static double SWORD_DPS = 0.75;

    /**
     * A modifier for how much damage should be applied to axes. Since axes have a relatively slower attack speed,
     * full charge hits happen less frequently meaning DPS thresholds are harder to hit.
     * Increase this to make axes more powerful.
     */
    private final static double AXE_DPS = 1.1;

    /**
     * How effective should defense be at mitigating damage? This contributes to a standard diminshing returns formula
     * where higher values mean that defense needs to be HIGHER in order to be more effective, where lower values
     * means that you can have less defense be more effective sooner.
     * A good way to think of this, is that once you hit whatever value is set below in defense, you will achieve
     * 50% damage reduction. When doubled from there, it's then increased to 66% damage reduction. When tripled, 75%...
     *
     * In even simpler math, let's say that K factor is 100. For every 100 defense you get, you are effectively
     * multiplying your health based on how m any times your defense divides into K factor after adding 1.
     * 0 / 100 + 1 = 1x
     * 100 / 100 + 1 = 2x
     * 200 / 100 + 1 = 3x
     * 500 / 100 + 1 = 6x
     * 1500 / 100 + 1 = 16x
     */
    private final static int DEFENSE_K_FACTOR = 100;

    private final static int MAX_LEVEL = 100;
    private final static int STARTING_HEALTH = 100;
    private final static int TARGET_HEALTH = 50000;

    /*
     * The core of how stat scaling should player out mid/late game.
     * A higher number will make the game feel more exponential.
     * (NOTE: This gets out of control FAST)
     */
    private final static double EHP_SCALING_FACTOR = 1.0964782;  // 1.075 results in ~100k, 1.0964782 results in ~1M

    /**
     * The level the calculator is set to run calculations for.
     */
    private int _level = 25;

    /**
     * Calculates what a standard enemy/player's effective health should be at a certain level.
     * Note that this does not factor in defense, so it is assumed that the entity will have 0 defense if they
     * want to use this health value.
     * @param level Level of an entity.
     * @return How much health (EHP) they should have.
     */
    private int calculateExpectedHealth(int level) {
        return (int) Math.ceil(STARTING_HEALTH*Math.pow(EHP_SCALING_FACTOR, level));
    }

    /**
     * Players have a defense stat, so their health won't go as far as enemies. Instead, their health will gradually
     * scale from 100-5000, where defense will then do the rest of the work to bring their EHP up.
     * @param level The level of the player.
     * @return How much health a player should have.
     */
    private int calculateExpectedPlayerHealth(int level) {
        double hpMultiplier = (level * level / 500.0) + 1;
        return (int) Math.ceil(calculateExpectedHealth(level) / hpMultiplier);
    }

    /**
     * Work out how much defense is expected of a player of a certain level.
     * Since HP will linearly scale, we can work out how much defense is required to hit their target EHP.
     * @param level The level of the player.
     * @return How much defense they should have.
     */
    private int calculateExpectedDefense(int level) {
        var multiplier = (double)calculateExpectedPlayerHealth(level) / calculateExpectedHealth(level);

        // Work the defense formula backwards so that we can find defense given multiplier.
        // I won't go into the specifics of the math, but it can be solved using K((1-multi)/multi)
        return Math.toIntExact(Math.round(DEFENSE_K_FACTOR * ((1 - multiplier) / multiplier)));
    }

    /**
     * Calculates how much damage a standard/average enemy should do at a certain level.
     * @param level Level of an enemy.
     * @return How much damage on average they should be doing a second.
     */
    private int calculateEnemyDps(int level) {
        // Since players and enemies are meant to be on the same playing field power wise, we can just divide
        // the entity's health by however many hits it should take to kill. Very simple :p
        return (int) Math.ceil(calculateExpectedHealth(level) / ENEMY_HITS_TO_KILL_PLAYER);
    }

    /**
     * Calculates how much damage a standard player should be doing per second at a certain level.
     * Keep in mind, this is the TOTAL, meaning that all augments of a player should be contributing towards this total
     * when dealing damage. As newer mechanics introduce to a player, the reliance on base stats and gear needs to
     * slowly and gradually decrease, as a measure of preventing power creep.
     * @param level The level of a player.
     * @return
     */
    private int calculatePlayerDps(int level) {
        // Similarly to enemy DPS...
        return (int) Math.ceil(calculateExpectedHealth(level) / PLAYER_HITS_TO_KILL_ENEMY);
    }

    /**
     * Works out what damage multiplier will be applied when an entity has a certain defense rating.
     * Keep in mind this is the multiplier itself, and not the damage reduction (achieved by adding -1).
     * Obviously, higher defense = more damage reduction (with diminishing returns!).
     * @param defense How much defense to use for damage reduction.
     * @return A floating point number from 0-1, representing what to multiply damage by in order to apply resistance.
     */
    private float calculateDamageMultiplierWithDefense(int defense) {
        return (float)DEFENSE_K_FACTOR / (defense + DEFENSE_K_FACTOR);
    }

    /**
     * The inverse of damage defense multiplier. This is simply a different way of looking at the same concept.
     * Use this method if you want to analyze how much damage is resisted.
     * @param defense How much defense to use for damage reduction.
     * @return A floating point number from 0-1, representing what percent damage reduction an entity has with this defense.
     */
    private float calculateDamageReductionWithDefense(int defense) {
        return 1 - calculateDamageMultiplierWithDefense(defense);
    }

    private void operationSetLevel() {
        var sc = new Scanner(System.in);
        System.out.println("Please enter a level you want to use.");
        try {
            _level = sc.nextInt();
        } catch (Exception e) {
            System.out.println("Failed to read level (level is unchanged): " + e.getMessage());
            return;
        }
        System.out.println("Set level to " + _level);
    }

    private void operationDumpPlayerExpectationsTable() {
        for (var i = 1; i <= 120; i++)
            System.out.println(i + ": " + calculateExpectedPlayerHealth(i) + "HP," + calculateExpectedDefense(i) + "DEF" + " EHP=" + calculateExpectedHealth(i) + "DPS=" + calculatePlayerDps(i) + " STR=" + (1.0 + i/11.0) + "x");
    }

    private void operationDumpHpTable() {
        for (var i = 1; i <= 120; i++)
            System.out.println(i + ": " + "player=" + calculateExpectedPlayerHealth(i) + "HP," + calculateExpectedDefense(i) + "DEF" + " EHP=" + calculateExpectedHealth(i));
    }

    private void operationDumpDpsTable() {
        for (var i = 1; i <= 120; i++)
            System.out.println(i + ": " + "player=" + calculatePlayerDps(i) + " entity=" + calculateEnemyDps(i));
    }

    private void operationCreateSword() {
        System.out.println("Creating sword for level " + _level);
        for (ItemRarity rarity : ItemRarity.values())
            System.out.println(rarity + ": " + (StatSource.getExpectedWeaponDamage(_level, rarity, SWORD_DPS)));
        var rareDmg = StatSource.getExpectedWeaponDamage(_level, ItemRarity.RARE, SWORD_DPS);
        var expectedMultiplier = calculatePlayerDps(_level) / (rareDmg * SWORD_DPS);
        System.out.println("Player should have a damage multiplier of x" + expectedMultiplier + " to achieve a DPS of " + calculatePlayerDps(_level));
    }

    private void operationCreateAxe() {
        System.out.println("Creating axe for level " + _level);
        for (ItemRarity rarity : ItemRarity.values())
            System.out.println(rarity + ": " + (StatSource.getExpectedWeaponDamage(_level, rarity, AXE_DPS)));
        var rareDmg = StatSource.getExpectedWeaponDamage(_level, ItemRarity.RARE, AXE_DPS);
        var expectedMultiplier = calculatePlayerDps(_level) / (rareDmg * AXE_DPS);
        System.out.println("Player should have a damage multiplier of x" + expectedMultiplier + " to achieve a DPS of " + calculatePlayerDps(_level));
    }

    private void operationCreateArmor() {
        System.out.println("Creating armor for level " + _level);
        var distribution = StatSource.generateStatDistribution(_level);
        var hp = distribution.get(StatSource.ARMOR) * (calculateExpectedPlayerHealth(_level) - 100);
        var def = distribution.get(StatSource.ARMOR) * calculateExpectedDefense(_level);
        System.out.println("Player should have " + hp + "HP and " + def + "DEF from armor at level " + _level);
        var enchHp = distribution.get(StatSource.ENCHANTMENTS) * (calculateExpectedPlayerHealth(_level) - 100);
        var enchDef = distribution.get(StatSource.ENCHANTMENTS) * calculateExpectedDefense(_level);
        for (var rarity : ItemRarity.values()) {
            var sb = new StringBuilder(rarity + ": ");
            for (var piece : ArmorPiece.values()) {
                sb.append(piece).append("=").append(piece.calculateStatTarget(def, rarity)).append("DEF/").append(piece.calculateStatTarget(hp, rarity)).append("HP").append(" | ");
            }
            System.out.println(sb);
        }
        System.out.println("Expected enchantment defense pool = " + enchDef + " (" + (enchDef / ArmorPiece.values().length) + ")");
        System.out.println("Expected enchantment hp pool      = " + enchHp + " (" + (enchHp / ArmorPiece.values().length) + ")");
    }

    private void operationDumpSkillExpectations() {
        System.out.println("Skill expectations");
        for (var i = 1; i <= 120; i++) {
            var distribution = StatSource.generateStatDistribution(i);
            var hp = distribution.get(StatSource.SKILLS) * (calculateExpectedPlayerHealth(i) - 100);
            var def = distribution.get(StatSource.SKILLS) * calculateExpectedDefense(i);
            var rareDmg = StatSource.getExpectedWeaponDamage(i, ItemRarity.RARE, 1.0);
            var expectedMultiplier = (double)calculatePlayerDps(i) / rareDmg;
            var dps = distribution.get(StatSource.SKILLS) * expectedMultiplier;
            System.out.println(i + ": " + hp + "HP " + def + "DEF " + dps + "xSTR");
        }

    }

    private void operationQuit() {
        System.out.println("Exiting! Bye bye :)");
        System.exit(0);
    }

    private void operationMissingLogic() {
        System.out.println("Missing logic for the selected handler!");
    }

    public Runnable getHandler(String key) {

        Operation op = Operation.get(key);
        if (op == null) {
            return null;
        }

        return switch (op) {
            case SET_LEVEL -> this::operationSetLevel;
            case CREATE_ARMOR -> this::operationCreateArmor;
            case CREATE_SWORD -> this::operationCreateSword;
            case CREATE_AXE -> this::operationCreateAxe;
            case DUMP_PLAYER_EXPECTATIONS -> this::operationDumpPlayerExpectationsTable;
            case DUMP_DPS_TABLE -> this::operationDumpDpsTable;
            case DUMP_HP_TABLE -> this::operationDumpHpTable;
            case DUMP_SKILLS_TABLE -> this::operationDumpSkillExpectations;
            case QUIT -> this::operationQuit;
            default -> this::operationMissingLogic;
        };
    }

    public static void main(String[] args) {
        var calculator = new StatCalculator();
        var scanner = new Scanner(System.in);
        while (true) {
            System.out.println();
            System.out.println("What would you like to do? Enter the characters before the colon to select an option.");
            for (Operation operation : Operation.values())
                System.out.println(operation.Key + ": " + operation.Description);

            var handler = calculator.getHandler(scanner.nextLine());
            if (handler == null) {
                System.out.println("Invalid input! Try again.\n");
                continue;
            }

            System.out.println();
            handler.run();
            System.out.println();
        }

    }

}
