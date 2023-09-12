package geektime.tdd.args;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.function.Function;

import static geektime.tdd.args.BooleanParserTest.option;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SingleValuedOptionParserTest {
    /**
     * sad path
     */
    @Test
    void should_not_parse_if_too_many_argument() {
        final TooManyArgumentsException e = assertThrows(TooManyArgumentsException.class, () ->
                new SingleValuedOptionParser<Integer>(0, Integer::valueOf).parse(asList("-p", "8080", "8081"), option("p"))
        );
        assertThat(e.getOption()).isEqualTo("p");
    }

    /**
     * sad path
     */
    @ParameterizedTest
    @ValueSource(strings = {"-p", "-p -l"})
    void should_not_accept_insufficient_argument(String argument) {
        final InsufficientArgumentsException e = assertThrows(InsufficientArgumentsException.class, () ->
                new SingleValuedOptionParser<Integer>(0, Integer::valueOf).parse(asList(argument.split(" ")), option("p"))
        );
        assertThat(e.getOption()).isEqualTo("p");
    }

    /**
     * default value
     */
    @Test
    void should_set_default_value_if_no_flag_present() {
        final Function<String, Object> whatever = it -> null;
        final Object defaultValue = new Object();

        final Object parsed = new SingleValuedOptionParser<>(defaultValue, whatever).parse(asList(), option("p"));
        assertThat(parsed).isSameAs(defaultValue);
    }

    /**
     * happy path
     */
    @Test
    void should_set_value_if_flag_present() {
        final Object parsed = new SingleValuedOptionParser<Integer>(0, Integer::valueOf).parse(asList("-p", "8080"), option("p"));
        assertThat(parsed).isEqualTo(8080);
    }

}