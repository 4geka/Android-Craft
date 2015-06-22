package ergo_proxy.example_four.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Spinner;

import ergo_proxy.example_four.R;
import ergo_proxy.example_four.adapters.ListAdapter;
import ergo_proxy.example_four.adapters.ListExAdapter;
import ergo_proxy.example_four.adapters.PagerAdapter;
import ergo_proxy.example_four.func.Collection;
import ergo_proxy.example_four.utils.IActtivityInteractionListener;
import ergo_proxy.example_four.utils.IFragmentInteractionListener;


public class MainActivity extends FragmentActivity
		implements IActtivityInteractionListener, IFragmentInteractionListener, AdapterView.OnItemSelectedListener, ViewPager.OnPageChangeListener, CompoundButton.OnCheckedChangeListener
{

	private ViewPager viewPager;
	private PagerAdapter pagerAdapter;
	private ListView listView;
	private boolean	isFavoriteChecked;
	private ExpandableListView expandableListView;
	private ListAdapter listAdapter;
	private ListExAdapter listExAdapter;
	private Collection collection;
	private Spinner	spinner;
	private EditText filterEdit;


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);
		collection = new Collection();
		viewPager = (ViewPager) findViewById(R.id.activity_viewpager);
		pagerAdapter = new PagerAdapter(getSupportFragmentManager(), collection);
		viewPager.setAdapter(pagerAdapter);
		viewPager.addOnPageChangeListener(this);
		listAdapter = new ListAdapter(this, collection);
		listExAdapter = new ListExAdapter(this, collection);
		listView = (ListView) findViewById(R.id.activity_listview);
		expandableListView = (ExpandableListView) findViewById(R.id.activity_exlistview);
		listView.setAdapter(listAdapter);
		expandableListView.setAdapter(listExAdapter);
		listView.setTextFilterEnabled(true);
		expandableListView.setTextFilterEnabled(true);

		((CheckBox)findViewById(R.id.activity_checkbox)).setOnCheckedChangeListener(this);
		spinner	= (Spinner) findViewById(R.id.activity_list_spinner);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new String[] {getString(R.string.listview_caption), getString(R.string.listviewex_caption) });

		filterEdit = (EditText)findViewById(R.id.activity_filter_edit);
		filterEdit.addTextChangedListener(new TextWatcher()
		{

			public void afterTextChanged(Editable s)
			{

			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after)
			{

			}

			public void onTextChanged(CharSequence s, int start, int before, int count)
			{
				listAdapter.getFilter().filter(s.toString(), isFavoriteChecked);
			}
		});

		spinner.setAdapter(adapter);
		spinner.setSelection(0);
		spinner.setOnItemSelectedListener(this);
		initData();

	}

	private void onDataReceived()
	{
		listAdapter.notifyDataSetChanged();
		listExAdapter.notifyDataSetChanged();
		pagerAdapter.notifyDataSetChanged();
	}

	private void setListSelection(int position)
	{
		listView.setSelection( position);
		expandableListView.setSelection(position);
	}

	@Override
	public void onSelectedItemChanged(int position)
	{
		setListSelection(position);
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
	{
		viewPager.setCurrentItem(position);
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent)
	{

	}

	@Override
	public void onPageScrolled(int i, float v, int i1)
	{

	}

	@Override
	public void onPageSelected(int i)
	{
		setListSelection(i);
	}

	@Override
	public void onPageScrollStateChanged(int i)
	{

	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
	{
		isFavoriteChecked = isChecked;
	}

	@Override
	public void onRegister(Fragment fragment)
	{

	}

	@Override
	public void onUnregister(Fragment fragment)
	{

	}

	private void initData()
	{
		String[] titles = getResources().getStringArray(R.array.titles);
		String[] groups = getResources().getStringArray(R.array.groups);
		String[] urls = getResources().getStringArray(R.array.urls);

		for(int i=0; i< titles.length; i++)
		{
			collection.addItem(i,groups[i],titles[i], urls[i], -1);
		}
		onDataReceived();
	}
}
