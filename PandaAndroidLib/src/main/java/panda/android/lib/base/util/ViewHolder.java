package panda.android.lib.base.util;

import android.util.SparseArray;
import android.view.View;

@SuppressWarnings("unchecked")
public class ViewHolder {
	public static <T extends View> T get(View view, int id) {
		return (T) get(view, id, true);
	}
	
	public static <T extends View> T get(View view, int id, boolean bindParentView) {
		SparseArray<Object> viewHolder = (SparseArray<Object>) view.getTag();
		if (viewHolder == null) {
			viewHolder = new SparseArray<Object>();
			view.setTag(viewHolder);
		}
		View childView = (View) viewHolder.get(id);
		if (childView == null) {
			childView = view.findViewById(id);
			if(bindParentView){				
				childView.setTag(view);
			}
			viewHolder.put(id, childView);
		}
		return (T) childView;
	}
	
	public static void putData(View view, int id, Object object) {
		SparseArray<Object> viewHolder = (SparseArray<Object>) view.getTag();
		if (viewHolder == null) {
			viewHolder = new SparseArray<Object>();
			view.setTag(viewHolder);
		}
		viewHolder.put(id, object);
	}
	
	public static Object getData(View view, int id) {
		SparseArray<Object> viewHolder = (SparseArray<Object>) view.getTag();
		if (viewHolder == null) {
			return null;
		}
		return viewHolder.get(id);
	}
}
