package com.googlecode.jutils.pp.freemarker.directive;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.googlecode.jutils.StringUtil;
import com.googlecode.jutils.pp.dataloader.exception.DataLoaderException;
import com.googlecode.jutils.templater.freemarker.template.directive.DirectiveUtil;

import freemarker.cache.TemplateLoader;
import freemarker.core.Environment;
import freemarker.template.SimpleHash;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateScalarModel;

public class LoadPropertiesDirective implements TemplateDirectiveModel {
	private static final String PARAM_NAME_FILE = "file";
	private static final String PARAM_NAME_ASSIGN_TO = "assignTo";

	@SuppressWarnings("rawtypes")
	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
		final TemplateScalarModel file = DirectiveUtil.getRequiredParameter(params, PARAM_NAME_FILE, TemplateScalarModel.class);
		final TemplateScalarModel assignTo = DirectiveUtil.getRequiredParameter(params, PARAM_NAME_ASSIGN_TO, TemplateScalarModel.class);

		Map<String, String> properties = null;
		try {
			properties = loadProperties(env, file.getAsString());
		} catch (final Exception e) {
			throw new TemplateException(e, env);
		}
		if (properties != null) {
			env.setVariable(assignTo.getAsString(), new SimpleHash(properties));
		}
	}

	private Map<String, String> loadProperties(Environment env, String fileName) throws Exception {
		Map<String, String> properties = null;
		if (!StringUtil.isBlank(fileName)) {
			final Reader reader = getReader(env, fileName);

			if (reader != null) {
				Properties props = null;
				try {
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
		}
		return properties;
	}

	private Reader getReader(Environment env, String fileName) throws FileNotFoundException, IOException {
		Reader reader = null;
		if (!StringUtil.isBlank(fileName)) {
			final File file = new File(fileName);
			if (file.exists()) {
				reader = new FileReader(fileName);
			} else {
				String templatePath = "";
				final String templateName = env.getTemplate().getName();
				if (StringUtil.contains(templateName, "/")) {
					templatePath = StringUtil.substringBeforeLast(templateName, "/");
					templatePath += "/";
				}
				final TemplateLoader templateLoader = env.getConfiguration().getTemplateLoader();
				final Object templateSource = templateLoader.findTemplateSource(templatePath + fileName);
				if (templateSource != null) {
					reader = templateLoader.getReader(templateSource, "UTF-8");
				}
			}
		}
		return reader;
	}
}
