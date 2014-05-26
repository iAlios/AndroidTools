package alios.app.utils.test;

import java.io.File;
import java.util.Date;

import alios.app.utils.apk.ApkUtil;
import alios.app.utils.apk.MStringUtils;

public class Main {

	public static final boolean makeChannelApk(ApkUtil cApkUtil,
			String channelId, String keystorePath, String apkPath, String passwd) {
		String fileName = "assets/channel.dat";
		File apkPathFile = new File(apkPath);
		File file = new File(apkPathFile.getParentFile(), fileName);
		System.out.println(file.getAbsolutePath());
		boolean result = MStringUtils.writeToFile(channelId, file);
		if (result == false) {
			return result;
		}
		File updateFile = cApkUtil.updateApk(apkPath, file, fileName);
		if (updateFile == null) {
			return false;
		}
		return cApkUtil.signApk(keystorePath, updateFile.getAbsolutePath(),
				passwd);
	}

	public static void main(String[] args) {
		ApkUtil cApkUtil = new ApkUtil();
		cApkUtil.updateToolsPath("./tool");

		Date dDate = new Date();
//		makeChannelApk(cApkUtil, "16", "app/sy599.p12",
//				"app/rexueshenjiang_soumi_05192017.apk", "up1234");
//		
		cApkUtil.signApk("app/sy599.p12",
				"app/rexueshenjiang_soumi_051920176777.apk", "up1234");
		
		System.out.println(dDate);
		System.out.println(new Date());
	}

}
