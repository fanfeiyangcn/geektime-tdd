package geektime.tdd.args;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


class ArgsTest {

    @Test
    void should_parse_multi() {
        Options options = Args.parse(Options.class, "-l", "-p", "8080", "-d", "/usr/logs");

        assertThat(options.logging()).isTrue();
        assertThat(options.port()).isEqualTo(8080);
        assertThat(options.directory()).isEqualTo("/usr/logs");
    }

    static record Options(@Option("l") boolean logging, @Option("p") int port, @Option("d") String directory) {
    }


    @Test
    @Disabled
    void should_example_2() {
        ListOptions options = Args.parse(ListOptions.class, "-g", "this", "is", "a", "list", "-d", "1", "2", "-3", "5");

        assertThat(options.group()).isEqualTo(new String[]{"this", "is", "a", "list"});
        assertThat(options.decimals()).isEqualTo(new int[]{1, 2, -3, 5});


    }


    static record ListOptions(@Option("g") String[] group, @Option("d") int[] decimals) {
    }

    @Test
    void should_throw_illegal_option_exception_when_annotation_not_present() {
        final IllegalOptionException e = assertThrows(IllegalOptionException.class, () ->
                Args.parse(OptionsWithoutAnnotation.class, "-l", "-p", "8080", "-d", "/usr/logs")
        );

        assertThat(e.getParameter()).isEqualTo("port");
    }


    static record OptionsWithoutAnnotation(@Option("l") boolean logging, int port, @Option("d") String directory) {
    }
}
