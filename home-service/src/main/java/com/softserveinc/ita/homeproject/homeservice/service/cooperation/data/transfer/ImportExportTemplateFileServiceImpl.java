package com.softserveinc.ita.homeproject.homeservice.service.cooperation.data.transfer;

import java.io.File;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class ImportExportTemplateFileServiceImpl implements ImportExportTemplateFileService {

    @Value("${import.export.template.file.path}")
    private String importExportTemplateFilePath;

    @Override
    public File getTemplateFile() {
        return new File(importExportTemplateFilePath);
    }
}
