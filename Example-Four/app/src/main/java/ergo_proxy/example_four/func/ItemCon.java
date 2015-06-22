package ergo_proxy.example_four.func;

/**
 * Created by ergo_proxy on 12.06.15.
 */

public class ItemCon
{
	private int id;
	private boolean isFavorite;
	Group group;
	private String title;
	private String url;
	private int	imageId;

	public ItemCon(int id, Group group, String title, String url, int imageId)
	{
		this.id	= id;
		this.title = title;
		this.url = url;
		this.imageId = imageId;
		this.group	= group;
	}

	public void setGroup(Group group)
	{
		this.group = group;
	}

	public void setIsFavorite(boolean isFavorite)
	{
		this.isFavorite = isFavorite;
	}

	public boolean isFavorite()
	{
		return isFavorite;
	}

	public String getTitle()
	{
		return title;
	}

	public String getUrl()
	{
		return url;
	}

	public int getImageId()
	{
		return imageId;
	}

	public Group getGroup()
	{
		return group;
	}

	public int getId()
	{
		return id;
	}
}
