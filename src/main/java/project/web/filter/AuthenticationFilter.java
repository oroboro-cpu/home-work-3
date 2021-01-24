package project.web.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import project.lib.Injector;
import project.service.DriverService;

public class AuthenticationFilter implements Filter {
    private static final String DRIVER_ID = "driver_id";
    private static final Injector injector = Injector.getInstance("project");
    private final DriverService driverService =
            (DriverService) injector.getInstance(DriverService.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        String url = req.getServletPath();
        if (url.equals("/login") || url.equals("/drivers/create")) {
            filterChain.doFilter(req, resp);
            return;
        }
        Long driverId = (Long) req.getSession().getAttribute(DRIVER_ID);
        if (driverId == null || driverService.get(driverId) == null) {
            resp.sendRedirect("/login");
            return;
        }
        filterChain.doFilter(req, resp);
    }

    @Override
    public void destroy() {

    }
}
