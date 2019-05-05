package happts;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Job {
    Queue<JCB> queue = new LinkedList<>();

    private boolean state = true;

    int time = 0;
    int x;
    JCB runningJCB;

    LinkedList<JCB> finishedJCB = new LinkedList<>();
    LinkedList<JCB> JCBlist = new LinkedList<>();

    public Job(int i){
        this.x =i;
    }

    private boolean oneCircleFCFS() throws Exception {
        Thread.sleep(1000);
        if (!queue.isEmpty()) {
            JCB runJCB = queue.poll();
            runJCB.start_time = time;

            ////

            time += runJCB.run_time;
            runJCB.end_time = time;
            finishedJCB.add(runJCB);
            runningJCB = runJCB;
            return true;
        } else {
            return false;
        }
    }

    private boolean oneSJF() throws Exception {
        if (state) {
            state = false;
            Collections.sort((List) queue, new CompareJCBSJF());
        }
        return oneCircleFCFS();
    }

    private boolean oneHRRN() throws Exception {

        setPriroty();
        Collections.sort((List) queue, new CompareJCBPriority());

        return oneCircleFCFS();
    }

    public boolean onCircle()throws Exception{
        boolean result;
        switch (x){
            case 0:result =oneCircleFCFS();break;
            case 1:result =oneSJF();break;
            case 2:result =oneHRRN();break;
            default:result = false;
        }
        return result;
    }

    public void testCircle() {
        Collections.sort((List) queue, new CompareJCBSJF());
        while (!queue.isEmpty()) {
            JCB runJCB = queue.poll();
            runJCB.start_time = time;

            System.out.println("ID:" + runJCB.id + " 优先级:" + runJCB.priority + " 开始时间:" + runJCB.start_time + "进入时间:" + runJCB.in_time);

            time += runJCB.run_time;
            runJCB.end_time = time;

            finishedJCB.add(runJCB);
        }
    }

    public void testHRRN() {
        setPriroty();
        Collections.sort((List) queue, new CompareJCBPriority());
        while (!queue.isEmpty()) {
            JCB runJCB = queue.poll();
            runJCB.start_time = time;

            System.out.println("ID:" + runJCB.id + " 优先级:" + runJCB.priority + " 开始时间:" + runJCB.start_time + "进入时间:" + runJCB.in_time + " 需要时间:" + runJCB.run_time);

            time += runJCB.run_time;
            runJCB.end_time = time;

            finishedJCB.add(runJCB);

            setPriroty();
            Collections.sort((List) queue, new CompareJCBPriority());

            System.out.println(getqueue());
            System.out.println();
        }
    }

    private void setPriroty() {
        for(JCB jcb :queue){
            jcb.priority =1 + ((time -jcb.in_time)+0.0) /jcb.run_time ;
        }
    }

    public void add_queue(JCB one) {
        queue.offer(one);
        JCBlist.add(one);
    }

    //id 优先级 进入时刻 运行时间 | 开始时刻 完成时刻 "
    public String getqueue() {
        DecimalFormat df = new DecimalFormat("0.00");

        StringBuffer result = new StringBuffer();
        for (JCB pcb : JCBlist) {
            result.append("   "+pcb.id + "  " + df.format(pcb.priority) + "   " + pcb.in_time +" " + pcb.run_time + " | " + pcb.start_time + " "+pcb.end_time + "\n");
        }
        String one = result.toString();
        return one;
    }

    public String getFinishqueue(){
        DecimalFormat df = new DecimalFormat("0.00");

        StringBuffer result = new StringBuffer();
        result.append("运行结果\n");
        for(JCB jcb:finishedJCB){
            result.append("   "+jcb.id + "  " + df.format(jcb.priority) + "   " + jcb.in_time +" "+jcb.run_time + " | " + jcb.start_time + " " + jcb.end_time+"\n");
        }
        return result.toString();
    }

    public double getAverageTurnaroundTime(){
        int AVT =0;
        for(JCB jcb:JCBlist){
            AVT += jcb.end_time-jcb.in_time;
        }
        return AVT+0.0 /JCBlist.size();
    }

    public String getTurnaroundTime(){
        StringBuffer buffer = new StringBuffer();
        buffer.append("周转时间\n");
        for(JCB jcb:JCBlist){
            int tt = jcb.end_time -jcb.in_time;
            buffer.append(jcb.id+"号进程: "+tt+"\n");
        }
        buffer.append("\n");
        buffer.append("平均周转时间:"+getAverageTurnaroundTime()+"\n");
        return buffer.toString();
    }

    public double getAverageWeightedTurnaroundTime(){
        double AWTT = 0;
        for(JCB jcb:JCBlist){
            AWTT+=(jcb.end_time-jcb.in_time+0.0)/jcb.run_time;
        }
        return AWTT/JCBlist.size();
    }

    public String getWeightedTurnaroundTime(){
        StringBuffer buffer = new StringBuffer();
        buffer.append("带权周转时间\n");
        for(JCB jcb:JCBlist){
            double tt = (jcb.end_time -jcb.in_time+0.0)/jcb.run_time;
            DecimalFormat df = new DecimalFormat("0.00");

            buffer.append(jcb.id+"号进程: "+df.format(tt)+"\n");
        }

        buffer.append("\n");
        buffer.append("平均带权周转时间:\n"+getAverageWeightedTurnaroundTime());
        return buffer.toString();
    }

    public int getTime() {
        return time;
    }

    public int getX() {
        return x;
    }

    public JCB getRunningJCB() {
        return runningJCB;
    }
}


