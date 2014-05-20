package alios.app.utils.test;

import java.util.Date;

import alios.app.util.apk.ApkUtil;

public class Main {

	public static void main(String[] args) {
		ApkUtil cApkUtil = new ApkUtil();
		 cApkUtil.updateToolsPath("./tool");
		 
		// System.out.println("the result is " + res);
		System.out.println(new Date());
//		cApkUtil.signApk("app/keystore.key", "app/abc.apk", "1234");
		System.out.println(new Date());
	}

}
