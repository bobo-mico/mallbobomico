package com.bobomico.pojo;

/**
 * @ClassName: com.bobomico.pojo.mallbobomico
 * @Author: DELL
 * @Date: 2019/4/11  14:22
 * @Description:
 * @version:
 */
public interface UserService {

    ActiveUser save(ActiveUser user);

    ActiveUser update(ActiveUser user);

    ActiveUser delete(ActiveUser user);

    void deleteAll();

    ActiveUser findById(final Long id);
}
