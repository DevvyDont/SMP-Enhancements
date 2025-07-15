package xyz.devvydont.smprpg.fishing.utils;

import org.bukkit.Particle;
import org.bukkit.Sound;
import xyz.devvydont.smprpg.util.misc.ILocationPredicate;

/**
 * Allows the managing of the various particles that spawn during key fishing events.
 * @param Predicate The behavior of what's considered a valid location for this hook.
 * @param CatchParticle The particle that spawns when you hook a fish.
 * @param IdleParticle The particle that spawns around the hook while waiting for a fish.
 * @param FishParticle The particle that spawns to denote an approaching fish.
 * @param SplashSound The sound to play when something is caught.
 */
public record HookEffectOptions(ILocationPredicate Predicate, Particle CatchParticle, Particle IdleParticle, Particle FishParticle, Sound SplashSound){}
