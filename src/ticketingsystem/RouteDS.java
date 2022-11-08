package ticketingsystem;

import java.util.Arrays;
import java.util.Random;

/**
 * 管理某趟列车的每一个车厢
 */
public class RouteDS {

    private int coachnum;
    private int seatnum;
    private int stationnum;

    // 每一节车厢中还有多少座位没有售出（车厢号从 1 开始）
    private int[] seatLeftInCoach;

    // 存放所有车厢的信息
    public CoachDS[] cDS;

    /*
     * 一趟列车的车厢数
     */
    RouteDS(int coachnum, int seatnum, int stationnum) {
        this.coachnum = coachnum;
        this.seatnum = seatnum;
        this.stationnum = stationnum;
        seatLeftInCoach = new int[coachnum + 1];
        Arrays.fill(seatLeftInCoach, seatnum);

        cDS = new CoachDS[coachnum + 1];
        for (int i = 1; i <= coachnum; i++) {
            cDS[i] = new CoachDS(seatnum, stationnum);
        }
    }

    /**
     * 给一个列车号，随机获得一个可分配的车厢号，若无可用返回 0
     * 
     * @param route
     * @return
     */
    public int getCoachNum(int departure, int arrival) {
        for (int i = 1; i <= coachnum; i++) {
            if (seatLeftInCoach[i] > 0 && seatLeftInCoach[i] <= seatnum) {
                seatLeftInCoach[i]--;
                return i;
            }
        }
        return 0;
    }

    /**
     * 
     * @return 调用 CoachDS 中的 inquiry 查询所有空余座位
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
     * @param coach
     * @param departure
     * @param arrival
     * @return
     */
    public boolean refundTicket(int coach, int seat, int departure, int arrival) {
        if (seatLeftInCoach[coach] > 0 && seatLeftInCoach[coach] < seatnum) {
            seatLeftInCoach[coach]--;
        }
        return cDS[coach].refundTicket(seat, departure, arrival);
    }


    public int[] getSeatLeftInCoach() {
        return seatLeftInCoach;
    }
}
