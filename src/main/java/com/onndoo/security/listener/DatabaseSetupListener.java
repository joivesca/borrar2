package com.onndoo.security.listener;

import com.onndoo.security.config.DatabaseSetup;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
@ApplicationScoped
public class DatabaseSetupListener implements ServletContextListener{

	@Inject
	private DatabaseSetup databaseSetup;
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		databaseSetup.init();
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		databaseSetup.destroy();
	}
}
