package alios.app.util.apk;

public class Version {

	public static String getVersion() {
		return String.format("%d.%d.%d", getMajorVersion(), getMinorVersion(), getRevisionNumber());
	}

	public static int getMajorVersion() {
		return 1;
	}

	public static int getMinorVersion() {
		return 1;
	}

	public static int getRevisionNumber() {
		return 0;
	}
}
