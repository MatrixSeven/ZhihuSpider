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


import com.spider.dao.SaveDaoInterface;
import com.spider.dao.SaveDaoImp;
import com.spider.entity.FollowNexus;
import com.spider.entity.UserBase;
import com.spider.entity.UserInfo;
import com.spider.https.ZhiHuHttp;
import com.spider.tool.Config;
import com.spider.tool.Console;
import com.spider.tool.LRUCache;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicLong;

/**
 * [Zhihu]https://www.zhihu.com/people/Sweets07
 * [Github]https://github.com/MatrixSeven
 * Created by seven on 2016/12/2.
 */
public class MainMangerControl {

    //  private Logger log= Logger.getLogger("error_file_dao");

    private long time = System.currentTimeMillis();
    //新增userBase
    private volatile List<UserBase> userBases;
    //获取关注完成的userBase
    private volatile List<UserBase> doneBaseUpdate;
    //parser完成的users数据
    private volatile List<UserInfo> userInfo;
    //需要parser的users
    private volatile List<UserBase> token;
    //缓存userbase
    private volatile LRUCache<UserBase> tempUserBases;
    //跟随者FollowNexus
    private volatile List<FollowNexus> followNexuses;
    private volatile AtomicLong atomicLong = new AtomicLong(0L);
    private volatile boolean isLoadTask_ = false;
    private volatile boolean isLoadTask__ = false;
    private volatile long time_ = 0L;
    private SaveDaoInterface daoInterface;
    private int max = 500;
    private boolean isOnlyParser = Config.INSTANCES().getIsOnlyParser();
    private int max_active = Integer.valueOf(Config.INSTANCES().getThread_max_active());
    private static final ThreadPoolExecutor servicePool = (ThreadPoolExecutor) Executors.
            newFixedThreadPool(Integer.valueOf(Config.INSTANCES().getThread_max_active()));
    private static final ThreadPoolExecutor servicePoolInfo = (ThreadPoolExecutor) Executors.
            newFixedThreadPool(Integer.valueOf(Config.INSTANCES().getThread_max_active()));

    public MainMangerControl() throws Exception {
        ZhiHuHttp.login();
        userBases = new ArrayList();
        doneBaseUpdate = new ArrayList();
        userInfo = new ArrayList();
        followNexuses = new ArrayList();
        daoInterface = SaveDaoImp.getInstance();
        token = new ArrayList<>(512);
        tempUserBases = new LRUCache<>(350000);
    }

    public void star() {
        if (!isOnlyParser) {
            try {
                tempUserBases.init(daoInterface.iniTemp(350000));
                for (UserBase u : daoInterface.Init(new UserBase(Config.INSTANCES().getStar_token())))
                    this.servicePool.execute(new ParserFollower(u, this));

                printStatus();

            } catch (Exception e) {
                e.printStackTrace();

            }
        }
    }

    public boolean isExist(UserBase userBase) throws Exception {
        if (tempUserBases.get(userBase) || daoInterface.isExist(userBase)) {
            return true;
        }
        return false;
    }


