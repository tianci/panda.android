package panda.android.lib.base.ui.view;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import butterfork.Bind;
import panda.android.lib.B;
import panda.android.lib.R;
import panda.android.lib.base.ui.fragment.BaseFragment;
import uk.co.senab.photoview.PhotoViewAttacher;


/**
 * Created by admin on 2016/4/19.
 */
public class ImageDetailFragment extends BaseFragment {
    private static final String TAG = ImageDetailFragment.class.getSimpleName();
    @Bind(B.id.image)
    ImageView image;
    @Bind(B.id.loading)
    ProgressBar loading;
    private String mImageUrl;
    private PhotoViewAttacher mAttacher;
    private int sex;
    DisplayImageOptions defaultOptions;

    public static ImageDetailFragment newInstance(String imageUrl, int age) {
        final ImageDetailFragment f = new ImageDetailFragment();
        final Bundle args = new Bundle();
        args.putString("url", imageUrl);
        args.putInt("sex", age);
        f.setArguments(args);
        return f;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_image_detail;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        mImageUrl = bundle.get("url") != null ? getArguments().getString("url") : null;
        sex = bundle.getInt("sex");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);


        mAttacher = new PhotoViewAttacher(image);
        mAttacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {

            @Override
            public void onPhotoTap(View arg0, float arg1, float arg2) {
                getActivity().finish();
            }

            @Override
            public void onOutsidePhotoTap() {

            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "" + sex);
        if (sex == 2) {
            defaultOptions = (new DisplayImageOptions.Builder()).showImageForEmptyUri(R.drawable.person_photo_woman_big)
                    .showImageOnFail(R.drawable.person_photo_woman_big).showImageOnLoading(R.drawable.person_photo_woman_big)
                   .cacheInMemory(true).cacheOnDisk(true).build();
        } else {
            defaultOptions = (new DisplayImageOptions.Builder()).showImageForEmptyUri(R.drawable.person_photo_man_big)
                    .showImageOnFail(R.drawable.person_photo_man_big).showImageOnLoading(R.drawable.person_photo_man_big)
                    .cacheInMemory(true).cacheOnDisk(true).build();
        }

        try {
            ImageLoader.getInstance().displayImage(mImageUrl, image, defaultOptions, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    loading.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    String message = null;
                    switch (failReason.getType()) {
                        case IO_ERROR:
                            message = "下载错误";
                            break;
                        case DECODING_ERROR:
                            message = "图片无法显示";
                            break;
                        case NETWORK_DENIED:
                            message = "网络有问题，无法下载";
                            break;
                        case OUT_OF_MEMORY:
                            message = "图片太大无法显示";
                            break;
                        case UNKNOWN:
//                            message = "未知的错误";
                            break;
                    }
                    loading.setVisibility(View.GONE);
                    if (message != null) {
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    loading.setVisibility(View.GONE);
                    mAttacher.update();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
