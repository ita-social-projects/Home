package com.softserveinc.ita.homeproject.application.api;

import java.io.File;
import javax.annotation.security.PermitAll;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.springframework.stereotype.Component;

@Provider
@Component
public class FileApiImpl implements FileApi {

    @PermitAll
    @Override
    public Response getTemplateFile() {
        File file = new File("home-application/src/main/resources/files/template.xlsx");

        Response.ResponseBuilder responseBuilder = Response.ok(file);
        responseBuilder.header("Content-Disposition", "attachment; filename=\"template.xlsx\"");
        return responseBuilder.build();
    }
}
