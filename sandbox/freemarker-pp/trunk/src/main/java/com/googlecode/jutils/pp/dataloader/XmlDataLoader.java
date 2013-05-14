package com.googlecode.jutils.pp.dataloader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import javax.xml.parsers.ParserConfigurationException;

import org.springframework.stereotype.Component;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.googlecode.jutils.collection.ArrayUtil;
import com.googlecode.jutils.pp.dataloader.exception.DataLoaderException;

import freemarker.ext.dom.NodeModel;

@Component
public class XmlDataLoader extends AbstractDataLoader {

	@Override
	public Object execute(File configFile, Object... values) throws DataLoaderException {
		NodeModel xmlModel = null;
		if (configFile != null && !ArrayUtil.isEmpty(values)) {
			final String fileName = (String) values[0];
			File file = new File(configFile.getParentFile(), fileName);
			if (!file.exists()) {
				file = new File(fileName);
			}

			Reader reader = null;
			try {
				reader = new FileReader(file);
				NodeModel.useJaxenXPathSupport();
				xmlModel = NodeModel.parse(new InputSource(reader));
			} catch (final FileNotFoundException e) {
				throw new DataLoaderException(e);
			} catch (final SAXException e) {
				throw new DataLoaderException(e);
			} catch (final IOException e) {
				throw new DataLoaderException(e);
			} catch (final ParserConfigurationException e) {
				throw new DataLoaderException(e);
			} catch (final Exception e) {
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
		}
		return xmlModel;
	}
}
