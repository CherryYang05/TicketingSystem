package ticketingsystem;

/**
 * 管理某一个车次中那个列车的每一个车厢，管理某一个车厢的所有座位
 */
public class CoachDS {
    private int seatnum;

    // 记录每个座位的购票情况，一个座位可能同时存在几张票
    private volatile SeatDS[] sDS;
    
    /*
     * 一个车厢的座位数
     */
    CoachDS(int seatnum, int stationnum) {
        this.seatnum = seatnum;
        sDS = new SeatDS[seatnum + 1];
        for (int i = 1; i <= seatnum; i++ ) {
            sDS[i] = new SeatDS(stationnum);
        }
    }

    /**
     * 给一个车厢号，随机获得一个可用的座位号，若无可用返回 0
     * @param tid
     * @param departure
     * @param arrival
     * @return
     */
    public int getSeatNum(long tid, int departure, int arrival) {
        for (int i = 1; i <= seatnum; i++) {
            // 购票原则：按照已经售出的座位优先购买
            if (sDS[i].getSeatNum(tid, departure, arrival)) {
                return i;
            }            
        }
        return 0;
    }

    /**
     * 退票，将对应的座位置空
     * @param tid
     * @param seat
     * @param departure
     * @param arrival
     * @return
     */
    public boolean refundTicket(Ticket ticket) {
        return sDS[ticket.seat].refundTicket(ticket);
    }

    public int inquiry(int departure, int arrival) {
        int seatAvailable = 0;
        for (int i = 1; i <= seatnum; i++) {
            
            if (sDS[i].inquiry(departure, arrival)) {
                seatAvailable++;
            }
        }
        return seatAvailable;
    }
}
