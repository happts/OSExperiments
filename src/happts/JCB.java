package happts;

public class JCB {
    int id ;
    double priority;

    int run_time;//需要的总时间
    int in_time;//进入时间
    int start_time;//开始时间
    int end_time;//结束时间

    /**
     * JOB
     * @param id
     * @param in_time
     * @param run_time
     */
    public JCB(int id, int in_time, int run_time){
        this.id = id;
        this.in_time = in_time;
        this.run_time = run_time;
    }

    public JCB(int id, int in_time, int run_time, int priority){
        this(id,in_time,run_time);
        this.priority = priority;
    }

    public int getId() {
        return id;
    }
}
