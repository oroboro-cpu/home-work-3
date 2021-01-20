package project.controllers;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import project.lib.Injector;
import project.service.DriverService;

public class DeleteDriversController extends HttpServlet {
    private static final Injector injector = Injector.getInstance("project");
    private final DriverService driverService = (DriverService)
            injector.getInstance(DriverService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String driverId = req.getParameter("id");
        Long id = Long.parseLong(driverId);
        driverService.delete(id);
        resp.sendRedirect(req.getContextPath() + "/drivers");
    }
}
