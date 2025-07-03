package xyz.devvydont.smprpg.entity.base;

import net.kyori.adventure.bossbar.BossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import xyz.devvydont.smprpg.attribute.AttributeWrapper;
import xyz.devvydont.smprpg.entity.CustomEntityType;
import xyz.devvydont.smprpg.entity.components.EntityConfiguration;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

public class CustomBossInstance<T extends LivingEntity> extends BossInstance<T> {

    private final CustomEntityType type;

    public CustomBossInstance(Entity entity, CustomEntityType type) {
        super(entity);
        this.type = type;
    }

    public CustomBossInstance(T entity, CustomEntityType type) {
        super(entity);
        this.type = type;
    }

    @Override
    public @Nullable BossBar createBossBar() {
        return BossBar.bossBar(ComponentUtils.EMPTY, 1.0f, BossBar.Color.RED, BossBar.Overlay.NOTCHED_10);
    }

    @Override
    public String getClassKey() {
        return type.key();
    }

    @Override
    public EntityType getDefaultEntityType() {
        return type.Type;
    }

    @Override
    public String getEntityName() {
        return type.Name;
    }

    @Override
    public void updateAttributes() {
        updateBaseAttribute(AttributeWrapper.HEALTH, this._config.getBaseHealth());
        updateBaseAttribute(AttributeWrapper.STRENGTH, this._config.getBaseDamage());
        heal();
    }

    @Override
    public EntityConfiguration getDefaultConfiguration() {
        return EntityConfiguration.builder()
                .withLevel(type.Level)
                .withHealth(type.Hp)
                .withDamage(type.Damage)
                .build();
    }

    public boolean isEntityOfType(Entity entity) {
        return type.isOfType(_plugin.getEntityService(), entity);
    }

    public boolean isEntity(Entity entity) {
        return entity.equals(this._entity);
    }
}