    private void addTask() {
        try {
            if (servicePool.getQueue().size() == 0 && servicePool.getActiveCount() < max_active && !isOnlyParser) {
                System.out.println("增加任务GetFollower");
                if (!isLoadTask_) {
                    synchronized (doneBaseUpdate) {
                        if (servicePool.getQueue().size() == 0) {
                            isLoadTask_ = true;
                            if (doneBaseUpdate.size() != 0 || userBases.size() != 0) {
                                updateUserBase();
                            }
                            for (UserBase u : this.daoInterface.getNewForUserBase()) {
                                this.servicePool.execute(new ParserFollower(u, this));
                            }
                        }
                        isLoadTask_ = false;
                    }
                }
            }
            if (servicePoolInfo.getQueue().size() == 0 && servicePoolInfo.getActiveCount() <= 4) {
                if (!isLoadTask__) {
                    synchronized (token) {
                        if (servicePoolInfo.getQueue().size() != 0) {
                            return;
                        }
                        isLoadTask__ = true;
                        System.out.println("增加任务ParserInfo");
                        if (token.size() != 0) {
                            time_ = daoInterface.UpdateParserInfo(token);
                        }
                        token = daoInterface.getParserInfoUserBase();
                        for (UserBase u : token) {
                            this.servicePoolInfo.execute(new ParserUserInfo(u, this));
                        }
                        isLoadTask__ = false;
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }

    }

    public void remove(UserBase o) throws Exception {
        doneBaseUpdate.add(o);
        if (this.doneBaseUpdate.size() > max) {
            daoInterface.UpdateBase(doneBaseUpdate);
            doneBaseUpdate.clear();
        }
    }

    private void updateUserBase() throws Exception {
        synchronized (doneBaseUpdate) {
            daoInterface.UpdateBase(doneBaseUpdate);
            doneBaseUpdate.clear();
        }
        synchronized (userBases) {
            daoInterface.SaveForUserBase(userBases);
            this.userBases.clear();
        }
    }

    private void addUserBase(List<UserBase> o) throws Exception {
        if (this.userBases.size() > max ||
                (servicePool.getQueue().size() == 0 &&
                        servicePool.getActiveCount() <
                                max_active && doneBaseUpdate.size() > 0)) {
            updateUserBase();
        }
        synchronized (userBases) {
            for (UserBase userBase : o) {
                if (!isExist(userBase)) {
                    this.userBases.add(userBase);
                    continue;
                }
                atomicLong.incrementAndGet();
            }
        }
    }

    private void addUserInfo(UserInfo o) throws Exception {
        if (this.userInfo.size() > max) {
            synchronized (o) {
                daoInterface.SaveForUser(userInfo);
                this.userInfo.clear();
            }
        }
        this.userInfo.add(o);

    }

    private void addUserFollower(List<FollowNexus> o) throws Exception {
        this.followNexuses.addAll(o);
        if (this.followNexuses.size() > max) {
            synchronized (followNexuses) {
                daoInterface.SaveForFollow(followNexuses);
                this.followNexuses.clear();
            }
        }
    }

    public void addType(Integer type, Object obj) throws Exception {
        switch (type) {
            case 1:
                this.addUserBase((List<UserBase>) obj);
                break;
            case 2:
                this.addUserInfo((UserInfo) obj);
                break;
            case 3:
                this.addUserFollower((List<FollowNexus>) obj);
                break;
            default:
                break;
        }
    }


    public void printStatus() {

        new Thread(() -> {
            try {
                while (true) {
                    Console.clear();
                    System.out.println(
                            "最小线程数：" + servicePool.getCorePoolSize() + "\n" +
                                    "最大线程数：" + servicePool.getMaximumPoolSize() + "\n" +
                                    "任务队列大小：" + servicePool.getQueue().size() + "\n" +
                                    "总任务数：" + servicePool.getTaskCount() + "\n" +
                                    "活跃线程数：" + servicePool.getActiveCount() + "\n" +
                                    "完成线程数：" + servicePool.getCompletedTaskCount() + "\n" +
                                    "遇到重复数据: " + atomicLong.intValue() + "\n" +
                                    "缓存大小:" + tempUserBases.size() + "\n" +
                                    this.tempUserBases.toString() + "\n" +
                                    "========================================================\n" +
                                    "最小线程数：" + servicePoolInfo.getCorePoolSize() + "\n" +
                                    "最大线程数：" + servicePoolInfo.getMaximumPoolSize() + "\n" +
                                    "任务队列大小：" + servicePoolInfo.getQueue().size() + "\n" +
                                    "总任务数：" + servicePoolInfo.getTaskCount() + "\n" +
                                    "活跃线程数：" + servicePoolInfo.getActiveCount() + "\n" +
                                    "完成线程数：" + servicePoolInfo.getCompletedTaskCount() + "\n" +
                                    "上次更新花费时间:" + time_ + "s" + "\n"
                    );
                    System.out.println("运行" + (System.currentTimeMillis() - time) / 3600000.00 + "h");
                    Thread.sleep(3000);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
        new Thread(() -> {
            while (true) {
                addTask();
                try {
                    Thread.sleep(50000);
                } catch (Exception e) {

                }
            }
        }).start();
    }
}