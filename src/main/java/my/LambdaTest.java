package my;

/**
 * @ClassName: my.mallbobomico
 * @Author: DELL
 * @Date: 2019/3/31  4:55
 * @Description:    Lambda 表达式主要用来定义行内执行的方法类型接口，
 *                  例如，一个简单方法接口。在上面例子中，
 *                     我们使用各种类型的Lambda表达式来定义MathOperation接口的方法。
 *                     然后我们定义了sayMessage的执行。
 *                      Lambda 表达式免去了使用匿名方法的麻烦，
 *                      并且给予Java简单但是强大的函数化的编程能力。
 * @version:
 */
public class LambdaTest {
    public static void main(String args[]){
        LambdaTest tester = new LambdaTest();

        // JDK1.7
        MathOperation add = new MathOperation() {
            @Override
            public int operation(int a, int b) {
                return a + b;
            }
        };

        /**
         * 接口中有且仅有一个函数时使用
         */
        // 参数列表 参数类型声明
        MathOperation addition = (int a, int b) -> a + b;

        // 参数列表 不用类型声明 可做类型推断时省略
        MathOperation subtraction = (a, b) -> a - b;

        // 大括号中的返回语句
        MathOperation multiplication = (int a, int b) -> { return a * b; };

        // 没有大括号及返回语句 单行语句时省略
        MathOperation division = (int a, int b) -> a / b;

        // 可把lambda函数当作参数传递
        System.out.println("10 + 5 = " + tester.operate(10, 5, addition));
        System.out.println("10 - 5 = " + tester.operate(10, 5, subtraction));
        System.out.println("10 x 5 = " + tester.operate(10, 5, multiplication));
        System.out.println("10 / 5 = " + tester.operate(10, 5, division));

        // 单行语句时不用括号
        GreetingService greetService1 = message ->
                System.out.println("Hello " + message);

        // 用括号
        GreetingService greetService2 = (message) ->
                System.out.println("Hello " + message);

        greetService1.sayMessage("Runoob");
        greetService2.sayMessage("Google");
    }

    interface MathOperation {
        int operation(int a, int b);
    }

    interface GreetingService {
        void sayMessage(String message);
    }

    private int operate(int a, int b, MathOperation mathOperation){
        return mathOperation.operation(a, b);
    }
}