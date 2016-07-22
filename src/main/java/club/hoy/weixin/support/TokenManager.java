package club.hoy.weixin.support;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import club.hoy.weixin.api.TokenAPI;
import club.hoy.weixin.model.Token;

@Service
public class TokenManager {
	private static Map<String, String> tokenMap = new LinkedHashMap<String, String>();
	private static Map<String, Timer> timerMap = new HashMap<String, Timer>();
	@Value("${weixin.appid}")
	private String APP_ID;
	@Value("${weixin.appsecret}")
	private String APP_SECRET;

	@PostConstruct
	private void postConstruct() {
		init(this.APP_ID, this.APP_SECRET);
	}

	@PreDestroy
	private void preDestroy() {
	}

	static void init(final String appid, final String secret) {
		if (timerMap.containsKey(appid)) {
			((Timer) timerMap.get(appid)).cancel();
		}
		TimerTask task = new TimerTask() {
			public void run() {
				Token token = TokenAPI.token(appid, secret);
				tokenMap.put(appid, token.getAccess_token());
			}
		};
		Timer timer = new Timer();
		timer.schedule(task, 0L, 7080000L);
		timerMap.put(appid, timer);
	}

	static void destroyed() {
		for (Timer timer : timerMap.values()) {
			timer.cancel();
		}
	}

	public static String getToken(String appid) {
		return (String) tokenMap.get(appid);
	}

	public static String getDefaultToken() {
		Object[] objs = tokenMap.values().toArray();
		return objs.length > 0 ? objs[0].toString() : null;
	}
}
