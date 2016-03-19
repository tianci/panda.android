package panda.android.lib.base.util;

import panda.android.lib.base.model.BaseModel;

/**
 * 存放跟Model数据对象相关的操作方法
 * Created by shitianci on 16/3/14.
 */
public class ModelUtil {

    public interface Preprocessor<T extends BaseModel>{
        void process(T a);
    }

    /**
     * 复制一个对象
     * @param a
     * @param <T>
     * @return
     */
    public static <T extends BaseModel> T clone(T a) {
        return (T) BaseModel.getGson().fromJson(a.toString(), a.getClass());
    }

    public static <T extends BaseModel> T clone(T a, Preprocessor p) {
        p.process(a);
        return (T) BaseModel.getGson().fromJson(a.toString(), a.getClass());
    }

    /**
     * 两对对象内容是否完全一致。
     * @param a
     * @param b
     * @param <T>
     * @return
     */
    public static <T extends BaseModel> boolean equals(T a, T b) {
        return a.toString().equals(b.toString());
    }

    public static <T extends BaseModel> boolean equals(T a, T b, Preprocessor p) {
        p.process(a);
        p.process(b);
        return equals(a, b);
    }

    public static <T extends BaseModel> boolean equals(T a, T b, Preprocessor pa, Preprocessor pb) {
        pa.process(a);
        pb.process(b);
        return equals(a, b);
    }
}
