package ${package_base}.filter;

import ${package_base}.models.response.ExceptionResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@ControllerAdvice
public class GenericExceptionHandler {

    @ExceptionHandler({InvalidDataAccessResourceUsageException.class, DataIntegrityViolationException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public @ResponseBody ExceptionResponse handleDatabaseException(
            final Exception exception, final HttpServletRequest request) {

        String message = exception.getCause() != null ?
            exception.getCause().getCause().getLocalizedMessage()
            : exception.getMessage();
        ExceptionResponse error = new ExceptionResponse();
        error.setExceptionObject(request, message, 400);
        return error;
    }

    @ExceptionHandler({IllegalArgumentException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public @ResponseBody ExceptionResponse handleExceptionIllegalArgument(
            final Exception exception, final HttpServletRequest request) {

        String message = exception.getMessage() != null ? exception.getMessage() : "Unknown Error";
        ExceptionResponse error = new ExceptionResponse();
        error.setExceptionObject(request, message, 400);
        return error;
    }

    @ExceptionHandler({Exception.class, IOException.class})
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public @ResponseBody ExceptionResponse handleException(
            final Exception exception, final HttpServletRequest request) {

        String message = exception.getMessage() != null ? exception.getMessage() : "Unknown Error";
        ExceptionResponse error = new ExceptionResponse();
        error.setExceptionObject(request, message, 500);
        return error;
    }

}
