package giraffe.auth.config.server;

import org.springframework.hateoas.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
@Controller
@RequestMapping("/info")
public class InfoController {

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    HttpEntity<Resource<String>> info() {

        Resource<String> resource = new Resource<>("Welcome to Giraffe");

        return new ResponseEntity<>(resource, HttpStatus.OK);
    }

}