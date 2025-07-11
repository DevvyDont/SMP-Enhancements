package xyz.devvydont.smprpg.util.persistence.adapters;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.entity.spawning.EntitySpawner;

import java.util.ArrayList;
import java.util.List;

public class SpawnerOptionsAdapter implements PersistentDataType<String, EntitySpawner.SpawnerOptions> {

    private static class SpawnerGsonContainer {
        private long radius;
        private long limit;
        private long level;
        private String entries;

        public SpawnerGsonContainer() {}

        public SpawnerGsonContainer(long radius, long limit, long level, String entries) {
            this.radius = radius;
            this.limit = limit;
            this.level = level;
            this.entries = entries;
        }

        public long getRadius() {
            return radius;
        }

        public long getLimit() {
            return limit;
        }

        public long getLevel() {
            return level;
        }

        public String getEntries() {
            return entries;
        }
    }

    @Override
    public @NotNull Class<String> getPrimitiveType() {
        return String.class;
    }

    @Override
    public @NotNull Class<EntitySpawner.SpawnerOptions> getComplexType() {
        return EntitySpawner.SpawnerOptions.class;
    }

    @Override
    public @NotNull String toPrimitive(EntitySpawner.@NotNull SpawnerOptions complex, @NotNull PersistentDataAdapterContext context) {

        // Create a JSON object and add the entries
        var gson = new Gson();

        StringBuilder entries = new StringBuilder();
        for (EntitySpawner.SpawnerEntry entry : complex.getEntries())
            entries.append(entry.toPrimitive()).append(",");

        // Delete comma at end if we had entries
        if (!complex.getEntries().isEmpty())
            entries.deleteCharAt(entries.length() - 1);

        var container = new SpawnerGsonContainer(complex.getRadius(), complex.getLimit(), complex.getLevel(), entries.toString());
        return gson.toJson(container);
    }

    @Override
    public @NotNull EntitySpawner.SpawnerOptions fromPrimitive(@NotNull String primitive, @NotNull PersistentDataAdapterContext context) {


        var gson = new Gson();
        SpawnerGsonContainer container;
        try {
            container = gson.fromJson(primitive, SpawnerGsonContainer.class);
        } catch (JsonSyntaxException e) {
            SMPRPG.getInstance().getLogger().severe("Failed to parse primitive spawning options. String is: " + primitive);
            SMPRPG.getInstance().getLogger().severe(e.toString());
            return new EntitySpawner.SpawnerOptions();
        }

        // Convert entries back to list of entries
        List<EntitySpawner.SpawnerEntry> listOfEntries = new ArrayList<>();
        String[] components = container.getEntries().split(",");

        if (!container.getEntries().isEmpty())
            for (String component : components)
                listOfEntries.add(EntitySpawner.SpawnerEntry.fromPrimitive(component));

        return new EntitySpawner.SpawnerOptions(listOfEntries, container.getLimit(), container.getRadius(), container.getLevel());
    }
}
