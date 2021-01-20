package project.controllers;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import project.lib.Injector;
import project.models.Car;
import project.models.Manufacturer;
import project.service.CarService;
import project.service.ManufacturerService;

public class CreateCarsController extends HttpServlet {
    private static final Injector injector = Injector.getInstance("project");
    private final CarService carService = (CarService) injector.getInstance(CarService.class);
    private final ManufacturerService manufacturerService = (ManufacturerService)
            injector.getInstance(ManufacturerService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/createCars.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String name = req.getParameter("name");
        String manufacturerId = req.getParameter("manufacturerId");
        Car car = new Car();
        Manufacturer manufacturer = manufacturerService.get(Long.parseLong(manufacturerId));
        car.setName(name);
        car.setManufacturer(manufacturer);
        carService.create(car);
        resp.sendRedirect(req.getContextPath() + "/cars");
    }
}
