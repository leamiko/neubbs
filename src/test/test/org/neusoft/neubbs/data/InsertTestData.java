package test.org.neusoft.neubbs.data;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.neusoft.neubbs.constant.api.ParamConst;
import org.neusoft.neubbs.controller.handler.SwitchDataSourceHandler;
import org.neusoft.neubbs.dao.IUserDAO;
import org.neusoft.neubbs.entity.UserDO;
import org.neusoft.neubbs.service.IFtpService;
import org.neusoft.neubbs.service.ITopicService;
import org.neusoft.neubbs.service.IUserService;
import org.neusoft.neubbs.utils.SecretUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Map;

/**
 * 测试插入数据
 *
 * @author Suvan
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-context.xml"})
public class InsertTestData {

    @Autowired
    private IUserDAO userDAO;

    @Autowired
    private IUserService userService;

    @Autowired
    private ITopicService topicService;

    @Autowired
    private IFtpService ftpService;

    @BeforeClass
    public static void init() {
//        SwitchDataSourceHandler.setDataSourceType(SwitchDataSourceHandler.CLOUD_DATA_SOURCE_MYSQL);
        SwitchDataSourceHandler.setDataSourceType(SwitchDataSourceHandler.LOCALHOST_DATA_SOURCE_MYSQL);
    }

    /**
     * 初始化脚本
     *      - 仅仅适用于数据库表刚构建
     *      - 数据库插入测试数据
     *          - forum_user (1 ~ 6 管理员)
     *          - forum_category(1 ~ 10 话题分类)
     *          - forum_topic, forum_topic_content（100 条话题内容）
     *          - forum_topic_reply (500 条话题回复)
     *      - ftp 服务
     *          - 新建用户自定义个人文件夹（本地数据库不创建）
     *
     */
    @Ignore
    public void InitializeScript() throws Exception {
        this.addSixAdministrator();
//        this.addSixAdministratorFtpPersonalDirectory();

        this.addTenTopicCategory();
        this.addOneHundredTopics();
        this.addFiveThousandTopicReply();
    }

    /*
     * ***********************************************
     * private method
     * ***********************************************
     */

    /**
     * 添加 6 个管理员
     */
    private void addSixAdministrator() {
        String [] administratorArray = {"ahonn", "AnAndroidXiang", "kayye", "topLynch", "Nancyshan", "suvan"};
        String password = "123456";
        String [] emailArray = {"ahonn95@outlook.com", "2389351081@qq.com",
                "584157012@qq.com", "805742416@qq.com",
                "1169251031@qq.com", "liushuwei0925@gmail.com"};

        int count = 0;
        UserDO user = new UserDO();
        for(String adminName: administratorArray){
            user.setName(adminName);
            user.setPassword(SecretUtil.encryptUserPassword(password));
            user.setEmail(emailArray[count++]);

            //register user newUserId > 0
            Assert.assertTrue(userDAO.saveUser(user) > 0);

            //change user perssion, effectRow != 0
            Assert.assertEquals(1, userDAO.updateUserRankByName(adminName, "admin"));

            //activate account, effectRow != 0
            Assert.assertEquals(1, userDAO.updateUserStateToActivateByEmail(user.getEmail()));

            //send reminder mail
//            SendEmailUtil.sendEmail("Nebbbs 开发团队",emailArray[count++],
//                    "Nebbbs 管理员账号提醒", "欢迎您成为 Neubbs 项目管理员\n您的的管理员帐号：" + adminName + " 密码：123456");

            //alter user avator, effectRow != 0
            Assert.assertEquals(1, userDAO.updateUserAvatorByName(adminName, ParamConst.USER_DEFAULT_IMAGE));

            System.out.println("already add administrator: " + adminName);
        }

        System.out.println("*************************** success add 6 administrator! ****************************");
    }

    /**
     * 添加 6 个管理员 ftp 个人目录
     */
    private void addSixAdministratorFtpPersonalDirectory() {
        String [] administratorArray = {"ahonn", "AnAndroidXiang", "kayye", "topLynch", "Nancyshan", "suvan"};

        for (String admin: administratorArray) {
            ftpService.registerUserCreatePersonDirectory(userDAO.getUserByName(admin));
        }

        System.out.println("*************************** success add 6 administrator ftp persion directory!"
                + "****************************");
    }

    /**
     * 添加 10 个话题分类
     */
    private void addTenTopicCategory() {
        String[][] paramsArray = {
                {"music", "音乐"}, {"game", "游戏"}, {"movie", "电影"}, {"read", "阅读"}, {"life", "生活"},
                {"school", "校园"}, {"technology", "技术"}, {"recruit", "招聘"}, {"news", "新闻"}, {"neusoft", "东软"}
        };

        for (String[] param: paramsArray) {
            String categoryNick = param[0];
            String categoryName = param[1];

            topicService.saveCategory(categoryNick, categoryName);
            System.out.println("add category[nick=" + categoryNick +", name=" + categoryName + "]");
        }

        System.out.println("*************************** success add 10 topic category! ****************************");
    }

    /**
     * 添加 100 条话题
     *      - 随机 6 个管理员 id
     *      - 随机 10 个话题分类 nick
     */
    private void addOneHundredTopics() {
        String topicTitle = " topic title ";
        String topicContent = " topic content";

        List<Map<String, Object>> allCategoryList =  topicService.listAllTopicCategorys();

        for (int i = 1; i <= 100; i++) {
            int userId = 1 + (int) (Math.random() * 6);
            int categoryId = 1 +  (int) (Math.random() * 10);
            Map<String, Object> pageDisplayCategoryMap = allCategoryList.get(categoryId - 1);
            String categoryNick = (String )pageDisplayCategoryMap.get("id");
            String title = "No." + i + topicTitle;
            String content = categoryNick + topicContent;

            topicService.saveTopic(userId, categoryNick, title, content);

            System.out.println("add topic(userid=" + userId + ", category=" + categoryNick
                    + ", title=" + title + ", content=" + content);
        }

        System.out.println("*************************** success add 100 topic! ****************************");
    }

    /**
     * 添加 500 条话题回复
     *      - 随机 6 个管理员用户 id
     *      - 随机 100 条话题 id
     */
    private void addFiveThousandTopicReply() {
        for (int i = 1; i <= 1000; i++) {
            int userId = 1 + (int) (Math.random() * 6);
            int topicId = 1 + (int) (Math.random() * 100);
            String replyContent = "No." + i + " casual reply content";

            topicService.saveReply(userId, topicId, replyContent);

            System.out.println("add reply(userid=" + userId + ", topicid=" + topicId
                    + ", content=" + replyContent +")");
        }

        System.out.println("*************************** success add 1000 topic reply! ****************************");
    }
}
