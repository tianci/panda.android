package panda.android.lib.base.model;


public class NetResultInfo extends BaseModel {
	public final static int RETURN_CODE_000000 = 0;	//成功	
	public final static int RETURN_CODE_000001 = 1;	//参数错误	提示服务端返回的描述信息
	public final static int RETURN_CODE_000002 = 2;	//数据库错误	提示服务端返回的描述信息
	public final static int RETURN_CODE_000003 = 3;	//内部错误	提示服务端返回的描述信息
	public final static int RETURN_CODE_999999 = 999999;	//其他错误	提示服务端返回的描述信息

	
	private int respCode = RETURN_CODE_999999;
	private String respDesc = "其他错误";

	public int getRespCode() {
		return respCode;
	}

	public void setRespCode(int respCode) {
		this.respCode = respCode;
	}

	public String getRespDesc() {
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
}
