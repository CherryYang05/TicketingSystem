package ticketingsystem;

import java.util.ArrayList;
import java.util.List;

/**
 * 管理每个座位，即标识每个已经售出的车票的起终点
 */
public class SeatDS {

    class Seat {
        private long tid;
        public int arrival;
        public int departure;
        public boolean isValid;

        public Seat(long tid, int departure, int arrival) {
            this.tid = tid;
            this.departure = departure;
            this.arrival = arrival;
            isValid = true;
        }
    }

    private List<Seat> seatList;
    private int stationnum;

    // 标识这个座位有同时被几张票购买
    private int ticketOnSeat;

    public SeatDS(int stationnum) {
        this.stationnum = stationnum;
        seatList = new ArrayList<>();
        ticketOnSeat = 0;
    }

    /**
     * 根据起始站和终点站获得可用座位
     * 
     * @param departure
     * @param arrival
     * @return
     */
    public boolean getSeatNum(long tid, int departure, int arrival) {
        boolean flag = judge(departure, arrival);

        if (flag) {
            ticketOnSeat++;
            seatList.add(new Seat(tid, departure, arrival));
        }
        return flag;
    }

    public boolean inquiry(int departure, int arrival) {
        boolean flag = judge(departure, arrival);
        return flag;
    }

    /**
     * 退票
     * 
     * @param departure
     * @param arrival
     * @return
     */
    public boolean refundTicket(Ticket ticket) {
        boolean flag = false;
        long tid = ticket.tid;
        int departure = ticket.departure;
        int arrival = ticket.arrival;

        Seat delSeat = new Seat(tid, departure, arrival);

        for (Seat s : seatList) {
            if (tid == s.tid) {
                if (departure == s.departure && arrival == s.arrival) {
                    flag = true;
                    delSeat = s;
                }
            }
        }

        if (flag) {
            seatList.remove(delSeat);
            if (--ticketOnSeat < 0) {
                flag = false;
            }
        }
        return flag;
    }

    /**
     * 根据 departure 和 arrival 判断能否找到可用座位
     * 
     * @param departure
     * @param arrival
     * @return
     */
    private boolean judge(int departure, int arrival) {
        // boolean flag = true;
        // List 为空，说明该座位没有票
        // if (seatList == null) {
        // return true;
        // }
        if (departure >= arrival) {
            return false;
        }

        if (departure < 1 || departure > stationnum || arrival < 1 || departure > stationnum) {
            return false;
        }

        for (Seat s : seatList) {
            if (departure >= s.departure && departure <= s.arrival) {
                return false;
            }
            if (arrival >= s.departure && arrival <= stationnum) {
                return false;
            }
        }
        return true;
    }

}
