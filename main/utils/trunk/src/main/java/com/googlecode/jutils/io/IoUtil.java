package com.googlecode.jutils.io;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import com.googlecode.jutils.StringUtil;
import com.googlecode.jutils.collection.ArrayUtil;
import com.googlecode.jutils.collection.CollectionUtil;

/**
 * Class IoUtil.
 */
public final class IoUtil extends IOUtils {

	/**
	 * Getter : return the zip entry names.
	 * 
	 * @param inputStream
	 *            the input stream
	 * @param extensions
	 *            the extensions
	 * @return the zip entry names
	 * @throws IOException
	 *             the iO exception
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
	 * @param inputStream
	 *            the input stream
	 * @param zipEntryName
	 *            the zip entry name
	 * @return the byte[]
	 * @throws IOException
	 *             the iO exception
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
	 * @param inputStream
	 *            the input stream
	 * @param extensions
	 *            the extensions
	 * @return the map
	 * @throws IOException
	 *             the iO exception
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
	 * Mkdir.
	 * 
	 * @param parent
	 *            the parent
	 * @param dirName
	 *            the dir name
	 * @return the file
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static File mkdir(File parent, String dirName) throws IOException {
		final File destination = new File(parent, dirName);

		return mkdir(destination);
	}

	/**
	 * Mkdir.
	 * 
	 * @param parent
	 *            the parent
	 * @param dirName
	 *            the dir name
	 * @return the file
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static File mkdir(String parent, String dirName) throws IOException {
		final File destination = new File(parent, dirName);

		return mkdir(destination);
	}

	/**
	 * Mkdir.
	 * 
	 * @param dirName
	 *            the dir name
	 * @return the file
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static File mkdir(String dirName) throws IOException {
		return mkdir(".", dirName);
	}

	/**
	 * Mkdir.
	 * 
	 * @param file
	 *            the file
	 * @return the file
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static File mkdir(File file) throws IOException {
		if (file != null) {
			if (file.isFile()) {
				throw new IOException("Can't create directory, a file with the name " + file + "already exists");
			}
			FileUtils.forceMkdir(file);
		}
		return file;
	}

	/**
	 * Gets the relative path.
	 * 
	 * @param rootDir
	 *            the root dir
	 * @param file
	 *            the file
	 * @return the relative path
	 */
	public static String getRelativePath(File rootDir, File file) {
		String relativePath = null;
		if (rootDir != null && file != null) {
			relativePath = getRelativePath(rootDir.getAbsolutePath(), file.getAbsolutePath());
		}
		return relativePath;
	}

	/**
	 * Gets the relative path.
	 * 
	 * @param rootDir
	 *            the root dir
	 * @param file
	 *            the file
	 * @return the relative path
	 */
	public static String getRelativePath(String rootDir, String file) {
		String relativePath = null;
		if (rootDir != null && file != null) {
			final String pathSeparator = "/";
			final String rootPath = FilenameUtils.normalize(rootDir, true);
			final String filePath = FilenameUtils.normalize(file, true);

			final String[] base = rootPath.split(Pattern.quote(pathSeparator));
			final String[] target = filePath.split(Pattern.quote(pathSeparator));

			int commonIndex = 0;
			while (commonIndex < target.length && commonIndex < base.length && target[commonIndex].equals(base[commonIndex])) {
				commonIndex++;
			}

			if (commonIndex == 0) {
				throw new IllegalArgumentException("No common path element found for '" + rootPath + "' and '" + filePath + "'");
			}

			final StringBuilder relative = new StringBuilder();
			for (int index = commonIndex; index < base.length; index++) {
				relative.append(".." + pathSeparator);
			}

			for (int index = commonIndex; index < target.length; index++) {
				relative.append(target[index]);
				if (index + 1 < target.length) {
					relative.append(pathSeparator);
				}
			}

			relativePath = relative.toString();
		}
		return relativePath;
	}

	/**
	 * Matches.
	 * 
	 * @param file
	 *            the file
	 * @param matcher
	 *            the matcher
	 * @return true, if successful
	 */
	public static boolean matches(String file, String matcher) {
		boolean matches = false;
		if (file != null && !StringUtil.isBlank(matcher)) {
			matches = matches(new File(file), matcher);
		}
		return matches;
	}

	/**
	 * Matches.
	 * 
	 * @param file
	 *            the file
	 * @param regex
	 *            the regex
	 * @return true, if successful
	 */
	public static boolean matches(File file, String regex) {
		boolean matches = false;
		if (file != null && !StringUtil.isBlank(regex)) {
			final List<String> children = wildcardMatch(file, regex);

			matches = !CollectionUtil.isEmpty(children);
		}
		return matches;
	}

	/**
	 * Wildcard match.
	 * 
	 * @param file
	 *            the file
	 * @param matcher
	 *            the matcher
	 * @return the list
	 */
	public static List<String> wildcardMatch(String file, String matcher) {
		List<String> matches = null;
		if (!StringUtil.isBlank(file) && !StringUtil.isBlank(matcher)) {
			matches = wildcardMatch(new File(file), matcher);
		}
		return matches;
	}

	/**
	 * Wildcard match.
	 * 
	 * @param file
	 *            the file
	 * @param matcher
	 *            the matcher
	 * @return the list
	 */
	public static List<String> wildcardMatch(File file, String matcher) {
		List<String> matches = null;
		if (file != null && !StringUtil.isBlank(matcher)) {
			matches = new ArrayList<String>();

			final String regex = matcher.replace("**", "*");
			if (file.isDirectory()) {
				for (final File child : file.listFiles()) {
					final List<String> childMatches = wildcardMatch(child, matcher);
					if (!CollectionUtil.isEmpty(childMatches)) {
						matches.addAll(childMatches);
					}
				}
			} else {
				final String filePath = FilenameUtils.normalize(file.getAbsolutePath(), true);
				if (FilenameUtils.wildcardMatch(filePath, regex)) {
					matches.add(file.getAbsolutePath());
				}
			}
		}
		return matches;
	}

	public static Map<String, List<File>> mapByExtension(File... files) {
		Map<String, List<File>> map = null;
		if (!ArrayUtil.isEmpty(files)) {
			map = mapByExtension(Arrays.asList(files));
		}
		return map;
	}

	public static Map<String, List<File>> mapByExtension(List<File> files) {
		Map<String, List<File>> map = null;
		if (!CollectionUtil.isEmpty(files)) {
			map = new HashMap<String, List<File>>();

			for (final File file : files) {
				String key = null;
				if (file.isFile()) {
					key = FilenameUtils.getExtension(file.getName());
					if (!StringUtil.isBlank(key)) {
						key = key.toLowerCase();
					}
				} else {
					key = "directory";
				}

				List<File> values = map.get(key);
				if (values == null) {
					values = new ArrayList<File>();
					map.put(key, values);
				}

				values.add(file);
			}
		}

		return map;
	}
}
