package com.liyun.car.remote.utils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.liyun.car.common.utils.StringUtils;

/**
 * 公共工具类
 * 
 * @author Zhaoxin.Zhao
 * 
 */
public class Utility {

	private static Logger log = LoggerFactory.getLogger(Utility.class);

	private Utility() {
	}

	/**
	 * 获取请求完整url
	 * 
	 * @param request
	 * @return
	 */
	public static String getRequestUrl(HttpServletRequest request) {
		String requestURI = request.getRequestURI();
		String url = request.getScheme() + "://" + request.getServerName()
				+ ":" + request.getServerPort() + requestURI;
		Map<String, String> paramsMap = getRequestParams2Map(request);
		if (paramsMap == null || paramsMap.size() == 0) {
			return url;
		}
		return url + "?" + getRequestParam2Str(request);
	}

	/**
	 * 获取请求参数
	 * 
	 * @param request
	 * @return
	 */
	public static Map<String, String> getRequestParams2Map(
			HttpServletRequest request) {
		@SuppressWarnings("unchecked")
		Map<String, String[]> pmap = request.getParameterMap();
		Map<String, String> paramsMap = new HashMap<String, String>();

		Iterator<String> iter = pmap.keySet().iterator();
		while (iter.hasNext()) {
			String key = iter.next();
			String[] v = pmap.get(key);
			String s = "";
			if (null != v && v.length > 0) {
				s = v[v.length - 1];
			}
			if (paramsMap.containsKey(key)) {
				paramsMap.put(key, paramsMap.get(key).concat(",").concat(s));
			} else {
				paramsMap.put(key, s);
			}
		}

		String keyTemp = null;
		for (Iterator<String> iterator = pmap.keySet().iterator(); iterator
				.hasNext();) {
			keyTemp = iterator.next();
			paramsMap.put(keyTemp,
					pmap.get(keyTemp) != null ? pmap.get(keyTemp)[0] : null);
		}
		return paramsMap;
	}

	/**
	 * 获取参数值，并拼装成url请求参数字符串
	 * 
	 * @param request
	 * @return 如：userName=zs&password=123456
	 * @throws Exception
	 */
	public static String getRequestParam2Str(HttpServletRequest request) {
		StringBuilder sb = new StringBuilder();
		Map<String, String> paramsMap = getRequestParams2Map(request);
		Iterator<String> iter = paramsMap.keySet().iterator();
		while (iter.hasNext()) {
			String key = iter.next();
			sb.append(key).append("=").append(paramsMap.get(key)).append("&");
		}
		String params = delLastStr(sb.toString());
		sb = null;
		return params;
	}

	/**
	 * 去掉最后一个字符
	 * 
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public static String delLastStr(String str) {
		if (StringUtils.isBlank(str)) {
			return "";
		}
		str = str.substring(0, str.length() - 1);
		return str;
	}

	/**
	 * 获取空字符串
	 * 
	 * @param str
	 * @return
	 */
	public static String getNullStr(String str) {
		return StringUtils.isBlank(str) ? "" : str;
	}
	
	/**
	 * 去掉字符串中空格
	 * 
	 * @param str
	 * @return
	 */
	public static String delSpace(String str) {
		if (StringUtils.isBlankObj(str))
			return "";
		return str.replace(" ", "");
	}

	/**
	 * Map --> Bean 2: 利用org.apache.commons.beanutils 工具类实现 Map --> Bean
	 * 
	 * @param map
	 * @param obj
	 * @throws Exception
	 */
	public static void mapToBean(Map<String, Object> map, Object obj)
			throws Exception {
		if (map == null || obj == null) {
			return;
		}
		BeanUtils.populate(obj, map);
	}

	/**
	 * Map --> Bean 1: 利用Introspector,PropertyDescriptor实现 Map --> Bean
	 * 
	 * @param map
	 * @param obj
	 * @throws Exception
	 */
	public static void map2ToBean(Map<String, Object> map, Object obj)
			throws Exception {

		BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
		PropertyDescriptor[] propertyDescriptors = beanInfo
				.getPropertyDescriptors();
		for (PropertyDescriptor property : propertyDescriptors) {
			String key = property.getName();
			if (map.containsKey(key)) {
				Object value = map.get(key);
				// 得到property对应的setter方法
				Method setter = property.getWriteMethod();
				setter.invoke(obj, value);
			}
		}
	}

	/**
	 * Bean --> Map 1: 利用Introspector和PropertyDescriptor 将Bean --> Map
	 * 
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> beanToMap(Object obj) throws Exception {

		if (obj == null)
			return null;
		if (obj instanceof Map) {
			return (Map<String, Object>) obj;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
		PropertyDescriptor[] propertyDescriptors = beanInfo
				.getPropertyDescriptors();
		for (PropertyDescriptor property : propertyDescriptors) {
			String key = property.getName();
			// 过滤class属性
			if (!key.equals("class")) {
				// 得到property对应的getter方法
				Method getter = property.getReadMethod();
				Object value = getter.invoke(obj);
				map.put(key, value);
			}
		}
		return map;
	}

	/**
	 * 拼接值，已逗号","隔开
	 * 
	 * @param list
	 * @return
	 * @throws Exception
	 */
	public static String getAppendStr(List<String> list) throws Exception {
		if (list == null || list.isEmpty())
			return "";
		StringBuilder sb = new StringBuilder("");
		for (String s : list) {
			sb.append(s).append(",");
		}
		String str = sb.toString();
		sb = null;
		return delLastStr(str);
	}

	/**
	 * 获取http请求方法名
	 * @param uri
	 * 			请求uri
	 * @return
	 * 			如：findPage.do
	 */
	public static String getRequestUriMethodName(String uri){
		
		String methodName = "";
		if(StringUtils.isBlank(uri))
			return methodName;
		int index = uri.indexOf("?");
		if(index != -1){
			int indexLast = uri.lastIndexOf("/");
			methodName = uri.substring(indexLast+1, index);
		}else {
			int indexLast = uri.lastIndexOf("/");
			methodName = uri.substring(indexLast+1, uri.length());
		}
		return methodName;
	}
	
	public static void main(String[] args) throws Exception {
		String url = "";
		url = "http://localhost:8080/demo/sys/login/forgotPwd?pwd=1&name=zha?sss";
		url = "http://localhost:8080/demo/sys/user/findPage";
		String name = getRequestUriMethodName(url);
		System.err.println(name);
	}

}
