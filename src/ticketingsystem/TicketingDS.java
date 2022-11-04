package ticketingsystem;

public class TicketingDS implements TicketingSystem {

	public int routenum;
	public int coachnum;
	public int seatnum;
	public int stationnum;
	public int threadnum;

	public TicketingDS(int routenum, int coachnum, int seatnum, int stationnum, int threadnum) {
		this.routenum = routenum;
		this.coachnum = coachnum;
		this.seatnum = seatnum;
		this.stationnum = stationnum;
		this.threadnum = threadnum;
	}

	@Override
	public Ticket buyTicket(String passenger, int route, int departure, int arrival) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int inquiry(int route, int departure, int arrival) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean refundTicket(Ticket ticket) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean buyTicketReplay(Ticket ticket) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean refundTicketReplay(Ticket ticket) {
		// TODO Auto-generated method stub
		return false;
	}

	// ToDo

}
