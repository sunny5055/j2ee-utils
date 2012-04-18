package com.google.code.jee.utils.mail.service.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.google.code.jee.utils.mail.exception.MailServiceException;
import com.google.code.jee.utils.mail.impl.MailServiceImpl;

public class MailServiceImplTest {
    public static void main(String[] args) {
        MailServiceImpl mailServiceImpl = new MailServiceImpl();
        List<String> recipients = new ArrayList<String>();
        recipients.add("mehdi.assem@gmail.com");
        try {
            //mailServiceImpl.sendMail("", recipients, "", "", true);
            mailServiceImpl.sendMail("mehdi.assem@gmail.com", "mehdi.assem@gmail.com", recipients, recipients, recipients, "subject", "text", true, new ArrayList<InputStream>());
        } catch (MailServiceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
