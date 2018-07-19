package com.jtd.tencent.service;

/**
 * OAuth授权授权服务
 * @author zl
 *
 */
public interface AuthService 
{
	/**
	 * 通过 Authorization Code 获取 Access Token 或刷新 Access Token
	 * @param grantType 1:授权码方式获取 token,2:refresh_token （刷新 token ）
	 * @param authorizationCode OAuth 认证 code，可通过获取 Authorization Code 接口获取，当 grantType=1 时必填
	 * @return
	 */
	public void getToken(Integer grantType,String authorizationCode);
	
}
