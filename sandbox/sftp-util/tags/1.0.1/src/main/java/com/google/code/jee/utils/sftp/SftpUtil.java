package com.google.code.jee.utils.sftp;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Vector;

import org.apache.commons.io.FilenameUtils;

import com.google.code.jee.utils.StringUtil;
import com.google.code.jee.utils.collection.ArrayUtil;
import com.google.code.jee.utils.collection.CollectionUtil;
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
    public static Channel getChannel(String channelType, String host, Integer port, String user, String password)
            throws JSchException {
        Channel channel = null;
        if (!StringUtil.isBlank(channelType) && !StringUtil.isBlank(host) && port != null && !StringUtil.isBlank(user)
                && !StringUtil.isBlank(password)) {
            final JSch jsch = new JSch();
            final Session session = jsch.getSession(user, host, port);
            session.setPassword(password);

            final Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();

            channel = session.openChannel(channelType);
            channel.connect();
        }

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
     * Checks if the element exists.
     * 
     * @param workingDirectory the working directory
     * @param elementName the element name
     * 
     * @return true, if element exists
     * @throws SftpException the sftp exception
     */
    public static boolean exist(ChannelSftp channel, String workingDirectory, String elementName) throws SftpException {
        boolean exists = false;
        if (!StringUtil.isBlank(workingDirectory) && !StringUtil.isBlank(elementName)) {
            String elementPath = FilenameUtils.concat(workingDirectory, elementName);
            elementPath = FilenameUtils.separatorsToUnix(elementPath);

            exists = exist(channel, elementPath);
        }

        return exists;
    }

    /**
     * Checks if the element exists.
     * 
     * @param channel the channel
     * @param elementPath the element path
     * @return true, if element exists
     * @throws SftpException the sftp exception
     */

    public static boolean exist(ChannelSftp channel, String elementPath) throws SftpException {
        boolean exists = false;
        if (channel != null && !StringUtil.isBlank(elementPath) && StringUtil.startsWith(elementPath, "/")) {
            try {
                final SftpATTRS attrs = channel.stat(elementPath);
                exists = attrs != null;
            } catch (final SftpException e) {
                exists = false;
            }
        }

        return exists;
    }

    /**
     * Creates the directory.
     * 
     * @param channel the channel
     * @param workingDirectory the working directory
     * @param directoryName the directory name
     * @throws SftpException the sftp exception
     */
    public static boolean createDirectory(ChannelSftp channel, String workingDirectory, String directoryName)
            throws SftpException {
        boolean created = false;
        if (!StringUtil.isBlank(workingDirectory) && !StringUtil.isBlank(directoryName)) {
            String directoryPath = FilenameUtils.concat(workingDirectory, directoryName);
            directoryPath = FilenameUtils.separatorsToUnix(directoryPath);

            created = createDirectory(channel, directoryPath);
        }

        return created;
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
        boolean created = false;
        if (channel != null && !StringUtil.isBlank(directoryPath) && StringUtil.startsWith(directoryPath, "/")) {
            if (!exist(channel, directoryPath)) {
                created = true;
                final String[] paths = StringUtil.split(directoryPath, "/");
                if (!ArrayUtil.isEmpty(paths)) {
                    String currentPath = "/";

                    channel.cd(currentPath);

                    for (final String path : paths) {
                        if (!exist(channel, currentPath, path)) {
                            try {
                                channel.mkdir(path);
                            } catch (final SftpException e) {
                                created = false;
                            }
                        }
                        currentPath += path + "/";
                        channel.cd(path);
                    }
                }
            }
        }

        return created;
    }

    /**
     * Deletes the element.
     * 
     * @param channel the channel
     * @param element the element
     * @return true, if successfulf
     * @throws SftpException the sftp exception
     */
    public static boolean delete(ChannelSftp channel, String element) throws SftpException {
        boolean deleted = false;
        if (!StringUtil.isBlank(element)) {
            final String workingDirectory = getPathNoEndSeparator(element);
            final String elementName = getElementName(element);

            deleted = delete(channel, workingDirectory, elementName);
        }

        return deleted;
    }

    /**
     * Deletes the element.
     * 
     * @param channel the channel
     * @param workingDirectory the working directory
     * @param elementName the element name
     * @throws SftpException the sftp exception
     */
    @SuppressWarnings("unchecked")
    public static boolean delete(ChannelSftp channel, String workingDirectory, String elementName) throws SftpException {
        boolean deleted = false;
        if (channel != null && !StringUtil.isBlank(workingDirectory) && StringUtil.startsWith(workingDirectory, "/")
                && !StringUtil.isBlank(elementName)) {
            if (exist(channel, workingDirectory, elementName)) {
                if (isDirectory(channel, workingDirectory, elementName)) {
                    if (isDirectoryEmpty(channel, workingDirectory, elementName)) {
                        channel.cd(workingDirectory);
                        channel.rmdir(elementName);
                        deleted = true;
                    } else {
                        String directoryPath = FilenameUtils.concat(workingDirectory, elementName);
                        directoryPath = FilenameUtils.separatorsToUnix(directoryPath);

                        channel.cd(workingDirectory);
                        final Vector<LsEntry> files = channel.ls(elementName);
                        if (!CollectionUtil.isEmpty(files)) {
                            for (final LsEntry file : files) {
                                if (!file.getFilename().equals(".") && !file.getFilename().equals("..")) {
                                    deleted = delete(channel, directoryPath, file.getFilename());
                                }

                                if (!deleted) {
                                    break;
                                }
                            }

                            if (deleted) {
                                deleted = false;
                                channel.cd(workingDirectory);
                                channel.rmdir(elementName);
                                deleted = true;
                            }
                        }
                    }
                } else {
                    channel.cd(workingDirectory);
                    channel.rm(elementName);
                    deleted = true;
                }
            }
        }

        return deleted;
    }

    /**
     * Rename an element.
     * 
     * @param channel the channel
     * @param oldElement the old element
     * @param newName the new name
     * @throws SftpException the sftp exception
     */
    public static boolean rename(ChannelSftp channel, String oldElement, String newName) throws SftpException {
        boolean renamed = false;
        if (channel != null && !StringUtil.isBlank(oldElement) && StringUtil.startsWith(oldElement, "/")
                && !StringUtil.isBlank(newName)) {
            final String workingDirectoryOld = getPathNoEndSeparator(oldElement);
            final String oldName = getElementName(oldElement);

            renamed = rename(channel, workingDirectoryOld, oldName, newName);
        }
        return renamed;
    }

    /**
     * Rename an element.
     * 
     * @param channel the channel
     * @param workingDirectory the working directory
     * @param oldName the old name
     * @param newName the new name
     * @throws SftpException the sftp exception
     */
    public static boolean rename(ChannelSftp channel, String workingDirectory, String oldName, String newName)
            throws SftpException {
        boolean renamed = false;
        if (channel != null && !StringUtil.isBlank(workingDirectory) && StringUtil.startsWith(workingDirectory, "/")
                && !StringUtil.isBlank(oldName) && !StringUtil.isBlank(newName)) {
            if (exist(channel, workingDirectory)) {
                String oldElement = FilenameUtils.concat(workingDirectory, oldName);
                oldElement = FilenameUtils.separatorsToUnix(oldElement);

                String newElement = FilenameUtils.concat(workingDirectory, newName);
                newElement = FilenameUtils.separatorsToUnix(newElement);

                if (exist(channel, oldElement)) {
                    try {
                        channel.cd(workingDirectory);
                        channel.rename(oldElement, newElement);
                        renamed = true;
                    } catch (final SftpException e) {
                        renamed = false;
                    }
                }
            }
        }
        return renamed;
    }

    /**
     * Move.
     * 
     * @param channel the channel
     * @param oldElement the old element
     * @param newElement the new element
     * @return true, if successful
     * @throws SftpException the sftp exception
     */
    public static boolean move(ChannelSftp channel, String oldElement, String newElement) throws SftpException {
        boolean copied = false;
        if (channel != null && !StringUtil.isBlank(oldElement) && StringUtil.startsWith(oldElement, "/")
                && !StringUtil.isBlank(newElement) && StringUtil.startsWith(newElement, "/")) {

            if (exist(channel, oldElement)) {
                try {
                    channel.rename(oldElement, newElement);
                    copied = true;
                } catch (final SftpException e) {
                    copied = false;
                }
            }
        }
        return copied;
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
        InputStream inputStream = null;
        if (!StringUtil.isBlank(filePath)) {
            final String workingDirectory = getPathNoEndSeparator(filePath);
            final String fileName = getElementName(filePath);

            inputStream = downloadFile(channel, workingDirectory, fileName);
        }
        return inputStream;
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
        InputStream inputStream = null;
        if (channel != null && !StringUtil.isEmpty(workingDirectory) && StringUtil.startsWith(workingDirectory, "/")
                && !StringUtil.isEmpty(fileName)) {
            if (exist(channel, workingDirectory, fileName)) {
                channel.cd(workingDirectory);
                inputStream = new BufferedInputStream(channel.get(fileName));
            }
        }
        return inputStream;
    }

    /**
     * Upload file.
     * 
     * @param channel the channel
     * @param inputStream the file
     * @param filePath the file path
     * @return true, if successful
     * @throws FileNotFoundException the file not found exception
     * @throws SftpException the sftp exception
     */
    public static boolean uploadFile(ChannelSftp channel, InputStream inputStream, String filePath)
            throws FileNotFoundException, SftpException {
        boolean upload = false;
        if (!StringUtil.isBlank(filePath)) {
            final String workingDirectory = getPathNoEndSeparator(filePath);
            final String fileName = getElementName(filePath);

            upload = uploadFile(channel, inputStream, workingDirectory, fileName);
        }
        return upload;
    }

    /**
     * Upload file.
     * 
     * @param channel the channel
     * @param workingDirectory the working directory
     * @param uploadFile the upload file
     * @throws SftpException the sftp exception
     * @throws FileNotFoundException the file not found exception
     */
    public static boolean uploadFile(ChannelSftp channel, InputStream inputStream, String workingDirectory,
            String fileName) throws SftpException, FileNotFoundException {
        boolean upload = false;
        if (channel != null && inputStream != null && !StringUtil.isEmpty(workingDirectory)
                && StringUtil.startsWith(workingDirectory, "/") && !StringUtil.isEmpty(fileName)) {
            boolean exist = exist(channel, workingDirectory);
            if (!exist) {
                exist = createDirectory(channel, workingDirectory);
            }

            if (exist) {
                String uploadPath = FilenameUtils.concat(workingDirectory, fileName);
                uploadPath = FilenameUtils.separatorsToUnix(uploadPath);

                channel.put(inputStream, uploadPath);
                upload = exist(channel, uploadPath);
            }
        }
        return upload;
    }

    /**
     * Checks if is directory.
     * 
     * @param channel the channel
     * @param workingDirectory the working directory
     * @param elementName the element name
     * @return true, if is directory
     * @throws SftpException the sftp exception
     */
    public static boolean isDirectory(ChannelSftp channel, String workingDirectory, String elementName)
            throws SftpException {
        boolean directory = false;
        if (!StringUtil.isBlank(workingDirectory) && !StringUtil.isBlank(elementName)) {
            String elementPath = FilenameUtils.concat(workingDirectory, elementName);
            elementPath = FilenameUtils.separatorsToUnix(elementPath);
            directory = isDirectory(channel, elementPath);
        }

        return directory;
    }

    /**
     * Checks if is directory.
     * 
     * @param channel the channel
     * @param elementPath the element path
     * @return true, if is directory
     * @throws SftpException the sftp exception
     */
    public static boolean isDirectory(ChannelSftp channel, String elementPath) throws SftpException {
        boolean directory = false;
        if (channel != null && !StringUtil.isBlank(elementPath) && StringUtil.startsWith(elementPath, "/")) {
            try {
                final SftpATTRS attrs = channel.stat(elementPath);
                directory = attrs != null && attrs.isDir();
            } catch (final SftpException e) {
                directory = false;
            }
        }

        return directory;
    }

    /**
     * Checks if a directory is empty.
     * 
     * @param channel the channel
     * @param directoryPath the directory path
     * @return true, if is directory empty
     * @throws SftpException the sftp exception
     */
    public static boolean isDirectoryEmpty(ChannelSftp channel, String directoryPath) throws SftpException {
        boolean empty = false;
        if (!StringUtil.isBlank(directoryPath)) {
            final String workingDirectory = getPathNoEndSeparator(directoryPath);
            final String diretoryName = getElementName(directoryPath);

            empty = isDirectoryEmpty(channel, workingDirectory, diretoryName);
        }

        return empty;
    }

    /**
     * Checks if a directory is empty.
     * 
     * @param channel the channel
     * @param workingDirectory the working directory
     * @param directoryName the directory name
     * @return true, if is directory empty
     * @throws SftpException the sftp exception
     */
    public static boolean isDirectoryEmpty(ChannelSftp channel, String workingDirectory, String directoryName)
            throws SftpException {
        boolean empty = false;
        if (channel != null && isDirectory(channel, workingDirectory, directoryName)) {
            channel.cd(workingDirectory);
            empty = channel.ls(directoryName).size() == 0;
        }
        return empty;
    }

    /**
     * Gets the element name.
     * 
     * @param elementeName the element name
     * @return the element name
     */
    private static String getElementName(String elementName) {
        String newElementName = null;
        if (!StringUtil.isBlank(elementName)) {
            newElementName = elementName;
            if (StringUtil.endsWith(newElementName, "/")) {
                newElementName = StringUtil.substring(newElementName, 0, StringUtil.lastIndexOf(newElementName, "/"));
            }

            newElementName = FilenameUtils.getName(newElementName);
        }
        return newElementName;
    }

    /**
     * Gets the path no end separator.
     * 
     * @param path the path
     * @return the path no end separator
     */
    private static String getPathNoEndSeparator(String path) {
        String newPath = null;
        if (!StringUtil.isBlank(path)) {
            newPath = path;
            if (StringUtil.endsWith(newPath, "/")) {
                newPath = StringUtil.substring(newPath, 0, StringUtil.lastIndexOf(newPath, "/"));
            }

            final int index = FilenameUtils.indexOfLastSeparator(newPath);
            if (index == 0) {
                newPath = "/";
            } else if (index > 0) {
                newPath = StringUtil.substring(newPath, 0, index);
            }
        }
        return newPath;
    }
}
