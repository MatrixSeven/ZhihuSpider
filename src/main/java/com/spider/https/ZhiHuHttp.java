package com.spider.https;
//=======================================================
//		          .----.
//		       _.'__    `.
//		   .--(^)(^^)---/!\
//		 .' @          /!!!\
//		 :         ,    !!!!
//		  `-..__.-' _.-\!!!/
//		        `;_:    `"'
//		      .'"""""`.
//		     /,  ya ,\\
//		    //狗神保佑\\
//		    `-._______.-'
//		    ___`. | .'___
//		   (______|______)
//=======================================================

/**
 * [Zhihu]https://www.zhihu.com/people/Sweets07
 * [Github]https://github.com/MatrixSeven
 * Created by seven on 2016/11/29.
 */

import com.spider.tool.Config;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

//=======================================================
//		          .----.
//		       _.'__    `.
//		   .--(^)(^^)---/!\
//		 .' @          /!!!\
//		 :         ,    !!!!
//		  `-..__.-' _.-\!!!/
//		        `;_:    `"'
//		      .'"""""`.
//		     /,  ya ,\\
//		    //狗神保佑\\
//		    `-._______.-'
//		    ___`. | .'___
//		   (______|______)
//=======================================================

/**
 * [Zhihu]https://www.zhihu.com/people/Sweets07
 * [Github]https://github.com/MatrixSeven
 * Created by seven on 2016/11/29.
 * 仅仅提供context
 */
public class ZhiHuHttp {
    final private static String INDEX_URL = "https://www.zhihu.com";
    final private static String EMAIL_LOGIN_URL = "https://www.zhihu.com/login/email";
    final private static String YZM_URL = "https://www.zhihu.com/captcha.gif?type=login";
    final private static Config CONFIG = Config.INSTANCES();
    public static String auth;
    public static HttpClientContext httpClientContext;

    static {
        try {
            login();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    private ZhiHuHttp() {

    }

    private static void  initAuthCookie() {
        for (Cookie c : httpClientContext.getCookieStore().getCookies()) {
            System.out.println(c.getName() + ":" + c.getValue());
            if ("z_c0".equals(c.getName())) {
                auth = c.getValue();
                break;
            }
        }
    }

    public static void login() throws Exception {
        httpClientContext = HttpUtil.getHttpClientContext();
        if (HttpUtil.deserializeCookie(CONFIG.getCookie_path(), httpClientContext)) {
            System.out.println("发现cookie");
            initAuthCookie();
            return;
        }

        CloseableHttpClient closeableHttpClient = HttpUtil.getHttpClient();
        CloseableHttpResponse closeableHttpResponse;
        System.out.println("登陆....");
        HttpGet get = new HttpGet(URI.create(INDEX_URL));
        Document doc = Jsoup.parse(EntityUtils.toString(closeableHttpClient.execute(get, httpClientContext).getEntity()));
        String xsf = doc.select("input[name=_xsrf]").first().attr("value");
        String username = CONFIG.getZhihu_name();
        String pass = CONFIG.getZhihu_pass();
        String imgPath = CONFIG.getYzm_path();
        HttpUtil.downloadFile(closeableHttpClient, httpClientContext, YZM_URL, imgPath, "YZM.jpg", true);
        List<NameValuePair> formParams = new ArrayList<NameValuePair>();
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入：");
        String Yzm = sc.nextLine();
        formParams.add(new BasicNameValuePair("captcha", Yzm));
        formParams.add(new BasicNameValuePair("_xsrf", xsf));
        formParams.add(new BasicNameValuePair("password", pass));
        formParams.add(new BasicNameValuePair("remember_me", "true"));
        formParams.add(new BasicNameValuePair("email", username));
        HttpPost request = new HttpPost(URI.create(EMAIL_LOGIN_URL));
        request.setEntity( new UrlEncodedFormEntity(formParams, "utf-8"));
        String res = EntityUtils.toString((closeableHttpResponse = closeableHttpClient.execute(request, httpClientContext)).getEntity());
        System.out.println(res);
        HttpUtil.serializeCookie(httpClientContext.getCookieStore(),
                CONFIG.getCookie_path());
        initAuthCookie();
    }

}
