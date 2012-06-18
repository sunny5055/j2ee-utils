package com.google.code.jee.utils.sftp;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Vector;

import org.apache.commons.io.FilenameUtils;

import com.google.code.jee.utils.StringUtil;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;

/**
 * The Class SftpUtil.
 * 
 */
public final class SftpUtil {

    /**
     * The Constructor.
     */
    private SftpUtil() {
        super();
    }

    /**
     * Gets the channel.
     * 
     * @param host the host
     * @param port the port
     * @param user the user
     * @param password the password
     * @return the channel
     * @throws JSchException the Jsch exception
     */
    public static Channel getChannel(String channelType, String host, int port, String user, String password)
            throws JSchException {
        Session session = null;
        Channel channel = null;

        // Create the connexion with the appropriate parameters
        JSch jsch = new JSch();
        session = jsch.getSession(user, host, port);
        session.setPassword(password);

        // Setup Strict HostKeyChecking to no so we dont get the
        // unknown host key exception
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        session.connect();

        // Open the channel
        channel = session.openChannel(channelType);
        channel.connect();

        return channel;
    }

    /**
     * Close channel.
     * 
     * @param channel the channel
     * @return the channel
     * @throws JSchException
     */
    public static void closeChannel(Channel channel) throws JSchException {
        if (channel != null && channel.isConnected() && channel.getSession().isConnected()) {
            channel.getSession().disconnect();
            channel.disconnect();
        }
    }

    /**
     * Download file.
     * 
     * @param channel the channel
     * @param workingDirectory the working directory
     * @param fileName the file name
     * @return the input stream
     * @throws SftpException the sftp exception
     */
    public static InputStream downloadFile(ChannelSftp channel, String workingDirectory, String fileName)
            throws SftpException {
        InputStream bis = null;
        if (!StringUtil.isEmpty(workingDirectory) && !StringUtil.isEmpty(fileName)) {
            // Fix the workingDirectory by appending a "/" at the end
            if (workingDirectory.lastIndexOf("/") == workingDirectory.length() - 1) {
                workingDirectory = workingDirectory.substring(0, workingDirectory.length() - 1);
            }
            String baseName = FilenameUtils.getBaseName(workingDirectory);
            String basePath = FilenameUtils.getPath(workingDirectory);
            if (SftpUtil.isDirectoryExisting(channel, baseName, basePath)) {
                channel.cd(workingDirectory);
                bis = new BufferedInputStream(channel.get(fileName));
            }
        }
        return bis;
    }

    /**
     * Download file.
     * 
     * @param channel the channel
     * @param filePath the file path
     * @return the input stream
     * @throws SftpException the sftp exception
     */
    public static InputStream downloadFile(ChannelSftp channel, String filePath) throws SftpException {
        String fullPath = FilenameUtils.getFullPath(filePath);
        if (StringUtil.isEmpty(fullPath)) {
            fullPath = "/";
        }

        return downloadFile(channel, fullPath);
    }

    /**
     * Upload file.
     * 
     * @param channel the channel
     * @param uploadFile the upload file
     * @param workingDirectory the working directory
     * @throws SftpException the sftp exception
     * @throws FileNotFoundException the file not found exception
     */
    public static boolean uploadFile(ChannelSftp channel, InputStream file, String fileName, String workingDirectory)
            throws SftpException, FileNotFoundException {
        if (file != null && !StringUtil.isEmpty(workingDirectory) && !StringUtil.isEmpty(fileName)) {
            // Fix the workingDirectory by appending a "/" at the end
            if (workingDirectory.lastIndexOf("/") == workingDirectory.length() - 1) {
                workingDirectory = workingDirectory.substring(0, workingDirectory.length() - 1);
            }
            String baseName = FilenameUtils.getBaseName(workingDirectory);
            String basePath = FilenameUtils.getPath(workingDirectory);
            if (!SftpUtil.isDirectoryExisting(channel, baseName, basePath)) {
                createDirectory(channel, baseName, basePath);
            }
            String uploadPath = workingDirectory + "/" + fileName;
            if (!isFileExisting(channel, fileName, workingDirectory)) {
                channel.put(file, uploadPath);
            }
        }
        return isFileExisting(channel, fileName, workingDirectory);
    }

