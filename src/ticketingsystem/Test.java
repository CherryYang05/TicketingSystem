package ticketingsystem;

public class Test {

	public static int routenum = 5;
	public static int coachnum = 8;
	public static int seatnum = 100;
	public static int stationnum = 10;
	public static int threadnum = 16;

	public static void main(String[] args) throws InterruptedException {
        
		final TicketingDS tds = new TicketingDS(routenum, coachnum, seatnum, stationnum, threadnum);

	}
}
