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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.spider.entity.FollowNexus;
import com.spider.entity.UserBase;
import com.spider.https.ZhiHuHttp;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * [Zhihu]https://www.zhihu.com/people/Sweets07
 * [Github]https://github.com/MatrixSeven
 * Created by seven on 2016/12/2.
 */
public class ParserFollower extends ParserBase {

    public ParserFollower(UserBase names, MainMangerControl mc) {
        super(names,mc);
    }

    protected void parser() throws Exception {
        final HttpClientContext context = ZhiHuHttp.httpClientContext;
        final String auth = ZhiHuHttp.auth;
        HttpClient httpClient = getHttpClient();
        List<FollowNexus> followNexuses = new ArrayList();
        List<UserBase> userBases = new ArrayList();
        UserBase ub = null;
        JSONObject jsonObject = JSON.parseObject(EntityUtils.toString(httpClient.execute(httpGetListV2(auth, userBase.getToken()), context).getEntity()));
        String max = jsonObject.getJSONObject("paging").getString("is_end").trim();
        String next_url = jsonObject.getJSONObject("paging").getString("next");
        for (int i = 0; i < jsonObject.getJSONArray("data").size(); i++) {
            ub = new UserBase();
            ub.setToken(jsonObject.getJSONArray("data").getJSONObject(i).get("url_token").toString());
            ub.setName(jsonObject.getJSONArray("data").getJSONObject(i).get("name").toString());
            ub.setUrl("https://www.zhihu.com/people/".concat(ub.getToken()));
            ub.setFrom_id(userBase.getId());
            ub.setFrom_token(userBase.getToken());
            ub.setFrom_name(userBase.getName());
            userBases.add(ub);
            followNexuses.add(new FollowNexus(userBase.getToken(), ub.getToken(),ub.getName(),userBase.getName()));

        }

        int wi=1;
        while (max.equals("false")) {
            jsonObject = JSON.parseObject(EntityUtils.toString(httpClient.execute(httpGetListV2_(next_url), context).getEntity()));
            next_url = jsonObject.getJSONObject("paging").getString("next");
            max = jsonObject.getJSONObject("paging").getString("is_end");
            for (int i = 0; i < jsonObject.getJSONArray("data").size(); i++) {
                ub = new UserBase();
                ub.setToken(jsonObject.getJSONArray("data").getJSONObject(i).get("url_token").toString());
                ub.setUrl("https://www.zhihu.com/people/".concat(ub.getToken()));
                ub.setFrom_id(userBase.getId());
                ub.setFrom_token(userBase.getToken());
                followNexuses.add(new FollowNexus(userBase.getToken(), ub.getToken(),ub.getName(),userBase.getName()));

                userBases.add(ub);
                if (userBases.size() >=5000) {
                    mc.addType(1, userBases);
                    userBases.clear();
                }
                if (followNexuses.size() >=5000) {
                    mc.addType(3, followNexuses);
                    followNexuses.clear();
                }
            }
        }
        mc.addType(3, followNexuses);
        mc.addType(1, userBases);
        mc.remove(this.userBase);
    }

    protected CloseableHttpClient getHttpClient() {
        CloseableHttpClient httpClient = null;
        RequestConfig globalConfig = RequestConfig.custom()
                .setCookieSpec(CookieSpecs.BROWSER_COMPATIBILITY)
                .setConnectTimeout(50000).setSocketTimeout(50000).setConnectionRequestTimeout(50000).build();
        httpClient = HttpClients.custom()
                .setDefaultRequestConfig(globalConfig)
                .build();
        return httpClient;
    }

    @Override
    public void run() {
        try {
            this.parser();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
