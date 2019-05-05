package happts;

import java.util.Comparator;

public class CompareJCBSJF implements Comparator<JCB>{

    public int compare(JCB p1, JCB p2) {
        if(p1.run_time>p2.run_time) {
            return 1;
        }else if (p1.run_time<p2.run_time){
            return -1;
        }else {
            return 0;
        }
    }
}
