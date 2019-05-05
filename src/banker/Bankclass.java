package banker;


import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Bankclass {
    int[] totalResource;
    int[] Available;
    int[][] Max;
    int[][] Allocation;
    int[][] Need;

    int resourcekind = 3;
    int processNum = 5;

    Queue<Integer> queue;

    String[] processNames;
    String[] resourceNames;

    public void init(int resourcekind,int processNum){
        this.resourcekind = resourcekind;
        this.processNum = processNum;
        Available = new int[resourcekind];
        Max = new int[processNum][resourcekind];
        Allocation = new int[processNum][resourcekind];
        Need = new int[processNum][resourcekind];
        totalResource = new int[resourcekind];

        processNames = new String[processNum];
        for(int i=0;i<processNum;i++){
            processNames[i] = "P"+i;
        }

        resourceNames = new String[resourcekind];
        for(int i=0;i<resourcekind;i++){
            resourceNames[i] = "R"+i;
        }
    }

    public Boolean setInitValue(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("print value:");

        for(int i = 0; i< totalResource.length; i++){
            totalResource[i] = scanner.nextInt();
        }

        for(int i=0;i<processNum;i++){
            for(int j=0;j<resourcekind;j++){
                Max[i][j] = scanner.nextInt();
            }
        }

        for(int i=0;i<processNum;i++){
            for(int j=0;j<resourcekind;j++){
                Allocation[i][j] = scanner.nextInt();
            }
        }

        for(int i=0;i<processNum;i++){
            for(int j=0;j<resourcekind;j++){
                Need[i][j] = Max[i][j]-Allocation[i][j];
            }
        }

        for(int j=0;j<resourcekind;j++){
            int num = totalResource[j];
            for(int i=0;i<processNum;i++){
                num-= Allocation[i][j];
            }
            Available[j] = num;
        }

        return securityAlgorithm(Available,Allocation,Need);
    }

    public Boolean initValue(int[][]max,int[][]allocation){

        this.Max = max;
        this.Allocation = allocation;

        for(int i=0;i<processNum;i++){
            for(int j=0;j<resourcekind;j++){
                Need[i][j] = Max[i][j]-Allocation[i][j];

                if(Need[i][j]<0)return false;
            }
        }

        for(int j=0;j<resourcekind;j++){
            int num = totalResource[j];
            for(int i=0;i<processNum;i++){
                num-= Allocation[i][j];
            }
            Available[j] = num;
        }

        return securityAlgorithm(Available,Allocation,Need);
    }

    private Boolean securityAlgorithm(int[] Available,int[][] Allocation,int[][] Need){

        int[] Work = new int[Available.length];
        for(int i=0;i<Available.length;i++){
            Work[i] = Available[i];
        }

        Boolean[] Finish = new Boolean[processNum];
        for(int x=0;x<processNum;x++){
            Finish[x] = false;
        }

        queue = new LinkedList<>();

        int i=0;
        Boolean flag = true;

        while (i<processNum){
            if(!Finish[i]){
                for(int j=0;j<resourcekind;j++){
                    flag = flag & (Work[j]>=Need[i][j]);
                }

                if(flag){
                    for(int j=0;j<resourcekind;j++){
                        Work[j] += Allocation[i][j];
                    }
                    Finish[i] = true;

                    queue.offer(i);

                    i = 0;
                }else {
                    flag = true;
                    i++;
                }
            }else {
                i++;
            }
        }

        for(i=0;i<processNum;i++){
            flag = flag & Finish[i];
        }
        return flag;
    }

    public boolean BankerAlgorithm(int process,int[] Request){
        for(int j=0;j<resourcekind;j++){
            if(Request[j]>Need[process][j])return false;
        }
        for(int j=0;j<resourcekind;j++){
            if(Request[j]>Available[j])return false;
        }

        int[] temAvailable = new int[resourcekind];
        for(int j=0;j<resourcekind;j++){
            temAvailable[j] = Available[j] - Request[j];
        }

        int[][] temAllocation = new int[processNum][resourcekind];
        System.arraycopy(Allocation,0,temAllocation,0,Allocation.length);
        for(int j=0;j<resourcekind;j++){
            temAllocation[process][j] = Allocation[process][j] + Request[j];
        }

        int[][] temNeed = new int[processNum][resourcekind];
        System.arraycopy(Need,0,temNeed,0,Need.length);
        for(int j=0;j<resourcekind;j++){
            temNeed[process][j] = Need[process][j] - Request[j];
        }

        if(securityAlgorithm(temAvailable,temAllocation,temNeed)){
            for(int j=0;j<resourcekind;j++){
                Available[j] = temAvailable[j];
                Allocation[process][j] = temAllocation[process][j];
                Need[process][j] = temNeed[process][j];
            }
            return true;
        }else {
            return false;
        }
    }


    public String getQueue() {

        StringBuffer stringBuffer = new StringBuffer();
        while (!queue.isEmpty()) {
            stringBuffer.append(queue.poll()+"");
            stringBuffer.append(" ");
        }
        return stringBuffer.toString();
    }


    public void setTotalResource(int[] totalResource) {
        this.totalResource = totalResource;
    }

    public int[] getTotalResource() {
        return totalResource;
    }

    public int getProcessNum() {
        return processNum;
    }

    public int getResourcekind() {
        return resourcekind;
    }

    public int[] getAvailable() {
        return Available;
    }

    public int[][] getAllocation() {
        return Allocation;
    }

    public int[][] getMax() {
        return Max;
    }

    public int[][] getNeed() {
        return Need;
    }

    public String[] getProcessNames() {
        return processNames;
    }
}
