package org.silentpom.docker.config;

import org.silentpom.docker.service.SimpleServiceImpl;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Vlad on 03.03.2020.
 */
@Configuration
@ComponentScan(basePackageClasses = SimpleServiceImpl.class)
public class ServiceConfig {

}
