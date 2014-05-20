package alios.app.util.apk;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MStringUtils {

	public static String getFormatSize(double size) {
		double kiloByte = size / 1024.0D;
		if (kiloByte < 1.0D) {
			return size + " B"; // Byte(s)
		}

		double megaByte = kiloByte / 1024.0D;
		if (megaByte < 1.0D) {
			BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
			return result1.setScale(2, 4).toPlainString() + " KB";
		}

		double gigaByte = megaByte / 1024.0D;
		if (gigaByte < 1.0D) {
			BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
			return result2.setScale(2, 4).toPlainString() + " MB";
		}

		double teraBytes = gigaByte / 1024.0D;
		if (teraBytes < 1.0D) {
			BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
			return result3.setScale(2, 4).toPlainString() + " GB";
		}
		BigDecimal result4 = new BigDecimal(teraBytes);
		return result4.setScale(2, 4).toPlainString() + " TB";
	}

	public static final boolean isEmail(String str) {
		if (isBlank(str)) {
			return false;
		}
		return str.matches("^[a-zA-Z0-9_.]+@[a-zA-Z0-9-]+[.a-zA-Z]+$");
	}

	private static boolean isBlank(String str) {
		if (str == null || str.matches("[ ]{0,}")) {
			return true;
		}
		return false;
	}

	public static final boolean isChineseMobile(String str) {
		if (isBlank(str)) {
			return false;
		}
		return str.matches("^1[3458]\\d{9}$");
	}

	public static final boolean isChinese(String str) {
		if (isBlank(str)) {
			return false;
		}
		return str.matches("[\u4e00-\u9fa5]");
	}

	public static final boolean isIconUrl(String str) {
		if (isBlank(str)) {
			return false;
		}
		return str
				.matches("((http|https|ftp|rtsp|mms):(\\/\\/|\\\\\\\\){1}((\\w)+[.]){1,}(net|com|cn|org|cc|tv|[0-9]{1,3})(\\S*\\/)((\\S)+[.]{1}(gif|jpg|png|bmp)))");
	}

	public static final boolean isRegName(String str) {
		if (isBlank(str)) {
			return false;
		}
		Pattern p = Pattern
				.compile("[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]");
		Matcher m = p.matcher(str);
		return m.find();
	}

	public static final void println(String... argvs) {
		StringBuffer mStringBuffer = new StringBuffer();
		for (String argv : argvs) {
			mStringBuffer.append(argv);
			mStringBuffer.append(" ");
		}
		System.out.println(mStringBuffer.toString());
	}

	public static final boolean isStartWithLetter(String str) {
		if (isBlank(str)) {
			return false;
		}
		return str.matches("[A-Za-z]{1}[A-Za-z0-9]{5,16}");
	}

	public static final boolean isPwd(String str) {
		if (isBlank(str)) {
			return false;
		}
		return str.matches("^[a-zA-Z]\\w{6,30}$");
	}

	public static final boolean isIdCard(String str) {
		if (isBlank(str)) {
			return false;
		}

		return str.matches("[0-9]{17}X") || str.matches("[0-9]{18}");
	}

	public static final String encodeInterfereWord(String src) {
		String base = "abcdefghijklmnopqrstuvwxyz0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		int len = base.length();
		for (int index = 0, size = src.length(); index < size; index++) {
			sb.append(base.charAt(random.nextInt(len)));
			sb.append(src.charAt(index));
		}
		sb.append(base.charAt(random.nextInt(len)));
		return sb.toString();
	}

	public static final String decodeInterfereWord(String src) {
		StringBuffer sb = new StringBuffer();
		for (int index = 0, size = src.length(); index < size; index++) {
			if (index % 2 == 0) {
				continue;
			}
			sb.append(src.charAt(index));
		}
		return sb.toString();
	}

	public static final boolean writeToFile(String s, File file) {
		try {
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			fileOutputStream.write(s.getBytes("utf-8"));
			fileOutputStream.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

}
