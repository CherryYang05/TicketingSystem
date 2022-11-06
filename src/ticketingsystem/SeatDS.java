package ticketingsystem;

/**
 * 管理每个座位，即标识每个已经售出的车票的起终点
 */
public class SeatDS {
    private int stationnum;
    private int arrival;
    private int departure;

    public SeatDS(int stationnum) {
        this.stationnum = stationnum;
    }
}
