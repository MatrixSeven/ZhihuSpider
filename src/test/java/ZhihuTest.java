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
//		    //��������\\
//		    `-._______.-'
//		    ___`. | .'___
//		   (______|______)
//=======================================================

import com.spider.dao.datasource.unpooled.UnpooledDataSource;
import com.spider.dao.imp;
import com.spider.entity.UserBase;
import com.spider.entity.UserInfo;
import com.spider.tool.Config;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import seven.ExcelFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * [Zhihu]https://www.zhihu.com/people/Sweets07
 * [Github]https://github.com/MatrixSeven
 * Created by seven on 2016/11/29.
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class ZhihuTest {
    final private static String FOLLOWER = "https://www.zhihu.com/api/v4/members/%1s/followers?per_page" +
            "=10&include=data%5B%2A%5D.answer_count%2Carticles_count%2Cfollower_" +
            "count%2Cis_followed%2Cbadge%5B%3F%28type%3Dbest_answerer%29%5D.topics&limit=10" +
            "&offset=30";

    @Test
    public void Test_01() throws Exception {
        imp imp = new imp();
        List<UserInfo> s = new ArrayList<>();
        UserInfo u = new UserInfo();
        u.setSex("1");
        u.setThanked("1");
        u.setFollowing("1");
        u.setBusiness("1");
        u.setName("1");
        u.setFollowing("1");
        s.add(u);
        // imp.SaveForUser(s);
        List<UserBase> userBases = new ArrayList<>();
        UserBase userBase = new UserBase("66666");
        userBase.setFrom_id("64554");
        userBase.setFrom_token("from_token");
        userBases.add(userBase);
        imp.SaveForUserBase(userBases);
    }

    @Test
    public void Test_02() throws Exception {
        Config config = Config.INSTANCES();
        UnpooledDataSource UNPOOLED_DATA_SOURCE = new UnpooledDataSource(
                "com.mysql.jdbc.Driver", (config.getDb_url().concat(config.getDb_name()))
                , config.getDb_user_name(), config.getDb_user_pass());

        Connection c = UNPOOLED_DATA_SOURCE.getConnection();
        PreparedStatement ps = c.prepareStatement("select * FROM USERs_info limit 1000");
        ResultSet res = ps.executeQuery();
        HashMap<String,String> stringStringHashMap;
        List<HashMap<String,String>> hashMaps=new ArrayList<>();
        ResultSetMetaData me= res.getMetaData();
        int size= me.getColumnCount()+1;
        while (res.next()){
            stringStringHashMap=new HashMap<>();
            for (int i = 1; i <size ; i++) {
                stringStringHashMap.put(me.getColumnName(i),
                        res.getString(me.getColumnName(i)));
            }
            hashMaps.add(stringStringHashMap);

        }
//        System.out.println(hashMaps);
        ExcelFactory.saveExcel(hashMaps,"/seven007.xlsx").Save();
    }
}
