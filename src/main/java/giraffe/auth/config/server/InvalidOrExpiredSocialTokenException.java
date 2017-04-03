package giraffe.auth.config.server;

import giraffe.domain.GiraffeException;

/**
 * @author Olga Gushchyna
 * @version 0.0.1
 */
public class InvalidOrExpiredSocialTokenException extends GiraffeException {

    public InvalidOrExpiredSocialTokenException() {
        super("Invalid or expired SSO token received from external authentication server");
    }

    @Override
    public Integer getErrorCode() {
        return 1200;
    }

}