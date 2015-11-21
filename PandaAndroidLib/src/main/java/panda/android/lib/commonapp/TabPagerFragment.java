package panda.android.lib.commonapp;

import android.support.v4.app.FragmentActivity;

import panda.android.lib.R;
import panda.android.lib.base.ui.fragment.BaseFragment;
import panda.android.lib.base.util.FragmentUtil;

/**
 * 针对 标签栏+内容区 的结构
 * <p/>
 * 和 panda_fragment_tab.xml配合使用
 *
 * @author shitianci
 */
public abstract class TabPagerFragment extends TabFragment {

    /**
     * 获取按钮栏对应的Fragment
     *
     * @return
     */
    public abstract BaseFragment[] getChildFragments();

    @Override
    public void chooseSame() {
    }

    @Override
    public void chooseTab(int index) {
        super.chooseTab(index);
        openFragment(getChildFragments()[getCurrentTabIndex()], getChildFragments()[index]);
    }

    private void openFragment(BaseFragment Lastfragment,
                              BaseFragment newfragment) {
        FragmentUtil.addFragmentToStack(Lastfragment, newfragment, this,
                R.id.main_content);
    }

    public static void openSecondFragment(FragmentActivity activity,
                                          BaseFragment newfragment) {
        FragmentUtil.addFragmentToStack(newfragment, activity, R.id.container);
    }
}
