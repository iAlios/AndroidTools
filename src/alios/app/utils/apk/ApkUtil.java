package alios.app.utils.apk;

import alios.app.utils.MStringUtils;
import alios.app.utils.ZipUtils;
import sun.security.tools.JarSigner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * apk工具类。封装了获取Apk信息的方法。
 */
public class ApkUtil {

	private ProcessBuilder mBuilder;
	private static final String SPLIT_REGEX = "(: )|(=')|(' )|'";
	/**
	 * aapt所在的目录。
	 */
	private String mAaptPath;

	public ApkUtil() {
		mBuilder = new ProcessBuilder();
		mBuilder.redirectErrorStream(true);
	}

	public void updateToolsPath(String path) {
		mAaptPath = path;
	}

	private String getToolsPath(String toolName) {
		if (mAaptPath == null || mAaptPath.length() < 0
				|| !(new File(mAaptPath).isDirectory())) {
			return toolName;
		} else {
			return mAaptPath + "/" + toolName;
		}
	}

	public boolean signApk(String keystorePath, String apkPath, String passwd, String apkSignedFile) {
		JarSigner cJarSigner = new JarSigner();
		cJarSigner.run(new String[] { "-verbose", "-sigalg", "MD5withRSA",
				"-digestalg", "SHA1", "-keystore", keystorePath, "-keypass",
				passwd, "-storepass", passwd, "-storetype", "pkcs12",
				"-sigfile", "cert", "-signedjar", apkSignedFile,
				apkPath, "1" });
		return true;
	}

	public boolean checkSign(String keystorePath, String apkPath, String passwd) {
		JarSigner cJarSigner = new JarSigner();
		cJarSigner.run(new String[] { "-verify", 
				apkPath});
		return true;
	}

