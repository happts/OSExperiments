import banker.Bankclass;

import javax.swing.*;

public class BankUI_5 {
    static Bankclass bank;

    public static void main(String[] args){
        bank = new Bankclass();
        SwingUtilities.invokeLater(BankUI_5::initView);
        bank.init(3,5);
    }

    static void initView(){
        initBankView(bank);
    }

    static void initBankView(Bankclass bankclass){
        JFrame frame = UtilView.createFrame("银行家算法");
        Box vbox = Box.createVerticalBox();
        JTextField resourcekindText = UtilView.createKeyValue(vbox,"资源种类:");
        JTextField processNumText = UtilView.createKeyValue(vbox,"进程数");

        JButton subBtn = new JButton("确定");
        vbox.add(subBtn);

        frame.setContentPane(vbox);
        frame.pack();
        frame.setVisible(true);

        subBtn.addActionListener(e -> {
            int resourcekind = UtilView.stringToInt(resourcekindText.getText());
            int processnum = UtilView.stringToInt(processNumText.getText());
            bankclass.init(resourcekind,processnum);
            frame.dispose();

            inputTotalResource(bankclass);
        });
    }

    static void inputTotalResource(Bankclass bankclass){
        JFrame frame = new JFrame("各个资源总数");

        Box vbox = Box.createVerticalBox();
        JTextField[] jTextFields = inputOneProcessState(vbox,"各个资源",bankclass);

        JButton subBtn = new JButton("确定");
        vbox.add(subBtn);

        frame.setContentPane(vbox);
        frame.pack();
        frame.setVisible(true);

        subBtn.addActionListener(e->{
            int[] totalResource = new int[bankclass.getResourcekind()];

            for(int i=0;i<totalResource.length;i++){
                totalResource[i] = UtilView.stringToInt(jTextFields[i].getText());
            }
            bankclass.setTotalResource(totalResource);
            frame.dispose();

            inputValueView(bankclass);
        });
    }

    static void inputValueView(Bankclass bankclass){
        int processNum = bankclass.getProcessNum();
        int resourcekind = bankclass.getResourcekind();

        JFrame frame = UtilView.createFrame("请输入");

        JPanel panel = new JPanel();

        Box MaxBox = Box.createVerticalBox();
        Box AllocationBox = Box.createVerticalBox();

        panel.add(MaxBox);
        panel.add(AllocationBox);

        Box vbox = Box.createVerticalBox();

        JButton subBtn = new JButton("确定");

        vbox.add(panel);
        vbox.add(subBtn);


        JTextField[][] maxTexts = inputOneTable(MaxBox,"MAX",bankclass);
        JTextField[][] allocationTexts = inputOneTable(AllocationBox,"Allocation",bankclass);

        frame.setContentPane(vbox);
        frame.pack();
        frame.setVisible(true);

        subBtn.addActionListener(e->{
            int[][] Max = new int[processNum][resourcekind];
            int[][] Allocation = new int[processNum][resourcekind];

            for(int i=0;i<processNum;i++){
                for(int j=0;j<resourcekind;j++){
                    Max[i][j] = UtilView.stringToInt(maxTexts[i][j].getText());
                    Allocation[i][j] = UtilView.stringToInt(allocationTexts[i][j].getText());
                }
            }

            if(bankclass.initValue(Max,Allocation)) {
                showBankView();
                frame.dispose();
            }else{
                JOptionPane.showMessageDialog(frame,"输入不合法,请重新输入","错误",JOptionPane.INFORMATION_MESSAGE);
            }

        });
    }

    static JTextField[] inputOneProcessState(Box box ,String processName,Bankclass bankclass){

        int num = bankclass.getResourcekind();

        JTextField[] textFields = new JTextField[num];

        JPanel panel = new JPanel();

        JLabel label = new JLabel(processName);
        panel.add(label);
        for(int i=0;i<num;i++){
            textFields[i] = new JTextField(5);
            panel.add(textFields[i]);
        }

        box.add(panel);

        return textFields;
    }

    static JTextField[][] inputOneTable(Box box,String title,Bankclass bankclass){

        int processNum = bankclass.getProcessNum();
        int resourcekind  = bankclass.getResourcekind();
        String[] processNames = bankclass.getProcessNames();

        JTextField[][] oneTextTable = new JTextField[processNum][resourcekind];

        Box vBox = Box.createVerticalBox();
        JLabel label = new JLabel(title);
        vBox.add(label);
        for(int i=0;i<processNum;i++){
            oneTextTable[i] = inputOneProcessState(vBox,processNames[i],bankclass);
        }

        box.add(vBox);

        return oneTextTable;
    }

