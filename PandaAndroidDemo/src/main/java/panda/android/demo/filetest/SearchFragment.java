package panda.android.demo.filetest;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import butterfork.Bind;
import butterfork.ButterFork;
import butterknife.ButterKnife;
import panda.android.demo.B;
import panda.android.demo.R;
import panda.android.lib.base.ui.fragment.BaseFragment;

/**
 * Created by admin on 2016/10/8.
 */

public class SearchFragment extends BaseFragment {
    @Bind(B.id.et_search)
    EditText etSearch;
    @Bind(B.id.list_view)
    ListView listView;

    @Override
    public int getLayoutId() {
        return R.layout.search_fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                onStartSearchFile(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return rootView;
    }


    /**
     * 开始搜索
     *
     * @param filename
     */
    private void onStartSearchFile(String filename) {
        String volumeName = "external";
        Uri uri = MediaStore.Files.getContentUri(volumeName);
        String selection = MediaStore.Files.FileColumns.TITLE
                + " LIKE '%" + filename + "%'";
        String[] columns = new String[]{MediaStore.Files.FileColumns._ID, MediaStore.Files.FileColumns.DATA,
                MediaStore.Files.FileColumns.SIZE, MediaStore.Files.FileColumns.DATE_MODIFIED};
        Cursor cursor = getContext().getContentResolver().query(uri, columns, selection,
                null, MediaStore.Files.FileColumns.TITLE + " asc");

        List<FileInfo> list = (List<FileInfo>) getAllFiles(cursor);
        listView.setAdapter(new MyAdapter(list));
    }


    private HashMap<Integer, FileInfo> mFileNameList = new HashMap<Integer, FileInfo>();
    private List<FileInfo> fileInfoList = new ArrayList<>();

    public Collection<FileInfo> getAllFiles(Cursor cursor) {
        fileInfoList.clear();
        if (cursor.moveToFirst()) {
            do {
                Integer position = Integer.valueOf(cursor.getPosition());
//                if (mFileNameList.containsKey(position))
//                    continue;
                FileInfo fileInfo = getFileInfo(cursor);
                if (fileInfo != null) {
//                    mFileNameList.put(position, fileInfo);
                    fileInfoList.add(fileInfo);
                }
            } while (cursor.moveToNext());
        }

        return fileInfoList;
    }

    private FileInfo getFileInfo(Cursor cursor) {
        return (cursor == null || cursor.getCount() == 0) ? null : GetFileInfo(cursor.getString(1));
    }

    public FileInfo GetFileInfo(String filePath) {
        File lFile = new File(filePath);
        if (!lFile.exists())
            return null;

        FileInfo lFileInfo = new FileInfo();
        lFileInfo.canRead = lFile.canRead();
        lFileInfo.canWrite = lFile.canWrite();
        lFileInfo.isHidden = lFile.isHidden();
        lFileInfo.fileName = getNameFromFilepath(filePath);
        lFileInfo.ModifiedDate = lFile.lastModified();
        lFileInfo.IsDir = lFile.isDirectory();
        lFileInfo.filePath = filePath;
        lFileInfo.fileSize = lFile.length();
        return lFileInfo;
    }

    public static String getNameFromFilepath(String filepath) {
        int pos = filepath.lastIndexOf('/');
        if (pos != -1) {
            return filepath.substring(pos + 1);
        }
        return "";
    }


    public class MyAdapter extends BaseAdapter {
        List<FileInfo> list;

        public MyAdapter(List<FileInfo> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = View.inflate(getContext(), R.layout.test_net_item_list_layout, null);
            ViewHolder holder = new ViewHolder(convertView);
            FileInfo model = (FileInfo) getItem(position);
            holder.tvName.setText(model.fileName);
            return convertView;
        }

        class ViewHolder {
            @Bind(B.id.tv_name)
            TextView tvName;

            ViewHolder(View view) {
                ButterFork.bind(this, view);
            }
        }
    }

}
