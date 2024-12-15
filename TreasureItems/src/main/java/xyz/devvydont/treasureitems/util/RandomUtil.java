package xyz.devvydont.treasureitems.util;

public class RandomUtil {

    public static int randomIntBound(int start, int end) {
        return (int) (Math.random() * (end+1 - start)) + start;
    }

}
