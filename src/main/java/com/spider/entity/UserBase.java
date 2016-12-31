package com.spider.entity;
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

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * [Zhihu]https://www.zhihu.com/people/Sweets07
 * [Github]https://github.com/MatrixSeven
 * Created by seven on 2016/12/4.
 */
@Getter
@Setter
@ToString(of = {"token"})
@RequiredArgsConstructor()
public class UserBase{
    private String token;
    private String url;
    private String isInit;
    private String id;
    private String from_id;
    private String from_token;
    private String from_name;
    private String name;

    public UserBase(String token){
        this.token=token;
    }

    @Override
    public boolean equals(Object o){
        if(o==null)
            return false;
        return this.token.equals(((UserBase)o).getToken());
    }

    @Override
    public int hashCode() {
        return token.hashCode();
    }
}