    static void showBankView(){
        JFrame frame = UtilView.createFrame("银行家算法");
        Box vBox_bottom = Box.createVerticalBox();

        Box h1Box = Box.createHorizontalBox();
        Box v2Box = Box.createHorizontalBox();
        vBox_bottom.add(h1Box);
        vBox_bottom.add(v2Box);

        JTextField[][] MaxText = inputOneTable(h1Box,"MAX",bank);
        JTextField[][] AllocationText = inputOneTable(h1Box,"Allocation",bank);
        JTextField[][] NeedText = inputOneTable(h1Box,"Need",bank);

        JTextField[] totalResourceText = inputOneProcessState(v2Box,"总资源",bank);
        JTextField[] AvailableText = inputOneProcessState(v2Box,"可分配",bank);

        resultView(v2Box,bank,MaxText,AllocationText,NeedText,AvailableText);

        frame.setContentPane(vBox_bottom);
        frame.pack();
        frame.setVisible(true);

        int[][] Max = bank.getMax();
        int[][] Allocation = bank.getAllocation();
        int[][] Need = bank.getNeed();
        int[] available = bank.getAvailable();
        int[] total = bank.getTotalResource();

        for(int i=0;i<totalResourceText.length;i++){
            totalResourceText[i].setEnabled(false);
            AvailableText[i].setEnabled(false);
            totalResourceText[i].setText(total[i]+"");
            AvailableText[i].setText(available[i]+"");
        }

        for(int i=0;i<bank.getProcessNum();i++){
            for(int j=0;j<bank.getResourcekind();j++){
                MaxText[i][j].setEnabled(false);
                AllocationText[i][j].setEnabled(false);
                NeedText[i][j].setEnabled(false);

                MaxText[i][j].setText(Max[i][j]+"");
                AllocationText[i][j].setText(Allocation[i][j]+"");
                NeedText[i][j].setText(Need[i][j]+"");
            }
        }
    }

    static void resultView(Box box,Bankclass bankclass,
                           JTextField[][] MaxText,JTextField[][] AllocationText,JTextField[][] NeedText,JTextField[] AvailableText){
        Box vBox = Box.createVerticalBox();

        Box hBox = Box.createHorizontalBox();
        hBox.add(new JLabel("请输入请求"));
        JTextField processNameText =  UtilView.createKeyValue(hBox,"进程名");
        JTextField[] input = inputOneProcessState(hBox,"各个资源",bankclass);

        JLabel alignment = new JLabel("安全序列:");
        JButton subBtn = new JButton("提交");

        vBox.add(hBox);
        vBox.add(alignment);
        vBox.add(subBtn);

        box.add(vBox);

        subBtn.addActionListener(e -> {
            int[] toAlloc = new int[bankclass.getResourcekind()];
            for(int i=0;i<toAlloc.length;i++){
                toAlloc[i] = UtilView.stringToInt(input[i].getText());
                UtilView.clearTextView(input[i]);
            }

            int process = UtilView.stringToInt(processNameText.getText());
            UtilView.clearTextView(processNameText);

            if(bankclass.BankerAlgorithm(process,toAlloc)){
                alignment.setText("安全序列:"+bankclass.getQueue());
                refreshView(MaxText,AllocationText,NeedText,AvailableText);
            }else {
                alignment.setText("无安全序列");
            }

        });
    }

    static void refreshView(JTextField[][] MaxText,JTextField[][] AllocationText,JTextField[][] NeedText,JTextField[] AvailableText){
        int[][] Max = bank.getMax();
        int[][] Allocation = bank.getAllocation();
        int[][] Need = bank.getNeed();
        int[] available = bank.getAvailable();

        for(int i=0;i<available.length;i++){
            AvailableText[i].setText(available[i]+"");
        }

        for(int i=0;i<bank.getProcessNum();i++){
            for(int j=0;j<bank.getResourcekind();j++){
                MaxText[i][j].setText(Max[i][j]+"");
                AllocationText[i][j].setText(Allocation[i][j]+"");
                NeedText[i][j].setText(Need[i][j]+"");
            }
        }
    }
}