    /**
     * Upload file.
     * 
     * @param channel the channel
     * @param file the file
     * @param filePath the file path
     * @return true, if successful
     * @throws FileNotFoundException the file not found exception
     * @throws SftpException the sftp exception
     */
    public static boolean uploadFile(ChannelSftp channel, InputStream file, String filePath)
            throws FileNotFoundException, SftpException {
        return uploadFile(channel, file, FilenameUtils.getName(filePath), FilenameUtils.getFullPath(filePath));
    }

    /**
     * Creates the directory.
     * 
     * @param channel the channel
     * @param directoryName the directory name
     * @param workingDirectory the working directory
     * @throws SftpException the sftp exception
     */
    public static boolean createDirectory(ChannelSftp channel, String directoryName, String workingDirectory)
            throws SftpException {
        if (!StringUtil.isEmpty(directoryName) && !StringUtil.isEmpty(workingDirectory)) {
            // If the directory doesn't exist, we create the tree hierarchy
            // until we reach the leaf directory
            if (!isDirectoryExisting(channel, directoryName, workingDirectory)) {
                String[] paths = workingDirectory.split("/");
                String currentPath = paths[0] + "/";
                channel.cd(currentPath);
                // For each directory, we create it if he doesn't exist
                for (int i = 1; i < paths.length; i++) {
                    String path = paths[i];
                    if (!isDirectoryExisting(channel, path, currentPath)) {
                        channel.mkdir(path);
                    }
                    currentPath += path + "/";
                    channel.cd(path);
                }
                // Create the leaf directory
                channel.mkdir(directoryName);
            }
        }
        return isDirectoryExisting(channel, directoryName, workingDirectory);
    }

    /**
     * Creates the directory.
     * 
     * @param channel the channel
     * @param directoryPath the directory path
     * @return true, if successful
     * @throws SftpException the sftp exception
     */

    public static boolean createDirectory(ChannelSftp channel, String directoryPath) throws SftpException {
        String correctPath = FilenameUtils.getPath(directoryPath);
        if (directoryPath.startsWith("./")) {
            correctPath = "./" + correctPath;
        } else if (directoryPath.startsWith("/")) {
            correctPath = "/" + correctPath;
        }

        return createDirectory(channel, FilenameUtils.getBaseName(directoryPath), correctPath);
    }

    /**
     * Rename a directory.
     * 
     * @param channel the channel
     * @param oldName the old name
     * @param newName the new name
     * @param workingDirectory the working directory
     * @throws SftpException the sftp exception
     */
    public static boolean renameDirectory(ChannelSftp channel, String oldName, String newName, String workingDirectory)
            throws SftpException {
        boolean success = true;
        if (!StringUtil.isEmpty(oldName) && !StringUtil.isEmpty(newName) && !StringUtil.isEmpty(workingDirectory)) {
            if (isDirectoryExisting(channel, oldName, workingDirectory)) {
                channel.cd(workingDirectory);
                try {
                    channel.rename(oldName, newName);
                } catch (Exception e) {
                    success = false;
                }
            }
        }
        return success;
    }

    /**
     * Rename file.
     * 
     * @param channel the channel
     * @param oldName the old name
     * @param newName the new name
     * @param workingDirectory the working directory
     * @throws SftpException the sftp exception
     */
    public static boolean renameFile(ChannelSftp channel, String oldName, String newName, String workingDirectory)
            throws SftpException {
        boolean exists = true;

        if (StringUtil.isNotEmpty(oldName) && StringUtil.isNotEmpty(newName) && StringUtil.isNotEmpty(workingDirectory)) {
            // Fix the workingDirectory by appending a "/" at the end
            if (workingDirectory.lastIndexOf("/") == workingDirectory.length() - 1) {
                workingDirectory = workingDirectory.substring(0, workingDirectory.length() - 1);
            }
            if (SftpUtil.isDirectoryExisting(channel, FilenameUtils.getBaseName(workingDirectory),
                    FilenameUtils.getPath(workingDirectory))) {
                channel.cd(workingDirectory);
                try {
                    channel.rename(oldName, newName);
                } catch (Exception e) {
                    return false;
                }
            }
        }
        return exists;
    }

