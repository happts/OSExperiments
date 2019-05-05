package happts;

/**
 * * 每个进程有一个进程控制块（ happts.PCB）表示。进程控制块可以包含 如下信息：进程名、优先数、到达时间、需要运行时间、已用 CPU 时间、进程状态等等。
 进程的优先数及需要的运行时间可以事先人为地指定（也可以由随机数产生），进程 的到达时间为进程输入的时间，进程的运行时间以时间片为单位进行计算。
 每个进程的状态 可以是就绪 W（Wait）、运行 R（Run）、或完成 F（Finish）三种状态之一（这是编程用到 的三个模拟状态，并非进程的三基态）。
 */
public class PCB {
    int id ;
    int run_time;//需要的总时间
    int allo_time;//已分配过的时间
    int req_time;//还需要时间

    String name;

    public PCB(int id,int run_time){
        this.id = id;
        this.run_time = run_time;
        this.allo_time = 0;
        this.req_time = run_time;
    }
}
