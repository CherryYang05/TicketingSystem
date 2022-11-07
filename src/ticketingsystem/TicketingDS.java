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
    // private HashMap<>
    
    // 存放所有车次的信息
    public RouteDS[] rDS;

    public TicketingDS(int routenum, int coachnum, int seatnum, int stationnum, int threadnum) {
        this.routenum = routenum;
        this.coachnum = coachnum;
        this.seatnum = seatnum;
        this.stationnum = stationnum;
        this.threadnum = threadnum;

        ticketID = new AtomicLong(0);

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
        ticket.route = route;               // 车次号

        // 随机获得可用车厢号
        int coachNum = rDS[route].getCoachNum(route, departure, arrival);  
        if (coachNum == 0) {
            // System.out.println("票已经卖完了");
            return null;
        }
        ticket.coach = coachNum;
        // 随机获得可用座位号
        int seatNum = rDS[route].cDS[coachNum].getSeatNum(coachNum, departure, arrival);    
        if (seatNum == 0) {
            return null;
        }
        ticket.seat = seatNum;
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
