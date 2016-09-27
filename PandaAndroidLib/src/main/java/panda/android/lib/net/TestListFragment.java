package panda.android.lib.net;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import panda.android.lib.R;
import panda.android.lib.base.model.ListNetResultInfo;

/**
 * Created by admin on 2016/9/26.
 */
public class TestListFragment extends ListNetFragment<TestModel> {
    private static final String TAG = TestListFragment.class.getSimpleName();

    @Override
    public int getLayoutId() {
        return R.layout.panda_net_list_layout;
    }

    @Override
    protected TestModel getErrItem(final IListModel.STATE state) {
        Log.d(TAG, "getErrItem: " + state.value);
        TestModel testModel = new TestModel(1, "2") {
            @Override
            public STATE getState() {
                return state;
            }
        };
        return testModel;
    }

    @Override
    protected ListNetResultInfo<TestModel> onDoInBackgroundSafely(int startIndex, int pageSize) {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return TestModel.getListNetResultInfo(pageSize);
    }

    @Override
    public int getItemTextViewResourceId() {
        return R.id.tv_name;
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.test_net_item_list_layout;
    }

    @Override
    public View bindView(int position, View view, ViewGroup parent) {
        view = View.inflate(getContext(), R.layout.test_net_item_list_layout, null);
        TextView name = (TextView) view.findViewById(R.id.tv_name);
        TestModel model = getItem(position);
        name.setText(model.getName());
        return view;
    }


}
