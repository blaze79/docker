package org.silentpom.docker.mvc;

import org.silentpom.docker.domain.VerySimpleObject;
import org.silentpom.docker.service.SimpleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Vlad on 03.03.2020.
 */
@RestController
public class SimpleController {

    @Autowired
    private SimpleService simpleService;

    @RequestMapping(path = "/object")
    public VerySimpleObject createObject() {
        return simpleService.createObject("Simple");
    }
}
