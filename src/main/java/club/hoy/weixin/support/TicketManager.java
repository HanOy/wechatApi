package club.hoy.weixin.support;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import club.hoy.base.exception.Exceptions;
import club.hoy.base.exception.SystemErrorCodes;
import club.hoy.weixin.api.TicketAPI;
import club.hoy.weixin.model.Ticket;

/**
 * TicketManager ticket 自动刷新
 * 
 * @author floating 支持在Spring环境中自动加载
 *
 */

@Service
public class TicketManager {

	// 微信平台的AppID值
	@Value("${weixin.appid}")
	private String APP_ID;

	@PostConstruct
	private void postConstruct() {
		init(APP_ID);
	}

	@PreDestroy
	private void preDestroy() {
		destroyed();
	}

	private static Map<String, String> ticketMap = new LinkedHashMap<String, String>();

	private static Map<String, Timer> timerMap = new HashMap<String, Timer>();

	/**
	 * 初始化ticket 刷新，每119分钟刷新一次。 依赖TokenManager
	 * 
	 * @param appid
	 */
	public static void init(final String appid) {
		if (!timerMap.containsKey(appid)) {
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					String access_token = TokenManager.getToken(appid);
					// access_token可能还没有加载好
					int count = 0;
					while (access_token == null) {
						if (count >= 5) {
							// 超过重试次数，应用启动异常
							throw Exceptions.except(SystemErrorCodes.APPLICATION_NOT_AVAILABLE);
						}
						try {
							count++;
							access_token = TokenManager.getToken(appid);
							TimeUnit.SECONDS.sleep(1);
						} catch (InterruptedException e) {
						}
					}
					Ticket ticket = TicketAPI.getTicket(access_token);
					int ticketCount = 0;
                    while (ticket == null){
                        if (ticketCount >= 5) {
                            // 超过重试次数，应用启动异常
                            throw Exceptions.except(SystemErrorCodes.APPLICATION_NOT_AVAILABLE);
                        }try {
                            ticketCount++;
                            ticket = TicketAPI.getTicket(access_token);
                            TimeUnit.SECONDS.sleep(1);
                        } catch (InterruptedException e) {
                        }
                    }
					ticketMap.put(appid, ticket.getTicket());
				}
			}, 0, 1000 * 60 * 119);
			timerMap.put(appid, timer);
		}
	}

	/**
	 * 取消 ticket 刷新
	 */
	public static void destroyed() {
		for (Timer timer : timerMap.values()) {
			timer.cancel();
		}
	}

	/**
	 * 获取 jsapi ticket
	 * 
	 * @param appid
	 * @return
	 */
	public static String getTicket(final String appid) {
		return ticketMap.get(appid);
	}

	/**
	 * 获取第一个appid 的 jsapi ticket 适用于单一微信号
	 * 
	 * @param appid
	 * @return
	 */
	public static String getDefaultTicket() {
		Object[] objs = ticketMap.values().toArray();
		return objs.length > 0 ? objs[0].toString() : null;
	}

}
