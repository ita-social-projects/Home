package com.softserveinc.ita.homeproject.application.api;

import javax.annotation.security.PermitAll;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import com.softserveinc.ita.homeproject.homeservice.service.cooperation.data_damping.ImportExportTemplateFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Provider
@Component
public class FileApiImpl implements FileApi {

    @Autowired
    private ImportExportTemplateFileService templateService;

    @PermitAll
    @Override
    public Response getTemplateFile() {
        Response.ResponseBuilder responseBuilder = Response.ok(templateService.getTemplateFile());
        responseBuilder.header("Content-Disposition", "attachment; filename=\"template.xlsx\"");

        return responseBuilder.build();
    }
}
