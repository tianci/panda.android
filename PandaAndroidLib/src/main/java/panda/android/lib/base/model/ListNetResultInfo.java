package panda.android.lib.base.model;

import java.util.List;


public abstract class ListNetResultInfo<T> extends NetResultInfo {
	public abstract List<T> getList();
}
