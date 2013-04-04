package com.googlecode.jutils.templater.freemarker.template.directive;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
import org.dom4j.io.DOMReader;
import org.dom4j.io.DOMWriter;

import com.googlecode.jutils.StringUtil;
import com.googlecode.jutils.collection.CollectionUtil;
import com.googlecode.jutils.xml.XmlUtil;

import freemarker.core.Environment;
import freemarker.ext.dom.NodeModel;
import freemarker.template.SimpleScalar;
import freemarker.template.SimpleSequence;
import freemarker.template.TemplateBooleanModel;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateScalarModel;

/**
 * The Class XPathDirective.
 */
public class XPathDirective implements TemplateDirectiveModel {
	private static final String PARAM_NAME_XML = "xml";
	private static final String PARAM_NAME_EXPRESSION = "expression";
	private static final String PARAM_NAME_ASSIGN_TO = "assignTo";
	private static final String PARAM_NAME_AS_STRING = "asString";

	/**
	 * {@inheritedDoc}
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
		final NodeModel xmlModel = DirectiveUtil.getRequiredParameter(params, PARAM_NAME_XML, NodeModel.class);
		final TemplateScalarModel expression = DirectiveUtil.getRequiredParameter(params, PARAM_NAME_EXPRESSION, TemplateScalarModel.class);
		final TemplateScalarModel assignTo = DirectiveUtil.getRequiredParameter(params, PARAM_NAME_ASSIGN_TO, TemplateScalarModel.class);
		final TemplateBooleanModel asString = DirectiveUtil.getParameter(params, PARAM_NAME_AS_STRING, TemplateBooleanModel.class);

		final Document xmlDocument = convertToDom4j(xmlModel);
		if (asString == null || !asString.getAsBoolean()) {
			final List<Node> nodes = getNodes(xmlDocument, expression.getAsString());
			if (!CollectionUtil.isEmpty(nodes)) {
				if (nodes.size() == 1) {
					final NodeModel nodeModel = getNodeModel(nodes.get(0));
					env.setVariable(assignTo.getAsString(), nodeModel);
				} else {
					final List<NodeModel> nodeModels = new ArrayList<NodeModel>();
					for (final Node node : nodes) {
						nodeModels.add(getNodeModel(node));
					}

					env.setVariable(assignTo.getAsString(), new SimpleSequence(nodeModels));
				}
			} else {
				env.setVariable(assignTo.getAsString(), null);
			}
		} else {
			final String value = getValue(xmlDocument, expression.getAsString());
			if (value != null) {
				env.setVariable(assignTo.getAsString(), new SimpleScalar(value));
			} else {
				env.setVariable(assignTo.getAsString(), null);
			}
		}
	}

	/**
	 * Gets the nodes.
	 * 
	 * @param xmlDocument
	 *            the xml document
	 * @param expression
	 *            the expression
	 * @return the nodes
	 * @throws TemplateModelException
	 *             the template model exception
	 */
	@SuppressWarnings("unchecked")
	private List<Node> getNodes(Document xmlDocument, String expression) throws TemplateModelException {
		List<Node> nodes = null;
		if (xmlDocument != null && !StringUtil.isBlank(expression)) {
			nodes = xmlDocument.selectNodes(expression);
		}
		return nodes;
	}

	/**
	 * Gets the value.
	 * 
	 * @param xmlDocument
	 *            the xml document
	 * @param expression
	 *            the expression
	 * @return the value
	 * @throws TemplateModelException
	 *             the template model exception
	 */
	private String getValue(Document xmlDocument, String expression) throws TemplateModelException {
		String value = null;
		if (xmlDocument != null && !StringUtil.isBlank(expression)) {
			value = xmlDocument.valueOf(expression);
		}
		return value;
	}

	/**
	 * Convert to dom4j.
	 * 
	 * @param xmlModel
	 *            the xml model
	 * @return the document
	 * @throws TemplateModelException
	 *             the template model exception
	 */
	private Document convertToDom4j(NodeModel xmlModel) throws TemplateModelException {
		Document document = null;
		if (xmlModel != null) {
			if (xmlModel.getWrappedObject() instanceof org.w3c.dom.Document) {
				final org.w3c.dom.Document w3cDocument = (org.w3c.dom.Document) xmlModel.getWrappedObject();
				if (w3cDocument != null) {
					final DOMReader xmlReader = new DOMReader();
					document = xmlReader.read(w3cDocument);
				}
			} else if (xmlModel.getWrappedObject() instanceof org.w3c.dom.Element) {
				final org.w3c.dom.Element w3cElement = (org.w3c.dom.Element) xmlModel.getWrappedObject();
				if (w3cElement != null) {
					final StringWriter writer = new StringWriter();
					Transformer transformer = null;
					try {
						transformer = TransformerFactory.newInstance().newTransformer();
						transformer.transform(new DOMSource(w3cElement), new StreamResult(writer));
					} catch (final TransformerConfigurationException e) {
						throw new TemplateModelException(e.getMessage());
					} catch (final TransformerFactoryConfigurationError e) {
						throw new TemplateModelException(e.getMessage());
					} catch (final TransformerException e) {
						throw new TemplateModelException(e.getMessage());
					}

					final String xml = writer.toString();
					try {
						document = XmlUtil.getXmlDocument(xml);
					} catch (final DocumentException e) {
						throw new TemplateModelException(e.getMessage());
					}
				}
			}
		}
		return document;
	}

	/**
	 * Gets the node model.
	 * 
	 * @param node
	 *            the node
	 * @return the node model
	 * @throws TemplateModelException
	 *             the template model exception
	 */
	private NodeModel getNodeModel(Node node) throws TemplateModelException {
		NodeModel nodeModel = null;
		if (node != null) {
			org.w3c.dom.Document w3cDoc = null;
			try {
				final Document document = DocumentHelper.parseText(node.asXML());
				w3cDoc = new DOMWriter().write(document);
			} catch (final DocumentException e) {
				throw new TemplateModelException(e.getMessage());
			}

			if (w3cDoc != null) {
				nodeModel = NodeModel.wrap(w3cDoc.getDocumentElement());
			}
		}
		return nodeModel;
	}
}
