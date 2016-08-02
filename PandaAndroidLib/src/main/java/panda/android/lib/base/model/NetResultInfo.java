package panda.android.lib.base.model;


public class NetResultInfo extends BaseModel {
	//由于机务段的接口成功值为1，此处需要特殊适配。
	public final static int RETURN_CODE_000000 = 0;	//成功
//	public final static int RETURN_CODE_000001 = 1;	//参数错误	提示服务端返回的描述信息
//	public final static int RETURN_CODE_000002 = 2;	//数据库错误	提示服务端返回的描述信息
//	public final static int RETURN_CODE_000003 = 3;	//内部错误	提示服务端返回的描述信息
    public final static int NON_USER=110001;//不是用户
	public final static int RETURN_CODE_999999 = 999999;	//其他错误	提示服务端返回的描述信息

	
	private int respCode = RETURN_CODE_999999;
	private String respDesc = "其他错误";

	public int getRespCode() {
        if (respCode == RETURN_CODE_999999){
            //兼容机务段项目
            return getReturnCode();
        }
		return respCode;
	}

	public void setRespCode(int respCode) {
		this.respCode = respCode;
	}

	public String getRespDesc() {
        if (respCode == RETURN_CODE_999999){
            //兼容机务段项目
            return getReturnDesc();
        }
		return respDesc;
	}

	public void setRespDesc(String respDesc) {
		this.respDesc = respDesc;
	}


	public static NetResultInfo getMockData() {
		NetResultInfo result = new NetResultInfo();
		result.setRespCode(RETURN_CODE_000000);
		result.setRespDesc("OK");
		return result;
	}

    /**
     * !!! 以下代码为兼容机务段项目做得适配，其它项目不要采用这样的配置
     */
    @Deprecated
    private int returnCode = RETURN_CODE_999999;
    @Deprecated
    private String returnDesc = "其他错误";

    @Deprecated
    public String getReturnDesc() {
        return returnDesc;
    }

    @Deprecated
    public void setReturnDesc(String mReturnDesc) {
        returnDesc = mReturnDesc;
    }

    @Deprecated
    public int getReturnCode() {
        return returnCode;
    }

    @Deprecated
    public void setReturnCode(int mReturnCode) {
        returnCode = mReturnCode;
    }
}