    /**
     * Rename file.
     * 
     * @param channel the channel
     * @param newName the new name
     * @param workingDirectory the working directory
     * @return true, if successful
     * @throws SftpException the sftp exception
     */
    public static boolean renameFile(ChannelSftp channel, String newName, String workingDirectory) throws SftpException {
        String oldName = FilenameUtils.getName(workingDirectory);
        String filePath = FilenameUtils.getPath(workingDirectory);
        if (workingDirectory.startsWith("./")) {
            filePath = "./" + filePath;
        } else if (workingDirectory.startsWith("/")) {
            filePath = "/" + filePath;
        }
        return renameFile(channel, oldName, newName, filePath);
    }

    /**
     * Deletes the directory.
     * 
     * @param channel the channel
     * @param directoryName the directory name
     * @param workingDirectory the working directory
     * @throws SftpException the sftp exception
     */
    public static boolean deleteDirectory(ChannelSftp channel, String directoryName, String workingDirectory)
            throws SftpException {
        boolean success = true;
        if (StringUtil.isNotEmpty(directoryName) && StringUtil.isNotEmpty(workingDirectory)) {
            if (isDirectoryExisting(channel, directoryName, workingDirectory)) {
                if (isDirectoryEmpty(channel, directoryName, workingDirectory)) {
                    channel.rmdir(directoryName);
                } else {
                    channel.cd(workingDirectory);
                    Vector files = channel.ls(directoryName);

                    for (int i = 0; i < files.size(); i++) {
                        LsEntry lsEntry = (LsEntry) files.get(i);

                        if (!lsEntry.getFilename().equals(".") && !lsEntry.getFilename().equals("..")) {
                            System.out.println("Downloading file" + lsEntry.getFilename());

                            channel.rm(lsEntry.getFilename());
                        }
                    }
                }
            } else {
                success = false;
            }
        }
        return success;
    }

    /**
     * Deletes the directory.
     * 
     * @param channel the channel
     * @param directoryPath the directory path
     * @return true, if successfulfd
     * @throws SftpException the sftp exception
     */
    public static boolean deleteDirectory(ChannelSftp channel, String directoryPath) throws SftpException {
        return deleteDirectory(channel, FilenameUtils.getBaseName(directoryPath),
                FilenameUtils.getFullPath(directoryPath));
    }

    /**
     * Deletes the file.
     * 
     * @param channel the channel
     * @param fileName the file name
     * @param workingDirectory the working directory
     * @throws SftpException the sftp exception
     */
    public static boolean deleteFile(ChannelSftp channel, String fileName, String workingDirectory)
            throws SftpException {
        boolean success = true;

        if (!StringUtil.isEmpty(workingDirectory) && !StringUtil.isEmpty(fileName)) {
            if (SftpUtil.isDirectoryExisting(channel, FilenameUtils.getBaseName(workingDirectory),
                    FilenameUtils.getPath(workingDirectory))) {
                channel.cd(workingDirectory);
                if (isFileExisting(channel, fileName, workingDirectory)) {
                    channel.rm(fileName);
                } else {
                    success = false;
                }
            } else {
                success = false;
            }
        } else {
            success = false;
        }

        return success;
    }

    /**
     * Deletes the file.
     * 
     * @param channel the channel
     * @param filePath the file path
     * @return true, if successful
     * @throws SftpException the sftp exception
     */
    public static boolean deleteFile(ChannelSftp channel, String filePath) throws SftpException {
        return deleteFile(channel, filePath, FilenameUtils.getFullPath(filePath));
    }

    /**
     * Change rights.
     * 
     * @param channel the channel
     * @param permissions the permissions
     * @param directoryPath the directory path
     * @throws SftpException the sftp exception
     */
    public static void changeRights(ChannelSftp channel, int permissions, String directoryName, String workingDirectory)
            throws SftpException {
        channel.cd(workingDirectory);
        channel.chmod(permissions, directoryName);
    }

