package club.hoy.weixin.api;

import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;

import club.hoy.weixin.client.LocalHttpClient;
import club.hoy.weixin.model.Token;

public class TokenAPI extends BaseAPI{

	/**
	 * 获取access_token
	 * @param appid
	 * @param secret
	 * @return
	 */
	public static Token token(String appid,String secret){
	    HttpUriRequest httpUriRequest = RequestBuilder.get()
                .setUri(BASE_URI + "/cgi-bin/token?grant_type=client_credential&appid="+appid+"&secret="+secret+"")
                .build();
		return LocalHttpClient.executeJsonResult(httpUriRequest,Token.class);
	}

}
