package com.googlecode.jutils.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import com.googlecode.jutils.io.IoUtil;

public final class HttpUtil {

	private HttpUtil() {
		super();
	}

	public static String getContent(String urlString) throws IOException {
		return getContent(new URL(urlString));
	}

	public static String getContent(URL url) throws IOException {
		String content = null;
		if (url != null) {
			final URLConnection connection = url.openConnection();
			if (connection != null) {
				InputStream inputStream = null;
				try {
					inputStream = connection.getInputStream();
					content = IoUtil.toString(inputStream);
				} finally {
					if (inputStream != null) {
						inputStream.close();
					}
				}
			}
		}

		return content;
	}
}
