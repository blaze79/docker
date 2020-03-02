package org.silentpom.docker.config;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.gzip.GzipHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * Created by Vlad on 03.03.2020.
 */
@Configuration
public class JettyConfig {
    //@Value("${jetty.port}")
    private String serverPort ="8080";

    //@Value("${jetty.threads.pool.max.size}")
    private String threadsPoolMaxSize = "20";



    @Bean(initMethod = "start", destroyMethod = "stop")
    public Server jettyServer() {
        QueuedThreadPool queuedThreadPool = new QueuedThreadPool(Integer.parseInt(threadsPoolMaxSize));
        Server server = new Server(queuedThreadPool);
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(Integer.parseInt(serverPort));
        server.setConnectors(new Connector[]{connector});

        GzipHandler gzipHandler = new GzipHandler();
        gzipHandler.setIncludedMimeTypes("application/json");
        //gzipHandler.setMimeTypes("application/json");
        gzipHandler.setHandler(defaultSpringDispatcherServlet());

        //HandlerList handlers = new HandlerList();
        //handlers.setHandlers(new Handler[] { gzipHandler, defaultSpringDispatcherServlet() });
        server.setHandler(gzipHandler);

        // Setup JMX
//        MBeanContainer mbContainer = new MBeanContainer(ManagementFactory.getPlatformMBeanServer());
//        server.addEventListener(mbContainer);
//        server.addBean(mbContainer);
//        // Add loggers MBean to server (will be picked up by MBeanContainer above)
//        server.addBean(Log.getLog());

        return server;
    }

    @Bean
    public ServletContextHandler defaultSpringDispatcherServlet() {
        AnnotationConfigWebApplicationContext webApplicationContext = new AnnotationConfigWebApplicationContext();
        webApplicationContext.register(AppConfig.class);

        ContextLoaderListener contextLoaderListener = new ContextLoaderListener(webApplicationContext);

        DispatcherServlet dispatcherServlet = new DispatcherServlet(webApplicationContext);


        ServletHolder servletHolder = new ServletHolder(dispatcherServlet);
        ServletContextHandler handler = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);

        handler.addEventListener(contextLoaderListener);
        handler.addServlet(servletHolder,  "/*");

        return handler;
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
