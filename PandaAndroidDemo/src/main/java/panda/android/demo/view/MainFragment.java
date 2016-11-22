package panda.android.demo.view;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import butterfork.Bind;
import butterfork.OnClick;
import panda.android.demo.B;
import panda.android.demo.R;
import panda.android.lib.base.ui.fragment.BaseFragment;
import panda.android.lib.base.util.IntentUtil;

/**
 * Created by shitianci on 16/1/27.
 */
public class MainFragment extends BaseFragment {

    @Bind(B.id.btn_show_toolbar)
    Button mBtnShowToolbar;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        return rootView;
    }

    @OnClick(B.id.btn_show_toolbar)
    public void showToolBar(){
        IntentUtil.gotoActivity(getActivity(), ToolBarActivity.class);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
