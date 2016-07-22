package club.hoy.weixin;

/**
	 * 定义目前所支持的事件类型
	 * @author floating
	 *
	 */
public enum EVENT_TYPE {
		/**
		 * 事件推送
		 */
		event("event"),
		/**
		 * 普通文本消息
		 */
		text("text"),
		/**
		 * 关注
		 */
		subscribe("subscribe"),
		/**
		 * 取消关注
		 */
		unsubscribe("unsubscribe"),
		/**
		 * 扫码
		 */
		SCAN("SCAN"),
		/**
		 * 用户上报地理位置
		 */
		LOCATION("LOCATION"),
		/**
		 * 点击菜单跳转链接时的事件推送 
		 */
		VIEW("VIEW"),
		/**
		 * 点击菜单拉取消息时的事件推送 
		 */
		CLICK("CLICK");
	
		
		private String code;
		private EVENT_TYPE(String code) {
			this.code = code;
		}
		public String toString() {
			return this.code;
		}
		
		public boolean equals(String str) {
			return this.code.equals(str);
		}
	}