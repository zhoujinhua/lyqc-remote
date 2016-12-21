package com.liyun.car.remote.utils;

/**
 * 金友远程请求xml数据
 * @author Cyn
 *
 */
public class JYReqXmlUtil {

	/**
	 * 全量品牌信息查询
	 * @return
	 */
	public static String reqCbrandXmlInfo(String JY_USERNAME,String JY_PASSWORD){
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"GBK\"?>");  
        sb.append("<RequestData>");  
        sb.append("<Head>");  
        sb.append("<requestFlag>201</requestFlag>");  
        sb.append("<requestType>02</requestType>");  
        sb.append("<requestUser>");  
        sb.append(JY_USERNAME);  
        sb.append("</requestUser>");
        sb.append("<requestPassword>");  
        sb.append(JY_PASSWORD);  
        sb.append("</requestPassword>");
        sb.append("</Head>");  
        sb.append("</RequestData>"); 
		return sb.toString();
	}
	
	/**
	 * 模糊查询品牌车系信息
	 * @param searchName
	 * @return
	 */
	public static String reqCbrandXmlInfo(String searchName,String JY_USERNAME,String JY_PASSWORD){
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"GBK\"?>");  
        sb.append("<RequestData>");  
        sb.append("<Head>");  
        sb.append("<requestFlag>202</requestFlag>");  
        sb.append("<requestType>02</requestType>");  
        sb.append("<requestUser>");  
        sb.append(JY_USERNAME);  
        sb.append("</requestUser>");
        sb.append("<requestPassword>");  
        sb.append(JY_PASSWORD);  
        sb.append("</requestPassword>");
        sb.append("</Head>");  
        sb.append("<Body>");  
        sb.append("<searchName>"); 
        sb.append(searchName); 
        sb.append("</searchName>"); 
        sb.append("</Body>"); 
        sb.append("</RequestData>"); 
		return sb.toString();
	}
	/**
	 * 品牌查询车系信息
	 * @param brandId
	 * @return
	 */
	public static String reqCseriesXmlInfo(String brandId,String JY_USERNAME,String JY_PASSWORD){
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"GBK\"?>");  
        sb.append("<RequestData>");  
        sb.append("<Head>");  
        sb.append("<requestFlag>203</requestFlag>");  
        sb.append("<requestType>02</requestType>");  
        sb.append("<requestUser>");  
        sb.append(JY_USERNAME);  
        sb.append("</requestUser>");
        sb.append("<requestPassword>");  
        sb.append(JY_PASSWORD);  
        sb.append("</requestPassword>");
        sb.append("</Head>");  
        sb.append("<Body>");  
        sb.append("<brandId>"); 
        sb.append(brandId); 
        sb.append("</brandId>"); 
        sb.append("</Body>"); 
        sb.append("</RequestData>"); 
		return sb.toString();
	}
	/**
	 * 车系查询车款信息
	 * @param familyId
	 * @return
	 */
	public static String reqCstyleXmlInfo(String familyId,String JY_USERNAME,String JY_PASSWORD){
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"GBK\"?>");  
        sb.append("<RequestData>");  
        sb.append("<Head>");  
        sb.append("<requestFlag>204</requestFlag>");  
        sb.append("<requestType>02</requestType>");  
        sb.append("<requestUser>");  
        sb.append(JY_USERNAME);  
        sb.append("</requestUser>");
        sb.append("<requestPassword>");  
        sb.append(JY_PASSWORD);  
        sb.append("</requestPassword>");
        sb.append("</Head>");  
        sb.append("<Body>");  
        sb.append("<familyId>"); 
        sb.append(familyId); 
        sb.append("</familyId>"); 
        sb.append("</Body>"); 
        sb.append("</RequestData>"); 
		return sb.toString();
	}
	
	/**
	 * 车款/排量吨位/档位查询排量吨位/档位/父车型
	 * @param groupId
	 * @param disTon
	 * @param gearBox
	 * @return
	 */
	public static String reqCparentXmlInfo(String groupId,String disTon,String gearBox,String JY_USERNAME,String JY_PASSWORD){
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"GBK\"?>");  
        sb.append("<RequestData>");  
        sb.append("<Head>");  
        sb.append("<requestFlag>205</requestFlag>");  
        sb.append("<requestType>02</requestType>");  
        sb.append("<requestUser>");  
        sb.append(JY_USERNAME);  
        sb.append("</requestUser>");
        sb.append("<requestPassword>");  
        sb.append(JY_PASSWORD);  
        sb.append("</requestPassword>");
        sb.append("</Head>");  
        sb.append("<Body>");  
        sb.append("<groupId>"); 
        sb.append(groupId); 
        sb.append("</groupId>");
        sb.append("<disTon>"); 
        sb.append(disTon); 
        sb.append("</disTon>");
        sb.append("<gearBox>"); 
        sb.append(gearBox); 
        sb.append("</gearBox>");
        sb.append("</Body>"); 
        sb.append("</RequestData>"); 
		return sb.toString();
	}
	/**
	 * 父车型查询车辆型号信息
	 * @param parentVehId
	 * @return
	 */
	public static String reqCmodelXmlInfo(String parentVehId,String JY_USERNAME,String JY_PASSWORD){
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"GBK\"?>");  
        sb.append("<RequestData>");  
        sb.append("<Head>");  
        sb.append("<requestFlag>206</requestFlag>");  
        sb.append("<requestType>02</requestType>");  
        sb.append("<requestUser>");  
        sb.append(JY_USERNAME);  
        sb.append("</requestUser>");
        sb.append("<requestPassword>");  
        sb.append(JY_PASSWORD);  
        sb.append("</requestPassword>");
        sb.append("</Head>");  
        sb.append("<Body>");  
        sb.append("<parentVehId>"); 
        sb.append(parentVehId); 
        sb.append("</parentVehId>"); 
        sb.append("</Body>"); 
        sb.append("</RequestData>"); 
		return sb.toString();
	}
	/**
	 * 车辆型号查询父车型信息
	 * @param vehicleFgwCode
	 * @return
	 */
	public static String reqCpXmlInfo(String vehicleFgwCode,String JY_USERNAME,String JY_PASSWORD){
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"GBK\"?>");  
        sb.append("<RequestData>");  
        sb.append("<Head>");  
        sb.append("<requestFlag>207</requestFlag>");  
        sb.append("<requestType>02</requestType>");  
        sb.append("<requestUser>");  
        sb.append(JY_USERNAME);  
        sb.append("</requestUser>");
        sb.append("<requestPassword>");  
        sb.append(JY_PASSWORD);  
        sb.append("</requestPassword>");
        sb.append("</Head>");  
        sb.append("<Body>");  
        sb.append("<vehicleFgwCode>"); 
        sb.append(vehicleFgwCode); 
        sb.append("</vehicleFgwCode>"); 
        sb.append("</Body>"); 
        sb.append("</RequestData>"); 
		return sb.toString();
	}
	/**
	 * 查询车型详细信息
	 * @param parentVehId
	 * @param vehicleFgwCode
	 * @return
	 */
	public static String reqCdetailXmlInfo(String parentVehId ,String vehicleFgwCode,String JY_USERNAME,String JY_PASSWORD){
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"GBK\"?>");  
        sb.append("<RequestData>");  
        sb.append("<Head>");  
        sb.append("<requestFlag>208</requestFlag>");  
        sb.append("<requestType>02</requestType>");  
        sb.append("<requestUser>");  
        sb.append(JY_USERNAME);  
        sb.append("</requestUser>");
        sb.append("<requestPassword>");  
        sb.append(JY_PASSWORD);  
        sb.append("</requestPassword>");
        sb.append("</Head>");  
        sb.append("<Body>");  
        sb.append("<parentVehId>"); 
        sb.append(parentVehId); 
        sb.append("</parentVehId>"); 
        sb.append("<vehicleFgwCode>"); 
        sb.append(vehicleFgwCode); 
        sb.append("</vehicleFgwCode>"); 
        sb.append("</Body>"); 
        sb.append("</RequestData>"); 
		return sb.toString();
	}
}