    /**
     * Checks if the directory exists.
     * 
     * @param directoryName the directory name
     * @param workingDirectory the working directory
     * @return true, if is directory existing
     * @throws SftpException the sftp exception
     */
    public static boolean isDirectoryExisting(ChannelSftp channel, String directoryName, String workingDirectory)
            throws SftpException {
        boolean exists = true;

        if (StringUtil.isNotEmpty(directoryName) && StringUtil.isNotEmpty(workingDirectory)) {
            try {
                String directoryPath = "";
                if (workingDirectory.startsWith("./")) {
                    directoryPath += "./";
                } else if (!workingDirectory.startsWith("/")) {
                    directoryPath += "/";
                }
                directoryPath += FilenameUtils.separatorsToUnix(FilenameUtils.concat(workingDirectory, directoryName));
                SftpATTRS attrs = channel.lstat(directoryPath);
                exists = attrs != null && attrs.isDir();
            } catch (Exception e) {
                exists = false;
            }
        } else {
            exists = false;
        }

        return exists;
    }

    /**
     * Checks if a directory is empty.
     * 
     * @param channel the channel
     * @param directoryName the directory name
     * @param workingDirectory the working directory
     * @return true, if is directory empty
     * @throws SftpException the sftp exception
     */
    public static boolean isDirectoryEmpty(ChannelSftp channel, String directoryName, String workingDirectory)
            throws SftpException {
        boolean empty = true;

        if (StringUtil.isNotEmpty(directoryName) && StringUtil.isNotEmpty(workingDirectory)) {
            if (channel.ls(directoryName).size() != 0) {
                empty = false;
            }
        }
        return empty;
    }

    /**
     * Checks if the directory exists.
     * 
     * @param channel the channel
     * @param directoryPath the directory path
     * @return true, if is directory existing
     * @throws SftpException the sftp exception
     */
    public static boolean isDirectoryExisting(ChannelSftp channel, String directoryPath) throws SftpException {
        boolean exists = true;
        if (StringUtil.isNotEmpty(directoryPath)) {
            try {
                SftpATTRS attrs = channel.stat(directoryPath);
                exists = attrs != null && attrs.isDir();
            } catch (Exception e) {
                exists = false;
            }
        } else {
            exists = false;
        }

        return exists;
    }

    /**
     * Checks if the file exists.
     * 
     * @param channel the channel
     * @param fileName the fileName
     * @param workingDirectory the working directory
     * @return true, if is file existing
     * @throws SftpException the sftp exception
     */
    public static boolean isFileExisting(ChannelSftp channel, String fileName, String workingDirectory)
            throws SftpException {
        boolean exists = true;
        if (!StringUtil.isEmpty(workingDirectory) && !StringUtil.isEmpty(fileName)) {
            try {
                String filePath = "";
                if (workingDirectory.startsWith("./")) {
                    filePath += "./";
                } else if (!workingDirectory.startsWith("/")) {
                    filePath += "/";
                }
                filePath += FilenameUtils.separatorsToUnix(FilenameUtils.concat(workingDirectory, fileName));
                SftpATTRS attrs = channel.lstat(filePath);
                exists = attrs != null && fileName.contains(".");
            } catch (Exception e) {
                exists = false;
            }
        } else {
            exists = false;
        }
        return exists;
    }

    /**
     * Checks if the file exists.
     * 
     * @param channel the channel
     * @param filePath the file path
     * @return true, if is file existing
     * @throws SftpException the sftp exception
     */

    public static boolean isFileExisting(ChannelSftp channel, String filePath) throws SftpException {
        boolean exists = true;
        if (!StringUtil.isEmpty(filePath)) {
            try {
                SftpATTRS attrs = channel.stat(filePath);
                exists = attrs != null && FilenameUtils.getName(filePath).contains(".");
            } catch (Exception e) {
                exists = false;
            }
        } else {
            exists = false;
        }
        return exists;
    }
}
