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

import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * [Zhihu]https://www.zhihu.com/people/Sweets07
 * [Github]https://github.com/MatrixSeven
 * Created by seven on 2016/12/4.
 */
@Data
@RequiredArgsConstructor()
public class UserBase{
    private String token;
    private String url;
    private String isInit;
    private String id;
    private String from_id;
    private String from_token;

    public UserBase(String token){
        this.token=token;
    }

    @Override
    public boolean equals(Object o){
        return this.token.equals(((UserBase)o).getToken());
    }

}
