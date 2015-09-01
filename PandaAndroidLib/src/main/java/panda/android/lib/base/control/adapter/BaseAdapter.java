package panda.android.lib.base.control.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;

import java.util.List;

import panda.android.lib.base.util.ViewHolder;

/**
 * 基础的适配器类
 * @author shitianci
 *
 * @param <T>
 */
public abstract class BaseAdapter<T> extends ArrayAdapter<T> implements OnItemClickListener{

	public static final int VIEW_POSITION = -1;
	public static final int ID_POS_BASE = VIEW_POSITION-1;  //可以放在ViewHolder tag里面保存的key的最大值

	public BaseAdapter(Context context, int resource, int textViewResourceId,
			List<T> objects) {
		super(context, resource, textViewResourceId, objects);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = super.getView(position, convertView, parent);
		}
		//把位置信息放入holder。
		ViewHolder.putData(convertView, VIEW_POSITION, position);
		return convertView;
	}
	
	public Integer getPosition(View convertView) {
		return (Integer) ViewHolder.getData(convertView, VIEW_POSITION);
	}
	
	/**
	 * 获取view所在item的数据，配合ViewHolder.get(View view, int id) 使用
	 * @param childView
	 * @return
	 */
	public T getItemData(View childView) {
		View mParentView = getItemView(childView);
		 return getItem(getPosition(mParentView));
	}

	/**
	 * 获取view所在的View
	 * @param childView
	 * @return
	 */
	public View getItemView(View childView) {
		View mParentView = (View) childView.getTag();
		return mParentView;
	}
	
}
