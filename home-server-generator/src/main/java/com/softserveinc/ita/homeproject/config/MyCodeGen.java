package com.softserveinc.ita.homeproject.config;

import org.openapitools.codegen.languages.*;

import static org.openapitools.codegen.utils.StringUtils.camelize;

public class MyCodeGen extends ObjcClientCodegen {

    @Override
    public String toApiName(String name) {
        return classPrefix + camelize(name)  + "Controller";
        //return super.toApiName("Controller");
    }

    @Override
    public String toApiFilename(String name) {
        return classPrefix + camelize(name) + "Controller";
        //return super.toApiFilename("Controller");
    }
}
