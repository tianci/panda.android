package panda.android.demo.view;

import android.os.Bundle;
import android.widget.ImageView;

import butterfork.Bind;
import panda.android.demo.R;
import panda.android.lib.B;
import panda.android.lib.base.control.SimpleSafeTask;
import panda.android.lib.base.ui.fragment.BaseFragment;
import panda.android.lib.base.util.IntentUtil;

/**
 * Created by shitianci on 15/10/14.
 */
public class LaunchFragment extends BaseFragment {
    @Bind(B.id.launch_view)
    ImageView mLaunchView;

    private long mLaunchViewDisplayTimeLength = 3000L; //启动页显示的时间

    @Override
    public int getLayoutId() {
        return R.layout.fragment_launch;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        configLaunchView();
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
                IntentUtil.gotoActivity(getActivity(), MainActivity.class);
                getActivity().finish();
            }
        };
        waitLaunchViewDismissedTask.execute();
    }
}
