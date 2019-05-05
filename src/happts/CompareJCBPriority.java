package happts;

import java.util.Comparator;

public class CompareJCBPriority implements Comparator<JCB> {

    public int compare(JCB p1, JCB p2) {
        if(p1.priority>p2.priority) {
            return -1;
        }else if (p1.priority<p2.priority){
            return 1;
        }else {
            return 0;
        }
    }
}
