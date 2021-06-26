package ${package_base}.models.response;

import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Getter
@Setter
public class ExceptionResponse {

    private String message;
    private String path;
    private int status;
    private Date timestamp;

    public void setExceptionObject(HttpServletRequest request, String message, int status) {
        setMessage(message);
        setPath(request.getRequestURI());
        setStatus(status);
        setTimestamp(new Date());
    }

}
