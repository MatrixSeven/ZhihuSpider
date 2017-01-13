package com.spider.dao;
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

import org.apache.log4j.Logger;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * [Zhihu]https://www.zhihu.com/people/Sweets07
 * [Github]https://github.com/MatrixSeven
 * Created by seven on 2017/1/11.
 */
public class Hook implements InvocationHandler {
    Object object;
    Logger logger=Logger.getLogger("error_");
    public Hook(Object object) {
        this.object = object;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object o=null;
        try {
            o=  method.invoke(object,args);
        }catch (Exception e){
            logger.error("数据库出错:方法"+method.getName()+"  参数："+
                    Arrays.toString(args)+"\n异常信息---->"+e.getCause());
        };
        return o;
    }

}
