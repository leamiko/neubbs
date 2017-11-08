package test.org.neusoft.neubbs.api;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.neusoft.neubbs.constant.api.ParamConst;
import org.neusoft.neubbs.controller.exception.AccountErrorException;
import org.neusoft.neubbs.controller.exception.DatabaseOperationFailException;
import org.neusoft.neubbs.controller.exception.ParamsErrorException;
import org.neusoft.neubbs.controller.handler.SwitchDataSourceHandler;
import org.neusoft.neubbs.entity.UserDO;
import org.neusoft.neubbs.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.NestedServletException;

import javax.servlet.http.Cookie;
import java.util.ArrayList;

/**
 * Account api 测试
 *      - 声明 Spring
 *      - 开启 web 服务
 *      - 设置配置文件(需设置 applicatonContext.xml 和 mvc.xm,否则会报错)
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextHierarchy({
        @ContextConfiguration(locations = {"classpath:spring-context.xml"}),
        @ContextConfiguration(locations = {"classpath:spring-mvc.xml"})
})
public class AccountCollectorTestCase {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    static class Param {
        String key;
        Object value;

        Param (String key, Object value) {
            this.key = key;
            this.value = value;
        }
    }

    /**
     * 打印成功通过 Test 函数消息
     */
    public void printSuccessPassTestMehtodMessage () {
        //当前正在执行函数（调用此函数的函数）
        String currentDoingMethod = Thread.currentThread().getStackTrace()[2].getMethodName();

        System.out.println("*************************** 【"+ currentDoingMethod + "()】 "
                            + "pass all the tests ****************************");
    }


    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
//      this.mockMvc = MockMvcBuilders.standaloneSetup(accountController).build();

        SwitchDataSourceHandler.setDataSourceType(SwitchDataSourceHandler.LOCALHOST_DATA_SOURCE_MYSQL);
    }

    /**
     * 【/api/account】test no login get user information
     */
    @Test
    public void testNoLoginGetUserInfo() throws Exception {
        ArrayList<Param> alist = new ArrayList<>();
            alist.add(new Param("username", "suvan"));
            alist.add(new Param("username", "liushuwei0925@gmail.com"));
            alist.add(new Param("email", "liushuwei0925@gmail.com"));

        for (Param param: alist) {
            mockMvc.perform(
                    MockMvcRequestBuilders.get("/api/account")
                            .param(param.key, (String) param.value)
            ).andExpect(MockMvcResultMatchers.status().is(200))
             .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
             .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
             .andExpect(MockMvcResultMatchers.jsonPath("$.message").doesNotExist());
        }

        printSuccessPassTestMehtodMessage();
    }

    /**
     * 【/api/account】 test same time input two param get user information
     */
    @Test
    public void testSameTimeInputTwoParamGetUserInformation() throws Exception{
//        StackTraceElement[] stacks = Thread.currentThread().getStackTrace();
//        for (StackTraceElement stack: stacks) {
//            System.out.println(stack.getMethodName() + "***" + stack.getClassName());
//        }
        //测试参数优先级（username=错误参数[有效用户]，email=正确参数[无效用户]）
        try {
             mockMvc.perform(
                    MockMvcRequestBuilders.get("/api/account")
                            .param("username", "failsuvan")
                            .param("email", "liushuwei0925@gmail.com")
            ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false));

        } catch (NestedServletException ne) {
            Throwable throwable = ne.getRootCause();
            Assert.assertThat(throwable, CoreMatchers.is(CoreMatchers.instanceOf(AccountErrorException.class)));
            Assert.assertEquals(throwable.getMessage(), "the user does not exist");
        }

        printSuccessPassTestMehtodMessage();
    }

    /**
     * 【/api/account】test already login get user information
     */
    @Test
    public void testAreadyLoginGetUserInfo() throws Exception {
        ArrayList<Param> alist = new ArrayList<>();
            alist.add(new Param("username", "suvan"));
            alist.add(new Param("username", "liushuwei0925@gmail.com"));
            alist.add(new Param("email", "liushuwei0925@gmail.com"));

        UserDO user = new UserDO();
            user.setId(6);
            user.setName("suvan");
            user.setRank("admin");
            user.setState(1);
        Cookie autoLoginCookie = new Cookie(ParamConst.AUTHENTICATION, JwtTokenUtil.createToken(user));

        for (Param param : alist) {
            mockMvc.perform(
                    MockMvcRequestBuilders.get("/api/account")
                        .param(param.key, (String) param.value)
                        .cookie(autoLoginCookie)
            ).andExpect(MockMvcResultMatchers.status().isOk())
             .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
             .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
             .andExpect(MockMvcResultMatchers.jsonPath("$.message").doesNotExist())
             .andExpect(MockMvcResultMatchers.jsonPath("$.model").exists());
        }

        printSuccessPassTestMehtodMessage();
    }

    /**
     * 【/api/account】 test get user information throws Exceptin
     */
    @Test
    public void testGetUserInfoThrowsException () throws Exception {
        ArrayList<Param> alist = new ArrayList<>();
            alist.add(new Param("username", null));
            alist.add(new Param("username", "a"));
            alist.add(new Param("username", "*asdf-="));
            alist.add(new Param("username", "12312311111111111111111"));
            alist.add(new Param("username", "suvanaa"));
            alist.add(new Param("emial", null));
            alist.add(new Param("email", "asdfasfsfaasf@"));
            alist.add(new Param("email", "suvan@liushuwei.cn"));

        for (Param param: alist) {
            try{
                mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/account")
                                .param(param.key, (String) param.value)
                ).andDo(MockMvcResultHandlers.print());
            } catch (NestedServletException ne) {
                //自定义异常通过
                Assert.assertThat(ne.getRootCause(),
                        CoreMatchers.anyOf(CoreMatchers.is(CoreMatchers.instanceOf(ParamsErrorException.class)),
                                           CoreMatchers.is(CoreMatchers.instanceOf(DatabaseOperationFailException.class)),
                                           CoreMatchers.is(CoreMatchers.instanceOf(AccountErrorException.class)))
                        );
            }
        }

        printSuccessPassTestMehtodMessage();
    }

    /**
     *【/api/account/state】test get user activate state
     */
    @Test
    public void testGetUserActivateState() throws Exception{
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/account/state")
                    .param("username","suvan")
        ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true));

        printSuccessPassTestMehtodMessage();
    }

    /**
     * 【/api/account/state】test get user activate state throw exception
     */
    @Test
    public void testGetUserActivateStateThrowException() throws Exception {
        String key = "username";
        String[] values = {null, "a", "12**+-3123", "hello"};

        for (String value: values) {
            try {
                mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/account/state")
                            .param(key, value)
                ).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false));

            } catch (NestedServletException ne) {
                Assert.assertThat(ne.getRootCause(),
                    CoreMatchers.anyOf(CoreMatchers.is(CoreMatchers.instanceOf(ParamsErrorException.class)),
                                       CoreMatchers.is(CoreMatchers.instanceOf(AccountErrorException.class)))
                    );
             }
        }

        printSuccessPassTestMehtodMessage();
    }
}
