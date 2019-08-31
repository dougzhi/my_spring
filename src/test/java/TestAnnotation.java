import com.dongz.spring.my_spring.mvcsramework.annotation.CacheResult;
import com.dongz.spring.my_spring.services.UserService;
import org.junit.Test;

import java.lang.reflect.Method;

/**
 * @author dongzhi
 * @date 2019/8/29
 * @desc
 */
public class TestAnnotation {

    /**
     * 通过反射实现获取注解信息
     */
    @Test
    public void testAnno() {
        Class<UserService> userServiceClass = UserService.class;
        Class<CacheResult> annoClazz = CacheResult.class;

        if (userServiceClass.isAnnotationPresent(annoClazz)) {
            CacheResult anno = userServiceClass.getAnnotation(annoClazz);
            System.out.println(anno.key());
            System.out.println(anno.cacheName());
        }

        Method[] methods = userServiceClass.getMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(annoClazz)) {
                CacheResult anno = method.getAnnotation(annoClazz);
                System.out.println(anno.key());
                System.out.println(anno.cacheName());
            }
        }
    }
}
