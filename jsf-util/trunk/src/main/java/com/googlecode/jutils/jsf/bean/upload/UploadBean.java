package com.googlecode.jutils.jsf.bean.upload;

import java.io.Serializable;

import javax.faces.event.ActionEvent;

import org.apache.commons.io.FilenameUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 * The Class UploadBean.
 */
@Controller
@Scope("session")
@SuppressWarnings("serial")
public class UploadBean implements Serializable {
    private UploadedFile file;

    /**
     * Instantiates a new upload bean.
     */
    public UploadBean() {
        super();
    }

    /**
     * Handle file upload.
     * 
     * @param event the event
     */
    public void handleFileUpload(FileUploadEvent event) {
        if (event != null) {
            this.file = event.getFile();
        }
    }

    /**
     * Re init.
     * 
     * @param event the event
     */
    public void reInit(ActionEvent event) {
        reInit();
    }

    /**
     * Re init.
     */
    public void reInit() {
        this.file = null;
    }

    /**
     * Gets the file.
     * 
     * @return the file
     */
    public UploadedFile getFile() {
        return file;
    }

    /**
     * Sets the file.
     * 
     * @param file the new file
     */
    public void setFile(UploadedFile file) {
        this.file = file;
    }

    /**
     * Gets the file name.
     * 
     * @return the file name
     */
    public String getFileName() {
        String fileName = null;
        if (file != null) {
            fileName = FilenameUtils.getName(file.getFileName());
        }
        return fileName;
    }
}
