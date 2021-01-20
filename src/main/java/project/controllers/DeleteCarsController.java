package project.controllers;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import project.lib.Injector;
import project.service.CarService;

public class DeleteCarsController extends HttpServlet {
    private static final Injector injector = Injector.getInstance("project");
    private final CarService carService = (CarService)
            injector.getInstance(CarService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String carId = req.getParameter("id");
        Long id = Long.parseLong(carId);
        carService.delete(id);
        resp.sendRedirect(req.getContextPath() + "/cars");
    }
}
