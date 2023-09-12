package geektime.tdd.args;

import java.util.List;

class BooleanParser implements OptionParser<Boolean> {
    @Override
    public Boolean parse(List<String> arguments, Option option) {
        final boolean b = arguments.contains("-" + option.value());
        final int index = arguments.indexOf("-" + option.value());
        if (index + 1 < arguments.size() &&
                !arguments.get(index + 1).startsWith("-")) {
            throw new TooManyArgumentsException(option.value());
        }
        return b;
    }
}
