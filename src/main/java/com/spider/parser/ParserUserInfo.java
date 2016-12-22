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
//		    //¹·Éñ±£ÓÓ\\
//		    `-._______.-'
//		    ___`. | .'___
//		   (______|______)
//=======================================================

import com.spider.entity.UserBase;
import com.spider.entity.UserInfo;
import com.spider.https.ZhiHuHttp;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.Random;

import static com.spider.https.HttpUtil.getHttpClient;

/**
 * [Zhihu]https://www.zhihu.com/people/Sweets07
 * [Github]https://github.com/MatrixSeven
 * Created by seven on 2016/12/8.
 */
public class ParserUserInfo extends ParserBase{

    public ParserUserInfo(UserBase userBase, MainMangerControl mc) {
        super(userBase, mc);
    }

    protected UserInfo getUserInfo(Document doc){
        UserInfo u=new UserInfo();

        u.setName(ParserHelper.Select(doc, ".name"));
        u.setAddress(ParserHelper.Select(doc, "span.location.item"));
        u.setBusiness(ParserHelper.Select(doc, "span.business.item"));
        u.setCompany(ParserHelper.Select(doc, ".employment.item"));
        u.setJob(ParserHelper.Select(doc, ".position.item"));
        u.setEducation(ParserHelper.Select(doc, ".education.item"));
        u.setHeadline(ParserHelper.Select(doc, "div.bio"));
        u.setId(userBase.getId());
        u.setContent(ParserHelper.Select(doc, ".fold-item .content"));
        u.setQuestion(ParserHelper.Select(doc, ".item[href$=asks] .num"));
        u.setAnswer(ParserHelper.Select(doc, ".item[href$=answers] .num"));
        u.setArticle(ParserHelper.Select(doc, ".item[href$=posts] .num"));
        u.setFavorite(ParserHelper.Select(doc, ".item[href$=collections] .num"));
        u.setEdit(ParserHelper.Select(doc, ".item[href$=logs] .num"));
        u.setFollowing(ParserHelper.Select(doc, ".item[href$=followees] strong"));
        u.setFollowers(ParserHelper.Select(doc, ".item[href$=followers] strong"));
        u.setAgree(ParserHelper.Select(doc, ".zm-profile-header-user-agree strong"));
        u.setThanked(ParserHelper.Select(doc, ".zm-profile-header-user-thanks strong"));
        u.setColumns(ParserHelper.Select(doc, "a[href$=followed] strong").replace(" \u4e2a\u4e13\u680f",""));
        u.setTopic(ParserHelper.Select(doc, "a[href$=topics] strong").replace(" \u4e2a\u8bdd\u9898",""));
        u.setSex(ParserHelper.Select(doc, ".item.gender i", e -> e.attr("class").equals("")?"\u5973":"\u7537"));
        u.setWeibo(ParserHelper.Select(doc, ".zm-profile-header-user-weibo",e->e.attr("href")));
        return  u;

    }

    @Override
    void parser() throws Exception {
        Thread.sleep(new Random().nextInt(1500));
        final HttpClientContext context = ZhiHuHttp.httpClientContext;
        HttpClient httpClient = getHttpClient();
        Document doc = null;
        HttpResponse res = null;
        res = httpClient.execute(getFollowerUrl(userBase.getToken()), context);
        doc = Jsoup.parse(EntityUtils.toString(res.getEntity()));
        mc.addType(2, getUserInfo(doc));
    }

    @Override
    public void run() {
        try {
            parser();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}