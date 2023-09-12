package geektime.tdd.args;

import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BooleanParserTest {

    /**
     * happy path
     */
    @Test
    void should_set_value_to_true_if_flag_present() {
        final Object parsed = new BooleanParser().parse(Arrays.asList("-l"), option("l"));
        assertThat(parsed).isEqualTo(true);
    }

    /**
     * sad path
     */
    @Test
    void should_not_accept_extra_argument_for_boolean_option() {
        final TooManyArgumentsException e = assertThrows(TooManyArgumentsException.class,
                () -> new BooleanParser().parse(Arrays.asList("-l", "t"), option("l")));

        assertThat(e.getOption()).isEqualTo("l");
    }

    /**
     * default value
     */
    @Test
    void should_set_default_value_if_flag_not_present() {
        final Object parsed = new BooleanParser().parse(Arrays.asList(), option("l"));
        assertThat(parsed).isEqualTo(false);
    }

    static Option option(String value) {
        return new Option() {

            @Override
            public Class<? extends Annotation> annotationType() {
                return Option.class;
            }

            @Override
            public String value() {
                return value;
            }
        };
    }

}