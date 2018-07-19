package com.jtd.web.adx.bes;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.jtd.web.constants.BaiduEsConstant;


public class BaseHttp {
	
	private static final Logger logger = LoggerFactory.getLogger(BaseHttp.class);
	public static <T> JSONObject post(String url, T request) {
		HttpPost httpost = new HttpPost(url);

		httpost.setHeader("Content-Type", BaiduEsConstant.CONTENT_TYPE);
		httpost.setHeader("Accept", BaiduEsConstant.ACCEPT);
		HttpResponse response=null;

		logger.info("REQUEST URL:\n" + url);
		try {
			String requestBody = JsonUtils.toJson(request);
			logger.info("REQUEST:\n" + requestBody);
			StringEntity input = new StringEntity(requestBody, "UTF-8");
			input.setContentType(BaiduEsConstant.CONTENT_TYPE);
			httpost.setEntity(input);

			DefaultHttpClient httpclient = getHttpClient(true);
			response = httpclient.execute(httpost);

			HttpEntity entity = response.getEntity();
			String body = EntityUtils.toString(entity);
			return JSONObject.parseObject(body);
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage(), e);
		} catch (ParseException e) {
			logger.error(e.getMessage(), e);
		} catch (ClientProtocolException e) {
			logger.error(e.getMessage(), e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			try {
				httpost.releaseConnection();
			
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
		return null;
	}

	@SuppressWarnings({ "deprecation" })
	private static DefaultHttpClient getHttpClient(boolean isSSL) throws Exception {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 20000);
		if (isSSL) {
			SSLContext ctx = SSLContext.getInstance("TLS");
			X509TrustManager tm = new X509TrustManager() {
				public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {
				}

				public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {
				}

				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
			};
			ctx.init(null, new TrustManager[] { tm }, null);
			SSLSocketFactory ssf = new SSLSocketFactory(ctx);
			ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			ClientConnectionManager ccm = httpclient.getConnectionManager();
			SchemeRegistry sr = ccm.getSchemeRegistry();
			sr.register(new Scheme("https", ssf, 443));
			httpclient = new DefaultHttpClient(ccm, httpclient.getParams());
			return httpclient;
		} else {
			return httpclient;
		}
	}

	public static AuthRequest getAuthHeader() {
		AuthRequest auth = new AuthRequest();
		auth.setDspId(BaiduEsConstant.DSPID);
		auth.setToken(BaiduEsConstant.TOKEN);
		return auth;
	}

}
