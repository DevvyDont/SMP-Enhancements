package xyz.devvydont.smprpg.services;

import xyz.devvydont.smprpg.fishing.listeners.FishingAnnouncementListeners;
import xyz.devvydont.smprpg.fishing.listeners.FishingBehaviorListeners;
import xyz.devvydont.smprpg.fishing.listeners.FishingGalleryListener;
import xyz.devvydont.smprpg.util.listeners.ToggleableListener;

import java.util.ArrayList;
import java.util.List;

public class FishingService implements IService {

    private final List<ToggleableListener> listeners = new ArrayList<>();

    @Override
    public void setup() throws RuntimeException {

        // Start up listeners that cause fishing to work.
        listeners.add(new FishingBehaviorListeners());
        listeners.add(new FishingGalleryListener());
        listeners.add(new FishingAnnouncementListeners());
        for (var listener : listeners)
            listener.start();

    }

    @Override
    public void cleanup() {

        // Stop listeners.
        for (var listener : listeners)
            listener.stop();
    }
}
