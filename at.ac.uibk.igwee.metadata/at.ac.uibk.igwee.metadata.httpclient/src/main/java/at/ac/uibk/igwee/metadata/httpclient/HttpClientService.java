package at.ac.uibk.igwee.metadata.httpclient;

import java.io.ByteArrayInputStream;
import java.util.List;

public interface HttpClientService {
	
	public static final int DEFAULT_PORT = 80;
	
	public static final String DEFAULT_ENCODING = "utf-8";

	public ByteArrayInputStream executeHttp(String host, int port, String path, 
			List<ParameterPair> parameters, HttpMethod method, String encoding) 
					throws HttpClientException;
	
	default public ByteArrayInputStream executeHttp(String host, String path, List<ParameterPair> parameters, 
			HttpMethod method, String encoding) throws HttpClientException {
		return executeHttp(host, DEFAULT_PORT,path, parameters, method, encoding);
	}
	
	default public ByteArrayInputStream executeHttp(String host, String path, List<ParameterPair> parameters, 
			HttpMethod method) throws HttpClientException {
		return executeHttp(host, DEFAULT_PORT,path, parameters, method, DEFAULT_ENCODING);
	}
	
	default public ByteArrayInputStream executeHttpGet(String host, String path, List<ParameterPair> params) 
			throws HttpClientException {
		return executeHttp(host, path, params, HttpMethod.GET);
	}
	
	default public ByteArrayInputStream executeHttpPost(String host, String path, List<ParameterPair> parameters) 
			throws HttpClientException {
		return executeHttp(host, path, parameters, HttpMethod.POST);
	}
	

}