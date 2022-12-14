package ticketingsystem;

/**
 * 管理某趟列车的每一个车厢
 */
public class RouteDS {

    private int coachnum;
    // private int seatnum;
    // private int stationnum;

    // 每一节车厢中还有多少座位没有售出（车厢号从 1 开始）
    // private int[] seatLeftInCoach;

    // 存放所有车厢的信息
    public CoachDS[] cDS;

    /*
     * 一趟列车的车厢数
     */
    RouteDS(int coachnum, int seatnum, int stationnum) {
        this.coachnum = coachnum;
        // this.seatnum = seatnum;
        // this.stationnum = stationnum;
        // seatLeftInCoach = new int[coachnum + 1];
        // Arrays.fill(seatLeftInCoach, seatnum);

        cDS = new CoachDS[coachnum + 1];
        for (int i = 1; i <= coachnum; i++) {
            cDS[i] = new CoachDS(seatnum, stationnum);
        }
    }

    /**
     * 给一个列车号，随机获得一个可分配的车厢号，若无可用返回 0
     * @param tid
     * @param departure
     * @param arrival
     * @return
     */
    public CoachSeat getSeatNum(long tid, int departure, int arrival) {
        int seatNum = 0;
        for (int i = 1; i <= coachnum; i++) {
            seatNum = cDS[i].getSeatNum(tid, departure, arrival);
            if (seatNum != 0) {
                return new CoachSeat(i, seatNum);
            }
        }
        return null;
    }

    /**
     * 调用 CoachDS 中的 inquiry 查询所有空余座位
     * @param departure
     * @param arrival
     * @return
     */
    public int inquiry(int departure, int arrival) {
        int totalSeatAvailable = 0;
        for (int i = 1; i <= coachnum; i++) {
            totalSeatAvailable += cDS[i].inquiry(departure, arrival);
        }
        return totalSeatAvailable;
    }


    /**
     * 退票，将相应车厢的座位置空
     * @param tid
     * @param coach
     * @param seat
     * @param departure
     * @param arrival
     * @return
     */
    public boolean refundTicket(Ticket ticket) {
        return cDS[ticket.coach].refundTicket(ticket);
    }

}
