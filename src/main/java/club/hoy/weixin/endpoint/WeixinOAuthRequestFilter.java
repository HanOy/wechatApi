package club.hoy.weixin.endpoint;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import club.hoy.weixin.api.SnsAPI;
import club.hoy.weixin.util.ExpireSet;

/**
 * 微信页面授权处理拦截器 处理流程为： 用户访问某个url时，被拦截器拦截 拦截器检查该用户是否已经确认授权
 * 如果已经授权，拦截器刷新token，并直接pass 如果未授权，拦截器通过302方式让用户重定向访问微信平台授权页面
 * 当用户完成授权页面操作时，会转跳回该url 拦截器判断该请求是否来自微信开放平台转跳
 * 如果是，根据code获取用户的access_token,fresh_token,头像等信息 如果用户未同意授权，则根据规则确定是否pass
 * 
 * @author floating
 *
 */

@Component("weixinOAuthRequestFilter")
public class WeixinOAuthRequestFilter extends OncePerRequestFilter {

	// 微信平台的Token值
	@Value("${weixin.token}")
	private String TOKEN;

	// 微信平台的AppID值
	@Value("${weixin.appid}")
	private String APP_ID;

	// 微信平台的AppID值
	@Value("${weixin.appsecret}")
	private String APP_SECRET;

	// HTTP HEADER上的UserAgent数据
	private static final String USER_AGENT = "User-Agent";

	// 微信浏览器在UserAgent中的标记信息
	private static final String WEIXIN_USER_AGENT_FLAG = "MicroMessenger".toLowerCase();

	// 微信pc版内置浏览器在UserAgent中的标记信息

	// 微信公众平台的标记信息
	private static final String WEIXIN_API_REFERER_FLAG = "state";

	// 在当前会话中保存的当前用户的open_id
	private static final String OPEN_ID_IN_SESSION = "__wx_open_id";

	// 保存State参数,60秒过期
	private ExpireSet<String> stateCache = new ExpireSet<String>(60);

	private static Logger log = LoggerFactory.getLogger(WeixinOAuthRequestFilter.class);

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response) {

		String uri = request.getRequestURI();
		log.debug("a request access url [{}].", uri);

		// 判断请求是否通过微信内置浏览器，如果没通过微信内置浏览器，则不做任何后续处理
		if (!isRequestFromWeixin(request))
			return true;

		// // 判断当前用户是否已经在本次会话中做过验证
		if (isAuthed(request)) {
			return true;
		}

		// 判断是否以GET方式请求，如果不是GET方式而且用户也没做过验证，则返回404
		if (!request.getMethod().equals("GET"))
			return false;

		// 判断请求是否来自微信平台跳转，如果来自则获取用户相关信息
		if (isReferenceFromWeixinOpenAPI(request, response)) {
			String code = request.getParameter("code");
			if (code == null) {
				// 当用户禁止授权时,不做任何处理
				return true;
			}
			return true;
		}

		// 当本次会话用户尚未进行授权或验证时，将用户请求转跳到微信开放平台
		if (redirectWeixinOauth2Api(request, response)) {
			// 通知容器不进行后续处理
			return false;
		}
		// 如果转跳失败，则不影响用户体验，继续后续操作。
		return true;
	}

	private boolean redirectWeixinOauth2Api(HttpServletRequest request, HttpServletResponse response) {
	    // 生成转跳url
        StringBuffer url = request.getRequestURL();
        final String state = UUID.randomUUID().toString();
        this.stateCache.add(state);
        String redirectURL = SnsAPI.connectOauth2Authorize(APP_ID, url.toString(), true, state);
        log.debug("redirect url is [{}].", redirectURL);

        // 转跳到微信开放平台的认证API
        try {
            response.sendRedirect(redirectURL);
        } catch (Exception e) {
            log.error("", e);
            return false;
        }
        return true;

	}

	private boolean isAuthed(HttpServletRequest request) {
		if (request.getSession(true).getAttribute(OPEN_ID_IN_SESSION) != null)
			return true;
		return false;
	}

	/**
	 * 判断请求是否来自微信浏览器
	 */
	private boolean isRequestFromWeixin(HttpServletRequest request) {

		String userAgent = request.getHeader(USER_AGENT);
		if (userAgent.toLowerCase().indexOf(WEIXIN_USER_AGENT_FLAG) > 0)
			return true;

		return false;
	}

	/**
	 * 判断请求是否来自微信公众平台的转跳
	 */
	private boolean isReferenceFromWeixinOpenAPI(HttpServletRequest request, HttpServletResponse response) {
		final String state = request.getParameter(WEIXIN_API_REFERER_FLAG);

		if (state != null && this.stateCache.contains(state))
			return true;

		return false;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		if (preHandle(request, response)) {
			filterChain.doFilter(request, response);
			return;
		}
		return;
	}

}
