package xyz.devvydont.smprpg.unstable.listeners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.Particle;
import xyz.devvydont.smprpg.SMPRPG;

public class DamageParticleRemover {

    SMPRPG plugin;

    Particle DAMAGE_PARTICLE = Particle.DAMAGE_INDICATOR;
    final int MAX_DAMAGE_PARTICLES = 10;


    public DamageParticleRemover(SMPRPG plugin) {

        this.plugin = plugin;

        if (plugin.getServer().getPluginManager().getPlugin("ProtocolLib") == null) {
            plugin.getLogger().warning("ProtocolLib is not installed. High damage particles will clutter clients' screens.");
            return;
        }


        // Create a packet listener where whenever we attempt to spawn damage particles over a certain amount,
        // cap the amount that can spawn so we don't obstruct player's views
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(plugin, ListenerPriority.HIGH, PacketType.Play.Server.WORLD_PARTICLES) {
            @Override
            public void onPacketSending(PacketEvent event) {
                PacketContainer packet = event.getPacket();

                // Only listen to world particle packets
                if (event.getPacketType() != PacketType.Play.Server.WORLD_PARTICLES)
                    return;

                // Only listen for damage particle packets
                if (packet.getNewParticles().read(0).getParticle() != DAMAGE_PARTICLE)
                    return;

                // Cap off the amount of particles in this packet
                if (packet.getIntegers().read(0) > MAX_DAMAGE_PARTICLES)
                    packet.getIntegers().write(0, MAX_DAMAGE_PARTICLES);
            }
        });
    }

}
