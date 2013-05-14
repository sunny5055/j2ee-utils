package com.googlecode.jutils.pp.action;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.googlecode.jutils.pp.freemarker.directive.FormatDirective;
import com.googlecode.jutils.pp.freemarker.directive.LoadPropertiesDirective;
import com.googlecode.jutils.pp.freemarker.directive.LoadXmlDirective;
import com.googlecode.jutils.pp.freemarker.directive.ResolveKeyDirective;
import com.googlecode.jutils.pp.freemarker.method.GetClassNameMethod;
import com.googlecode.jutils.pp.freemarker.method.GetFqdnMethod;
import com.googlecode.jutils.pp.freemarker.method.GetImportsMethod;
import com.googlecode.jutils.pp.freemarker.method.GetModifiersMethod;
import com.googlecode.jutils.pp.freemarker.method.GetPackageMethod;
import com.googlecode.jutils.pp.freemarker.method.GetTypeMethod;
import com.googlecode.jutils.pp.freemarker.method.IsPrimitiveMethod;
import com.googlecode.jutils.pp.freemarker.method.OutputDirectoryMethod;
import com.googlecode.jutils.pp.freemarker.method.OutputFileMethod;
import com.googlecode.jutils.pp.freemarker.method.OutputFileNameMethod;
import com.googlecode.jutils.pp.freemarker.method.PackageToDirMethod;
import com.googlecode.jutils.pp.freemarker.method.RealOutputDirectoryMethod;
import com.googlecode.jutils.pp.freemarker.method.RealOutputFileMethod;
import com.googlecode.jutils.pp.freemarker.transform.ChangeOutputFileTransform;
import com.googlecode.jutils.pp.freemarker.transform.DropOutputFileTransform;
import com.googlecode.jutils.pp.freemarker.transform.RenameOutputFileTransform;
import com.googlecode.jutils.pp.io.FileOutputWriter;
import com.googlecode.jutils.pp.settings.Settings;
import com.googlecode.jutils.templater.exception.TemplaterServiceException;
import com.googlecode.jutils.templater.service.TemplaterService;

@Component
public class ProcessAction extends AbstractAction {
	@Autowired
	private TemplaterService templaterService;

	@Override
	public void execute(Settings settings, File file, Map<String, Object> parameters) {
		if (file != null && settings != null) {
			final File destFile = settings.getOutputFile(file);

			FileOutputWriter writer = null;
			try {
				writer = new FileOutputWriter(settings, destFile);

				templaterService.addTemplateLoaderPath(settings.getSourceDirectory());
				templaterService.processFromFile(file, getData(settings, writer), writer);
			} catch (final TemplaterServiceException e) {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug(e.getMessage(), e);
				}
			} finally {
				if (writer != null) {
					try {
						writer.close();
					} catch (final IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	private Map<String, Object> getData(Settings settings, FileOutputWriter writer) {
		Map<String, Object> data = null;
		if (settings != null) {
			data = new HashMap<String, Object>();
			data.putAll(settings.getData());

			data.put("renameOutputFile", new RenameOutputFileTransform());
			data.put("changeOutputFile", new ChangeOutputFileTransform());
			data.put("dropOutputFile", new DropOutputFileTransform());

			data.put("loadProperties", new LoadPropertiesDirective());
			data.put("loadXml", new LoadXmlDirective());
			data.put("format", new FormatDirective());
			data.put("resolveKey", new ResolveKeyDirective());

			data.put("outputDirectory", new OutputDirectoryMethod(settings, writer));
			data.put("outputFile", new OutputFileMethod(settings, writer));
			data.put("outputFileName", new OutputFileNameMethod(writer));
			data.put("realOutputDirectory", new RealOutputDirectoryMethod(settings, writer));
			data.put("realOutputFile", new RealOutputFileMethod(settings, writer));
			data.put("packageToDir", new PackageToDirMethod());

			data.put("getImports", new GetImportsMethod());
			data.put("getPackage", new GetPackageMethod());
			data.put("getClassName", new GetClassNameMethod());
			data.put("getFqdn", new GetFqdnMethod());
			data.put("getModifiers", new GetModifiersMethod());
			data.put("getType", new GetTypeMethod());
			data.put("isPrimitive", new IsPrimitiveMethod());
		}
		return data;
	}
}
