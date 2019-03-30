package my;

/**
 * @ClassName: com.bobomico.dao.po.mallbobomico
 * @Author: DELL
 * @Date: 2019/3/31  4:33
 * @Description:
 * @version:
 */
public class Colon {

    // constructor methods
    Colon() {}

    Colon(String something) {
        System.out.println(something);
    }

    // static methods
    static String startsWith(String s) {
        return String.valueOf(s.charAt(0));
    }

    // object methods
    String endWith(String s) {
        return String.valueOf(s.charAt(s.length()-1));
    }

    void endWith() {}
}
