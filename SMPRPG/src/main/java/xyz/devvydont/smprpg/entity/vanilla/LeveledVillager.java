package xyz.devvydont.smprpg.entity.vanilla;

import org.bukkit.entity.Villager;
import xyz.devvydont.smprpg.entity.base.VanillaEntity;
import xyz.devvydont.smprpg.util.attributes.AttributeWrapper;
import xyz.devvydont.smprpg.util.formatting.MinecraftStringUtils;

public class LeveledVillager extends VanillaEntity<Villager> {

    public LeveledVillager(Villager entity) {
        super(entity);
    }

    @Override
    public void updateAttributes() {
        super.updateAttributes();
        updateBaseAttribute(AttributeWrapper.DEFENSE.getAttribute(), 100);
    }

    @Override
    public String getEntityName() {

        Villager.Profession profession = _entity.getProfession();
        if (!profession.equals(Villager.Profession.NONE))
            return MinecraftStringUtils.getTitledString(profession.key().value());

        return super.getEntityName();
    }
}
