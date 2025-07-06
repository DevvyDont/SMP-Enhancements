package xyz.devvydont.smprpg.services;

/**
 * An interface that the backbones of the plugin utilize. A service represents a singleton that provides core
 * plugin functionality that is meant for other services and listeners to call. These instances are meant to be
 * tightly coupled, so treat any public methods in a service like contracts.
 */
public interface IService {

    /**
     * Set up the service. When this method executes, all other services will be instantiated, making SMPRPG.getService()
     * calls safe to run. Run any initialization code that wasn't fit at construction time.
     * @exception RuntimeException Thrown when the service was unable to startup.
     */
    void setup() throws RuntimeException;

    /**
     * Clean up the service. Run any code that this required for graceful cleanup of this service.
     */
    void cleanup();
}
