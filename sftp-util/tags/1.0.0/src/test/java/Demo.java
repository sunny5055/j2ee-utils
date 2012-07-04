import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import com.google.code.jee.utils.sftp.SftpUtil;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

/**
 * Read me :
 * 
 * If you want to use this program, you need to follow these steps : 1) Create a
 * directory named "ftp" at the root of your C: directory 2) Launch
 * "msftpsrvr.exe" located in "src/test/resources" 3) Once the program is
 * launched, enter the following informations - User : test - Password : test -
 * Port : 22 - Root path : C:/ftp Then, click on the start button. 4) Launch the
 * Demo.java program
 * 
 */
public class Demo {
    private static final Logger LOGGER = Logger.getLogger(Demo.class);

    public static void main(String[] args) throws JSchException, SftpException, IOException {
        // Init the SFTP connexion
        final ChannelSftp channel = (ChannelSftp) SftpUtil.getChannel("sftp", "localhost", 22, "test", "test");

        // Tree creation
        boolean creation = SftpUtil.createDirectory(channel, "/users/t&s");
        creation = SftpUtil.createDirectory(channel, "/users/t&s/files", "images");
        creation = SftpUtil.createDirectory(channel, "/users/t&s/files", "documents");
        creation = SftpUtil.createDirectory(channel, "/users/properties/xml");

        if (creation) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Creation successful");
            }
        }

        // Renames a directory
        final boolean rename = SftpUtil.rename(channel, "/", "users", "home");

        if (rename) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Directory's name changed");
            }
        }

        // Upload a file
        final File file = new File("src/test/resources/logo.jpg");
        final boolean upload = SftpUtil.uploadFile(channel, new FileInputStream(file), "/home/t&s/files/images/logos",
                "logo.jpg");

        if (upload) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("File uploaded");
            }
        }

        // Rename the file
        final boolean renameFile = SftpUtil
                .rename(channel, "/home/t&s/files/images/logos/", "logo.jpg", "logo_t&s.jpg");

        if (renameFile) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("File renamed properly");
            }
        }

        // Download the file
        final InputStream input = SftpUtil.downloadFile(channel, "/home/t&s/files/images/logos", "logo_t&s.jpg");

        if (input != null) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("File downloaded");
            }
        }

        // Write the InputStream to a FileOutputStream
        final OutputStream output = new FileOutputStream(new File("src/test/resources/newFile.jpg"));

        IOUtils.copy(input, output);

        // Deletes the file
        final boolean deleteFile = SftpUtil.delete(channel, "/home/t&s/files/images/logos", "logo_t&s.jpg");

        if (deleteFile) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("File has been deleted");
            }
        }

        // Deletes the directory
        channel.cd("/home");

        final boolean deleteDirectory = SftpUtil.delete(channel, "/home/t&s", "files");

        if (deleteDirectory) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Directory has been deleted");
            }
        }

        // Close the connexion
        SftpUtil.closeChannel(channel);
    }
}