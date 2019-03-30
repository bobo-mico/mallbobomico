package my;

/**
 * @ClassName: my.mallbobomico
 * @Author: DELL
 * @Date: 2019/3/31  5:15
 * @Description: 作为方法引用接口 只能有且仅有一个函数定义
 * @version:
 */
@FunctionalInterface
public interface Supplier<T> {
    T get();
}
