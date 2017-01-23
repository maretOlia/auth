package giraffe.auth.config.server;

import giraffe.domain.GiraffeException;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
public class InvalidOrExpiredSocialToken extends GiraffeException {

    public InvalidOrExpiredSocialToken() {
        super("Invalid or expired SSO token received from external authentication server");
    }

    @Override
    public Integer getErrorCode() {
        return 1200;
    }
}