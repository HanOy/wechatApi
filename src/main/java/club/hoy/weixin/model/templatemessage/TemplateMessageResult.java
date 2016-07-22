package club.hoy.weixin.model.templatemessage;

import club.hoy.weixin.model.BaseResult;

public class TemplateMessageResult extends BaseResult{

	private Long msgid;

	public Long getMsgid() {
		return msgid;
	}

	public void setMsgid(Long msgid) {
		this.msgid = msgid;
	}


}
