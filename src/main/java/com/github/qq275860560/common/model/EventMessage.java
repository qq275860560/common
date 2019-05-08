package com.github.qq275860560.common.model;

import java.io.Serializable;

/**
 * @author jiangyuanlin@163.com
 * 事件或消息
 */
public class EventMessage implements Serializable {

	/**
	 * 文本消息
	 */
	public static final int TEXT_Type = 1;

	/**
	 * 消息说明
	 */
	public static final String TEXT_REMARK = "文本消息";

	/**
	 * 初始化此对象的时间戳
	 */
	private long timestamp = System.currentTimeMillis();//
	/**
	 * 此对象的序列号，默认与时间戳相同	
	 */
	private long seq = timestamp;

	/**
	 * 消息类型，消息格式,比如{1:文本消息,2:视频消息,3:音乐消息,4:图片消息}
	 */
	private int type = TEXT_Type;
	/**
	 * 消息命令
	 */
	// private int command = TEXT_Type;
	/**
	 * 消息动作 
	 */
	// private int action = TEXT_Type;

	/**
	 * 当前发送方id,此id不一定为用户id，可能是群组id，某种组织id，如果业务需要区分，建议继承此类，扩展字段,比如增加fromType
	 */
	private String from;

	/**
	 * 当前接收方id,此id不一定为用户id.可能是群组id，某种组织id，如果业务需要区分，建议继承此类，扩展字段,比如增加toType
	 */
	private String to;

	/**
	 * 消息说明,让接收方更容易知道消息大概，比如解释一下此消息类型为文本消息，发送方的称呼,发送方的称呼,
	 */
	private String remark = TEXT_REMARK;

	/**
	 * 消息内容
	 */
	private Object data;

	public EventMessage() {

	}

	public EventMessage(Object data) {
		this.data = data;
	}

	public EventMessage(int type, Object data) {
		this.type = type;
		this.data = data;
	}

	public EventMessage(int type, String from, String to, Object data) {
		this.type = type;
		this.from = from;
		this.to = to;
		this.data = data;
	}

	public EventMessage(int type, String from, String to, String remark, Object data) {
		this.type = type;
		this.from = from;
		this.to = to;
		this.remark = remark;
		this.data = data;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public long getSeq() {
		return seq;
	}

	public void setSeq(long seq) {
		this.seq = seq;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "{\"timestamp\":" + timestamp + ",\"seq\":" + seq + ", \"type\":" + type + ", \"from\":" + from
				+ ", \"to\":" + to + ", \"remark\":" + remark + ", \"data\":" + data + "}";
	}

}
