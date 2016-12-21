package com.liyun.car.remote.service;

import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.liyun.car.common.utils.ConverterUtil;
import com.liyun.car.common.utils.PropertyUtil;
import com.liyun.car.common.utils.ResponseUtils;
import com.liyun.car.remote.enums.ReturnCodeEnum;
import com.liyun.car.remote.utils.JYReqXmlUtil;

/**
 * 访问远程接口,jy
 * 网销模式
 * 返回中文需解码(URLDecoder)
 * @author Cyn
 *
 */
public class NetworkModeCarService {
	public final static String JY_USERNAME = PropertyUtil.getPropertyValue("JY_USERNAME");
	public final static String JY_PASSWORD = PropertyUtil.getPropertyValue("JY_PASSWORD");
	public final static String REQURL_BASE_URL = PropertyUtil.getPropertyValue("JY_REQURL_BASE_URL");
	/**
	 * 设置连接主机超时（单位：毫秒）  
	 */
	public final static Integer CONNECT_TIMEOUT =10000;
	/**
	 * 设置从主机读取数据超时（单位：毫秒）  
	 */
	public final static Integer READ_TIMEOUT =10000;
	
	private static Logger logger = LoggerFactory.getLogger(NetworkModeCarService.class);
	
	/**
	 * 模糊查询品牌车系信息
	 * @param searchName
	 * @return familyId
	 */
	public static String getCseriesBySchName(String searchName){
		String str="";
		String flag="";
		String msg="";
		try {  
            URL url = new URL(REQURL_BASE_URL);  
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setRequestMethod("POST");
            con.setConnectTimeout(CONNECT_TIMEOUT); 
            con.setReadTimeout(READ_TIMEOUT); 
            con.setRequestProperty("Pragma", "no-cache");  
            con.setRequestProperty("Cache-Control", "no-cache");  
            con.setRequestProperty("Content-Type", "text/xml");
            con.setRequestProperty("Accept-Charset", "UTF-8");
            
            OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream());      
            String xmlInfo = JYReqXmlUtil.reqCbrandXmlInfo(java.net.URLEncoder.encode(searchName, "UTF-8"),JY_USERNAME,JY_PASSWORD);
            out.write(new String(xmlInfo.getBytes("UTF-8")));  
            out.flush();  
            out.close();
            
            //取得输入流
    		InputStream is = con.getInputStream();  
                    
    		//读取输入流
    		SAXReader sr = new SAXReader() ;
    		Document document = sr.read(is);
    		//得到xml的根元素节点
    		Element root = document.getRootElement();
    		Element head = root.element("head");
    		Element body = root.element("body");
    		Element result = head.element("vehCode");
    		Element errorMessage = head.element("vehMessage");
    		flag = result.getText();
    		msg = errorMessage.getText();
    		
    		StringBuffer sb = new StringBuffer();
    		//遍历familyList点
    		if (ReturnCodeEnum.code05.getValue().equals(flag)) {//成功
    			Element dataList = body.element("dataList");
    			List<Element> familys = dataList.elements("family");
    			
    			sb.append("{\"responseCode\":\""+flag+"\",\"responseMsg\":\""+ConverterUtil.URLDecoder(msg)+"\",\"" + ResponseUtils.RESPONSE_TEXT_KEY+ "\":[");
    			for(Element element : familys){
    				sb.append("{");
    				sb.append("\"familyId\":\""+element.element("familyId").getText()+"\",");
    				sb.append("\"familyName\":\""+ConverterUtil.URLDecoder(element.element("familyName").getText().trim())+"\"");
    				sb.append("},");
    			}
    			str = sb.substring(0, sb.length()-1);
    			str +="]}";
			}
        } catch (Exception e) {
        	logger.error("模糊查询车系异常",e);
            e.printStackTrace();
        }
		return str;
	}
	
	/**
	 * 
	 * @return 全量品牌
	 */
	public static String getBrand(){
		String str="";
		String flag="";
		String msg="";
		try {  
            URL url = new URL(REQURL_BASE_URL);  
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setRequestMethod("POST");
            con.setConnectTimeout(CONNECT_TIMEOUT); 
            con.setReadTimeout(READ_TIMEOUT); 
            con.setRequestProperty("Pragma", "no-cache");  
            con.setRequestProperty("Cache-Control", "no-cache");  
            con.setRequestProperty("Content-Type", "text/xml");
            con.setRequestProperty("Accept-Charset", "UTF-8");
            
            OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream());      
            String xmlInfo = JYReqXmlUtil.reqCbrandXmlInfo(JY_USERNAME,JY_PASSWORD);
            out.write(new String(xmlInfo.getBytes("UTF-8")));  
            out.flush();  
            out.close();
            
            //取得输入流
    		InputStream is = con.getInputStream();  
                    
    		//读取输入流
    		SAXReader sr = new SAXReader() ;
    		Document document = sr.read(is);
    		//得到xml的根元素节点
    		Element root = document.getRootElement();
    		Element head = root.element("head");
    		Element body = root.element("body");
    		Element result = head.element("vehCode");
    		Element errorMessage = head.element("vehMessage");
    		flag = result.getText();
    		msg = errorMessage.getText();
    		
    		StringBuffer sb = new StringBuffer();
    		//遍历familyList点
    		if (ReturnCodeEnum.code05.getValue().equals(flag)) {//成功
    			Element dataList = body.element("dataList");
    			List<Element> brands = dataList.elements("brand");
    			
    			sb.append("{\"responseCode\":\""+flag+"\",\"responseMsg\":\""+ConverterUtil.URLDecoder(msg)+"\",\"" + ResponseUtils.RESPONSE_TEXT_KEY+ "\":[");
    			for(Element element : brands){
    				sb.append("{");
    				sb.append("\"brandId\":\""+element.element("brandId").getText()+"\",");
    				sb.append("\"brandName\":\""+element.element("brandInitial").getText() +" "+ConverterUtil.URLDecoder(element.element("brandName").getText().trim())+"\"");
    				sb.append("},");
    			}
    			str = sb.substring(0, sb.length()-1);
    			str +="]}";
			}else {
				System.out.println();
			}
        } catch (Exception e) {
        	logger.error("请求全量品牌异常",e);
            e.printStackTrace();
        }
		return str;
	}
	
	/**
	 * 品牌查车系
	 * @param brandId
	 */
	public static String getCseriesByBrandId(String brandId){
		String str="";
		String flag="";
		String msg="";
		try {  
            URL url = new URL(REQURL_BASE_URL);  
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setRequestMethod("POST");
            con.setConnectTimeout(CONNECT_TIMEOUT); 
            con.setReadTimeout(READ_TIMEOUT); 
            con.setRequestProperty("Pragma", "no-cache");  
            con.setRequestProperty("Cache-Control", "no-cache");  
            con.setRequestProperty("Content-Type", "text/xml");
            con.setRequestProperty("Accept-Charset", "UTF-8");
            
            OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream());      
            String xmlInfo = JYReqXmlUtil.reqCseriesXmlInfo(brandId,JY_USERNAME,JY_PASSWORD);
            out.write(new String(xmlInfo.getBytes("UTF-8")));  
            out.flush();  
            out.close();
            
            //取得输入流
    		InputStream is = con.getInputStream();  
                    
    		//读取输入流
    		SAXReader sr = new SAXReader() ;
    		Document document = sr.read(is);
    		//得到xml的根元素节点
    		Element root = document.getRootElement();
    		Element head = root.element("head");
    		Element body = root.element("body");
    		Element result = head.element("vehCode");
    		Element errorMessage = head.element("vehMessage");
    		flag = result.getText();
    		msg = errorMessage.getText();
    		
    		StringBuffer sb = new StringBuffer();
    		//遍历familyList点
    		if (ReturnCodeEnum.code05.getValue().equals(flag)) {//成功
    			Element dataList = body.element("dataList");
    			List<Element> familys = dataList.elements("family");
    			
    			sb.append("{\"responseCode\":\""+flag+"\",\"responseMsg\":\""+ConverterUtil.URLDecoder(msg)+"\",\"" + ResponseUtils.RESPONSE_TEXT_KEY+ "\":[");
    			for(Element element : familys){
    				sb.append("{");
    				sb.append("\"familyId\":\""+element.element("familyId").getText()+"\",");
    				sb.append("\"familyName\":\""+ConverterUtil.URLDecoder(element.element("familyName").getText().trim())+"\"");
    				sb.append("},");
    			}
    			str = sb.substring(0, sb.length()-1);
    			str +="]}";
			}
        } catch (Exception e) {
        	logger.error("查询车系异常",e);
            e.printStackTrace();
        }
		//System.out.println(flag+":"+ConverterUtil.URLDecoder(msg));
		return str;
	}
	
	/**
	 * 车系查车款
	 * @param familyId
	 * @return
	 */
	public static String getCstyleByFamilyId(String familyId){
		String flag = ""; //返回标志
		String msg =""; //返回信息
		String str ="";
		try {  
            URL url = new URL(REQURL_BASE_URL);  
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setRequestMethod("POST");
            con.setConnectTimeout(CONNECT_TIMEOUT); 
            con.setReadTimeout(READ_TIMEOUT); 
            con.setRequestProperty("Pragma", "no-cache");  
            con.setRequestProperty("Cache-Control", "no-cache");  
            con.setRequestProperty("Content-Type", "text/xml");
            con.setRequestProperty("Accept-Charset", "UTF-8");
            
            OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream());      
            String xmlInfo = JYReqXmlUtil.reqCstyleXmlInfo(familyId,JY_USERNAME,JY_PASSWORD);  
            out.write(new String(xmlInfo.getBytes("UTF-8")));  
            out.flush();  
            out.close();
            
            //取得输入流
    		InputStream is = con.getInputStream();  
                    
    		//读取输入流
    		SAXReader sr = new SAXReader() ;
    		Document document = sr.read(is);
    		//得到xml的根元素节点
    		Element root = document.getRootElement();
    		//得到根元素的子节点head，body
    		Element head = root.element("head");
    		Element body = root.element("body");
    		Element result = head.element("vehCode");
    		Element errorMessage = head.element("vehMessage");
    		flag = result.getText();
    		msg = errorMessage.getText();
    		StringBuffer sb = new StringBuffer();
    		sb.append("{\"responseCode\":\""+flag+"\" ,\"responseMsg\":\""+ConverterUtil.URLDecoder(msg)+"\",");
    		if (ReturnCodeEnum.code05.getValue().equals(flag)) {//成功
    			Element styleList = body.element("dataList");
    			List<Element> groups = styleList.elements("group");
    			sb.append("\"groups\":[");
    			for (Element element : groups) {
    				Element groupIdE = element.element("groupId");
    				Element groupCodeE = element.element("groupCode");
    				Element groupNameE = element.element("groupName");
    				String groupId = groupIdE==null?"":groupIdE.getText();
    				String groupCode = groupCodeE==null?"":groupCodeE.getText();
    				String groupName = groupNameE==null?"":ConverterUtil.URLDecoder(element.element("groupName").getText());
    				sb.append("{");
    				sb.append("\"groupId\":\""+groupId+"\",");
    				sb.append("\"groupCode\":\""+groupCode+"\",");
    				sb.append("\"groupName\":\""+groupName+"\"");
    				sb.append("},");
				}
    			 str = sb.substring(0, sb.length()-1);
    			str +="]}";
			}else{
				//待处理
			}
        } catch (Exception e) {
        	logger.error("查询车款异常","responseCode:"+flag+",errorMessage:"+msg+"  "+e);
            e.printStackTrace();
        }
		//System.out.println(str);
		return str;
	}
	
	/**
	 * 车系查车款
	 * @param familyId
	 * @return list
	 */
	public static List<Map<String, String>> getCstyleByFamilyId2(String familyId){
		Map<String, String> resultMap = null;
		String flag = ""; //返回标志
		String msg =""; //返回信息
		List<Map<String, String>> listM = new ArrayList<Map<String, String>>();
		try {  
            URL url = new URL(REQURL_BASE_URL);  
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setRequestMethod("POST");
            con.setConnectTimeout(CONNECT_TIMEOUT); 
            con.setReadTimeout(READ_TIMEOUT);  
            con.setDoOutput(true);  
            con.setRequestProperty("Pragma", "no-cache");  
            con.setRequestProperty("Cache-Control", "no-cache");  
            con.setRequestProperty("Content-Type", "text/xml");
            con.setRequestProperty("Accept-Charset", "UTF-8");
            
            OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream());      
            String xmlInfo = JYReqXmlUtil.reqCstyleXmlInfo(familyId,JY_USERNAME,JY_PASSWORD);  
            out.write(new String(xmlInfo.getBytes("UTF-8")));  
            out.flush();  
            out.close();
            
            //取得输入流
    		InputStream is = con.getInputStream();  
                    
    		//读取输入流
    		SAXReader sr = new SAXReader() ;
    		Document document = sr.read(is);
    		//得到xml的根元素节点
    		Element root = document.getRootElement();
    		//得到根元素的子节点head，body
    		Element head = root.element("head");
    		Element body = root.element("body");
    		Element result = head.element("vehCode");
    		Element errorMessage = head.element("vehMessage");
    		flag = result.getText();
    		msg = errorMessage.getText();
    		if (ReturnCodeEnum.code05.getValue().equals(flag)) {//成功
    			Element styleList = body.element("dataList");
    			List<Element> groups = styleList.elements("group");
    			for (Element element : groups) {
    				resultMap = new HashMap<String, String>();
    				resultMap.put("groupId",element.element("groupId").getText());
    				resultMap.put("groupCode",element.element("groupCode").getText());
    				resultMap.put("groupName", ConverterUtil.URLDecoder(element.element("groupName").getText()));
    				listM.add(resultMap);
				}
			}else{
				//待处理
			}
        } catch (Exception e) {
        	logger.error("查询车款异常","responseCode:"+flag+",errorMessage:"+msg+"  "+e);
            e.printStackTrace();
        }
		return listM;
	}
	
	/**
	 * 车款/排量吨位/档位查询排量吨位/档位/父车型
	 * @param groupId
	 * @param disTon
	 * @param gearBox (encode转码)
	 * @return
	 */
	public static String getCparentByPam(String groupId,String disTon,String gearBox){
		String str="";
		try {
			//groupName = listMap.get(i).get("groupName");
            URL url = new URL(REQURL_BASE_URL);  
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setRequestMethod("POST");
            con.setConnectTimeout(CONNECT_TIMEOUT); 
            con.setReadTimeout(READ_TIMEOUT); 
            con.setRequestProperty("Pragma", "no-cache");  
            con.setRequestProperty("Cache-Control", "no-cache");  
            con.setRequestProperty("Content-Type", "text/xml");
            con.setRequestProperty("Accept-Charset", "UTF-8");
            
            OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream());      
            String xmlInfo = JYReqXmlUtil.reqCparentXmlInfo(groupId, disTon, gearBox,JY_USERNAME,JY_PASSWORD);  
            out.write(new String(xmlInfo.getBytes("UTF-8")));  
            out.flush();  
            out.close();
            
            //取得输入流
            InputStream is = con.getInputStream();  
                    
    		//读取输入流
    		SAXReader sr = new SAXReader() ;
    		Document document = sr.read(is);
    		//得到xml的根元素节点
    		Element root = document.getRootElement();
    		//得到根元素的子节点head，body
    		//List<Element> elements = root.elements();
    		Element head = root.element("head");
    		Element body = root.element("body");
    		Element result = head.element("vehCode");
    		Element errorMessage = head.element("vehMessage");
    		String flag = result.getText();
    		String msg = errorMessage.getText();
    		
    		
    		StringBuffer sb = new StringBuffer();
    		sb.append("{\"responseCode\":\""+flag+"\" ,\"responseMsg\":\""+ConverterUtil.URLDecoder(msg)+"\"");
    		//遍历节点
    		if (ReturnCodeEnum.code05.getValue().equals(flag)) {//成功
    			Element dataList = body.element("dataList");
    			List<Element> lists = dataList.elements("list");
    			//已知xml格式
    			List<Element> disTons = lists.get(0).elements("disTons");
    			List<Element> gearBoxs = lists.get(1).elements("gearBoxs");
    			List<Element> parents = lists.get(2).elements("parent");
    			sb.append(",");
				sb.append("\"disTons\":[");
    			for (Element element : disTons) {
    				Element disTonE = element.element("disTon");
    				String disTon_p = disTonE==null?"":disTonE.getText();
    				sb.append("{");
    				sb.append("\"disTon\":\""+disTon_p+"\"");
    				sb.append("},");
    			}
    			sb.deleteCharAt(sb.length()-1);
    			sb.append("],");
				sb.append("\"gearBoxs\":[");
    			for (Element element : gearBoxs) {
    				Element gearBoxE = element.element("gearBox");
    				String gearBox_p = gearBoxE==null?"":ConverterUtil.URLDecoder(gearBoxE.getText());
    				sb.append("{");
    				sb.append("\"gearBox\":\""+gearBox_p+"\"");
    				sb.append("},");
    			}
    			sb.deleteCharAt(sb.length()-1);
    			sb.append("],");
    			System.out.println(str);
    			
    			sb.append("\"parents\":[");
    			for (Element element : parents) {
    				Element parentVehIdE = element.element("parentVehId");
    				Element parentVehNameE = element.element("parentVehName");
    				Element parentVehCodeE = element.element("parentVehCode");
    				Element vehicleConfigureE = element.element("vehicleConfigure");
    				
    				String vehicleCode = parentVehIdE==null?"":ConverterUtil.URLDecoder(parentVehIdE.getText());
    				String parentVehName = parentVehNameE==null?"":ConverterUtil.URLDecoder(parentVehNameE.getText());
    				String parentVehCode = parentVehCodeE==null?"":ConverterUtil.URLDecoder(parentVehCodeE.getText());
    				String vehicleConfigure	= vehicleConfigureE==null?"":ConverterUtil.URLDecoder(vehicleConfigureE.getText());
    				
    				sb.append("{");
    				sb.append("\"vehicleCode\":\""+vehicleCode+"\",");
    				sb.append("\"parentVehName\":\""+parentVehName+"\",");
    				sb.append("\"parentVehCode\":\""+parentVehCode+"\",");
    				sb.append("\"vehicleConfigure\":\""+vehicleConfigure+"\"");
    				sb.append("},");
				}
    			str = sb.substring(0, sb.length()-1);
    			str +="]}";
				} //if end
    		else {
    			sb.append("}");
    			str = sb.substring(0, sb.length());
    			System.out.println(str);
			}
        } catch (Exception e) {
        	logger.error("查询车款/排量吨位/档位查询排量吨位/档位/父车型",e);
            e.printStackTrace();
        }
		return str;
	}
	
	/**
	 *  查车辆详细配置
	 * @param listMap
	 * @param disTon
	 * @param gearBox
	 * @return
	 */
	public static String getCparentByListGroupId(List<Map<String, String>> listMap,String disTon,String gearBox){
		String str="";
		String groupId ="";
		try {
			for (int i = 0; i < listMap.size(); i++) {
				groupId = listMap.get(i).get("groupId");
	            URL url = new URL(REQURL_BASE_URL);  
	            HttpURLConnection con = (HttpURLConnection) url.openConnection();
	            
	            con.setDoOutput(true);
	            con.setDoInput(true);
	            con.setRequestMethod("POST");
	            con.setConnectTimeout(CONNECT_TIMEOUT); 
	            con.setReadTimeout(READ_TIMEOUT); 
	            con.setRequestProperty("Pragma", "no-cache");  
	            con.setRequestProperty("Cache-Control", "no-cache");  
	            con.setRequestProperty("Content-Type", "text/xml");
	            con.setRequestProperty("Accept-Charset", "UTF-8");
	            
	            OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream());      
	            String xmlInfo = JYReqXmlUtil.reqCparentXmlInfo(groupId, disTon, gearBox,JY_USERNAME,JY_PASSWORD);  
	            out.write(new String(xmlInfo.getBytes("UTF-8")));  
	            out.flush();  
	            out.close();
	            
	            //取得输入流
	            InputStream is = con.getInputStream();  
	                    
	    		//读取输入流
	    		SAXReader sr = new SAXReader() ;
	    		Document document = sr.read(is);
	    		//得到xml的根元素节点
	    		Element root = document.getRootElement();
	    		//得到根元素的子节点head，body
	    		Element head = root.element("head");
	    		Element body = root.element("body");
	    		Element result = head.element("vehCode");
	    		Element errorMessage = head.element("vehMessage");
	    		String flag = result.getText();
	    		String msg = errorMessage.getText();
	    		//遍历节点
	    		StringBuffer sb = new StringBuffer();
	    		sb.append("{\"responseCode\":\""+flag+"\" ,\"responseMsg\":\""+ConverterUtil.URLDecoder(msg)+"\"");
	    		if (ReturnCodeEnum.code05.getValue().equals(flag)) {//成功
	    			Element dataList = body.element("dataList");
	    			List<Element> lists = dataList.elements("list");
	    			//已知xml格式
	    			List<Element> disTons = lists.get(0).elements("disTons");
	    			List<Element> gearBoxs = lists.get(1).elements("gearBoxs");
	    			List<Element> parents = lists.get(2).elements("parent");
	    			sb.append(",");
    				sb.append("\"disTons\":[");
	    			for (Element element : disTons) {
	    				Element disTonE = element.element("disTon");
	    				String disTon_p = disTonE==null?"":disTonE.getText();
	    				sb.append("{");
	    				sb.append("\"disTon\":\""+disTon_p+"\"");
	    				sb.append("},");
	    			}
	    			sb.deleteCharAt(sb.length()-1);
	    			sb.append("],");
	    			//System.out.println(str);
    				sb.append("\"gearBoxs\":[");
	    			for (Element element : gearBoxs) {
	    				Element gearBoxE = element.element("gearBox");
	    				String gearBox_p = gearBoxE==null?"":ConverterUtil.URLDecoder(gearBoxE.getText());
	    				sb.append("{");
	    				sb.append("\"gearBox\":\""+gearBox_p+"\"");
	    				sb.append("},");
	    			}
	    			sb.deleteCharAt(sb.length()-1);
	    			sb.append("],");
	    			System.out.println(str);
	    			
	    			sb.append("\"parents\":[");
	    			for (Element element : parents) {
	    				Element parentVehIdE = element.element("parentVehId");
	    				Element parentVehNameE = element.element("parentVehName");
	    				Element parentVehCodeE = element.element("parentVehCode");
	    				Element vehicleConfigureE = element.element("vehicleConfigure");
	    				
	    				String vehicleCode = parentVehIdE==null?"":ConverterUtil.URLDecoder(parentVehIdE.getText());
	    				String parentVehName = parentVehNameE==null?"":ConverterUtil.URLDecoder(parentVehNameE.getText());
	    				String parentVehCode = parentVehCodeE==null?"":ConverterUtil.URLDecoder(parentVehCodeE.getText());
	    				String vehicleConfigure	= vehicleConfigureE==null?"":ConverterUtil.URLDecoder(vehicleConfigureE.getText());
	    				
	    				sb.append("{");
	    				sb.append("\"vehicleCode\":\""+vehicleCode+"\",");
	    				sb.append("\"parentVehName\":\""+parentVehName+"\",");
	    				sb.append("\"parentVehCode\":\""+parentVehCode+"\",");
	    				sb.append("\"vehicleConfigure\":\""+vehicleConfigure+"\"");
	    				sb.append("},");
					}
	    			str = sb.substring(0, sb.length()-1);
	    			str +="]}";
				} //if end
	    		else {
	    			sb.append("}");
	    			str = sb.substring(0, sb.length());
	    			System.out.println(str);
				}
			}// list's for end 
        } catch (Exception e) {
        	logger.error("查询车辆详细配置异常",e);
            e.printStackTrace();
        }
		return str;
	}
}
