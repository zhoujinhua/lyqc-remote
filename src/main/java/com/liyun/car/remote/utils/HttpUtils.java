package com.liyun.car.remote.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * request --HTTP/HTTPS 
 * method --GET/POST
 * @date 2015年12月2日 下午6:42:43
 */
public class HttpUtils {

	static Logger logger = LoggerFactory.getLogger(HttpUtils.class);
	private static final int SO_TIMEOUT = 30000;
	private static final int CONNECT_TIMEOUT = 30000;
	private static final String SSL = "SSL";
	public static final String SSL_V3 = "SSLv3";

	/**
	 * 发送 get 请求
	 * @param params 参数
	 * @param url 地址
	 * @return String
	 */
	public static String invokeGet(Map<String, Object> params, String url) {
		return invokeGet(params,url,SO_TIMEOUT,CONNECT_TIMEOUT);
	}
	/**
	 * 发送 get 请求
	 * @param params 参数
	 * @param url 地址
	 * @param socketTimeout socketTimeout
	 * @param connectTimeout connectTimeout
	 * @return String
	 */
	public static String invokeGet(Map<String, Object> params, String url, int socketTimeout,int connectTimeout) {
		// 组织请求参数
		StringBuilder urlParams = new StringBuilder();
		String result = "";
		CloseableHttpClient closeableHttpClient = null;
		try {
			if (null != params) {
				for (Map.Entry<String, Object> entry : params.entrySet()) {
				       if(entry.getValue()!=null){
					urlParams.append(entry.getKey()).append("=")
						.append(URLEncoder.encode(entry.getValue().toString(), "utf-8")).append("&");
				       }else{
				           logger.info("[invokeGet]key="+entry.getKey()+",value=null");
				       }
				}

				if (!params.isEmpty()) {
					urlParams.deleteCharAt(urlParams.length() - 1);
				}
				url += "?" + urlParams;
			}
			logger.info("# GET 请求URL为:" + url);

			HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
			if(url.toLowerCase().startsWith("https"))
				httpClientBuilder.setSSLSocketFactory(createGenerousSSLSocketFactory(SSL));
			closeableHttpClient = httpClientBuilder.build();
			
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(socketTimeout).
					setConnectTimeout(connectTimeout).build();//设置请求和传输超时时间
			HttpGet get = new HttpGet(url);
			get.setConfig(requestConfig);
			HttpResponse resp = closeableHttpClient.execute(get);
			result = convertResponseBytes2String(resp);
		} catch (Exception e) {
			logger.error("#httpGet error", e);
		} finally{
			try {
				if(closeableHttpClient!=null)
					closeableHttpClient.close();
			} catch (IOException e) {
				logger.info("# close client error" + result);
			}
		}
		logger.info("# GET JSON end ");
		return result;
	}
	/**
	 * 发送 Post 请求
	 * @param jsonStrData 参数
	 * @param url 地址
	 * @return String
	 */
	public static String invokePost(String jsonStrData, String url) {
		return invokePost(jsonStrData, url, SO_TIMEOUT, CONNECT_TIMEOUT);
	}
	public static String invokePost(String jsonStrData, String url, String sslType) {
		return invokePost(jsonStrData, url, SO_TIMEOUT, CONNECT_TIMEOUT, sslType);
	}
	public static String invokePost(String jsonStrData, String url, int socketTimeout,int connectTimeout) {
		return invokePost(jsonStrData, url, SO_TIMEOUT, CONNECT_TIMEOUT, SSL);
	}
	public static String invokePostExceptionHandler(String jsonStrData, String url) throws ClientProtocolException, IOException {
		return invokePostExceptionHandler(jsonStrData, url, SO_TIMEOUT, CONNECT_TIMEOUT, SSL);
	}
	/**
	 * 发送 Post 请求
	 * @param jsonStrData 参数
	 * @param url 地址
	 * @param socketTimeout socketTimeout
	 * @param connectTimeout connectTimeout
	 * @return String
	 */
	public static String invokePost(String jsonStrData, String url, int socketTimeout,int connectTimeout,String sslType) {
		logger.info("# POST JSON 请求URL为：" + url);
		logger.info("# POST JSON 数据为：" + jsonStrData);
		String result = "";
		CloseableHttpClient closeableHttpClient = null;
		try {
			HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
			if(url.toLowerCase().startsWith("https")){
				httpClientBuilder.setSSLSocketFactory(createGenerousSSLSocketFactory(sslType));
			}
			HttpEntity entity = new StringEntity(jsonStrData, "UTF-8");
			closeableHttpClient = httpClientBuilder.build();
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(socketTimeout).
						setConnectTimeout(connectTimeout).build();//设置请求和传输超时时间
			HttpPost post = new HttpPost(url);
			post.setConfig(requestConfig);
			post.setEntity(entity);
			
			post.setHeader("Content-type", "application/json");
			HttpResponse resp = closeableHttpClient.execute(post);
			result = convertResponseBytes2String(resp);
		} catch (Exception e) {
			logger.error("#httpPOST error", e);
		} finally{
			try {
				if(closeableHttpClient!=null)
					closeableHttpClient.close();
			} catch (IOException e) {
				logger.info("# close client error" + result);
			}
		}
		logger.info("# POST JSON end");
		return result;
	}
	
