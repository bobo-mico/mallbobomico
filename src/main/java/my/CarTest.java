package my;

import com.bobomico.pojo.SysUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName: my.mallbobomico
 * @Author: DELL
 * @Date: 2019/3/31  5:16
 * @Description:
 * @version:
 */
public class CarTest {
    public static void main(String[] args) {
        // 构造器引用：它的语法是Class::new，或者更一般的Class< T >::new实例如下：
        // 把函数赋值给接口的方法
        Supplier<Object> s = Car::new;
        s.get();
        // Car car2 = s.get();
        // System.out.println(car2);


        final Car car = Car.create( Car::new );
        final List< Car > cars = Arrays.asList( car );
        // System.out.println(cars.size());

        SysQuestionAnswer qas = new SysQuestionAnswer();
        IQuestionAnswer<Integer> qa = qas::setSysUserId;
        qa.setSysUserId(1);
        // System.out.println( qas.getSysUserId() );

        List<SysQuestionAnswer> questionAnswers = new ArrayList();
        SysQuestionAnswer q1 = new SysQuestionAnswer();
        SysQuestionAnswer q2 = new SysQuestionAnswer();
        SysQuestionAnswer q3 = new SysQuestionAnswer();
        q1.setSysUserId(1);
        q2.setSysUserId(2);
        q3.setSysUserId(4);
        questionAnswers.add(q1);
        questionAnswers.add(q2);
        questionAnswers.add(q3);

        IQuestionAnswer<Integer> qa1 = qas::setSysUserId;
        // questionAnswers.stream().map(qa1.setSysUserId(1));

        // 流 - 赋值
        // questionAnswers = questionAnswers.stream().map(i -> SysQuestionAnswer::setSysUserId(1));

        // 流 - 过滤
        questionAnswers = questionAnswers.stream()
                .filter(questionAnswer -> questionAnswer.getSysUserId() < 4)
                .collect(Collectors.toList());

        // System.out.println(questionAnswers.get(0).getSysUserId());
        System.out.println(questionAnswers.size());
    }
}
