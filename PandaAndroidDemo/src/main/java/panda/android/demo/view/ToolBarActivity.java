package panda.android.demo.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import butterknife.Bind;
import butterknife.ButterKnife;
import panda.android.demo.R;
import panda.android.lib.base.util.DevUtil;

/**
 * 演示ToolBar
 * Created by shitianci on 16/11/22.
 */
public class ToolBarActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    private ShareActionProvider mShareActionProvider;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_toolbar);
        ButterKnife.bind(this);
        mToolbar.setLogo(R.drawable.ic_launcher);
        mToolbar.setTitle(getString(R.string.app_name));// 标题的文字需在setSupportActionBar之前，不然会无效
        mToolbar.setSubtitle("副标题");
        setSupportActionBar(mToolbar);
///* 这些通过ActionBar来设置也是一样的，注意要在setSupportActionBar(toolbar);之后，不然就报错了 */
//        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem shareMenuItem = menu.findItem(R.id.action_share);

        if (shareMenuItem != null) {
            ShareActionProvider shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareMenuItem);
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareActionProvider.setShareIntent(shareIntent);
            shareMenuItem.setIcon(android.R.drawable.ic_menu_share);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id){
            case R.id.action_settings:
                DevUtil.showInfo(this, "设置");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
