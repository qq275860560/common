package com.github.qq275860560.common.filter;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.github.qq275860560.common.model.ApiResult;
import com.github.qq275860560.common.util.JsonUtil;
import com.github.qq275860560.common.util.JwtUtil;
import com.github.qq275860560.common.util.ResponseUtil;

/**
 * @author jiangyuanlin@163.com
 * 把token转换成userId,配置此过滤器后，controller方法可以通过String userId=(String)request.getAttribute("userId");获取用户id，然后根据此用户id去数据库，缓存，或其他服务获取用户详情
 * 如果某个url不需要经过此过滤器，请配置loginFilterExcludeUrls参数,以逗号区分,例如
 * loginFilterExcludeUrls=/api/neva3/user/login,/api/spms/neva3/user/getCheckCode
  * 如果某些token不需要解密,比如开发或测试联调时的固定token，请配置loginFilter.exclude.urls参数,以逗号区分,其中冒号前半部分为token后半部分为userId,例如
 * loginFilterTokenUserIdPairs=admin_token:cabde236a3f84b2898ef99521c7e852e,neva3_token:9a9f1dd687c246d99583fed4e4755c68
 */
public class LoginFilter implements Filter {
	private static final Log log = LogFactory.getLog(LoginFilter.class);

	private Pattern excludeUrlsPattern = null; // 匹配不需要过滤路径的正则表达式
	private String[] tokens = null; // 匹配特殊的token，这些token不需要经过jwt解密，直接转换成对应的userid
	private String[] userIds = null;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		try {
			Configuration configuration = new Configurations().properties(new File("/", "application.properties"));
			String loginFilterExcludeUrls = configuration.getString("loginFilterExcludeUrls");
			log.info("loginFilterExcludeUrls=" + loginFilterExcludeUrls);
			if (StringUtils.isNotBlank(loginFilterExcludeUrls)) {
				String[] excludeUrls = loginFilterExcludeUrls.split(","); // 以逗号进行分割
				int excludeUrlsLength = excludeUrls.length;
				for (int i = 0; i < excludeUrlsLength; i++) {
					String excludeUrl = excludeUrls[i].trim();
					// 对点、反斜杠和星号进行转义
					excludeUrl = excludeUrl.replace("\\", "\\\\").replace(".", "\\.").replace("*", ".*");
					excludeUrl = "^" + excludeUrl + "$";
					excludeUrls[i] = excludeUrl;
				}
				excludeUrlsPattern = Pattern.compile(StringUtils.join(excludeUrls, "|"));
			}

			String loginFilterTokenUserIdPairs = configuration.getString("loginFilterTokenUserIdPairs");
			log.info("loginFilterTokenUserIdPairs=" + loginFilterTokenUserIdPairs);
			if (StringUtils.isNotBlank(loginFilterTokenUserIdPairs)) {
				String[] pairs = loginFilterTokenUserIdPairs.split(",");// 以逗号进行分割
				int pairsLength = pairs.length;
				tokens = new String[pairsLength];
				userIds = new String[pairsLength];
				for (int i = 0; i < pairsLength; i++) {
					String pair = pairs[i].trim();
					tokens[i] = pair.split(":")[0].trim();
					userIds[i] = pair.split(":")[1].trim();
				}
			}
		} catch (Exception e) {
			log.error("", e);
		}

	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;

		// 获得用户请求的URI
		String path = request.getRequestURI();

		// 登陆接口和获取验证码接口无需过滤，清配置loginFilter.exclude.url-pattern
		if (excludeUrlsPattern != null && excludeUrlsPattern.matcher(path).matches()) {
			chain.doFilter(request, response);
			return;
		}

		// 获取request头的token
		String token = request.getHeader("token");
		String userId = null;
		for (int i = 0; i < tokens.length; i++) {
			if (tokens[i].equals(token)) {
				userId = userIds[i];
				break;
			}
		}
		if (StringUtils.isBlank(userId)) {// 在没有特殊token情况时，需要根据jwt解密
			try {
				if (token.startsWith("\"") || token.startsWith("‘")) {
					log.error("前端传输token前后不要添加双引号或单引号");
					token = token.substring(1, token.length() - 1);// 兼容前端误传参数问题
				}

				Configuration configuration = new Configurations().properties(new File("/", "application.properties"));
				String issUser = configuration.getString("issUser");
				String audience = configuration.getString("audience");
				Map<String, Object> payLoadMap0 = JwtUtil.decrypt(token, issUser, audience);
				userId = payLoadMap0.get("user_id").toString();

			} catch (Exception e) {
				log.error("", e);
				String result = JsonUtil
						.toJSONString(new ApiResult(com.github.qq275860560.common.model.ApiResult.AUTHENTICATION_FAIL,
								ApiResult.AUTHENTICATION_FAIL_MSG, ExceptionUtils.getStackTrace(e)));
				ResponseUtil.sendResult(response, result);

			}
		}
		log.info("请求用户，user_id=" + userId);
		// 校验user_id的有效性,是否在用户有效期内,是否白名单等等，略，放在另外一些过滤器

		request.setAttribute("userId", userId);
		request.setAttribute("token", token);

		// controller方法可以通过String
		// userId=(String)request.getAttribute("userId");获取用户id，然后根据此用户id去数据库，缓存，或其他服务获取用户详情
		chain.doFilter(request, response);

	}

	@Override
	public void destroy() {
		return;
	}
}