package panda.android.demo.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterfork.Bind;
import butterfork.ButterFork;
import panda.android.demo.B;
import panda.android.demo.R;
import panda.android.demo.model.ChannelModel;
import panda.android.demo.model.ChannelModelsNetResultInfo;
import panda.android.demo.model.RtspModel;
import panda.android.lib.base.model.ListNetResultInfo;
import panda.android.lib.base.ui.fragment.ListNetFragment;
import panda.android.lib.base.util.DevUtil;
import panda.android.lib.base.util.Log;
import panda.android.lib.commonapp.DeviceInfoFragment;

/**
 * Created by shitianci on 16/1/27.
 */
public class MainFragment extends DeviceInfoFragment {

//    private static final String TAG = MainFragment.class.getSimpleName();
//    public static final String BASE_URL = "http://120.24.67.92:8080/rtsp";
//    @Bind(B.id.title)
//    TextView title;
//    @Bind(B.id.list)
//    ListView mNetResult;
//    private MyAdapter adapter;
//
//
//    @Override
//    public int getLayoutId() {
//        return R.layout.fragment_main;
//    }
//
//    @Override
//    protected ListNetResultInfo<ChannelModel> onDoInBackgroundSafely(final int startIndex, int pageSize) {
////        return BaseRepositoryCollection.executeGetRequest(BASE_URL + "/all", null, ChannelModelsNetResultInfo.class);
//        ChannelModelsNetResultInfo result = ChannelModelsNetResultInfo.getMockData();
//        try {
//            Thread.sleep(3000L);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        return result;
//    }
//
//    @Override
//    protected void displayResult(List<ChannelModel> list) {
//        Log.d(TAG, "displayResult, list.size()" + list.size());
//        //转换成真正需要的数据 List<RtspModel[2]>。
//        List<RtspModel[]> rtspModels = new ArrayList<RtspModel[]>();
//        for (ChannelModel cm : list) {
//            List<RtspModel> cmList = cm.getList();
//            for (int i = 0; i < cmList.size(); i = i + 2) {
//                RtspModel[] rms = new RtspModel[2];
//                rms[0] = cmList.get(i);
//                rms[0].setChannel_name(cm.getName());
//                rms[0].setChannel_pos(i);
//                rms[0].setChannel_num(cmList.size());
//                if (i+1 < cmList.size()) {
//                    rms[1] = cmList.get(i + 1);
//                    rms[1].setChannel_name(cm.getName());
//                    rms[1].setChannel_pos(i + 1);
//                    rms[1].setChannel_num(cmList.size());
//                }
//                rtspModels.add(rms);
//            }
//        }
//
//        if(adapter == null){
//            adapter = new MyAdapter(rtspModels);
//            mNetResult.setAdapter(adapter);
//        }
//        else{
//            for (RtspModel[] rtspModel : rtspModels){
//                adapter.add(rtspModel);
//            }
//            adapter.notifyDataSetChanged();
//        }
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View rootView = super.onCreateView(inflater, container, savedInstanceState);
//        title.setText("秀蜜");
//        return rootView;
//    }
//
//    class MyAdapter extends ArrayAdapter<RtspModel[]> {
//
//        private static final int TYPE_BANNER = 0;
//        private static final int TYPE_NORMAL = 1;
//
//        public MyAdapter(List<RtspModel[]> mList) {
//            super(getActivity(), R.layout.item_rtsp_models, R.id.iv_rtsp_name, mList);
//        }
//
//        @Override
//        public int getViewTypeCount() {
//            return 2;
//        }
//
//        @Override
//        public int getItemViewType(int position) {
//            if (position == 0){
//                return TYPE_BANNER;
//            }
//            else{
//                return TYPE_NORMAL;
//            }
//        }
//
//        @Override
//        public int getCount() {
//            return super.getCount()+1;
//        }
//
//        @Override
//        public RtspModel[] getItem(int position) {
//            if (position == 0){
//                return null;
//            }
//            return super.getItem(position-1);
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            if (position == 0){
//                if (convertView == null){
//                    convertView = getActivity().getLayoutInflater().inflate(R.layout.item_rtsp_banner, null);
//                }
//                return convertView;
//            }
//            View mView = super.getView(position, convertView, parent);
//            ViewHolder mViewHolder = new ViewHolder(mView);
//            RtspViewHolder mViewLeftHolder = new RtspViewHolder(mViewHolder.mViewLeft);
//            RtspViewHolder mViewRightHolder = new RtspViewHolder(mViewHolder.mViewRight);
//
//            RtspModel[] mItem = getItem(position);
//            mViewHolder.mTvChannelTitle.setText(mItem[0].getChannel_name());
//            if (mItem[0].getChannel_pos() == 0) {
//                mViewHolder.mTvChannelTitle.setVisibility(View.VISIBLE);
//                mViewHolder.mIvSpace.setVisibility(View.VISIBLE);
//            }
//            else{
//                mViewHolder.mTvChannelTitle.setVisibility(View.GONE);
//                mViewHolder.mIvSpace.setVisibility(View.GONE);
//            }
//
//            if (mItem[0].getChannel_pos() == mItem[0].getChannel_num()-1 || mItem[0].getChannel_pos() == mItem[0].getChannel_num()-2){
//                mViewHolder.mIvSpace2.setVisibility(View.VISIBLE);
//            }
//            else {
//                mViewHolder.mIvSpace2.setVisibility(View.GONE);
//            }
//
//            mViewLeftHolder.updateData(mItem[0]);
//            mViewRightHolder.updateData(mItem[1]);
//            return mView;
//        }
//
//        class ViewHolder {
//            @Bind(B.id.tv_channel_title)
//            TextView mTvChannelTitle;
//
//            @Bind(B.id.view_left)
//            View mViewLeft;
//
//            @Bind(B.id.view_right)
//            View mViewRight;
//
//            @Bind(B.id.iv_space)
//            ImageView mIvSpace;
//
//            @Bind(B.id.iv_space2)
//            View mIvSpace2;
//
//            ViewHolder(View view) {
//                ButterFork.bind(this, view);
//            }
//        }
//
//        class RtspViewHolder {
//            private final View mView;
//            @Bind(B.id.iv_rtsp_img)
//            ImageView mIvRtspImg;
//            @Bind(B.id.iv_rtsp_name)
//            TextView mIvRtspName;
//
//            RtspViewHolder(View view) {
//                ButterFork.bind(this, view);
//                mView = view;
//            }
//
//            public void updateData(final RtspModel mItem) {
//                if(mItem == null){
//                    mView.setVisibility(View.INVISIBLE);
//                    return;
//                }
//                mView.setVisibility(View.VISIBLE);
//                mView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View mView) {
//                        DevUtil.showInfo(getActivity(), mItem.getHlsUrl());
//                    }
//                });
//                mIvRtspName.setText(mItem.getTitle());
//                ImageLoader.getInstance().displayImage(mItem.getPic(), mIvRtspImg);
//            }
//        }
//    }
}
