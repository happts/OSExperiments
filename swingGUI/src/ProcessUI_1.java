import happts.PCB;
import happts.RR;

import javax.swing.*;
import java.util.List;


/**
 * 2.编写并调试一个模拟的进程调度程序，采用“轮转法”调度算法对五个进程进行调 度。
 轮转法可以是简单轮转法、可变时间片轮转法，或多队列轮转法。
 操作系统原理实验指导
 简单轮转法的基本思想是:所有就绪进程按 FCFS 排成一个队列，总是把处理机分配给 队首的进程，各进程占用 CPU 的时间片相同。
 如果运行进程用完它的时间片后还为完成，就 把它送回到就绪队列的末尾，把处理机重新分配给队首的进程，直至所有的进程运行完毕。
 */

public class ProcessUI_1 {
    static JTextField IDTextView;
    static JTextField timeTextView;
    static JButton addBtn;
    static JButton runBtn;
    static JTextArea processText;
    static JTextArea cmdArea;
    static JFrame frame;

    public static void main(String[] args){
        SwingUtilities.invokeLater(()-> initView());
    }

    static void initView(){
        frame = UtilView.createFrame("轮转法调度");

        Box vBox = Box.createVerticalBox();
        Box addProcessBox = Box.createVerticalBox();
        Box hBox = Box.createHorizontalBox();
        Box processQueueBox  = Box.createVerticalBox();
        Box cmdBox = Box.createVerticalBox();

        hBox.add(processQueueBox);
        hBox.add(cmdBox);

        vBox.add(addProcessBox);
        vBox.add(hBox);

        IDTextView = UtilView.createKeyValue(addProcessBox,"ID");
        timeTextView = UtilView.createKeyValue(addProcessBox,"总时间");
        addBtn = new JButton("添加");
        runBtn = new JButton("运行");
        JPanel btnPanel = new JPanel();
        btnPanel.add(addBtn);
        btnPanel.add(runBtn);
        addProcessBox.add(btnPanel);

        JLabel title = new JLabel("id 总时间 还需时间 已分配时间");
        processQueueBox.add(title);
        processText = UtilView.createCMD(processQueueBox);

        cmdArea = UtilView.createCMD(cmdBox);

        frame.setContentPane(vBox);
        frame.pack();
        frame.setVisible(true);

        addBtn.addActionListener(e -> {
            new addAction().execute();
            frame.pack();
        });

        runBtn.addActionListener(e -> {
            cmdArea.setText("");
            new runAction().execute();
            frame.pack();
            runBtn.setEnabled(false);
        });


    }

    static class addAction extends SwingWorker<Boolean,Void>{

        @Override
        protected Boolean doInBackground() {
            int ID = UtilView.stringToInt(IDTextView.getText());
            int runtime = UtilView.stringToInt(timeTextView.getText());

            RR.add_queue(new PCB(ID,runtime));
            return true;
        }

        @Override
        protected void done(){
            processText.setText(RR.getqueue());
            UtilView.clearTextView(IDTextView,timeTextView);
        }
    }

    static class runAction extends SwingWorker<String,String>{
        @Override
        protected String doInBackground()throws Exception{
            boolean state = true;
            while (state){
                state =  RR.oneCircleTime();
                publish(RR.getRunningPCB());
            }
            return "执行完毕";
        }

        @Override
        protected void process(List<String> chunks){
            String string = chunks.get(0);
            cmdArea.append(string);
            processText.setText(RR.getqueue());
            frame.pack();
        }

        @Override
        protected void done(){
            cmdArea.append("执行完毕\n");
            runBtn.setEnabled(true);
        }

    }
}
