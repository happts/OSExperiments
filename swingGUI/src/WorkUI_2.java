import happts.JCB;
import happts.Job;

import javax.swing.*;
import java.util.List;

public class WorkUI_2 {

    static JTextField IDTextView;
    static JTextField priorityTextView;
    static JTextField timeTextView;
    static JButton addBtn;
    static JButton runBtn;

    static JFrame frame;

    static JTextArea[][] allAreas;

    public static void main(String[] args){
        SwingUtilities.invokeLater(()-> initView());
    }

    static void initView(){
        frame = UtilView.createFrame("作业调度");

        Box vBox = Box.createVerticalBox();

        Box hBox = Box.createHorizontalBox();

        JTextArea[] FCFSAreas = oneCMDView(hBox,"FCFS");
        JTextArea[] SJFAreas = oneCMDView(hBox,"SJF");
        JTextArea[] HRRNAreas = oneCMDView(hBox,"HRRN");

        allAreas = new JTextArea[][]{FCFSAreas,SJFAreas,HRRNAreas};

        addWorkView(vBox);
        vBox.add(hBox);

        frame.setContentPane(vBox);
        frame.pack();
        frame.setVisible(true);

    }

    static JTextArea[] oneCMDView(Box box,String name){
        Box vCMDBox = Box.createVerticalBox();
        JLabel label = new JLabel(name);
        vCMDBox.add(label);

        Box oneCMDbox = Box.createVerticalBox();
        Box processQueueBox  = Box.createVerticalBox();
        Box cmdBox = Box.createVerticalBox();
        oneCMDbox.add(processQueueBox);
        oneCMDbox.add(cmdBox);

        vCMDBox.add(oneCMDbox);
        box.add(vCMDBox);

        JTextArea processText;
        JTextArea cmdArea;

        //开始运行时刻、完成时刻、周转时间、带权周转时 间，

        JLabel title = new JLabel("id 优先级 进入时刻 运行时间 | 开始时刻 完成时刻 ");
        processQueueBox.add(title);
        processText = UtilView.createCMD(processQueueBox);
        cmdArea = UtilView.createCMD(cmdBox);

        return new JTextArea[]{processText,cmdArea};
    }

    static void addWorkView(Box box){
        Box addWorkbox = Box.createVerticalBox();
        addBtn = new JButton("添加");
        runBtn = new JButton("运行");
        JPanel btnPanel = new JPanel();
        btnPanel.add(addBtn);
        btnPanel.add(runBtn);
        addWorkbox.add(btnPanel);
        IDTextView = UtilView.createKeyValue(addWorkbox,"ID");
        priorityTextView = UtilView.createKeyValue(addWorkbox,"优先级");
        priorityTextView.setEnabled(false);
        timeTextView = UtilView.createKeyValue(addWorkbox,"总时间");
        box.add(addWorkbox);


        Job[] jobs = new Job[3];
        for(int i=0;i<allAreas.length;i++){
            jobs[i] = new Job(i);
        }

        addBtn.addActionListener(e -> {
            for(int i=0;i<jobs.length;i++){
                new addAction(jobs[i]).execute();
            }
            frame.pack();
        });

        runBtn.addActionListener(e -> {
            for(JTextArea[] each:allAreas){
                each[1].setText("");
            }

            new runAction(jobs).execute();
            runBtn.setEnabled(false);
            frame.pack();
        });

    }

    static class addAction extends SwingWorker<Boolean,Void>{
        Job job ;
        addAction(Job job){
            this.job = job;
        }

        @Override
        protected Boolean doInBackground() {
//            String name = priorityTextView.getText();
//            int priority = UtilView.stringToInt(name);

            int ID = UtilView.stringToInt(IDTextView.getText());
            int runtime = UtilView.stringToInt(timeTextView.getText());
            job.add_queue(new JCB(ID, job.getTime(),runtime,0));
            return true;
        }

        @Override
        protected void done(){
            String string = job.getqueue();
            allAreas[job.getX()][0].setText(string);
            UtilView.clearTextView(priorityTextView,IDTextView,timeTextView);
        }
    }

    static class runAction extends SwingWorker<String,Job>{
        Job[] jobs;

        runAction(Job[] jobs){
            this.jobs = jobs;
        }

        @Override
        protected String doInBackground()throws Exception{
            boolean state;
            for(int i=0;i<jobs.length;i++){
                state =true;

                Job job = jobs[i];
                while (state){
                    state =  job.onCircle();
                    if(state)publish(job);
                }
            }
            return "执行完毕";
        }

        @Override
        protected void process(List<Job> chunks){
            Job job = chunks.get(0);
            allAreas[job.getX()][0].setText(job.getqueue());
            allAreas[job.getX()][1].append(job.getRunningJCB().getId()+"号进程运行\n");
            frame.pack();
        }

        @Override
        protected void done(){
            runBtn.setEnabled(true);
            for(int i=0;i<jobs.length;i++){
                Job job = jobs[i];
                allAreas[job.getX()][1].setText("");
                allAreas[job.getX()][1].append(job.getFinishqueue());
                allAreas[job.getX()][1].append(job.getTurnaroundTime());
                allAreas[job.getX()][1].append(job.getWeightedTurnaroundTime());
            }
            frame.pack();
        }

    }
}
