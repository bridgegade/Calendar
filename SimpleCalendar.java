package calendar;
/**
 * 
 * @author Anthony Vo
 * Main class, begins the Calendar console simulation
 * based on console input
 */
public class SimpleCalendar
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		
		MyCalendar c = new MyCalendar();
		Controller con = new Controller();
		Viewer v = new Viewer(c,con);
		c.attach(v);
		
			
		
	}

}
