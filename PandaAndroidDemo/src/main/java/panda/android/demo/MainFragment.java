package panda.android.demo;

import android.os.Bundle;
import android.view.View;


/**
 * Created by shitianci on 15/6/16.
 */
public class MainFragment extends panda.android.lib.commonapp.MainFragment {

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getGuideView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishGuide();
            }
        });

    }
}
