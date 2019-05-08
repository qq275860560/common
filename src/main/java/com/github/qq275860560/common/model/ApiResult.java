package com.github.qq275860560.common.model;

import java.io.Serializable;

/**
 * @author jiangyuanlin@163.com
 * Api接口返回数据模型
 */
public class ApiResult implements Serializable {

	public static final int SUCCESS = 0;

	public static final int FAIL = -1;

	public static final int ERROR = -2;

	public static final int AUTHENTICATION_FAIL = -3;
	public static final int AUTHORIZATION_FAIL = -4;

	public static final String SUCCESS_MSG = "请求成功";// 运行成功

	public static final String FAIL_MSG = "请求失败";// 运行失败(客户端原因导致，一般是参数错误)

	public static final String ERROR_MSG = "请求错误";// 运行错误(服务端原因导致，一般是内存，cpu，硬盘，网络等原因导致)

	public static final String AUTHENTICATION_FAIL_MSG = "认证失败，token过期或者无效";//

	public static final String AUTHORIZATION_FAIL_MSG = "授权失败，登录用户无此接口权限,请联系管理员分配权限";//

	private int code = ApiResult.SUCCESS;

	private String msg = ApiResult.SUCCESS_MSG;

	private Object data = new Object();

	public ApiResult() {

	}

	public ApiResult(int code, String msg, Object data) {
		this.code = code;
		this.msg = msg;
		this.data = data;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "{\"code\":" + code + ",\"msg\":" + msg + ", \"data\":" + data + "}";
	}
}
