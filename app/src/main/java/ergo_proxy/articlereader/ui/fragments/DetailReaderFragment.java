package ergo_proxy.articlereader.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import ergo_proxy.articlereader.R;
import ergo_proxy.articlereader.ui.utils.IFragmentInteractionListener;

/**
 * Created by ergo_proxy on 12.06.15.
 */

public class DetailReaderFragment extends Fragment
{

	private IFragmentInteractionListener mListener;

	private int imageId;
	private boolean isFavorite;

	private static final String TITLE_KEY = "title_key";

	private TextView tvTitle;
	private ImageView ivImage;
	private WebView wvDescription;

	public static DetailReaderFragment newInstance(String title, String url, int imageId, boolean isFavorite)
	{
		DetailReaderFragment fragment = new DetailReaderFragment();
		Bundle args = new Bundle();
		args.putString(TITLE_KEY,title);
		args.putString(TITLE_KEY,url);
		fragment.setArguments(args);
		fragment.setImageId(imageId);
		fragment.setFavorite(isFavorite);
		return fragment;
	}

	public DetailReaderFragment()
	{

	}

	public void setImageId(int imageId)
	{
		this.imageId = imageId;
	}

	public void setFavorite(boolean isFavorite)
	{
		this.isFavorite	= isFavorite;
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_details, container, false);

		String title = getArguments().getString(TITLE_KEY);
		String url = getArguments().getString(TITLE_KEY);

		tvTitle	= (TextView) view.findViewById(R.id.fragment_description_textview);
		ivImage	= (ImageView) view.findViewById(R.id.fragment_description_imageview);
		wvDescription = (WebView) view.findViewById(R.id.fragment_descritpion_webview);
		tvTitle.setText(title);

		if(imageId >=0)
		{
			ivImage.setImageResource(imageId);
		}
		if(isFavorite)
		{

		}
		wvDescription.loadUrl(url);

		return view;
	}

	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		if(activity instanceof IFragmentInteractionListener)
		{
			mListener = (IFragmentInteractionListener) activity;
			mListener.onRegister(this);
		} else
		{
			throw new ClassCastException(activity.toString() + " must implement OnFragmentInteractionListener");
		}
	}

	@Override
	public void onDetach()
	{
		super.onDetach();
		mListener.onUnregister(this);
		mListener = null;
	}

}
