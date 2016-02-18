package panda.android.lib.base.util;

import java.lang.reflect.Field;

/**
 * Created by shitianci on 16/2/18.
 */
public class ReflectionUtil {
    private static final String TAG = ReflectionUtil.class.getSimpleName();

    public static void printInfo(Object o) {
        try {
            /**
             * 一个对象所属的类
             */
            Class<?> c = o.getClass();
            /**
             * 一个类所具有的成员变量
             */
            Field[] fs = c.getDeclaredFields(); // 获取实体类的所有属性，返回Field数组
            for (int i = 0; i < fs.length; i++) {
                Field f = fs[i];
                f.setAccessible(true); //设置些属性是可以访问的
                Object val = null;//得到此属性的值
                val = f.get(o);
                String type = f.getType().toString();//得到此属性的类型
                Log.i(TAG, type + "\t" + f.getName() + "\t  = " + val);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 返回某个对象 的 某个属性
     * @param o 对象
     * @param type 属性类型
     * @param fieldName 属性名
     * @param <T>
     * @return
     */
    public static <T> T getField(Object o, Class<?> type, String fieldName) {
        try {
            /**
             * 一个对象所属的类
             */
            Class<?> c = o.getClass();
            /**
             * 一个类所具有的成员变量
             */
            Field[] fs = c.getDeclaredFields(); // 获取实体类的所有属性，返回Field数组
            for (int i = 0; i < fs.length; i++) {
                Field f = fs[i];
                f.setAccessible(true); //设置些属性是可以访问的
                if (f.getType().equals(type) && fieldName.equals(f.getName())) {
                    Log.i(TAG, type + "\t" + f.getName() + "\t  = " + f.get(o));
                    return (T) f.get(o);
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
