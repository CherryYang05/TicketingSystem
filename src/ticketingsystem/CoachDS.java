package ticketingsystem;

import java.util.Arrays;
import java.util.HashMap;

/**
 * 管理某一个车次中那个列车的每一个车厢，管理某一个车厢的座位
 */
public class CoachDS {
    private int seatnum;
    private int stationnum;

    // 座位是否被购买(标号从 1 开始)
    private volatile boolean[] seatSold;
    
    /*
     * 一个车厢的座位数
     */
    CoachDS(int seatnum, int stationnum) {
        this.seatnum = seatnum;
        this.stationnum = stationnum;
        seatSold = new boolean[seatnum + 1];
        Arrays.fill(seatSold, false);
    }

    /**
     * 给一个车厢号，随机获得一个可用的座位号，若无可用返回 0
     * @param coach
     * @return
     */
    public int getSeatNum(int coach, int departure, int arrival) {
        for (int i = 1; i <= seatnum; i++) {
            if (!seatSold[i]) {
                seatSold[i] = true;
                return i;
            }
        }
        return 0;
    }

    public int inquiry(int departure, int arrival) {
        return seatnum;
    }
}
