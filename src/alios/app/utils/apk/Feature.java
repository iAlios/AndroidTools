package alios.app.utils.apk;

public class Feature {

	private static final String FEATURE_SPLIT_REGEX = "(:')|(',')|'";

	/**
	 * 要的设备特性名称。
	 */
	private String feature;

	/**
	 * 表明所需特性的内容。
	 */
	private String requestedPermissions;

	public Feature() {
		super();
	}

	public Feature(String feature) {
		super();
		String[] result = feature.split(FEATURE_SPLIT_REGEX);
		this.feature = result[1];
		this.requestedPermissions = result[2];
	}

	public String getFeature() {
		return feature;
	}

	public String getRequestedPermissions() {
		return requestedPermissions;
	}

	@Override
	public String toString() {
		return "Feature [feature=" + feature + ", requestedPermissions="
				+ requestedPermissions + "]";
	}
}
