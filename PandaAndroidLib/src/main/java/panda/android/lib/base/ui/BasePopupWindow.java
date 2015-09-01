package panda.android.lib.base.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.PopupWindow;
import android.widget.LinearLayout.LayoutParams;

public abstract class BasePopupWindow extends PopupWindow implements View.OnClickListener {

	private View mViewWindow;

	public BasePopupWindow(Context activity, int layoutId) {
		LayoutInflater inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mViewWindow = inflater.inflate(layoutId, null);
		// 设置SelectPicPopupWindow的View
		this.setContentView(mViewWindow);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.MATCH_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.MATCH_PARENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		// 设置SelectPicPopupWindow弹出窗体动画效果
		// this.setAnimationStyle(R.style.PopupAnimation);
		// // 实例化一个ColorDrawable颜色为半透明
		// ColorDrawable dw = new ColorDrawable(0x08f00000);
		// // 设置SelectPicPopupWindow弹出窗体的背景
		// this.setBackgroundDrawable(dw);
		// mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
//		mViewWindow.setOnTouchListener(new OnTouchListener() {
//			public boolean onTouch(View v, MotionEvent event) {
//				int height = mViewWindow.findViewById(id).getBottom();
//				int y = (int) event.getY();
//				if (event.getAction() == MotionEvent.ACTION_UP) {
//					if (y > height) {
//						dismiss();
//					}
//				}
//				return true;
//			}
//		});
		
		initView(mViewWindow);
	}

	protected abstract void initView(View viewWindow);

	public View getViewWindow() {
		return mViewWindow;
	}

	public void setViewWindow(View mViewWindow) {
		this.mViewWindow = mViewWindow;
	}

}
