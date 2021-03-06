package test.org.neusoft.neubbs.log;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * 测试 Log4j 日志框架
 */
@RunWith(JUnit4.class)
public class TestLog4j {

    //public static Logger logger = Logger.getRootLogger();
    private Logger LOGGER = Logger.getLogger(TestLog4j.class);

    /**
     * 测试打印信息
     */
    @Test
    public void testPrintInfo(){
        //BasicConfigurator.configure(); //进行默认配置
        //PropertyConfigurator.configure("");//可设置配置文件路径，默认是找默认会在 CLASSPATH 寻找 Log4j.properties 文件

        LOGGER.setLevel(Level.DEBUG); //日志级别
        LOGGER.fatal("致命信息");
        LOGGER.error("错误信息");
        LOGGER.warn("警告信息");
        LOGGER.info("输出信息");
        LOGGER.debug("调试信息");
    }
}
