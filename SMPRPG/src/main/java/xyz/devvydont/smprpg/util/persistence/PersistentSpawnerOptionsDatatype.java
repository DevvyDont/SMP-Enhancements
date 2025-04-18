package xyz.devvydont.smprpg.util.persistence;

import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.entity.spawning.EntitySpawner;

import java.util.ArrayList;
import java.util.List;

public class PersistentSpawnerOptionsDatatype implements PersistentDataType<String, EntitySpawner.SpawnerOptions> {

    public static final PersistentDataType<String, EntitySpawner.SpawnerOptions> INSTANCE = new PersistentSpawnerOptionsDatatype();

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
        JSONObject obj = new JSONObject();

        StringBuilder entries = new StringBuilder();
        for (EntitySpawner.SpawnerEntry entry : complex.getEntries())
            entries.append(entry.toPrimitive()).append(",");

        // Delete comma at end if we had entries
        if (!complex.getEntries().isEmpty())
            entries.deleteCharAt(entries.length() - 1);

        obj.put("entries", entries.toString());

        // Now add the easy options
        obj.put("radius", complex.getRadius());
        obj.put("limit", complex.getLimit());
        obj.put("level", complex.getLevel());

        return obj.toString();
    }

    @Override
    public @NotNull EntitySpawner.SpawnerOptions fromPrimitive(@NotNull String primitive, @NotNull PersistentDataAdapterContext context) {

        JSONObject json;
        try {
            json = new JSONObject(primitive);
        } catch (JSONException e) {
            SMPRPG.getInstance().getLogger().severe("Failed to parse primitive spawning options. String is: " + primitive);
            SMPRPG.getInstance().getLogger().severe(e.toString());
            throw new RuntimeException(e);
        }

        String entries = json.getString("entries");
        var radius = json.getLong("radius");
        var limit = json.getLong("limit");
        var level = json.getLong("level");

        // Convert entries back to list of entries
        List<EntitySpawner.SpawnerEntry> listOfEntries = new ArrayList<>();
        String[] components = entries.split(",");

        if (!entries.isEmpty())
            for (String component : components)
                listOfEntries.add(EntitySpawner.SpawnerEntry.fromPrimitive(component));

        return new EntitySpawner.SpawnerOptions(listOfEntries, limit, radius, level);
    }
}
