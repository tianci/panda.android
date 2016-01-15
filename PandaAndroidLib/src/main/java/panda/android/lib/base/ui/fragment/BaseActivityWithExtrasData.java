package panda.android.lib.base.ui.fragment;

import android.os.Bundle;

import panda.android.lib.base.model.BaseModel;
import panda.android.lib.base.ui.BaseActivity;

/**
 * 作为需要解析额外数据的基类
 *
 * @param <T> 需要加载的主Fragment的类型
 * @param <D> 需要加载的数据的类型
 *
 * Created by shitianci on 15/8/19.
 */
public abstract class BaseActivityWithExtrasData<T extends BaseFragment, D> extends BaseActivity<T>{

    public static final String ACTIVITY_EXTRA_DATA = "data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 获取Activity的额外数据
     * @param param
     * @return
     */
    public D getExtrasData(Class<? extends D> param) {
        try{
            return BaseModel.getGson().fromJson(getIntent().getStringExtra(ACTIVITY_EXTRA_DATA), param);
        }
        catch (Exception e){
            return null;
        }
    }
}
