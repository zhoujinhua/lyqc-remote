package com.liyun.car.remote.enums;

/**
 * 远程访问返回码 枚举
 * @author Cyn
 *
 */
public enum ReturnCodeEnum {

	code01("100","参数格式正确"),
	code02("108","请求标志不符合条件"),
	code03("109","参数格式有误"),
	code04("400","请求参数为空"),
	code05("0041","校验通过并有结果"),
	code06("0045","数据完整性错误（缺少请求类型或唯一标识码）"),
	code07("0046","唯一标识码不存在或已过期"),
	code08("0047","账号密码不能为空"),
	code09("9991","用户名或密码错误，请联系管理员"),
	code10("9992","用户名无权该操作，请联系管理员"),
	code11("9993","账号查询条数已满，请联系管理员"),
	code12("9205","网络或参数格式异常");
	
	private String name;
	private String value;
	
	private ReturnCodeEnum(String value,String name){
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return this.name;
	}

	public String getValue() {
		return this.value;
	}
	
	public static ReturnCodeEnum[] valuesNotNull(){
		return new ReturnCodeEnum[]{code01,code02,code03,code04,code05,code06,code07,code08,code09,code10,code11,code12};
	}
}
