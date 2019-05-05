package happts;

/**
 * 进程调度算法：采用最高优先数优先的调度算法（即把处理机分配给优先数最高的进 程）和先来先服务算法。
 就绪进程获得 CPU 后都只能运行一个时间片，用 已占用 CPU 时间加 1 来表示。
 如果运行一个时间片后，进程的已占用 CPU 时间已达到所需 要的运行时间，则撤消该进程，如果运行一个时间片后进程的已占用 CPU 时间还未达所需要的运行时间，也就是进程还需要继续运行，此时应将进程的优先数减 1（即降低一级），然 后把它插入就绪队列等待 CPU。
 每进行一次调度程序都打印一次运行进程、就绪队列、以及各个进程的 happts.PCB，以便进行 检查。重复以上过程，直到所要进程都完成为止。
 */
public class Main {

    public static void main(String[] args) throws Exception {
//        RR.add_queue(new PCB(1,20,"one"));
//        RR.add_queue(new PCB(2,5,"two"));
//        RR.add_queue(new PCB(3,15,"three"));
//        RR.add_queue(new PCB(4,3,"four"));
//        RR.circleTime();

//        Memory m1 = new Memory();
//        m1.showZones();
//        m1.allocation(12);
//        m1.showZones();
//        m1.collection(0);
//        m1.showZones();

//
//        Bankclass bank = new Bankclass();
//        bank.init(3,5);
//        System.out.println(bank.setInitValue());
//        System.out.println(bank.BankerAlgorithm(1,new int[]{1,0,2}));
//        System.out.println(bank.BankerAlgorithm(4,new int[]{3,3,0}));
//        System.out.println(bank.BankerAlgorithm(0,new int[]{0,2,0}));

//        FileOS fileOS = new FileOS();
//        fileOS.createUser("one");

        /////////////////////////////
//        Job.add_queue(new PCB(1,Job.getTime(),5,3));
//        Job.add_queue(new PCB(2,Job.getTime(),3,1));
//        Job.add_queue(new PCB(3,Job.getTime(),8,11));
//        Job.add_queue(new PCB(4,Job.getTime(),4,6));
//
//        Job.testCircle();

//        Job.add_queue(new PCB(1, Job.getTime(),7));
//        Job.add_queue(new PCB(2, Job.getTime(),13));
//        Job.add_queue(new PCB(3, Job.getTime(),8));
//        Job.add_queue(new PCB(4, Job.getTime(),4));
//        Job.HRRN();
    }
}
