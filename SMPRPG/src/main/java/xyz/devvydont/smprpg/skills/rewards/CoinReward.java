package xyz.devvydont.smprpg.skills.rewards;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.services.EconomyService;
import xyz.devvydont.smprpg.skills.SkillType;

import static net.kyori.adventure.text.format.NamedTextColor.GOLD;
import static xyz.devvydont.smprpg.util.formatting.ComponentUtils.create;
import static xyz.devvydont.smprpg.util.formatting.ComponentUtils.merge;

public class CoinReward implements ISkillReward {

    private final int amount;

    public CoinReward(int amount) {
        this.amount = amount;
    }

    @Override
    public Component generateRewardComponent(Player player) {
        return create("+" + EconomyService.formatMoney(this.amount), GOLD)
                .hoverEvent(merge(
                        create(EconomyService.formatMoney(this.amount), GOLD),
                        create(" coins were added directly to your balance!")
                ));
    }

    /**
     * Ideally we shouldn't 'remove' coin rewards from players. This makes no sense.
     * Either way, we will implement the logic. Just be careful if you are actually trying to use this.
     * @param player The player to remove the reward from.
     * @param skill The type of skill this reward is associated with. This is necessary for stackable skill rewards.
     */
    @Override
    public void remove(Player player, SkillType skill) {
        SMPRPG.getInstance().getEconomyService().takeMoney(player, this.amount);
    }

    @Override
    public void apply(Player player, SkillType skill) {
        SMPRPG.getInstance().getEconomyService().addMoney(player, this.amount);
    }
}
