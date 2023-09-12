package geektime.tdd.args;

public class InsufficientArgumentsException extends RuntimeException {

    private String option;

    public InsufficientArgumentsException(String option) {
        this.option = option;
    }

    public String getOption() {
        return option;
    }
}
