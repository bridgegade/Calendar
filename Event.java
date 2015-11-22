package calendar;


import java.util.Calendar;
import java.util.Comparator;

//make this an events per day
/**
 * Holds the event's title, month, day, year, start time, and end time.
 * @author anthony
 *
 */
public class Event
{	String title;
	int month;
	int day;
	int year;
	Calendar startTime;
	Calendar endTime;
	public Event(){
		
	}
	
	public Event(String t, int m, int d, int y, Calendar sT, Calendar eT)
	{	title = t;
		month = m;
		day = d;
		year = y;
		startTime =  sT;
		endTime = eT;
		
		
	}

	
	public Comparator<Event> createComparator()
	{
		return new Comparator<Event>()
		{

			@Override
			public int compare(Event a, Event b)
			{
				// TODO Auto-generated method stub
				if (a.year != b.year)
				{
					return a.year - b.year;
				}
				else if(a.month!= b.month){
					return a.month-b.month;
				}
				else if(a.day != b.day){
					return a.day-b.day;
				}
				else if(a.startTime.before(b.startTime)){
					return -1;
				}
				else if(a.startTime.after(b.startTime)){
					return 1;
				}
				
				return 0;
			}
		};
	}
}
