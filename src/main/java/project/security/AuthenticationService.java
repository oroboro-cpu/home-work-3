package project.security;

import project.exception.AuthenticationException;
import project.models.Driver;

public interface AuthenticationService {
    Driver login(String login, String password) throws AuthenticationException;
}
