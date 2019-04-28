package com.bobomico.ehcache;

import com.bobomico.pojo.ActiveUser;
import com.bobomico.pojo.UserService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * @ClassName: com.bobomico.ehcache.mallbobomico
 * @Author: DELL
 * @Date: 2019/4/11  14:20
 * @Description:
 * @version:
 */
public class Rmi_1000_CacheTest {

    @Autowired
    private UserService userService;

    @Test
    public void testCache() {

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        do {
            try {
                // System.out.print("> ");
                // System.out.flush();
                // String line = in.readLine().trim();
                String line = "start";
                if (line.equalsIgnoreCase("quit")
                        || line.equalsIgnoreCase("exit"))
                    break;

                if(line.equalsIgnoreCase("start")){
                    // while(true){
                        Long id = 1L;
                        ActiveUser user = new ActiveUser(id, "zhang@gmail.com");
                        System.out.println(user.toString());
                        userService.save(user);
                        System.out.println(userService.findById(id));
                        Thread.sleep(30000);
                        System.out.println(userService.delete(user));
                        Thread.sleep(30000);
                    // }
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Wrong arguments.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } while (false);
        System.exit(0);
    }
}
