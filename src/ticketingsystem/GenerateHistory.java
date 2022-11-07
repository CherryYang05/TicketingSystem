package ticketingsystem;

import java.util.List;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;

class ThreadId {
    // Atomic integer containing the next thread ID to be assigned
    private static final AtomicInteger nextId = new AtomicInteger(0);

    // Thread local variable containing each thread's ID
    private static final ThreadLocal<Integer> threadId = new ThreadLocal<Integer>() {
        @Override
        protected Integer initialValue() {
            return nextId.getAndIncrement();
        }
    };

    // Returns the current thread's unique ID, assigning it if necessary
    public static int get() {
        return threadId.get();
    }
}

class myInt {
    volatile int value;
}

public class GenerateHistory {
    static int threadnum;		// input
    static int testnum;			// input
    static boolean isSequential;// input 第三个参数
    static int msec = 0;		// TrainConfig 中的第 4 个参数
    static int nsec = 0;		// TrainConfig 中的第 5 个参数
    static int totalPc;			// ?

    static AtomicInteger sLock = new AtomicInteger(0); // Synchronization Lock
    static boolean[] fin;		// ？

    protected static boolean exOthNotFin(int tNum, int tid) {
        boolean flag = false;
        for (int k = 0; k < tNum; k++) {
            if (k == tid)
                continue;
            flag = (flag || !(fin[k]));
        }
        return flag;
    }

    static void SLOCK_TAKE() {
        while (sLock.compareAndSet(0, 1) == false) {
        }
    }

    static void SLOCK_GIVE() {
        sLock.set(0);
    }

    static boolean SLOCK_TRY() {
        return (sLock.get() == 0);
    }

    /**************** Manually Set Testing Information **************/

    static int routenum = 3; // route is designed from 1 to 3
    static int coachnum = 3; // coach is arranged from 1 to 5
    static int seatnum = 5; // seat is allocated from 1 to 20
    static int stationnum = 5; // station is designed from 1 to 5

    static int refRatio = 10;
    static int buyRatio = 20;
    static int inqRatio = 30;

    static TicketingDS tds;
    final static List<String> methodList = new ArrayList<String>();
    // 存放三种方法所占的比例
    final static List<Integer> freqList = new ArrayList<Integer>();

    // 当前已有的车票
    final static List<Ticket> currentTicket = new ArrayList<Ticket>();

    // 当前方法执行的结果（true/false）
    final static List<String> currentRes = new ArrayList<String>();

    // 已经售出的车票，用于查询退票
    final static ArrayList<List<Ticket>> soldTicket = new ArrayList<List<Ticket>>();

    // 初始化锁，要保证初始化成功
    volatile static boolean initLock = false;
    // final static AtomicInteger tidGen = new AtomicInteger(0);
    final static Random rand = new Random();

    public static void initialization() {
        tds = new TicketingDS(routenum, coachnum, seatnum, stationnum, threadnum);
        for (int i = 0; i < threadnum; i++) {
            List<Ticket> threadTickets = new ArrayList<Ticket>();
            soldTicket.add(threadTickets);
            currentTicket.add(null);
            currentRes.add("");
        }
        // method freq is up to
        methodList.add("refundTicket");
        freqList.add(refRatio);
        methodList.add("buyTicket");
        freqList.add(refRatio + buyRatio);
        methodList.add("inquiry");
        freqList.add(refRatio + buyRatio + inqRatio);
        totalPc = refRatio + buyRatio + inqRatio;
    }

    public static String getPassengerName() {
        long uid = rand.nextInt(testnum);
        return "passenger" + uid;
    }

    // 读配置文件（车次总数、每个列车的车厢总数、车厢座位数、经停站数以及三种业务的比例）
    private static boolean readConfig(String filename) {
        try {
            Scanner scanner = new Scanner(new File(filename));

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                // System.out.println(line);
                Scanner linescanner = new Scanner(line);
                if (line.equals("")) {
                    linescanner.close();
                    continue;
                }
                if (line.substring(0, 1).equals("#")) {
                    linescanner.close();
                    continue;
                }
                routenum = linescanner.nextInt();
                coachnum = linescanner.nextInt();
                seatnum = linescanner.nextInt();
                stationnum = linescanner.nextInt();

                refRatio = linescanner.nextInt();
                buyRatio = linescanner.nextInt();
                inqRatio = linescanner.nextInt();
                // System.out.println("route: " + routenum + ", coach: " + coachnum + ",
                // seatnum: " + seatnum + ", station: " + stationnum + ", refundRatio: " +
                // refRatio + ", buyRatio: " + buyRatio + ", inquiryRatio: " + inqRatio);
                linescanner.close();
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println(e);
        }
        return true;
    }

    /**
     * history 输出格式为 执行前时间戳，执行后时间戳，进程号，执行业务名称，车票id，乘车人姓名，车次号，车厢号，出发地，目的地，座位号，该业务执行成功与否
     * @param preTime
     * @param postTime
     * @param actionName
     */
    public static void print(long preTime, long postTime, String actionName) {
        Ticket ticket = currentTicket.get(ThreadId.get());
        System.out.println(preTime + " " + postTime + " " + ThreadId.get() + " " + actionName + " " + ticket.tid + " "
                + ticket.passenger + " " + ticket.route + " " + ticket.coach + " " + ticket.departure + " "
                + ticket.arrival + " " + ticket.seat + " " + currentRes.get(ThreadId.get()));
    }


