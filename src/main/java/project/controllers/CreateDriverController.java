package project.controllers;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import project.lib.Injector;
import project.models.Driver;
import project.service.DriverService;

public class CreateDriverController extends HttpServlet {
    private static final Injector injector = Injector.getInstance("project");
    private final DriverService driverService = (DriverService)
            injector.getInstance(DriverService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/driver/create.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String name = req.getParameter("name");
        String licenseNumber = req.getParameter("licenseNumber");
        String login = req.getParameter("login");
        Driver driver = new Driver();
        driver.setName(name);
        driver.setLicenseNumber(licenseNumber);
        driver.setLogin(login);
        String password = req.getParameter("password");
        String submitPassword = req.getParameter("submitPassword");
        if (password.equals(submitPassword)) {
            driver.setPassword(password);
            driverService.create(driver);
            resp.sendRedirect(req.getContextPath() + "/");
        } else {
            req.setAttribute("error", "Please repeat password! Passwords should be same");
            req.setAttribute("login", "login");
            req.getRequestDispatcher("/WEB-INF/views/driver/create.jsp").forward(req, resp);
        }
    }
}
