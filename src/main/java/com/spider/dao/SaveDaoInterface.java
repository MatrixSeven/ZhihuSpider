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

import com.spider.entity.FollowNexus;
import com.spider.entity.UserBase;
import com.spider.entity.UserInfo;

import java.util.List;

/**
 * [Zhihu]https://www.zhihu.com/people/Sweets07
 * [Github]https://github.com/MatrixSeven
 * Created by seven on 2016/12/2.
 */
public interface SaveDaoInterface {




    /**
     * 存储UserInfo
     * @param info
     * @throws Exception
     */
    void SaveForUser(List<UserInfo> info)throws Exception;
    /**
     * 存储followNexuses
     * @param followNexuses
     * @throws Exception
     */
    void SaveForFollow(List<FollowNexus> followNexuses) throws Exception;

    /**
     *
     * @throws Exception
     */
    List<UserBase> iniTemp(int size) throws Exception;
    /**
     * 储存userBases
     * @param userBases
     * @throws Exception
     */
    void SaveForUserBase(List<UserBase> userBases)throws Exception;
    /**
     * 取出最新的UserBase
     * @return
     * @throws Exception
     */
    List<UserBase> getNewForUserBase()throws Exception;

    List<UserBase>  getParserInfoUserBase() throws Exception;
    /**
     * 取出最新的UserBase
     * @return
     * @throws Exception
     */
    long  UpdateParserInfo(List<UserBase> info)throws Exception;

    /**
     * 初始化userBase
     * @param userBase
     * @return
     * @throws Exception
     */
    List<UserBase> Init(UserBase userBase)throws Exception;

    /**
     * 更新基本表
     * @param userBase
     * @throws Exception
     */
    void UpdateBase(List<UserBase>userBase)throws Exception;

    /**
     * 初始化数据库
     * @throws Exception
     */
    void InitDb()throws Exception;

    boolean isExist(UserBase userBase) throws Exception;
}
