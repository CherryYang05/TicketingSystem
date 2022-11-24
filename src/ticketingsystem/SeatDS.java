package ticketingsystem;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

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
    private final ReadWriteLock lock = new ReentrantReadWriteLock(true); // 可重入锁
    private final Lock readLock = lock.readLock(); // 读锁
    private final Lock writeLock = lock.writeLock(); // 写锁

    // 标识这个座位有同时被几张票购买
    private int ticketOnSeat;

    public SeatDS(int stationnum) {
        this.stationnum = stationnum;
        seatList = new ArrayList<>();
        ticketOnSeat = 0;
    }

    /**
     * 根据起始站和终点站获得可用座位，购票功能
     * 
     * @param departure
     * @param arrival
     * @return
     */
    public boolean getSeatNum(long tid, int departure, int arrival) {
        // 加上写锁，同时只能有一个线程去找空座位
        // writeLock.lock();
        boolean flag = judge(departure, arrival);

        if (flag) {
            ticketOnSeat++;
            seatList.add(new Seat(tid, departure, arrival));
        }
        // writeLock.unlock();
        return flag;
    }

    public boolean inquiry(int departure, int arrival) {
        // writeLock.lock();
        try {
            return judge(departure, arrival);
        } finally {
            // writeLock.unlock();
        }
    }

    /**
     * 退票
     * 
     * @param departure
     * @param arrival
     * @return
     */
    public boolean refundTicket(Ticket ticket) {
        // 加上写锁，同时只能有一个线程去退票
        // writeLock.lock();
        try {
            boolean flag = false;
            long tid = ticket.tid;
            int departure = ticket.departure;
            int arrival = ticket.arrival;

            Seat delSeat = null;

            for (Seat s : seatList) {
                if (!s.isValid) {
                    continue;
                }
                // 从已经买出的票中找到要退的票
                if (tid == s.tid) {
                    if (departure == s.departure && arrival == s.arrival) {
                        flag = true;
                        delSeat = s;
                        s.isValid = false;
                        break;
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
        } finally {
            // writeLock.unlock();
        }
    }

    /**
     * 根据 departure 和 arrival 判断能否找到可用座位，因为在 TicketingDS 中已经判断过车票有效性，这里无需判断
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
        if (seatList.isEmpty()) {
            return true;
        }

        // if (departure >= arrival) {
        // return false;
        // }

        // if (departure < 1 || departure > stationnum || arrival < 1 || departure >
        // stationnum) {
        // return false;
        // }

        // 判定车站区间的票的可用性
        for (Seat s : seatList) {
            if ((departure < s.departure && arrival <= s.departure)
                    || (departure >= s.arrival && arrival > s.arrival)) {
                continue;
            } else {
                return false;
            }
        }
        return true;
    }

}
