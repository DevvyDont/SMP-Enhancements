package xyz.devvydont.smprpg.services;

import xyz.devvydont.smprpg.unstable.listeners.DamageParticleRemover;

/**
 * If ProtocolLib is required for a listener, then it should be handled here.
 * The job of this service is to instantiate ProtocolLib packet handlers, hence the name "Unstable".
 */
public class UnstableListenersService implements IService {

    @Override
    public void setup() throws RuntimeException {
        new DamageParticleRemover();
    }

    @Override
    public void cleanup() {

    }
}
