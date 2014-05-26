package alios.app.utils.test;

import alios.app.utils.MStringUtils;
import alios.app.utils.apk.ApkUtil;

import java.io.File;
import java.util.Date;

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
//		makeChannelApk(cApkUtil, "16", "app/keystore.key",
//				"app/05192017.apk", "1234");
//		
		cApkUtil.signApk("app/keystore.key",
				"app/05192017.apk", "1234");
		
		System.out.println(dDate);
		System.out.println(new Date());
	}

}
