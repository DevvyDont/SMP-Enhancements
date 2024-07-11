package xyz.devvydont.smprpg.services;

public interface BaseService {

    boolean setup();
    void cleanup();

    boolean required();  // Is this service required for the plugin to operate?

}