	/**
	 * 发送 Post 请求
	 * @param jsonStrData 参数
	 * @param url 地址
	 * @param socketTimeout socketTimeout
	 * @param connectTimeout connectTimeout
	 * @return String
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public static String invokePostExceptionHandler(String jsonStrData, String url, int socketTimeout,int connectTimeout,String sslType) throws ClientProtocolException, IOException {
		logger.info("# POST JSON 请求URL为：" + url);
		logger.info("# POST JSON 数据为：" + jsonStrData);
		String result = "";
		CloseableHttpClient closeableHttpClient = null;
		try {
			HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
			if(url.toLowerCase().startsWith("https")){
				httpClientBuilder.setSSLSocketFactory(createGenerousSSLSocketFactory(sslType));
			}
			HttpEntity entity = new StringEntity(jsonStrData, "UTF-8");
			closeableHttpClient = httpClientBuilder.build();
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(socketTimeout).
						setConnectTimeout(connectTimeout).build();//设置请求和传输超时时间
			HttpPost post = new HttpPost(url);
			post.setConfig(requestConfig);
			post.setEntity(entity);
			
			post.setHeader("Content-type", "application/json");
			HttpResponse resp = closeableHttpClient.execute(post);
			result = convertResponseBytes2String(resp);
		} finally{
			try {
				if(closeableHttpClient!=null)
					closeableHttpClient.close();
			} catch (IOException e) {
				logger.info("# close client error" + result);
			}
		}
		logger.info("# POST JSON end");
		return result;
	}

//	/**
//	 * 发送 Post 请求
//	 * @param jsonStrData 参数
//	 * @param url 地址
//	 * @param socketTimeout socketTimeout
//	 * @param connectTimeout connectTimeout
//	 * @return String
//	 */
//	public static String invokePostFile(String url, Map<String,Object> params, File file){
//		logger.info("# POST JSON 请求URL为：" + url); 
//		StringBuilder urlParams = new StringBuilder();
//		logger.info("# GET 请求URL为:" + url);
//		String result = "";
//		CloseableHttpClient closeableHttpClient = null;
//		try {
//			if (null != params) {
//				for (Map.Entry<String, Object> entry : params.entrySet()) {
//				       if(entry.getValue()!=null){
//					urlParams.append(entry.getKey()).append("=")
//						.append(URLEncoder.encode(entry.getValue().toString(), "utf-8")).append("&");
//				       }else{
//				           logger.info("[invokeGet]key="+entry.getKey()+",value=null");
//				       }
//				}
//
//				if (!params.isEmpty()) {
//					urlParams.deleteCharAt(urlParams.length() - 1);
//				}
//				url += "?" + urlParams;
//			}
//			logger.info("# POST JSON 请求组装参数URL为：" + url); 
//			HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
//			if(url.toLowerCase().startsWith("https"))
//				httpClientBuilder.setSSLSocketFactory(createGenerousSSLSocketFactory("SSL"));
//			closeableHttpClient = httpClientBuilder.build();
//			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
//	        builder.addBinaryBody("file", file, ContentType.TEXT_PLAIN, "card");
//	        HttpEntity entity = builder.build();
//			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(3000).
//						setConnectTimeout(3000).build();//设置请求和传输超时时间 
//			HttpPost post = new HttpPost(url);
//			post.setConfig(requestConfig);
//			post.setEntity(entity);
//			HttpResponse resp = closeableHttpClient.execute(post);
//			result = convertResponseBytes2String(resp);
//		} catch (Exception e) {
//			logger.error("#httpPOST error", e);
//		} finally{
//			try {
//				if(closeableHttpClient!=null)
//					closeableHttpClient.close();
//			} catch (IOException e) {
//				logger.info("# close client error" + result);
//			}
//		}
//		logger.info("# POST JSON end");
//		return result;
//	}
	/**
	 * HTTPS 证书
	 * 
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 */
	public static void trustAllHttpsCertificates() throws NoSuchAlgorithmException, KeyManagementException {
		javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[1];
		javax.net.ssl.TrustManager tm = new miTM();
		trustAllCerts[0] = tm;
		javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext.getInstance("SSL"); // SSL,TLS
		sc.init(null, trustAllCerts, null);
		javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
	}

	static class miTM implements javax.net.ssl.TrustManager, javax.net.ssl.X509TrustManager {
		public java.security.cert.X509Certificate[] getAcceptedIssuers() {
			return null;
		}

		public boolean isServerTrusted(java.security.cert.X509Certificate[] certs) {
			return true;
		}

		public boolean isClientTrusted(java.security.cert.X509Certificate[] certs) {
			return true;
		}

		public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) throws java.security.cert.CertificateException {
			return;
		}

		public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) throws java.security.cert.CertificateException {
			return;
		}
	}
	
	/**
	 * 转换响应数据
	 * @param resp
	 * @return
	 */
	private static String convertResponseBytes2String(HttpResponse resp) {
		String result = "";
		InputStream instream  =null;
		try {
			instream  = resp.getEntity().getContent();
			byte[] respBytes = IOUtils.toByteArray(instream );
			result = new String(respBytes, Charset.forName("UTF-8"));
		} catch (Exception e) {
			logger.error("# error", e);
		} finally{
			try {
				if(instream != null)
					instream .close();
			} catch (IOException e) {
				logger.error("# close Io error", e);
			}
		}
		return result;
	}
	
	/***
     * @param sslType 
	 * @return SSLConnectionSocketFactory that bypass certificate check and bypass HostnameVerifier
     */
    private static SSLConnectionSocketFactory createGenerousSSLSocketFactory(String sslType) {
        SSLContext sslContext;
        try {
            sslContext = SSLContext.getInstance(sslType);
            sslContext.init(null, new TrustManager[]{createGenerousTrustManager()}, new SecureRandom());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("create SSLConnectionSocketFactory error",e);
            return null;
        }
        return new SSLConnectionSocketFactory(sslContext, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
    }

    private static X509TrustManager createGenerousTrustManager() {
        return new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] cert, String s) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] cert, String s) throws CertificateException {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };
    }
}
