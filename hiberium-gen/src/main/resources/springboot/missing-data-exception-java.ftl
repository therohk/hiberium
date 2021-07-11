package ${package_base}.models.exception;

public class MissingDataException extends Exception {

    public MissingDataException(String message) {
        super(message);
    }

    public MissingDataException(final Throwable cause) {
        super(cause);
    }

}
