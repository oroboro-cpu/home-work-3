package project.security;

import project.exception.AuthenticationException;
import project.lib.Inject;
import project.lib.Service;
import project.models.Driver;
import project.service.DriverService;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    @Inject
    private DriverService driverService;

    @Override
    public Driver login(String login, String password) throws AuthenticationException {
        Driver driverFromDB = driverService.findByLogin(login).orElseThrow(() ->
                    new AuthenticationException("Invalid login or password!"));
        if (driverFromDB.getPassword().equals(password)) {
            return driverFromDB;
        }
        throw new AuthenticationException("Invalid login or password!");
    }
}
