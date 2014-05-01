package info.blogstack.misc;

import org.apache.tapestry5.services.AssetPathConverter;

public class AppAssetPathConverter implements AssetPathConverter {

	@Override
	public boolean isInvariant() {
		return true;
	}

	@Override
	public String convertAssetPath(String assetPath) {
		return assetPath.replaceFirst("/assets/[^/]*/[^/]*/", "/assets/");
	}
}