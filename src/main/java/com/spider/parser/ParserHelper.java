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

import org.jsoup.nodes.Document;

/**
 * [Zhihu]https://www.zhihu.com/people/Sweets07
 * [Github]https://github.com/MatrixSeven
 * Created by seven on 2016/12/4.
 */
public class ParserHelper {
    private static final String CSS_ID = "#";
    private static final String CSS_CLASS = ".";
    private static final String CSS_ATTR= "[/s]";

    /**
     *
     * @param doc
     * @param cssQuery
     * @return
     */
    public static String ParserByClass(Document doc, String cssQuery) {
        if (doc.select(CSS_CLASS.concat(cssQuery))!=null){
            return doc.select(CSS_CLASS.concat(cssQuery)).text().trim();
        }
        return "";
    }

    //ParserCallBack

    public static String ParserByClass(Document doc, String cssQuery, ParserCallBack callback) {
        if (doc.select(CSS_CLASS.concat(cssQuery))!=null&&doc.select(CSS_CLASS.concat(cssQuery)).size()>0){
                return callback.selectEle(doc.select(CSS_CLASS.concat(cssQuery)));
        }
        return "";
    }
    /***
     *
     * @param doc
     * @param cssQuery
     * @return
     */
    public static String ParserByAttr(Document doc, String cssQuery) {
        if (doc.select(String.format(CSS_ATTR,cssQuery))!=null){
            return doc.select(String.format(CSS_ATTR,cssQuery)).text().trim();
        }
        return "";
    }

    public static String Select(Document doc, String cssQuery) {
        if (doc.select(cssQuery)!=null){
            return doc.select(cssQuery).text().trim();
        }
        return "";
    }
    public static String Select(Document doc, String cssQuery,ParserCallBack p) {
        if (doc.select(cssQuery)!=null){
            return p.selectEle(doc.select(cssQuery));
        }
        return "";
    }


}
