package org.silentpom.docker.service;

import org.silentpom.docker.domain.VerySimpleObject;
import org.springframework.stereotype.Service;

/**
 * Created by Vlad on 03.03.2020.
 */
@Service
public class SimpleServiceImpl implements SimpleService {

    @Override
    public VerySimpleObject createObject(String name) {
        return new VerySimpleObject(name);
    }
}
