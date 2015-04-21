package com.project.vert_backend.controller;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Controller to listen to the state of the application. Performs any necessary checks and configuration when the
 * application is deployed or undeployed.
 * @author Selwyn Lehmann
 */
@WebListener
public class ApplicationController implements ServletContextListener {

    /// The servlet context with which we are associated.
    private ServletContext context = null;

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        System.out.println("\nApplicationController: Context destroyed...\n");
        this.context = null;
    }

    @Override
    public void contextInitialized(ServletContextEvent event) {
        this.context = event.getServletContext();

        System.out.println("\nApplicationController: Running DB configuration...");
        SqlScriptLauncher sqlSetup = new SqlScriptLauncher();
        sqlSetup.run();
        System.out.println("ApplicationController: Initialization complete...\n");
        context.setAttribute("DatabaseSetup", sqlSetup);
    }
}
