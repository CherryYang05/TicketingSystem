package ticketingsystem;

public class Test extends Object {

	public static int routenum = 5;
	public static int coachnum = 8;
	public static int seatnum = 100;
	public static int stationnum = 10;
	public static int threadnum = 16;

	public static void main(String[] args) throws InterruptedException {
        
		final TicketingDS tds = new TicketingDS(routenum, coachnum, seatnum, stationnum, threadnum);
		int res = tds.inquiry(1, 1, 3);
		System.out.println(res);

		for (int i = 0; i < 10; i++) {
			Ticket ticket = tds.buyTicket("Cherry", 1, 1, 3);
			System.out.println(ticket.toString());
		}
	}
}
