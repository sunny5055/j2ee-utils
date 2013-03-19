package com.googlecode.jutils.spring;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import org.springframework.core.io.ClassRelativeResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.ResourceUtils;

import com.googlecode.jutils.StringUtil;
import com.googlecode.jutils.io.IoUtil;

public class ResourceUtil {

	public static Resource getResource(String location) throws IOException {
		Resource resource = null;
		if (!StringUtil.isBlank(location)) {
			final ResourceLoader resourceLoader = new ClassRelativeResourceLoader(ResourceUtil.class);
			resource = resourceLoader.getResource(location);
		}
		return resource;
	}

	public static String getContent(String location) throws IOException {
		String content = null;
		final Resource resource = getResource(location);
		if (resource != null) {
			content = getContent(resource);
		}
		return content;
	}

	public static String getContent(Resource resource) throws IOException {
		String content = null;
		if (resource != null) {
			Reader reader = null;
			try {
				reader = new InputStreamReader(resource.getInputStream());
				content = IoUtil.toString(reader);
			} finally {
				if (reader != null) {
					reader.close();
				}
			}
		}
		return content;
	}

	public static Reader getReader(String location) throws IOException {
		Reader reader = null;
		if (!StringUtil.isBlank(location)) {
			final ResourceLoader resourceLoader = new ClassRelativeResourceLoader(ResourceUtil.class);
			final Resource resource = resourceLoader.getResource(location);
			if (resource.exists()) {
				reader = new InputStreamReader(resource.getInputStream());
			}
		}
		return reader;
	}

	public static File getFile(String location) throws IOException {
		File file = null;
		if (!StringUtil.isBlank(location)) {
			final ResourceLoader resourceLoader = new ClassRelativeResourceLoader(ResourceUtil.class);
			final Resource resource = resourceLoader.getResource(location);
			if (resource.exists() && isFile(location)) {
				file = resource.getFile();
			}
		}
		return file;
	}

	public static boolean isFile(String location) throws IOException {
		boolean file = false;
		if (!StringUtil.isBlank(location)) {
			final ResourceLoader resourceLoader = new ClassRelativeResourceLoader(ResourceUtil.class);
			final Resource resource = resourceLoader.getResource(location);
			file = isFile(resource);
		}
		return file;
	}

	public static boolean isFile(Resource resource) throws IOException {
		boolean file = false;
		if (resource != null) {
			if (ResourceUtils.isFileURL(resource.getURL())) {
				file = true;
			}
		}
		return file;
	}
}
