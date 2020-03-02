package org.silentpom.docker.service;

import org.silentpom.docker.domain.VerySimpleObject;

/**
 * Created by Vlad on 03.03.2020.
 */
public interface SimpleService {
    VerySimpleObject createObject(String name);
}
