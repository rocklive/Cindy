/////////////////////////////////////////////////
// Project : WSFramework
// Package : co.mindie.wsframework.serveradapter
// Request.java
//
// Author : Simon CORSIN <simoncorsin@gmail.com>
// File created on Aug 1, 2014 at 5:59:51 PM
////////

package co.mindie.cindy.webservice.controller.manager;

import co.mindie.cindy.webservice.controller.HttpMethod;
import org.apache.commons.fileupload.FileItem;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface HttpRequest {

	Map<String, String[]> getQueryParameters();

	Map<String, List<FileItem>> getBodyParameters();

	InputStream getBody();

	String getPathInfo();

	String getServerUrl();

	HttpMethod getMethod();

	String getRemoteAddr();

	String getHeader(String name);

}
