package com.googlecode.jutils.csvexporter.service;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.configuration.PropertiesConfiguration;

import com.googlecode.jutils.StringUtil;
import com.googlecode.jutils.collection.ArrayUtil;
import com.googlecode.jutils.collection.CollectionUtil;
import com.googlecode.jutils.csvexporter.exception.ExportCsvException;
import com.googlecode.jutils.csvexporter.model.CsvCell;
import com.googlecode.jutils.csvexporter.model.CsvExporter;
import com.googlecode.jutils.spring.ExpressionUtil;

/**
 * The Class AbstractExportCsvService.
 */
public abstract class AbstractExportCsvService implements ExportCsvService {

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public List<String> export(List<?> elements) throws ExportCsvException {
		List<String> lines = null;
		if (!CollectionUtil.isEmpty(elements)) {
			final CsvExporter exporter = getCsvExporter();
			if (exporter != null) {
				try {
					lines = export(exporter, elements);
				} catch (final IllegalAccessException e) {
					throw new ExportCsvException(e);
				} catch (final InvocationTargetException e) {
					throw new ExportCsvException(e);
				} catch (final NoSuchMethodException e) {
					throw new ExportCsvException(e);
				}
			}
		}
		return lines;
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public void export(OutputStream outputStream, List<?> elements) throws ExportCsvException {
		if (outputStream != null && !CollectionUtil.isEmpty(elements)) {
			final CsvExporter exporter = getCsvExporter();
			if (exporter != null) {
				try {
					export(outputStream, true, exporter, elements);
				} catch (final UnsupportedEncodingException e) {
					throw new ExportCsvException(e);
				} catch (final IllegalAccessException e) {
					throw new ExportCsvException(e);
				} catch (final InvocationTargetException e) {
					throw new ExportCsvException(e);
				} catch (final NoSuchMethodException e) {
					throw new ExportCsvException(e);
				}
			}
		}
	}

	/**
	 * Gets the configuration.
	 * 
	 * @return the configuration
	 */
	protected abstract PropertiesConfiguration getConfiguration();

	/**
	 * Gets the csv exporter.
	 * 
	 * @return the csv exporter
	 */
	protected CsvExporter getCsvExporter() {
		final CsvExporter exporter = new CsvExporter();

		final String defaultTrue = getConfiguration().getString("csv_default_true");
		exporter.setDefaultTrue(defaultTrue);

		final String defaultFalse = getConfiguration().getString("csv_default_false");
		exporter.setDefaultFalse(defaultFalse);

		final Boolean encapsulateValue = getConfiguration().getBoolean("csv_encapsulate_value");
		exporter.setEncapsulateValue(encapsulateValue);

		final String[] cells = getConfiguration().getStringArray("csv_order");
		if (!ArrayUtil.isEmpty(cells)) {
			for (final String cell : cells) {
				final String headerValue = getHeaderValue(cell);
				final String propertyValue = getPropertyValue(cell);
				final String formatValue = getFormatValue(cell);
				final String conditionalDisplayValue = getConditionalDisplayValue(cell);

				exporter.addCell(headerValue, propertyValue, formatValue, conditionalDisplayValue);
			}
		}

		return exporter;
	}

	/**
	 * Gets the header value.
	 * 
	 * @param propertyName
	 *            the property name
	 * @return the header value
	 */
	protected String getHeaderValue(String propertyName) {
		String resourceValue = null;
		if (!StringUtil.isBlank(propertyName)) {
			resourceValue = getConfiguration().getString(propertyName + "_header");
		}
		return resourceValue;
	}

	/**
	 * Gets the property value.
	 * 
	 * @param propertyName
	 *            the property name
	 * @return the property value
	 */
	protected String getPropertyValue(String propertyName) {
		String resourceValue = null;
		if (!StringUtil.isBlank(propertyName)) {
			resourceValue = getConfiguration().getString(propertyName + "_property");
		}
		return resourceValue;
	}

	/**
	 * Gets the format value.
	 * 
	 * @param propertyName
	 *            the property name
	 * @return the format value
	 */
	protected String getFormatValue(String propertyName) {
		String resourceValue = null;
		if (!StringUtil.isBlank(propertyName)) {
			resourceValue = getConfiguration().getString(propertyName + "_format");
		}
		return resourceValue;
	}

	/**
	 * Gets the conditional display value.
	 * 
	 * @param propertyName
	 *            the property name
	 * @return the conditional display value
	 */
	protected String getConditionalDisplayValue(String propertyName) {
		String resourceValue = null;
		if (!StringUtil.isBlank(propertyName)) {
			resourceValue = getConfiguration().getString(propertyName + "_conditional_display");
		}
		return resourceValue;
	}

	/**
	 * Export.
	 * 
	 * @param outputStream
	 *            the output stream
	 * @param closeStream
	 *            the close stream
	 * @param exporter
	 *            the exporter
	 * @param values
	 *            the values
	 * @throws UnsupportedEncodingException
	 *             the unsupported encoding exception
	 * @throws IllegalAccessException
	 *             the illegal access exception
	 * @throws InvocationTargetException
	 *             the invocation target exception
	 * @throws NoSuchMethodException
	 *             the no such method exception
	 */
	private void export(OutputStream outputStream, boolean closeStream, CsvExporter exporter, List<?> values) throws UnsupportedEncodingException, IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {
		if (outputStream != null && exporter != null) {
			final List<String> lines = export(exporter, values);
			if (!CollectionUtil.isEmpty(lines)) {
				final OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, exporter.getEncoding());
				final PrintWriter writer = new PrintWriter(outputStreamWriter);
				for (final String line : lines) {
					writer.println(line);
				}

				writer.flush();
				if (closeStream) {
					writer.close();
				}
			}
		}
	}

