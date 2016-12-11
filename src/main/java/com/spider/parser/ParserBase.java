package com.spider.parser;
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

import com.spider.entity.UserBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;

import java.net.URI;

/**
 * [Zhihu]https://www.zhihu.com/people/Sweets07
 * [Github]https://github.com/MatrixSeven
 * Created by seven on 2016/12/2.
 */
public abstract class ParserBase implements Runnable {
    final private static String FOLLOWER = "https://www.zhihu.com/api/v4/members/%s/";
    final private static String PRIXF = "/followers?per_page=10&include=data%5B%2A%5D.answer_count%2Carticles_count%2Cfollower_count%2Cis_followed%2Cbadge%5B%3F%28type%3Dbest_answerer%29%5D.topics&limit=20&offset=20";
    protected HttpGet httpGet;
    protected HttpGet httpGet2;
    protected MainMangerControl mc;
    protected UserBase userBase;


    public ParserBase(UserBase userBase, MainMangerControl mc) {
        httpGet = new HttpGet();
        httpGet2 = new HttpGet();
        this.userBase = userBase;
        this.mc=mc;
    }
    abstract void parser() throws Exception;
    protected String getUrl(String name) {
        return String.format("https://www.zhihu.com/people/%s/followers", name);
    }
    protected HttpGet getFollowerUrl(String name) {
        httpGet2.setURI(URI.create(getUrl(name)));
        httpGet2.setHeader("User-Agent", "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.76 Mobile Safari/537.36");
        httpGet2.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        httpGet2.setHeader("Accept-Encoding", "gzip, deflate, sdch");
        httpGet2.setHeader("Accept-Language", "zh-CN,zh;q=0.8");
        httpGet2.setHeader("Host", "www.zhihu.com");
        httpGet2.setHeader("Upgrade-Insecure-Requests", "1");
        return httpGet2;
    }

    protected HttpGet httpGetListV2_(String url) {
        httpGet.setURI(URI.create(url));
        return httpGet;
    }
    protected HttpGet httpGetListV2(String auth, String name) {
        httpGet.setURI(URI.create(String.format(FOLLOWER, name).concat(PRIXF)));
        httpGet.setHeader("authorization", "Bearer " + auth);
        httpGet.setHeader("Referer", String.format("https://www.zhihu.com/people/%s/followers", name));
        httpGet.setHeader("Host", "www.zhihu.com");
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.102 Safari/537.36");
        return httpGet;
    }
    @Deprecated
    protected HttpPost httpPost(String url, String auth) {
        HttpPost HttpPost = new HttpPost();
        HttpPost.setURI(URI.create(FOLLOWER));
        HttpPost.setHeader("authorization", "Bearer " + auth);
        HttpPost.setHeader("Host", "www.zhihu.com");
        HttpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.102 Safari/537.36");
        return HttpPost;
    }

}
