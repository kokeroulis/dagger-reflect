package dagger.reflect;

public class BenchmarkUtil {

    interface Measurement<T> {
        T actionToBeMesured();
    }

    public static <T> T measure(String key, Measurement<T> cb) {
        long now = System.currentTimeMillis();
        final T result = cb.actionToBeMesured();
        long diff = System.currentTimeMillis() - now;

        System.out.println("Dagger.Reflect.BenchmarkUtil " + key + " " + diff + "ms");
        return result;
    }

}
