package logic;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 *
 * @author Wontaek
 */
public abstract class LogicFactory {

    private static final String PACKAGE = "logic.";
    private static final String SUFFIX = "Logic";

    private LogicFactory() {
    }

    //TODO this code is not complete, it is just here for sake of programe working. need to be changed ocmpletely
    public static < T> T getFor(String entityName) {
        try {
            T newInstance = getFor((Class<T>) Class.forName(PACKAGE + entityName + SUFFIX));
            return newInstance;
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }

    public static <T> T getFor(Class<T> type) throws ClassNotFoundException {
        try {
            Constructor<T> declaredConstructor = type.getDeclaredConstructor();
            return declaredConstructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) { 
            throw new IllegalStateException(e);
        }
    }
}
