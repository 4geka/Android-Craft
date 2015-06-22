package ergo_proxy.exampletwo.img;

/**
 * Created by Ergo-Proxy on 01.06.2015.
 */
import java.io.Serializable;

public class Made implements Serializable
{
    private String  firstFragment;
    private String secondFragment;

    public Made(String firstFragment,String secondFragment)
    {
        this.firstFragment=firstFragment;
        this.secondFragment=secondFragment;
    }

    public Made()
    {

    }

    public String getFirstFragment()
    {
        return firstFragment;
    }

    public void setFirstFragment(String firstFragment)
    {
        this.firstFragment = firstFragment;
    }

    public String getSecondFragment()
    {
        return secondFragment;
    }

    public void setSecondFragment(String secondFragment) {
        this.secondFragment = secondFragment;
    }
}
