package xyz.devvydont.smprpg.util.world;

import org.bukkit.util.Transformation;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;

public class TransformationUtil {

    private final static Vector3f NO_SCALE = new Vector3f(1, 1, 1);
    private final static AxisAngle4f NO_ROTATION = new AxisAngle4f(0.0f, 0.0f, 0.0f, 0.0f);

    public static Transformation getTranslation(Vector3f vector) {
        return new Transformation(
                vector,
                NO_ROTATION,
                NO_SCALE,
                NO_ROTATION
        );
    }

}
