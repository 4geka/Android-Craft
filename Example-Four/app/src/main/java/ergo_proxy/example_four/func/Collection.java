package ergo_proxy.example_four.func;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ergo_proxy on 12.06.15.
 */
public class Collection
{
	private List<Group>	groups;
	private List<ItemCon> items;

	public Collection()
	{
		groups	= new ArrayList<>();
		items	= new ArrayList<>();
	}

	public List<ItemCon> getItems()
	{
		return items;
	}

	public List<Group> getGroups()
	{
		return groups;
	}

	public int getItemsCount()
	{
		return items.size();
	}

	public int getGroupsCount()
	{
		return groups.size();
	}

	public int getGroupItemsCount(int groupIndex)
	{
		Group group	= groups.get(groupIndex);
		if(group != null)
		{
			return group.getItemsCount();
		}
		return 0;
	}

	public void addItem(int id,String groupName, String title, String url, int imageId)
	{
		Group group	= findGroupByName(groupName);
		ItemCon newItem	= new ItemCon(id, group,  title,  url, imageId);
		group.addChild(newItem);
		items.add(newItem);
	}

	public void removeItem(int index)
	{
		ItemCon item	= items.get(index);
		if(item != null){
			item.getGroup().removeChild( item);
		}
		items.remove(index);
	}

	public void removeItem(int groupIndex, int itemIndex)
	{
		Group group	= groups.get(groupIndex);

		if(group != null)
		{
			ItemCon item = group.getChild(itemIndex);
			items.remove(item);
			group.removeChild(itemIndex);
		}
	}

	public Group getGroup(int index)
	{
		return groups.get(index);
	}

	public ItemCon getItem(int index)
	{
		return items.get(index);
	}

	public ItemCon getItem(int groupIndex, int childIndex)
	{
		Group group	= groups.get(groupIndex);
		if(group != null)
		{
			return	group.getChild(childIndex);
		}
		return null;
	}

	private Group findGroupByName( String groupName)
	{
		for(Group currentGroup: groups)
		{
			if( currentGroup.getTitle().equals(groupName))
			{
				return currentGroup;
			}
		}
		Group result = new Group(groupName);
		groups.add(result);
		return result;
	}
}
