import java.util.Set;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
/**
 * Write a description of class item here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class item
{
    // instance variables - replace the example below with your own   
    private HashMap<String, item> Item;
    private String itemDescription;
    
    List<String> inv = new ArrayList<String>();
    
    /**
     * Constructor for objects of class item
     */
    public void Item(String itemDescription)
    {
        this.itemDescription = itemDescription;
        Item = new HashMap<>();
    }
    
    /**
     * @param itemDescription
     */
    public String itemRooms(String itemDescription)
    {
    this.itemDescription = itemDescription;
    return itemDescription;
    }
    
    public void setItem(String item, item itemFunction) 
    {
        Item.put(item, itemFunction);
    }
}
