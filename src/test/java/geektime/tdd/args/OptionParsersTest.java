package geektime.tdd.args;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.lang.annotation.Annotation;
import java.util.function.Function;

import static geektime.tdd.args.OptionParsersTest.BooleanParserTest.option;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OptionParsersTest {

    @Nested
    class UnaryOptionParserTest {

        /**
         * sad path
         */
        @Test
        void should_not_parse_if_too_many_argument() {
            final TooManyArgumentsException e = assertThrows(TooManyArgumentsException.class, () ->
                    OptionParsers.unary(0, Integer::valueOf).parse(asList("-p", "8080", "8081"), option("p"))
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
                    OptionParsers.unary(0, Integer::valueOf).parse(asList(argument.split(" ")), option("p"))
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

            final Object parsed = OptionParsers.unary(defaultValue, whatever).parse(asList(), option("p"));
            assertThat(parsed).isSameAs(defaultValue);
        }

        /**
         * happy path
         */
        @Test
        void should_set_value_if_flag_present() {
            final Object parsed = OptionParsers.unary(0, Integer::valueOf).parse(asList("-p", "8080"), option("p"));
            assertThat(parsed).isEqualTo(8080);
        }
    }

    @Nested
    class BooleanParserTest {

        /**
         * happy path
         */
        @Test
        void should_set_value_to_true_if_flag_present() {
            final Object parsed = OptionParsers.bool().parse(asList("-l"), option("l"));
            assertThat(parsed).isEqualTo(true);
        }

        /**
         * sad path
         */
        @Test
        void should_not_accept_extra_argument_for_boolean_option() {
            final TooManyArgumentsException e = assertThrows(TooManyArgumentsException.class,
                    () -> OptionParsers.bool().parse(asList("-l", "t"), option("l")));

            assertThat(e.getOption()).isEqualTo("l");
        }

        /**
         * default value
         */
        @Test
        void should_set_default_value_if_flag_not_present() {
            final Object parsed = OptionParsers.bool().parse(asList(), option("l"));
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
}