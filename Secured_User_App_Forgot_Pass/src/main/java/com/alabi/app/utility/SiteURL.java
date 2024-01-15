package com.alabi.app.utility;

import jakarta.servlet.http.HttpServletRequest;

public class SiteURL {

	public static String getURL(HttpServletRequest request) {
		
		String siteURL = request.getRequestURL().toString();
		return siteURL.replace(request.getServletPath(), "");
	}
}
