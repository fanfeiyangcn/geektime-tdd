package geektime.tdd.args;

import java.util.List;
import java.util.function.Function;

class SingleValuedOptionParser<T> implements OptionParser<T> {

    private final Function<String, T> valueParser;

    private final T defaultValue;

    public SingleValuedOptionParser(T defaultValue, Function<String, T> valueParser) {
        this.defaultValue = defaultValue;
        this.valueParser = valueParser;
    }

    @Override
    public T parse(List<String> arguments, Option option) {
        final int i = arguments.indexOf("-" + option.value());

        if (i == -1) {
            return defaultValue;
        }

        if (i + 1 == arguments.size() ||
                arguments.get(i + 1).startsWith("-")) {
            throw new InsufficientArgumentsException(option.value());
        }

        if (i + 2 < arguments.size() &&
                !arguments.get(i + 2).startsWith("-")) {
            throw new TooManyArgumentsException(option.value());
        }
        final String s = arguments.get(i + 1);
        return valueParser.apply(s);
    }

}