	public void dumpApk(String apkPath) {
		try {
			ZipFile zipFile = new ZipFile(apkPath);
			Enumeration<? extends ZipEntry> zipEntries = zipFile.entries();
			ZipEntry zipEntry = null;
			while (zipEntries.hasMoreElements()) {
				zipEntry = zipEntries.nextElement();
				System.out.println(zipEntry.getName());
			}
			zipFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void dumpApkFile(String apkPath, String dumpFilePath) {
		try {
			ZipFile zipFile = new ZipFile(apkPath);
			Enumeration<? extends ZipEntry> zipEntries = zipFile.entries();
			ZipEntry zipEntry = null;
			FileWriter fileWriter = new FileWriter(new File(dumpFilePath));
			while (zipEntries.hasMoreElements()) {
				zipEntry = zipEntries.nextElement();
				fileWriter.write(zipEntry.getName());
			}
			fileWriter.close();
			zipFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public File updateApk(String apkPath, File file, String fileName) {
		try {
			return ZipUtils.updateZipFile(new File(apkPath), file, fileName);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public boolean deleteFileApk(String apkPath, String fileName) {
		try {
			ZipUtils.removeZipFile(new File(apkPath), fileName);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public List<String> getAppFileList(String app) throws Exception {
		return exeCommand(getAppListCmd(app));
	}

	public boolean singleCrunch(String src, String dest) {
		try {
			return exeSCommand(getSingleCrunch(src, dest));
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 删除app中某些文件
	 * 
	 * @param appPath
	 * @param fileName
	 * @return
	 */
	public final boolean removeFiles(String appPath, String... fileName) {
		try {
			return exeSCommand(getRemoveFile(appPath, fileName));
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 添加app中某些文件
	 * 
	 * @param appPath
	 * @param fileName
	 * @return
	 */
	public final boolean addFiles(String appPath, String... fileName) {
		try {
			return exeSCommand(getAddFile(appPath, fileName));
		} catch (Exception e) {
			return false;
		}
	}

	private ArrayList<String> exeCommand(String... argv) throws Exception {
		Process process = null;
		try {
			process = mBuilder.command(argv).start();
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(process.getInputStream(), "GBK"));
			ArrayList<String> result = new ArrayList<String>();
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				result.add(line);
			}
			bufferedReader.close();
			return result;
		} catch (Exception e) {
			throw e;
		} finally {
			if (process != null) {
				process.destroy();
			}
		}
	}

	private boolean exeSCommand(String... argv) throws Exception {
		Process process = null;
		try {
			MStringUtils.println(argv);
			process = mBuilder.command(argv).start();
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(process.getErrorStream(), "GBK"));
			ArrayList<String> result = new ArrayList<String>();
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				result.add(line);
				result.add("\n");
			}
			bufferedReader.close();
			boolean resul = result.size() <= 0;
			if (resul == false) {
				System.err.println(result.toString());
			}
			return resul;
		} catch (Exception e) {
			throw e;
		} finally {
			if (process != null) {
				process.destroy();
			}
		}
	}

	/**
	 * 从app中删除某个文件
	 * 
	 * @param fileName
	 * @param appPath
	 * @return
	 */
	private final String[] getRemoveFile(String appPath, String... fileName) {
		String[] result = new String[fileName.length + 2];
		result[0] = "remove";
		result[1] = appPath;
		System.arraycopy(fileName, 0, result, 2, fileName.length);
		return getAppCmd(result);
	}

	/**
	 * 从app中删除某个文件
	 * 
	 * @param fileName
	 * @param appPath
	 * @return
	 */
	private final String[] getAddFile(String appPath, String... fileName) {
		String[] result = new String[fileName.length + 2];
		result[0] = "add";
		result[1] = appPath;
		System.arraycopy(fileName, 0, result, 2, fileName.length);
		return getAppCmd(result);
	}

	/**
	 * 列出apk中文件列表
	 * 
	 * @param appPath
	 * @return
	 */
	protected final String[] getAppListCmd(String appPath) {
		return getAppCmd("list", appPath);
	}

	/**
	 * dump应用中label和icon相关的信息
	 * 
	 * @param appPath
	 * @return
	 */
	protected final String[] getDumpAppLabelsCmd(String appPath) {
		return getDumpAppCmd("badging", appPath);
	}

	/**
	 * dump应用中权限相关的信息
	 * 
	 * @param appPath
	 * @return
	 */
	protected final String[] getDumpAppPermissionsCmd(String appPath) {
		return getDumpAppCmd("permissions", appPath);
	}

	/**
	 * dump应用中资源相关的信息
	 * 
	 * @param appPath
	 * @return
	 */
	protected final String[] getDumpAppResourcesCmd(String xmlFileName,
			String appPath) {
		return getDumpAppCmd("xmlstrings", appPath, xmlFileName);
	}

	/**
	 * dump应用中xmlTree相关的信息
	 * 
	 * @param appPath
	 * @return
	 */
	protected final String[] getDumpAppXmlTreeCmd(String xmlFileName,
			String appPath) {
		return getDumpAppCmd("xmltree", appPath, xmlFileName);
	}

	/**
	 * dump应用中xml相关的信息
	 * 
	 * @param appPath
	 * @return
	 */
	private final String[] getDumpAppCmd(String... argv) {
		String[] result = new String[argv.length + 1];
		result[0] = "d";
		System.arraycopy(argv, 0, result, 1, argv.length);
		return getAppCmd(result);
	}

	private String[] getSingleCrunch(String src, String dest) {
		return getAppCmd("singleCrunch", "-i", src, "-o", dest);
	}

	private final String[] getAppCmd(String... argv) {
		String[] result = new String[argv.length + 1];
		result[0] = getToolsPath("aapt");
		System.arraycopy(argv, 0, result, 1, argv.length);
		return result;
	}

	/**
	 * 返回一个apk程序的信息。
	 * 
	 * @param apkPath
	 *            apk的路径。
	 * @return apkInfo 一个Apk的信息。
	 */
	public ApkInfo getApkInfo(String apkPath) throws Exception {
		ArrayList<String> result = exeCommand(getDumpAppLabelsCmd(apkPath));
		ApkInfo apkInfo = new ApkInfo();
		for (String line : result) {
			setApkInfoProperty(apkInfo, line);
		}
		return apkInfo;
	}

	/**
	 * 通过解析AndroidManifest来返回一个apk程序的信息。
	 * 
	 * @param apkPath
	 *            apk的路径。
	 * @return apkInfo 一个Apk的信息。
	 */
	public ApkInfo getApkInfoByManifest(String apkPath) throws Exception {
		ArrayList<String> result = exeCommand(getDumpAppXmlTreeCmd(
				"AndroidManifest.xml", apkPath));
		ApkInfo apkInfo = new ApkInfo();
		for (String line : result) {
			setApkInfoProperty(apkInfo, line);
		}
		return apkInfo;
	}

	/**
	 * 设置APK的属性信息。
	 * 
	 * @param apkInfo
	 * @param source
	 */
	private void setApkInfoProperty(ApkInfo apkInfo, String source) {
		if (source.startsWith(ApkInfo.PACKAGE)) {
			splitPackageInfo(apkInfo, source);
		} else if (source.startsWith(ApkInfo.LAUNCHABLE_ACTIVITY)) {
			apkInfo.setLaunchableActivity(getPropertyInQuote(source));
		} else if (source.startsWith(ApkInfo.SDK_VERSION)) {
			apkInfo.setSdkVersion(getPropertyInQuote(source));
		} else if (source.startsWith(ApkInfo.TARGET_SDK_VERSION)) {
			apkInfo.setTargetSdkVersion(getPropertyInQuote(source));
		} else if (source.startsWith(ApkInfo.MIN_SDK_VERSION)) {
			apkInfo.setMinSdkVersion(getPropertyInQuote(source));
		} else if (source.startsWith(ApkInfo.USES_PERMISSION)) {
			apkInfo.addToUsesPermissions(getPropertyInQuote(source));
		} else if (source.startsWith(ApkInfo.APPLICATION_LABEL)) {
			apkInfo.setApplicationLable(getPropertyInQuote(source));
		} else if (source.startsWith(ApkInfo.APPLICATION_ICON)) {
			apkInfo.addToApplicationIcons(getKeyBeforeColon(source),
					getPropertyInQuote(source));
		} else if (source.startsWith(ApkInfo.APPLICATION)) {
			String[] rs = source.split("( icon=')|'");
			apkInfo.setApplicationIcon(rs[rs.length - 1]);
		} else if (source.startsWith(ApkInfo.USES_FEATURE)) {
			apkInfo.addToFeatures(getPropertyInQuote(source));
		} else if (source.startsWith(ApkInfo.USES_IMPLIED_FEATURE)) {
			apkInfo.addToImpliedFeatures(new Feature(source));
		} else if (source.startsWith(ApkInfo.SUPPORTS_ANY_DENSITY)) {
			apkInfo.setSupportsAnyDensity(Boolean
					.valueOf(getPropertyInQuote(source)));
		} else {
			System.out.println(source);
		}
	}

	/**
	 * 返回出格式为name: 'value'中的value内容。
	 * 
	 * @param source
	 * @return
	 */
	private String getPropertyInQuote(String source) {
		int index = source.indexOf("'") + 1;
		return source.substring(index, source.indexOf('\'', index));
	}

	/**
	 * 返回冒号前的属性名称
	 * 
	 * @param source
	 * @return
	 */
	private String getKeyBeforeColon(String source) {
		return source.substring(0, source.indexOf(':'));
	}

	/**
	 * 分离出包名、版本等信息。
	 * 
	 * @param apkInfo
	 * @param packageSource
	 */
	private void splitPackageInfo(ApkInfo apkInfo, String packageSource) {
		String[] packageInfo = packageSource.split(SPLIT_REGEX);
		apkInfo.setPackageName(packageInfo[2]);
		apkInfo.setVersionCode(packageInfo[4]);
		apkInfo.setVersionName(packageInfo[6]);
	}

}
