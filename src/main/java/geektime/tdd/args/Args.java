package geektime.tdd.args;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Args {
    public static <T> T parse(Class<T> optionsClass, String... args) {
        try {
            final Constructor<?> constructor = optionsClass.getDeclaredConstructors()[0];
            final List<String> arguments = Arrays.asList(args);
            final Parameter[] parameters = constructor.getParameters();
            final Object[] values = Arrays.stream(parameters).map(parameter -> value(arguments, parameter)).toArray();
            return (T) constructor.newInstance(values);
        } catch (IllegalOptionException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Object value(List<String> arguments, Parameter parameter) {
        if (!parameter.isAnnotationPresent(Option.class)) {
            throw new IllegalOptionException(parameter.getName());
        }
        final Option option = parameter.getAnnotation(Option.class);
        return PARSERS.get(parameter.getType()).parse(arguments, option);
    }

    private static Map<Class<?>, OptionParser> PARSERS = Map.of(
            boolean.class, new BooleanParser(),
            int.class, new SingleValuedOptionParser<>(0, Integer::parseInt),
            String.class, new SingleValuedOptionParser<>("", String::valueOf)
    );


}
