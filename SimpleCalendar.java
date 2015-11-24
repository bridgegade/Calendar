package calendar;
/**
 * 
 * @author Anthony Vo
 * Main class, creates a MyCalendar object, Controller object, and a Viewer object.
 * Passes the MyCalendar object as a parameter for the constructor of Controller.
 * Passes the Controller object and the MyCalendar object as parameters for the constructor of Viewer.
 * Attaches the Viewer to the MyCalendar object.
 * 
 */
public class SimpleCalendar
{

	
	public static void main(String[] args)
	{
		
		MyCalendar c = new MyCalendar();
		Controller con = new Controller(c);
		Viewer v = new Viewer(c,con);
		c.attach(v);
		
			
		
	}

}
