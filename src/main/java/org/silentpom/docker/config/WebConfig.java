package org.silentpom.docker.config;

import org.silentpom.docker.mvc.SimpleController;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Created by Vlad on 03.03.2020.
 */
@Configuration()
@ComponentScan(basePackageClasses = SimpleController.class)
@EnableWebMvc
public class WebConfig {
}
