package com.workfront.internship.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by Anna Asmangulyan on 8/18/2016.
 */
public class SpringContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
       OnlineShopApplication.init(servletContextEvent.getServletContext());

    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
