package giraffe.auth.controllers;

import giraffe.domain.GiraffeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 *
 * Invoked only for exception thrown in Spring Security filter chain
 *
 * @author Olga Gushchyna
 * @version 0.0.1
 */
@RestController
public class GiraffeErrorController implements ErrorController {

    private final ErrorAttributes errorAttributes;

    private static final String PATH = "/error";

    @Autowired
    public GiraffeErrorController(ErrorAttributes errorAttributes) {
        Assert.notNull(errorAttributes, "ErrorAttributes must not be null");
        this.errorAttributes = errorAttributes;
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }

    private Map<String, Object> getErrorAttributes(HttpServletRequest aRequest, boolean includeStackTrace) {
        RequestAttributes requestAttributes = new ServletRequestAttributes(aRequest);
        return errorAttributes.getErrorAttributes(requestAttributes, includeStackTrace);
    }

    @RequestMapping(value = PATH, produces = MediaType.APPLICATION_JSON_VALUE)
    public GiraffeException.ErrorResponse handleErrorJson(HttpServletRequest request) {
        Map<String, Object> errorAttributes = getErrorAttributes(request, false);

        GiraffeException.ErrorResponse errorResponse = new GiraffeException.ErrorResponse();
        errorResponse.setMessage(errorAttributes.get("message").toString());

        return errorResponse;
    }

}
