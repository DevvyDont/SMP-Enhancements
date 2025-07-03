package xyz.devvydont.smprpg.services;

public interface IService {

    boolean setup();
    void cleanup();

    boolean required();  // Is this service required for the plugin to operate?

}
