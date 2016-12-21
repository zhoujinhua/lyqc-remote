package com.liyun.car.remote.utils;

import org.apache.log4j.Logger;

import com.liyun.car.remote.service.JHSendSmsService;

import net.sf.json.JSONObject;

/**
 * 聚合数据短信接口
 * 建立日期 2014-12-25
 */

public class JHSendSmsUtil implements Runnable {
	
	private static Logger log = Logger.getLogger(JHSendSmsUtil.class);
	
	//收件人手机号码
	private String _toPhone ;
	//短信模板ID
	private String _modelId;
	//短信模板参数
	private String _param;
	
	public JHSendSmsUtil(String _toPhone, String _modelId, String _param) {
		super();
		this._toPhone = _toPhone;
		this._modelId = _modelId;
		this._param = _param;
	}

	public String get_toPhone() {
		return _toPhone;
	}

	public void set_toPhone(String _toPhone) {
		this._toPhone = _toPhone;
	}

	public String get_modelId() {
		return _modelId;
	}

	public void set_modelId(String _modelId) {
		this._modelId = _modelId;
	}

	public String get_param() {
		return _param;
	}

	public void set_param(String _param) {
		this._param = _param;
	}

	private void sendTextMain(String toAddress,String modelId,String param){
        // 短信返回消息
    	String  message = "";
    	
		JSONObject object= JHSendSmsService.getRequest2(toAddress, modelId, param);
		if (object!=null) {
    		String code = object.getString("error_code");
    		if("0".equals(code)){ //不同渠道code意义不同
    			System.out.println("短信提交成功");
				message = "短信已经发送成功[to:"+toAddress+"]";
            }else{
            	message = "短信发送失败[to:"+toAddress+"]";
                System.out.println(object.get("error_code")+":"+object.get("reason"));
            }
		}else {
			message = "短信发送失败[to:"+toAddress+"]";
		}
    	
    	if(log.isInfoEnabled()){
            log.info(message);
        }
    	
    }
	
	public void run() {
		sendTextMain(_toPhone,_modelId,_param );
	}
}
