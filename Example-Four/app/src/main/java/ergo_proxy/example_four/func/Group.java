package ergo_proxy.example_four.func;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ergo_proxy on 12.06.15.
 */

public class Group
{
	private String title;
	private List<ItemCon> children;

	public Group(String title)
	{
		this.title	= title;
		children	= new ArrayList<>();
	}

	public int getItemsCount()
	{
		return children.size();
	}

	public void addChild(ItemCon item)
	{
		children.add(item);
	}

	public void removeChild(int childPos)
	{
		children.remove(childPos);
	}

	public void removeChild(ItemCon child)
	{
		children.remove(child);
	}

	public ItemCon getChild(int position)
	{
		return children.get(position);
	}

	public String getTitle()
	{
		return  title;
	}
}