	/**
	 * Export.
	 * 
	 * @param exporter
	 *            the exporter
	 * @param values
	 *            the values
	 * @return the list< string>
	 * @throws IllegalAccessException
	 *             the illegal access exception
	 * @throws InvocationTargetException
	 *             the invocation target exception
	 * @throws NoSuchMethodException
	 *             the no such method exception
	 */
	private List<String> export(CsvExporter exporter, List<?> values) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		List<String> lines = null;

		if (exporter != null) {
			lines = new ArrayList<String>();

			if (exporter.hasHeaders()) {
				final String header = getHeader(exporter);
				if (!StringUtil.isBlank(header)) {
					lines.add(header);
				}
			}

			if (!CollectionUtil.isEmpty(values)) {
				for (final Object value : values) {
					String line = null;

					for (final CsvCell cell : exporter.getCells()) {
						final String cellValue = getCellValue(exporter, cell, value);

						if (line != null) {
							line = StringUtil.join(new String[] { line, cellValue }, exporter.getSeparator());
						} else if (StringUtil.isBlank(cellValue)) {
							line = "";
						} else {
							line = cellValue;
						}
					}
					lines.add(line);
				}
			}
		}
		return lines;
	}

	/**
	 * Gets the cell value.
	 * 
	 * @param exporter
	 *            the exporter
	 * @param cell
	 *            the cell
	 * @param value
	 *            the value
	 * @return the cell value
	 * @throws IllegalAccessException
	 *             the illegal access exception
	 * @throws InvocationTargetException
	 *             the invocation target exception
	 * @throws NoSuchMethodException
	 *             the no such method exception
	 */
	private String getCellValue(CsvExporter exporter, CsvCell cell, Object value) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		String cellValue = "";
		if (exporter != null && cell != null && value != null) {
			boolean display = true;
			if (!StringUtil.isBlank(cell.getConditionalDisplay())) {
				display = (Boolean) ExpressionUtil.evaluate(value, cell.getConditionalDisplay());
			}
			if (display) {
				final Object cellObject = ExpressionUtil.evaluate(value, cell.getPropertyName());
				if (cellObject != null) {
					cellValue = getStringValue(exporter, cellObject, cell.getPropertyName(), cell.getFormat());
				}
			}
		}
		return cellValue;
	}

	/**
	 * Gets the string value.
	 * 
	 * @param exporter
	 *            the exporter
	 * @param value
	 *            the value
	 * @param attribute
	 *            the attribute
	 * @param format
	 *            the format
	 * @return the string value
	 */
	private String getStringValue(CsvExporter exporter, Object value, String attribute, String format) {
		String formattedValue = null;

		if (exporter != null) {
			Object val = value;

			if (exporter.hasReplaceValues()) {
				if (exporter.getReplaceValues().containsKey(attribute)) {
					final Map<String, String> attributeValues = exporter.getReplaceValues().get(attribute);
					if (!MapUtils.isEmpty(attributeValues)) {
						if (val != null && attributeValues.containsKey(val.toString())) {
							val = attributeValues.get(val.toString());
						}
					}
				}
			}

			formattedValue = formatValue(exporter, val, format);
			if (exporter.isEncapsulateValue()) {
				formattedValue = formatCellValue(exporter, formattedValue);
			}
		}
		return formattedValue;
	}

	/**
	 * Format value.
	 * 
	 * @param exporter
	 *            the exporter
	 * @param value
	 *            the value
	 * @param format
	 *            the format
	 * @return the string
	 */
	private String formatValue(CsvExporter exporter, Object value, String format) {
		String formattedValue = "";
		if (exporter != null && value != null) {
			if (!StringUtil.isBlank(format) && !StringUtil.equals(format, "\"\"")) {
				if (value instanceof Boolean) {
					final String[] valeur = StringUtil.split(format, ',');

					if ((Boolean) value) {
						formattedValue = valeur[1];
					} else {
						formattedValue = valeur[0];
					}
				} else {
					formattedValue = String.format(format, value);
				}
			} else {
				if (value instanceof String) {
					final String StringValue = (String) value;
					if (!StringUtil.isBlank(StringValue)) {
						formattedValue = String.format(exporter.getDefaultStringFormat(), value);
					}
				} else if (value instanceof Integer) {
					formattedValue = String.format(exporter.getDefaultIntegerFormat(), value);
				} else if (value instanceof Double) {
					formattedValue = String.format(exporter.getDefaultDoubleFormat(), value);
				} else if (value instanceof Date) {
					formattedValue = String.format(exporter.getDefaultDateFormat(), value);
				} else if (value instanceof Boolean) {
					if ((Boolean) value) {
						formattedValue = exporter.getDefaultTrue();
					} else {
						formattedValue = exporter.getDefaultFalse();
					}
				} else {
					formattedValue = value.toString();
				}
			}
		}

		return formattedValue;
	}

	/**
	 * Gets the header.
	 * 
	 * @param exporter
	 *            the exporter
	 * @return the header
	 */
	private String getHeader(CsvExporter exporter) {
		String header = null;

		if (exporter != null) {
			for (final CsvCell cell : exporter.getCells()) {
				String cellHeader = cell.getHeader();
				if (!StringUtil.isBlank(cellHeader)) {
					if (StringUtil.contains(cellHeader, '\n') && !exporter.isEncapsulateValue()) {
						cellHeader = "\"" + cellHeader + "\"";
					}
					cellHeader = formatCellValue(exporter, cellHeader);

					if (!StringUtil.isBlank(header)) {
						header = StringUtil.join(new String[] { header, cellHeader }, exporter.getSeparator());
					} else {
						header = cellHeader;
					}
				}
			}
		}
		return header;
	}

	/**
	 * Format cell value.
	 * 
	 * @param exporter
	 *            the exporter
	 * @param value
	 *            the value
	 * @return the string
	 */
	private String formatCellValue(CsvExporter exporter, String value) {
		String formattedValue = null;

		if (!StringUtil.isBlank(value)) {
			formattedValue = value;
			if (exporter.isEncapsulateValue() && !StringUtil.startsWith(value, "\"") && !StringUtil.endsWith(value, "\"")) {
				formattedValue = "\"" + formattedValue + "\"";
			}

			if (exporter.hasCharacterEscapes()) {
				for (final Map.Entry<String, String> entry : exporter.getCharacterEscapes().entrySet()) {
					formattedValue = StringUtil.replace(formattedValue, entry.getKey(), entry.getValue());
				}
			}

			if (exporter.isTrimValue()) {
				formattedValue = StringUtil.trim(formattedValue);
			}
		}
		return formattedValue;
	}
}
