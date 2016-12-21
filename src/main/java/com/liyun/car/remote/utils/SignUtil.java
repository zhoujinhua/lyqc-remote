package com.liyun.car.remote.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.liyun.car.common.utils.Md5Util;
import com.liyun.car.common.utils.PropertyUtil;
import com.liyun.car.common.utils.StringUtils;

/**
 * Sign签名生成与验证
 * 
 * @author Zhaoxin.Zhao
 * 
 */
public class SignUtil {

	private static Logger log = LoggerFactory.getLogger(SignUtil.class);

	private static final String JOIN_FLAG = "";
	private static final String ENCODE = "UTF-8";
	// 请求参数中sign key
	public static final String SIGN_PARAM_KEY = "sign";
	// Sign 密钥，默认值mljrcrm001
	private static String SECRET_KEY = PropertyUtil.getPropertyValue("sign_secret_key","mljrcrm001");
	// sign 验签过滤的url
	private static String FILTER_URL = PropertyUtil.getPropertyValue("sign_filter_url","mljrcrm001");

	private SignUtil() {
	}

	/**
	 * 生成Sign加密字符串，按照参数字符串的升序排序加密，对参数不进行编码
	 * 
	 * @param request
	 * @param secretKey
	 *            密钥 ，对sign进行加严，可为空
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String getSign(HttpServletRequest request, String secretKey)
			throws UnsupportedEncodingException {
		return getSign(request, secretKey, false);
	}

	/**
	 * 生成Sign加密字符串，按照参数字符串的升序排序加密，对参数进行编码
	 * 
	 * @param request
	 * @param secretKey
	 *            密钥 ，对sign进行加严，可为空
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String getSignEncode(HttpServletRequest request,
			String secretKey) throws UnsupportedEncodingException {
		return getSign(request, secretKey, true);
	}

	/**
	 * 生成Sign加密字符串，按照参数字符串的升序排序加密
	 * 
	 * @param request
	 * @param secretKey
	 *            密钥 ，对sign进行加严，可为空
	 * @param isEncode
	 *            是否对参数编码
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String getSign(HttpServletRequest request, String secretKey,
			boolean isEncode) throws UnsupportedEncodingException {

		// Sign Key
		Map<String, Object> params = new HashMap<String, Object>();

		Enumeration<?> keyEnums = request.getParameterNames();
		while (keyEnums.hasMoreElements()) {
			String key = (String) keyEnums.nextElement();
			if (SIGN_PARAM_KEY.equals(key))
				continue;
			Object v = request.getParameter(key);
			params.put(key, v);
		}

		Set<String> keysSet = params.keySet();
		Object[] keys = keysSet.toArray();
		Arrays.sort(keys);
		// 获取请求方法名
		String methodName = Utility.getRequestUriMethodName(request
				.getPathInfo());
		StringBuilder sb = new StringBuilder();
		sb.append(methodName).append(JOIN_FLAG);

		// boolean first = true;
		for (Object key : keys) {
			// if (first) {
			// first = false;
			// } else {
			// sb.append("&");
			// }
			sb.append(key).append("=");
			Object value = params.get(key);
			String valueString = "";
			if (null != value) {
				valueString = String.valueOf(value);
			}
			if (isEncode) {
				sb.append(URLEncoder.encode(valueString, ENCODE));
			} else {
				sb.append(valueString);
			}
		}

		// 添加密钥
		sb.append(JOIN_FLAG).append(null == secretKey ? "" : secretKey);
		String signMing = sb.toString();
		sb = null;
		String sign = Md5Util.md5(signMing);

		log.info("sign加密前：" + signMing);
		log.info("sign加密后：" + sign);

		return sign;
	}
	
	public static String getQueryUrl(HttpServletRequest request,String method, String secretKey) throws UnsupportedEncodingException{
		return getQueryUrl(request, method, secretKey, false);
	}
	
	public static String getQueryUrl(HttpServletRequest request,String method, String secretKey,
			boolean isEncode) throws UnsupportedEncodingException{
		Map<String, Object> params = new HashMap<String, Object>();

		Enumeration<?> keyEnums = request.getParameterNames();
		while (keyEnums.hasMoreElements()) {
			String key = (String) keyEnums.nextElement();
			if (SIGN_PARAM_KEY.equals(key))
				continue;
			Object v = request.getParameter(key);
			params.put(key, v);
		}

		Set<String> keysSet = params.keySet();
		Object[] keys = keysSet.toArray();
		Arrays.sort(keys);
		StringBuilder url = new StringBuilder();//拼接请求字符串
		StringBuffer signStr = new StringBuffer(); //获取sign
		url.append(method).append("?");
		signStr.append(method).append(JOIN_FLAG);
		boolean first = true;
		for (Object key : keys) {
			if (first) {
				first = false;
			} else {
				url.append("&");
			}
			url.append(key).append("=");
			signStr.append(key).append("=");
			Object value = params.get(key);
			String valueString = "";
			if (null != value) {
				valueString = String.valueOf(value);
			}
			if (isEncode) {
				url.append(URLEncoder.encode(valueString, ENCODE));
				signStr.append(URLEncoder.encode(valueString, ENCODE));
			} else {
				url.append(valueString);
				signStr.append(valueString);
			}
		}
		signStr.append(JOIN_FLAG).append(null == secretKey ? "" : secretKey);
		String signMing = signStr.toString();
		signStr = null;
		String sign = Md5Util.md5(signMing);
		url.append("&").append(SIGN_PARAM_KEY).append("=").append(sign);
		String httpUrl = url.toString();
		url = null;
		return httpUrl;
	}

	/**
	 * 请求url是否过滤
	 * 
	 * @param url
	 * @return
	 */
	public static boolean isFilterUrl(HttpServletRequest request) {

		//FILTER_URL 例子：/statistic/countInfo,/statistic/dealerList
		
		String url = request.getPathInfo();
		if (!StringUtils.isBlank(FILTER_URL)) {
			String[] arr = FILTER_URL.split(",");
			for (String s : arr) {
				int index = url.indexOf(s);
				if (index != -1)
					return true;
			}
		}
		return false;
	}

	/**
	 * 验证Sign
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public static boolean validateSign(HttpServletRequest request){

		// 过滤url
		if (isFilterUrl(request))
			return true;

		try {
			String clientSign = request.getParameter(SIGN_PARAM_KEY);
			// TODO secretKey需要配置
			String serverSign = SignUtil.getSign(request, SECRET_KEY);
			log.info("******* server Sign : " + serverSign);
			log.info("******* client Sgin:  " + clientSign);
			if (!serverSign.equals(clientSign)) {
				log.error("【validateSign】参数Sign验证不正确:" + request.getQueryString());
				return false;
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}