    /**
     * 处理三种业务逻辑：购票，查询和退票
     * @param num 退票为0，购票为1，查询为2
     * @return
     */
    public static boolean execute(int num) {
        int route, departure, arrival;
        Ticket ticket = new Ticket();

        switch (num) {
            case 0:// refund
                if (soldTicket.get(ThreadId.get()).size() == 0)
                    return false;
                int n = rand.nextInt(soldTicket.get(ThreadId.get()).size());
                ticket = soldTicket.get(ThreadId.get()).remove(n);
                if (ticket == null) {
                    return false;
                }
                currentTicket.set(ThreadId.get(), ticket);
                boolean flag = tds.refundTicket(ticket);
                currentRes.set(ThreadId.get(), "true");
                return flag;
            case 1:// buy
                String passenger = getPassengerName();
                route = rand.nextInt(routenum) + 1;
                departure = rand.nextInt(stationnum - 1) + 1;
                arrival = departure + rand.nextInt(stationnum - departure) + 1;
                ticket = tds.buyTicket(passenger, route, departure, arrival);
                if (ticket == null) {
                    ticket = new Ticket();
                    ticket.passenger = passenger;
                    ticket.route = route;
                    ticket.departure = departure;
                    ticket.arrival = arrival;
                    ticket.seat = 0;
                    currentTicket.set(ThreadId.get(), ticket);
                    currentRes.set(ThreadId.get(), "false");
                    return true;
                }
                currentTicket.set(ThreadId.get(), ticket);
                currentRes.set(ThreadId.get(), "true");
                soldTicket.get(ThreadId.get()).add(ticket);
                return true;
            case 2:// inquiry
                ticket.passenger = getPassengerName();
                // 随机获得一个车次
                ticket.route = rand.nextInt(routenum) + 1;
                // 随机获得一个出发地
                ticket.departure = rand.nextInt(stationnum - 1) + 1;
                // 随机获得一个目的地（目的地应该比出发地编号大）
                ticket.arrival = ticket.departure + rand.nextInt(stationnum - ticket.departure) + 1; 

                /*
                    通过 TicketDS 的 inquiry 方法查询余票数
                 */
                ticket.seat = tds.inquiry(ticket.route, ticket.departure, ticket.arrival);

                currentTicket.set(ThreadId.get(), ticket);
                currentRes.set(ThreadId.get(), "true");
                return true;
            default:
                System.out.println("Error in execution.");
                return false;
        }
    }

    /*********** VeriLin ***********/
    public static void main(String[] args) throws InterruptedException {
        // if (args.length != 5) {
        // 	System.out.println(
        // 			"The arguments of GenerateHistory is threadNum,  testNum, isSequential(0/1), delay(millionsec), delay(nanosec)");
        // 	return;
        // }
        // threadnum = Integer.parseInt(args[0]);
        // testnum = Integer.parseInt(args[1]);
        // if (args[2].equals("0")) {
        // 	isSequential = false;
        // } else if (args[2].equals("1")) {
        // 	isSequential = true;
        // } else {
        // 	System.out.println("The arguments of GenerateHistory is threadNum,  testNum, isSequential(0/1)");
        // 	return;
        // }
        // msec = Integer.parseInt(args[3]);
        // nsec = Integer.parseInt(args[4]);

        threadnum = 3;
        testnum = 100;
        isSequential = true;
        msec = 0;
        nsec = 0;

        readConfig("TrainConfig");
        Thread[] threads = new Thread[threadnum];
        // ?
        myInt barrier = new myInt();
        fin = new boolean[threadnum];
        final long startTime = System.nanoTime();

        // START
        for (int i = 0; i < threadnum; i++) {
            threads[i] = new Thread(new Runnable() {
                public void run() {
                    // 第一个线程执行需要初始化
                    if (ThreadId.get() == 0) {
                        initialization();
                        initLock = true;
                    } else {
                        while (!initLock) {
                            ;
                        }
                    }
                    for (int k = 0; k < testnum; k++) {
                        int sel = rand.nextInt(totalPc);		// ？
                        int cnt = 0;							// ？
                        // 如果串行执行
                        if (isSequential) {
                            while (ThreadId.get() != barrier.value && exOthNotFin(threadnum, ThreadId.get()) == true) {
                            }
                            SLOCK_TAKE();
                        }
                        // 依次处理业务，这里是三种
                        for (int j = 0; j < methodList.size(); j++) {
                            /*
                             * 按照配置文件中的三种业务的比例进行处理 ？？？
                             */
                            if (sel >= cnt && sel < cnt + freqList.get(j)) {
                                if (msec != 0 || nsec != 0) {
                                    try {
                                        Thread.sleep(msec, nsec);
                                    } catch (InterruptedException e) {
                                        return;
                                    }
                                }
                                long preTime = System.nanoTime() - startTime;

                                /* 开始执行业务逻辑 */ 
                                boolean flag = execute(j);

                                long postTime = System.nanoTime() - startTime;
                                if (flag) {
                                    print(preTime, postTime, methodList.get(j));
                                }
                                cnt += freqList.get(j);
                            }
                        }

                        if (isSequential) {
                            if (k == testnum - 1)
                                fin[ThreadId.get()] = true;
                            if (exOthNotFin(threadnum, ThreadId.get()) == true) {
                                barrier.value = rand.nextInt(threadnum);
                                while (fin[barrier.value] == true) {
                                    barrier.value = rand.nextInt(threadnum);
                                }
                            }
                            SLOCK_GIVE();
                        }
                    }

                }
            });
            threads[i].start();
        }

        for (int i = 0; i < threadnum; i++) {
            threads[i].join();
        }
    }
}
