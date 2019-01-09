import java.util.*;

public class passenger {
    public static int updatePassenger(int low, int high) {
        if (high < low) {
            throw new IllegalArgumentException("distribution high is lower than low");
        }

        Random r = new Random();
        return r.nextInt((high - low) + 1) + low;
    }
}
