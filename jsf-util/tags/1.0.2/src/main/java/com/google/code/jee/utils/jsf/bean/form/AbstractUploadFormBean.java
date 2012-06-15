package com.google.code.jee.utils.jsf.bean.form;

import java.io.Serializable;

import javax.faces.event.ActionEvent;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.code.jee.utils.dal.dto.Dto;
import com.google.code.jee.utils.dal.service.GenericService;
import com.google.code.jee.utils.jsf.bean.upload.UploadBean;

@SuppressWarnings("serial")
public abstract class AbstractUploadFormBean<PK extends Serializable, E extends Dto<PK>, S extends GenericService<PK, E>>
        extends AbstractFormBean<PK, E, S> {
    protected String allowTypes;
    protected String sizeLimit;

    @Autowired
    protected UploadBean uploadBean;

    public AbstractUploadFormBean() {
        super();
    }

    @Override
    protected void init() {
        super.init();
        if (uploadBean != null) {
            uploadBean.reInit();
        }
    }

    public String getAllowTypes() {
        return allowTypes;
    }

    public String getSizeLimit() {
        return sizeLimit;
    }

    public void saveBeforeUpload(ActionEvent event) {
        // do nothing but required
    }

    public void cancel(ActionEvent event) {
        redirectToList();
    }

    protected abstract boolean uploadFile();

    public abstract String getInvalidFileMessage();

    public abstract String getInvalidSizeMessage();
}