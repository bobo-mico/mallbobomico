package my;

import java.util.List;

/**
 * @ClassName: my.mallbobomico
 * @Author: DELL
 * @Date: 2019/3/31  4:35
 * @Description:
 * @version:
 */
public class ColonTest {
    public static void main(String[] args) {
        // The target type of this expression must be a functional interface
        IConvert<String, String> convert = Colon::startsWith;
        String converted = convert.convert("123");
        System.out.println(converted);

        Apple apple = new Apple();
        IApple iApple = apple::setName;
        iApple.setName("乔布斯");
        System.out.println(apple.getName());

    }
}
