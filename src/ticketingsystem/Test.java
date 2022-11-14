package ticketingsystem;

import java.util.Random;

public class Test {

    public static int routenum = 1;
    public static int coachnum = 3;
    public static int seatnum = 10;
    public static int stationnum = 5;
    public static int threadnum = 1;

    final static TicketingDS tds = new TicketingDS(routenum, coachnum, seatnum, stationnum, threadnum);

    public static void main(String[] args) throws InterruptedException {
        testBuyTicket("passenger01", 1, 3);
        testBuyTicket("passenger01", 2, 3);
        testBuyTicket("passenger01", 3, 4);
        testInquiry(1, 3, 4);
        // testInquiry(1, 1, 5);
        // testInquiry(1, 4, 5);
        // testRefundTicket();
    }

    /**
     * 测试查询车票功能
     */
    public static void testInquiry(int route, int departure, int arrival) {
        // Ticket ticket = new Ticket();
        // ticket.tid =
        int res = tds.inquiry(route, departure, arrival);
        System.out.println(res);
        // System.out.println("inquiry: " + " " + ticket.tid + " "
        // + ticket.passenger + " " + ticket.route + " " + ticket.coach + " " +
        // ticket.departure + " "
        // + ticket.arrival + " " + ticket.seat + " " + currentRes.get(ThreadId.get()));
    }

    /**
     * 测试购买车票功能
     */
    public static void testBuyTicket(String passenger, int departure, int arrival) {
        Ticket ticket = tds.buyTicket(passenger, 1, departure, arrival);
        System.out.println(ticket.toString());
    }

    /**
     * 测试退票功能
     */
    public static void testRefundTicket() {

    }

}
