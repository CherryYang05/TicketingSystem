package ticketingsystem;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicLong;

public class TicketingDS implements TicketingSystem {

    public int routenum;
    public int coachnum;
    public int seatnum;
    public int stationnum;
    public int threadnum;

    private AtomicLong ticketID; // 车票 ID，不能重复
    // 记录已经售出的车票
    private HashMap<Long, Ticket> soldTicket;

    // 存放所有车次的信息
    public RouteDS[] rDS;

    public TicketingDS(int routenum, int coachnum, int seatnum, int stationnum, int threadnum) {
        this.routenum = routenum;
        this.coachnum = coachnum;
        this.seatnum = seatnum;
        this.stationnum = stationnum;
        this.threadnum = threadnum;

        ticketID = new AtomicLong(0);

        soldTicket = new HashMap<>();

        // 初始化每个车次的车厢数组
        rDS = new RouteDS[routenum + 1];
        for (int i = 1; i <= routenum; i++) {
            rDS[i] = new RouteDS(coachnum, seatnum, stationnum);
        }
    }

    /**
     * 购买车票功能
     */
    @Override
    public Ticket buyTicket(String passenger, int route, int departure, int arrival) {
        Ticket ticket = new Ticket();

        // 填写车票信息
        ticket.tid = ticketID.incrementAndGet();
        ticket.passenger = passenger;
        ticket.arrival = arrival;
        ticket.departure = departure;
        ticket.route = route; // 车次号

        // 随机获得可用车厢号
        CoachSeat coachSeat = rDS[route].getSeatNum(ticket.tid, departure, arrival);
        // RouteDS tmp = rDS[route];
        // for (int i = 1; i <= coachnum; i++) {
        // System.out.println("Coach " + i + "'s seat left: " +
        // tmp.getSeatLeftInCoach()[i]);
        // }
        if (coachSeat == null) {
            // System.out.println("票已经卖完了");
            // RouteDS tmp = rDS[route];
            
            return null;
        }
        ticket.coach = coachSeat.coachNum;
        ticket.seat = coachSeat.seatNum;
        soldTicket.put(ticket.tid, ticket);
        return ticket;
    }

    /**
     * 查询余票功能
     */
    @Override
    public int inquiry(int route, int departure, int arrival) {
        return rDS[route].inquiry(departure, arrival);
    }

    /**
     * 退票功能
     */
    @Override
    public boolean refundTicket(Ticket ticket) {

        // 这里为细粒度锁做准备
        Long tid = ticket.tid;
        if (isTheSameTicket(tid, ticket)) {
            soldTicket.remove(tid);
            // System.out.println("tickct.coach = " + ticket.coach);
            return rDS[ticket.coach].refundTicket(ticket);
        }

        return false;
    }

    private boolean isTheSameTicket(Long tid, Ticket ticket) {
        Ticket tmp = soldTicket.get(tid);

        if (ticket.tid == tmp.tid
                && ticket.passenger.equals(tmp.passenger)       // 切记这里不能用 == 判断
                && ticket.route == tmp.route
                && ticket.coach == tmp.seat
                && ticket.departure == tmp.departure
                && ticket.arrival == tmp.arrival) {
            return true;
        }
        return false;
    }

    @Override
    public boolean buyTicketReplay(Ticket ticket) {
        return false;
    }

    @Override
    public boolean refundTicketReplay(Ticket ticket) {
        return false;
    }

}

/**
 * 存放 Coach 和 Seat 号
 */
class CoachSeat {
    int coachNum;
    int seatNum;

    public CoachSeat(int coachNum, int seatNum) {
        this.coachNum = coachNum;
        this.seatNum = seatNum;
    }
}
