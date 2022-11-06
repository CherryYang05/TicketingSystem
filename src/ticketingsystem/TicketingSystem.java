/*
 * @Author         : Cherry
 * @Date           : 2022-11-03 08:33:27
 * @LastEditors    : Cherry
 * @LastEditTime   : 2022-11-06 18:42:58
 * @FilePath       : TicketingSystem.java
 * @Description    : TicketSystem 接口，TicketingDS 类需要实现该接口
 */
package ticketingsystem;

import java.util.concurrent.atomic.AtomicLong;

class Ticket {
    long tid;
    String passenger;
    int route;
    int coach;
    int seat;
    int departure;
    int arrival;

    @Override
	public String toString() {
		String res = "";
        res = "tid = " + tid
            + "\npassenger = " + passenger
            + "\nroute = " + route
            + "\ncoach = " + coach
            + "\nseat = " + seat
            + "\ndeparture = " + departure
            + "\narrival = " + arrival + "\n";

        return res;
	}
}

public interface TicketingSystem {

    /**
     * @description: 买票
     * @param {String} passenger 乘客姓名
     * @param {int}    route 列车车次
     * @param {int}    departure 出发站
     * @param {int}    arrival 终到站
     * @return {Ticket} 返回：Ticket 结构体
     */
    Ticket buyTicket(String passenger, int route, int departure, int arrival);

    /**
     * @description: 查询余票
     * @param {int} route 列车车次
     * @param {int} departure 出发站
     * @param {int} arrival 终到站
     * @return {int} 返回：余票数
     */
    int inquiry(int route, int departure, int arrival);

    /**
     * @description: 退票
     * @param {Ticket} ticket 有效 Ticket
     * @return {boolean} 返回：是否成功退票
     */
    boolean refundTicket(Ticket ticket);

    /**
     * @description:
     * @param {Ticket} ticket
     * @return {boolean}
     */
    boolean buyTicketReplay(Ticket ticket);

    /**
     * @description:
     * @param {Ticket} ticket
     * @return {boolean}
     */
    boolean refundTicketReplay(Ticket ticket);
}
