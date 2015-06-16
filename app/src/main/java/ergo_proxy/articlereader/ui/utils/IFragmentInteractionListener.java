package ergo_proxy.articlereader.ui.utils;

import android.support.v4.app.Fragment;

/**
 * Created by ergo_proxy on 12.06.15.
 */

public interface IFragmentInteractionListener {
	void onRegister(Fragment fragment);
	void onUnregister(Fragment fragment);
}
