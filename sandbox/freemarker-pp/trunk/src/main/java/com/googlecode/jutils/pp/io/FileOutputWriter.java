package com.googlecode.jutils.pp.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.jutils.StringUtil;
import com.googlecode.jutils.collection.CollectionUtil;
import com.googlecode.jutils.io.IoUtil;
import com.googlecode.jutils.pp.formatter.Formatter;
import com.googlecode.jutils.pp.formatter.JavaFormatter;
import com.googlecode.jutils.pp.formatter.XmlFormatter;
import com.googlecode.jutils.pp.formatter.exception.FormatterException;
import com.googlecode.jutils.pp.settings.Settings;

public class FileOutputWriter extends Writer {
	protected static final Logger LOGGER = LoggerFactory.getLogger(FileOutputWriter.class);
	private static final int BUFFER_SIZE = 160; // large buffer slows down

	private File dstFile;
	private Integer freeBuffer;
	private StringBuilder buffer;
	private Writer fileWriter;
	private Settings settings;
	private List<Formatter> formatters;

	public FileOutputWriter(Settings settings, File dstFile) {
		super();
		this.settings = settings;
		this.dstFile = dstFile;
		this.buffer = new StringBuilder(BUFFER_SIZE);
		this.freeBuffer = BUFFER_SIZE;

		this.formatters = new ArrayList<Formatter>();
		this.formatters.add(new XmlFormatter());
		this.formatters.add(new JavaFormatter());
	}

	@Override
	public void write(String data) throws IOException {
		if (fileWriter != null) {
			fileWriter.write(data);
		} else {
			final int ln = data.length();
			if (ln <= freeBuffer) {
				buffer.append(data);
				freeBuffer -= ln;
			} else {
				createFileWriter();
				fileWriter.write(buffer.toString());
				buffer = null;
				fileWriter.write(data);
			}
		}
	}

	@Override
	public void write(char[] data, int off, int len) throws IOException {
		if (fileWriter != null) {
			fileWriter.write(data, off, len);
		} else {
			if (buffer != null) {
				final int ln = data.length;

				if (ln <= freeBuffer) {
					buffer.append(data, off, len);
					freeBuffer -= ln;
				} else {
					createFileWriter();
					fileWriter.write(buffer.toString());
					buffer = null;
					fileWriter.write(data, off, len);
				}
			}
		}
	}

	@Override
	public void flush() throws IOException {
		if (buffer != null && buffer.length() > 0) {
			if (fileWriter == null) {
				createFileWriter();
			}
			fileWriter.write(buffer.toString());
			fileWriter.flush();

			buffer = null;
		}
	}

	@Override
	public void close() throws IOException {
		if (fileWriter != null || buffer != null && buffer.length() != 0) {
			try {
				flush();
			} finally {
				if (fileWriter != null) {
					fileWriter.close();
				}
			}

			formatFile(dstFile);
		}
		fileWriter = null;
	}

	private void formatFile(File file) throws IOException {
		if (file != null && file.exists()) {
			final String extension = FilenameUtils.getExtension(file.getName());
			final Formatter formatter = getFormatter(extension);
			if (formatter != null) {
				try {
					formatter.format(file);
				} catch (final FormatterException e) {
					throw new IOException(e);
				}
			}
		}
	}

	private Formatter getFormatter(String key) {
		Formatter formatter = null;
		if (!StringUtil.isBlank(key)) {
			if (!CollectionUtil.isEmpty(formatters)) {
				for (final Formatter f : formatters) {
					if (f.accept(key)) {
						formatter = f;
						break;
					}
				}
			}
		}
		return formatter;
	}

	public void dropOutputFile() throws IOException {
		if (fileWriter != null) {
			fileWriter.close();
			if (dstFile.isFile()) {
				dstFile.delete();
			}
		}

		fileWriter = null;
		buffer = null;
	}

	public void changeOutputFile(String newName) throws IOException {
		final File newDst = getNewFile(newName);
		if (dstFile.equals(newDst)) {
			return;
		}

		// flush();
		// if (fileWriter != null) {
		// fileWriter.close();
		// }
		close();

		initOutputBufferAndWriter();

		dstFile = newDst;
	}

	public void renameOutputFile(String newName) throws IOException {
		final File newDst = renameFile(newName);
		if (dstFile.equals(newDst)) {
			return;
		}

		flush();
		if (fileWriter != null) {
			fileWriter.close();
		}

		initOutputBufferAndWriter();

		dstFile = newDst;
	}

	private File renameFile(String newName) throws IOException {
		File file = null;
		if (!StringUtil.isBlank(newName)) {
			final String relativePath = IoUtil.getRelativePath(settings.getOutputDirectory(), dstFile.getParentFile());
			file = new File(settings.getOutputDirectory(), FilenameUtils.concat(relativePath, newName));
		}
		return file;
	}

	private File getNewFile(String newName) throws IOException {
		File file = null;
		if (!StringUtil.isBlank(newName)) {
			file = new File(settings.getOutputDirectory(), newName);
		}
		return file;
	}

	private void createFileWriter() throws IOException {
		final File parentFile = dstFile.getParentFile();
		if (!parentFile.exists()) {
			try {
				IoUtil.mkdir(parentFile);
			} catch (final IOException e) {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug(e.getMessage(), e);
				}
			}
		}

		fileWriter = new BufferedWriter(new FileWriter(dstFile));
	}

	private void initOutputBufferAndWriter() {
		fileWriter = null;
		if (buffer == null) {
			buffer = new StringBuilder(BUFFER_SIZE);
		} else {
			buffer.delete(0, buffer.length());
		}
		freeBuffer = BUFFER_SIZE;
	}

	public File getOutputFile() {
		return dstFile;
	}
}
