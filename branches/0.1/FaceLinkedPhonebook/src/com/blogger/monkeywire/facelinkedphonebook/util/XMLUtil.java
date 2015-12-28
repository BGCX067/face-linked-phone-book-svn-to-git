package com.blogger.monkeywire.facelinkedphonebook.util;

import java.util.ArrayList;

import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

abstract class XMLUtil {

	public static void validateExecludingNil(final XmlObject xmlObject)
			throws XmlException {
		final XmlOptions xmlOptions = new XmlOptions();
		final ArrayList<XmlError> errors = new ArrayList<XmlError>();
		xmlOptions.setErrorListener(errors);
		xmlObject.validate(xmlOptions);
		for (XmlError error : errors) {
			Node errorNode = error.getCursorLocation().getDomNode();
			final NamedNodeMap nodeAttributes = errorNode.getAttributes();
			if (nodeAttributes.getNamedItem("xsi:nil") != null) {
				nodeAttributes.removeNamedItem("xsi:nil");
			}
		}
		errors.clear();
		if (!xmlObject.validate(xmlOptions)) {
			throw new XmlException(assembleExceptionMessage(errors));
		}

	}

	private static String assembleExceptionMessage(
			final ArrayList<XmlError> errors) {
		String message = "";
		for (XmlError xmlError : errors) {
			String errorNodeName = xmlError.getCursorLocation().getDomNode()
					.getLocalName();
			final StringBuilder errorMessage = new StringBuilder();
			errorMessage.append(xmlError.getMessage());
			errorMessage.append("in node ");
			errorMessage.append("\"");
			errorMessage.append(errorNodeName);
			errorMessage.append("\"");
			message = errorMessage.toString();
		}
		return message;
	}
}