import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;

import com.google.code.jee.utils.sftp.SftpUtil;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import com.zehon.exception.FileTransferException;

/**
 * Read me :
 * 
 * If you want to use this program, you need to follow these steps : 
 * 1) Create a directory named "ftp" at the root of your C: directory 
 * 2) Launch "msftpsrvr.exe" located in "src/test/resources" 
 * 3) Once the program is launched, enter the following informations 
 * - User : test 
 * - Password : test 
 * - Port : 22 
 * - Root path : C:/ftp 
 *  Then, click on the start button. 
 * 4) Launch the Demo.java program
 * 
 */
public class Demo {

    public static void main(String[] args) throws JSchException, SftpException, IOException, FileTransferException {
        // Init the SFTP connexion
        ChannelSftp channel = (ChannelSftp) SftpUtil.getChannel("sftp", "localhost", 22, "test", "test");

        // Tree creation
        boolean creation = SftpUtil.createDirectory(channel, "t&s", "/users");
        creation = SftpUtil.createDirectory(channel, "images", "./t&s/files");
        creation = SftpUtil.createDirectory(channel, "logos", "./images");
        creation = SftpUtil.createDirectory(channel, "documents", "/users/t&s/files");

        if (creation) {
            System.out.println("Creation successful");
        }

        // Renames a directory
        boolean rename = SftpUtil.renameDirectory(channel, "users", "home", "/");

        if (rename) {
            System.out.println("Directory's name changed");
        }

        // Upload a file
        File file = new File("src/test/resources/logo.jpg");
        boolean upload = SftpUtil.uploadFile(channel, new FileInputStream(file), "logo.jpg",
                "/home/t&s/files/images/logos");

        if (upload) {
            System.out.println("File uploaded");
        }
        
        // Rename the file
        boolean renameFile = SftpUtil.renameFile(channel, "logo.jpg", "logo_t&s.jpg", "/home/t&s/files/images/logos");
        
        if(renameFile) {
            System.out.println("File renamed properly");
        }

        // Download the file
        InputStream input = SftpUtil.downloadFile(channel, "/home/t&s/files/images/logos", "logo_t&s.jpg");

        if (input != null) {
            System.out.println("File downloaded");
        }

        // write the inputStream to a FileOutputStream
        OutputStream output = new FileOutputStream(new File("src/test/resources/newFile.jpg"));

        IOUtils.copy(input, output);

        // Deletes the file
        boolean deleteFile = SftpUtil.deleteFile(channel, "logo_t&s.jpg", "/home/t&s/files/images/logos");
        
        if(deleteFile) {
            System.out.println("File has been deleted");
        }
        
        // Deletes the directory
        channel.cd("/home");
         boolean deleteDirectory = SftpUtil.deleteDirectory(channel, "files", "./t&s");
        
        if(deleteDirectory) {
            System.out.println("Directory has been deleted");
        }
        
        // Close the connexion
        SftpUtil.closeChannel(channel);
    }
}