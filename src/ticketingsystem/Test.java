package ticketingsystem;

import java.util.Random;

public class Test {

    public static int routenum = 5;
    public static int coachnum = 8;
    public static int seatnum = 100;
    public static int stationnum = 10;
    public static int threadnum = 16;

    final static TicketingDS tds = new TicketingDS(routenum, coachnum, seatnum, stationnum, threadnum);

    public static void main(String[] args) throws InterruptedException {
        // testInquiry();
        testBuyTicket();
        // testRefundTicket();
    }

    /**
     * 测试查询车票功能
     */
    public static void testInquiry() {
        int res = tds.inquiry(1, 1, 3);
        System.out.println(res);
    }

    /**
     * 测试购买车票功能
     */
    public static void testBuyTicket() {
        Random rand = new Random();
        for (int i = 0; i < 10; i++) {
            long uid = rand.nextInt(100) + 1;
            String passenger = "passenger" + uid;
            Ticket ticket = tds.buyTicket(passenger, 1, 1, 3);
            System.out.println(ticket.toString());
        }
    }

    /**
     * 测试退票功能
     */
    public static void testRefundTicket() {

    }


}
