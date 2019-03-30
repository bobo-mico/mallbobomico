package my;

/**
 * @ClassName: my.mallbobomico
 * @Author: DELL
 * @Date: 2019/3/31  4:36
 * @Description: @FunctionalInterface 注解要求接口有且只有一个抽象方法
 *                  JDK中有许多类用到该注解 比如Runnable 它只有一个Run方法
 * @version:
 */

@FunctionalInterface
interface IConvert<F, T> {
    T convert(F form);
}