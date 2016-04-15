package panda.android.lib.commonapp.person;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import panda.android.lib.base.control.SimpleSafeTaskWithTips;
import panda.android.lib.base.model.NetResultInfo;
import panda.android.lib.base.ui.UIUtil;
import panda.android.lib.base.ui.fragment.BaseFragment;
import panda.android.lib.base.util.DevUtil;
import panda.android.lib.base.util.TextUtil;

import panda.android.lib.R;

/**
 * 登录
 */
public abstract class LoginFragment<T extends NetResultInfo> extends BaseFragment implements View.OnClickListener{
//    @Bind(R.id.et_uesrphone)
    EditText etUesrphone;
//    @Bind(R.id.et_userpassword)
    EditText etUserpassword;
//    @Bind(R.id.bt_login)
    Button btLogin;
//    @Bind(R.id.tv_register)
    TextView tvRegister;
//    @Bind(R.id.tv_find_password)
    TextView tvFindPassword;
//    @Bind(R.id.iv_wx_login)
    ImageView ivWxLogin;
//    @Bind(R.id.iv_qq_login)
    ImageView ivQqLogin;
//    @Bind(R.id.iv_sina_login)
    ImageView ivSinaLogin;

    @Override
    public int getLayoutId() {
        return R.layout.person_login;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        if (view != null) {
            etUesrphone = findViewById(view, R.id.et_uesrphone, this);
            etUserpassword = findViewById(view, R.id.et_userpassword, this);
            btLogin = findViewById(view, R.id.bt_login, this);
            tvRegister = findViewById(view, R.id.tv_register, this);
            tvFindPassword = findViewById(view, R.id.tv_find_password, this);
            ivWxLogin = findViewById(view, R.id.iv_wx_login, this);
            ivQqLogin = findViewById(view, R.id.iv_qq_login, this);
            ivSinaLogin = findViewById(view, R.id.iv_sina_login, this);
        }
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

//    @OnClick({R.id.bt_login, R.id.tv_register, R.id.tv_find_password, R.id.iv_wx_login, R.id.iv_qq_login, R.id.iv_sina_login})
    public void onClick(View view) {
        if (view.getId() == R.id.bt_login){
            login();
        }
        else if(view.getId() == R.id.tv_register){
            gotoRegister();
        }
        else if(view.getId() == R.id.tv_find_password){
            gotoFindPassword();
        }
        else if(view.getId() == R.id.iv_wx_login){
            wxLogin();
        }
        else if(view.getId() == R.id.iv_qq_login){
            qqLogin();
        }
        else if(view.getId() == R.id.iv_sina_login){
            sinaLogin();
        }
    }

    /**
     * 新浪登录
     */
    public void sinaLogin() {
    }

    /**
     * QQ登录
     */
    public void qqLogin() {
    }

    /**
     * 微信登录
     */
    public void wxLogin() {
    }

    /**
     * 找回密码
     */
    public void gotoFindPassword() {

    }

    /**
     * 注册
     */
    public void gotoRegister() {
    }


    /**
     * 手机号登录
     */
    public void login() {
        new SimpleSafeTaskWithTips<T>(getActivity(), UIUtil.getLoadingDlg(getActivity(), true)){

            public String phone;
            private String pass;

            @Override
            protected void onPreExecuteSafely() throws Exception {
                phone = etUesrphone.getText().toString();
                pass = etUserpassword.getText().toString();
                if (TextUtil.isNull(phone)) {
                    DevUtil.showInfo(getActivity(), "请输入手机号");
                    cancel(true);
                    return;
                }
                if (!TextUtil.isPhone(phone)) {
                    DevUtil.showInfo(getActivity(), "请输入正确手机号");
                    cancel(true);
                    return;
                }
                if (TextUtil.isNull(pass)) {
                    DevUtil.showInfo(getActivity(), "请输入密码");
                    cancel(true);
                    return;
                }
                super.onPreExecuteSafely();

            }

            @Override
            protected T doInBackgroundSafely() throws Exception {
                return loginInBackgroundSafely(phone, pass);
            }

            @Override
            protected void onPostExecuteSafely(T result, Exception e) {
                super.onPostExecuteSafely(result, e);
                if (result != null && result.getReturnCode() == NetResultInfo.RETURN_CODE_000000){
                    loginOK(result);
                }
            }
        }.execute();
        return;
    }

    protected abstract void loginOK(T result);
    protected abstract T loginInBackgroundSafely(String phone, String pass);

}
