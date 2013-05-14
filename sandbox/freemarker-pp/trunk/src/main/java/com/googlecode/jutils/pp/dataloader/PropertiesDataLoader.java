package com.googlecode.jutils.pp.dataloader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.stereotype.Component;

import com.googlecode.jutils.collection.ArrayUtil;
import com.googlecode.jutils.pp.dataloader.exception.DataLoaderException;

@Component
public class PropertiesDataLoader extends AbstractDataLoader {

	@Override
	public Object execute(File configFile, Object... values) throws DataLoaderException {
		Map<String, String> properties = null;
		if (configFile != null && !ArrayUtil.isEmpty(values)) {
			final String fileName = (String) values[0];
			File file = new File(configFile.getParentFile(), fileName);
			if (!file.exists()) {
				file = new File(fileName);
			}

			Properties props = null;
			Reader reader = null;
			try {
				reader = new FileReader(file);
				props = new Properties();
				props.load(reader);
			} catch (final FileNotFoundException e) {
				throw new DataLoaderException(e);
			} catch (final IOException e) {
				throw new DataLoaderException(e);
			} finally {
				try {
					if (reader != null) {
						reader.close();
					}
				} catch (final IOException e) {
					throw new DataLoaderException(e);
				}
			}

			if (props != null) {
				properties = new HashMap<String, String>();
				for (final String key : props.stringPropertyNames()) {
					final String value = props.getProperty(key);

					properties.put(key, value);
				}
			}
		}
		return properties;
	}
}
