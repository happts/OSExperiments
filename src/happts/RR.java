package happts;

import java.util.LinkedList;
import java.util.Queue;

public class RR {
    static Queue<PCB> queue = new LinkedList<>();

    static int circle_size = 2;
    private static boolean state = false;
    static PCB runningPCB ;

    public static void circleTime() throws Exception{
        state = true;
        while (true){
            if(!queue.isEmpty()) {
                PCB runPCB = queue.poll();
                int reqtime =runPCB.req_time;
                System.out.println(runPCB.id+"号进程运行中:"+runPCB.name);
                if(reqtime>circle_size){
                    runPCB.run_time+=circle_size;
                    runPCB.req_time-=circle_size;
                    queue.add(runPCB);
                    Thread.sleep(circle_size*1000);
                    System.out.println("本次时间片未能执行完毕,还需要时间"+runPCB.req_time);
                }else {
                    Thread.sleep(runPCB.req_time*1000);
                    System.out.println("进程执行完毕");
                }

            }else{
                System.out.println("finished");
                state = false;
                break;
            }
        }
    }

    public static boolean oneCircleTime() throws Exception {
        Thread.sleep(1000);
        if(!queue.isEmpty()) {
            PCB runPCB = queue.poll();
            int reqtime =runPCB.req_time;
            if(reqtime>circle_size){
                runPCB.run_time+=circle_size;
                runPCB.req_time-=circle_size;
                queue.add(runPCB);
            }else {
                runPCB.req_time=0;
                System.out.println("进程执行完毕");
            }
            runningPCB = runPCB;
            return true;
        }else{
            return false;
        }
    }

    public static boolean add_queue(PCB one){
        return queue.offer(one);
    }

    public static boolean getState(){
        return state;
    }

    public static String getqueue(){

        StringBuffer result = new StringBuffer() ;
        for(PCB pcb:queue){
            result.append(pcb.id+" "+pcb.run_time+" "+pcb.req_time+" "+pcb.allo_time+"\n");
        }
        return result.toString();
    }

    public static String getRunningPCB(){
        String result;
        if(runningPCB.req_time>0) {
            result = runningPCB.id + "号进程执行,未完成,还需要时间" + runningPCB.req_time +"    \n";
        }else {
            result = runningPCB.id + "号进程,本次执行完毕\n";
        }
        return result;
    }
}
