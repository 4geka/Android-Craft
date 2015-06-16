package ergo_proxy.articlereader.ui.fragments;

import android.content.res.Resources;

/**
 * Created by ergo_proxy on 12.06.15.
 */

public class Utils
{
	public static boolean isLandscape(Resources resource)
	{
		return resource.getConfiguration().orientation == 2;
	}
}
