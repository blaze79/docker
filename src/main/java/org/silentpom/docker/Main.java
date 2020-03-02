package org.silentpom.docker;

import org.silentpom.docker.config.JettyConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by Vlad on 04.11.2019.
 */
public class Main {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(JettyConfig.class);
    }

}
