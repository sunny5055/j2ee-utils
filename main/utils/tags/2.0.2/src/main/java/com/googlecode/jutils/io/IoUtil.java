package com.googlecode.jutils.io;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.IOUtils;

import com.googlecode.jutils.StringUtil;
import com.googlecode.jutils.collection.ArrayUtil;

/**
 * Class IoUtil.
 */
public final class IoUtil extends IOUtils {

    /**
     * Getter : return the zip entry names.
     * 
     * @param inputStream the input stream
     * @param extensions the extensions
     * @return the zip entry names
     * @throws IOException the iO exception
     */
    public static List<String> getZipEntryNames(InputStream inputStream, String... extensions) throws IOException {
        List<String> zipEntryNames = null;
        if (inputStream != null && !ArrayUtil.isEmpty(extensions)) {
            zipEntryNames = new ArrayList<String>();
            final ZipInputStream zipInputStream = new ZipInputStream(inputStream);
            ZipEntry currentZipEntry = null;

            while ((currentZipEntry = zipInputStream.getNextEntry()) != null) {
                if (StringUtil.endsWithAnyIgnoreCase(currentZipEntry.getName(), extensions)) {
                    zipEntryNames.add(currentZipEntry.getName());
                }
                zipInputStream.closeEntry();
            }

            zipInputStream.close();
        }
        return zipEntryNames;
    }

    /**
     * Read zip entry.
     * 
     * @param inputStream the input stream
     * @param zipEntryName the zip entry name
     * @return the byte[]
     * @throws IOException the iO exception
     */
    public static byte[] readZipEntry(InputStream inputStream, String zipEntryName) throws IOException {
        byte[] data = null;
        if (inputStream != null && !StringUtil.isBlank(zipEntryName)) {
            final ZipInputStream zipInputStream = new ZipInputStream(inputStream);
            ZipEntry currentZipEntry = null;
            while ((currentZipEntry = zipInputStream.getNextEntry()) != null) {
                if (StringUtil.equals(currentZipEntry.getName(), zipEntryName)) {
                    int n;
                    final byte[] buf = new byte[1024];

                    final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    while ((n = zipInputStream.read(buf, 0, 1024)) > -1) {
                        outputStream.write(buf, 0, n);
                    }

                    data = outputStream.toByteArray();

                    outputStream.close();
                    zipInputStream.closeEntry();
                    break;
                }
            }
            zipInputStream.close();

        }
        return data;
    }

    /**
     * Read zip entries.
     * 
     * @param inputStream the input stream
     * @param extensions the extensions
     * @return the map
     * @throws IOException the iO exception
     */
    public static Map<String, byte[]> readZipEntries(InputStream inputStream, String... extensions) throws IOException {
        Map<String, byte[]> zipEntries = null;

        if (inputStream != null && !ArrayUtil.isEmpty(extensions)) {
            zipEntries = new LinkedHashMap<String, byte[]>();
            final ZipInputStream zipInputStream = new ZipInputStream(inputStream);
            ZipEntry currentZipEntry = null;

            while ((currentZipEntry = zipInputStream.getNextEntry()) != null) {
                if (StringUtil.endsWithAnyIgnoreCase(currentZipEntry.getName(), extensions)) {
                    int n;
                    final byte[] buf = new byte[1024];

                    final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    while ((n = zipInputStream.read(buf, 0, 1024)) > -1) {
                        outputStream.write(buf, 0, n);
                    }

                    zipEntries.put(currentZipEntry.getName(), outputStream.toByteArray());

                    outputStream.close();
                    zipInputStream.closeEntry();
                }
            }

            zipInputStream.close();
        }
        return zipEntries;
    }

    /**
     * Method mkdir
     * 
     * @param parent the parent
     * @param dirName the dirName
     * @return File
     * @throws IOException the IOException
     */
    public static File mkdir(File parent, String dirName) throws IOException {
        final File destination = new File(parent, dirName);
        if (destination.isFile()) {
            throw new IOException("Can't create directory, a file with the name " + dirName + "already exists in "
                    + parent);
        }
        destination.mkdirs();
        return destination;
    }

    /**
     * Method mkdir
     * 
     * @param parent the parent
     * @param dirName the dirName
     * @return File
     * @throws IOException the IOException
     */
    public static File mkdir(String parent, String dirName) throws IOException {
        final File destination = new File(parent, dirName);
        if (destination.isFile()) {
            throw new IOException("Can't create directory, a file with the name " + dirName + "already exists in "
                    + parent);
        }
        destination.mkdirs();
        return destination;
    }

    /**
     * Method mkdir
     * 
     * @param dirName the dirName
     * @return File
     * @throws IOException the IOException
     */
    public static File mkdir(String dirName) throws IOException {
        return mkdir(".", dirName);
    }
}
