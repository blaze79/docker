package org.silentpom.docker.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by Vlad on 03.03.2020.
 */
@Configuration
@Import({WebConfig.class, ServiceConfig.class})
public class AppConfig {
}
