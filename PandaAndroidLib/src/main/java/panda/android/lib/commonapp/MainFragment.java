package panda.android.lib.commonapp;

import android.os.Bundle;
import android.view.View;

import de.greenrobot.event.EventBus;
import panda.android.lib.R;
import panda.android.lib.base.control.SimpleSafeTask;
import panda.android.lib.base.ui.fragment.BaseFragment;
import panda.android.lib.base.util.DevUtil;

/**
 * Created by shitianci on 15/6/16.
 */
public class MainFragment extends BaseFragment {

    public View getGuideView() {
        return mGuideView;
    }

    public View getLaunchView() {
        return mLaunchView;
    }

    private View mGuideView;
    private View mLaunchView;


    private long mLaunchViewDisplayTimeLength = 3000L; //启动页显示的时间
    private boolean isGuided; //引导页是否展示过
    private int mGuideViewID = R.id.guide_view;

    /**
     * 设置启动页对应的view id
     *
     * @param mLaunchViewID
     */
    public void setLaunchViewID(int mLaunchViewID) {
        this.mLaunchViewID = mLaunchViewID;
    }

    /**
     * 设置引导页对应的view id
     *
     * @param mGuideViewID
     */
    public void setGuideViewID(int mGuideViewID) {
        this.mGuideViewID = mGuideViewID;
    }

    private int mLaunchViewID = R.id.launch_view;

    @Override
    public int getLayoutId() {
        return R.layout.commonapp_activity_main;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mGuideView = getView().findViewById(mGuideViewID);
        mLaunchView = getView().findViewById(mLaunchViewID);
        configLaunchView();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public long getLaunchViewDisplayTimeLength() {
        return mLaunchViewDisplayTimeLength;
    }

    public void setLaunchViewDisplayTimeLength(long mLaunchViewDisplayTimeLength) {
        this.mLaunchViewDisplayTimeLength = mLaunchViewDisplayTimeLength;
    }


    /**
     * 配置启动界面
     */
    public void configLaunchView() {
        SimpleSafeTask<Void> waitLaunchViewDismissedTask = new SimpleSafeTask<Void>(
                getActivity()) {

            @Override
            protected Void doInBackgroundSafely() throws Exception {
                Thread.sleep(mLaunchViewDisplayTimeLength);
                return null;
            }

            @Override
            protected void onPostExecuteSafely(Void aVoid, Exception e) {
                super.onPostExecuteSafely(aVoid, e);
                mLaunchView.setVisibility(View.GONE);

                EventBus.getDefault().post(new LaunchEndEvent());
            }
        };
        waitLaunchViewDismissedTask.execute();
    }

    public void onEventMainThread(LaunchEndEvent event) {
        DevUtil.showInfo(getActivity(), "启动结束");
        if (!isGuided) {
            mGuideView.setVisibility(View.VISIBLE);
        } else {
            mGuideView.setVisibility(View.GONE);
        }
    }

    /**
     * 引导结束
     */
    public void finishGuide() {
        mLaunchView.setVisibility(View.GONE);
        EventBus.getDefault().post(new GuideEndEvent());
    }

    public void onEventMainThread(GuideEndEvent event) {
        DevUtil.showInfo(getActivity(), "引导结束，欢迎进入PandaAndroidDemo");
        mGuideView.setVisibility(View.GONE);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private class LaunchEndEvent {
    }

    private class GuideEndEvent {
    }
}
