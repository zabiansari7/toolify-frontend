package de.srh.toolify.frontend.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.vaadin.flow.server.VaadinSession;

public class HelperUtil {
	
	public static String getEmailFromSession() {
    	JsonNode userNode = (JsonNode) VaadinSession.getCurrent().getAttribute("user");
    	return userNode.get("email").textValue();
	}

}
