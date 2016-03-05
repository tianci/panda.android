package panda.android.demo.view;

import panda.android.demo.model.SimpleModel;
import panda.android.lib.base.model.NetResultInfo;
import panda.android.lib.base.ui.fragment.NetFragment;
import panda.android.lib.base.util.DevUtil;


/**
 * Created by shitianci on 15/6/16.
 */
public class MainFragment extends NetFragment<SimpleModel> {

    @Override
    protected SimpleModel onDoInBackgroundSafely() {
        SimpleModel result = new SimpleModel();
        result.setInfo("test");
        result.setRespCode(NetResultInfo.RETURN_CODE_000000);
        return result;
    }

    @Override
    protected void showResult(SimpleModel result) {
        super.showResult(result);
        DevUtil.showInfo(getActivity(), result.getInfo());
    }

}
