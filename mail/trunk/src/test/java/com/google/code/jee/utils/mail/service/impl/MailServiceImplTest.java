package com.google.code.jee.utils.mail.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.google.code.jee.utils.mail.exception.MailServiceException;
import com.google.code.jee.utils.mail.service.impl.MailServiceImpl;

public class MailServiceImplTest {
    public static void main(String[] args) throws IOException {
        ApplicationContext context = new ClassPathXmlApplicationContext("springMail.xml");
        MailServiceImpl mailServiceImpl = (MailServiceImpl) context.getBean("mailServiceImpl");
        
        List<String> recipients = new ArrayList<String>();
        recipients.add("m.assem@technologyandstrategy.com");
        
        
        List<String> carbonCopies = new ArrayList<String>();
        carbonCopies.add("mehdi.assem@gmail.com");
        
        List<String> blindCarbonCopies = new ArrayList<String>();
        blindCarbonCopies.add("mehdi.assem@gmail.com");
        
        List<InputStream> inputStreams = new ArrayList<InputStream>();
        FileInputStream file = null;
        String currentDirectoryPath = new File(".").getCanonicalPath();
        try {
            file = new FileInputStream(currentDirectoryPath + "/src/test/resources/image/logo_t&s.png");
        } catch (FileNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        inputStreams.add(file);
        try {
            mailServiceImpl.sendMail("mehdi.assem@gmail.com", "m.assem@technologyandstrategy.com", recipients, carbonCopies, blindCarbonCopies, "", "text", true, inputStreams);
        } catch (MailServiceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
