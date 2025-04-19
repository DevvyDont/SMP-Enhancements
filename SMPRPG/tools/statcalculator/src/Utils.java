import java.util.Map;

public class Utils {

    public static String prettyMapToString(Map<?, ?> map) {
        var sb = new StringBuilder("{ ");
        for (Map.Entry<?, ?> entry : map.entrySet())
            sb.append(entry.getKey()).append(": ").append(entry.getValue()).append(", ");
        sb.append(" }");
        return sb.toString();
    }

}
