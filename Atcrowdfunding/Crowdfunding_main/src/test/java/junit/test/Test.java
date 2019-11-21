package junit.test;

import com.qzn.crowdfunding.bean.User;
import com.qzn.crowdfunding.manager.service.UserService;
import com.qzn.crowdfunding.util.MD5Util;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author qzn
 * @create 2019/10/28 15:33
 */
public class Test {
    public static void main(String[] args) {
        ApplicationContext ioc = new ClassPathXmlApplicationContext("spring/spring*.xml");

        UserService userService = ioc.getBean(UserService.class);

        for (int i = 1; i <= 100; i++) {
            User user = new User();
            user.setLoginacct("test" + i);
            user.setUserpswd(MD5Util.digest("123"));
            user.setUsername("test" + i);
            user.setEmail("test" + i + "@qq.com");
            user.setCreatetime("2019-10-28 15:39:00");
            userService.saveUser(user);
        }

    }
}
