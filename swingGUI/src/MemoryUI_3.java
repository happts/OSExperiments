import happts.Memory;
import javax.swing.*;


public class MemoryUI_3 {

    static JButton allocBtn;
    static JButton collecBtn;
    static JList<String> jList;
    static int num;
    static Memory memory;
    static String[] a;

    public static void main(String[] args){
        memory = new Memory();

        SwingUtilities.invokeLater(()->initView());
    }

    static void initView(){
        JFrame frame = UtilView.createFrame("首次适应算法分区管理");
        Box vBox = Box.createVerticalBox();

        JPanel panel = new JPanel();
        JLabel MIN_SIZElable = new JLabel("最小剩余分区:5");
        allocBtn = new JButton("分配内存");
        collecBtn = new JButton("回收内存");
        panel.add(MIN_SIZElable);
        panel.add(allocBtn);
        panel.add(collecBtn);
        vBox.add(panel);

        jList = new JList<>();
        a = new String[100];
        for(int i=0;i<100;i++){
            a[i] = "              "+i;
        }
        ListModel jlistModel = new DefaultComboBoxModel(a);
        jList.setModel(jlistModel);
        jList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane jScrollPane = new JScrollPane();
        jScrollPane.setViewportView(jList);
        vBox.add(jScrollPane);


        frame.setContentPane(vBox);
        frame.pack();
        frame.setVisible(true);


        allocBtn.addActionListener( e -> {
            String numS = JOptionPane.showInputDialog(frame,
                    "请输入分配内存大小:",
                    "");
            if(numS!=null){
                num = UtilView.stringToInt(numS);
                allocAction(num);
                refreshList();
            }

        });

        collecBtn.addActionListener(e -> {

            String numS = JOptionPane.showInputDialog(frame,
                    "请输入要回收的分区号:",
                    "");
            if(numS!=null){
                num = UtilView.stringToInt(numS);
                collecAction(num);
            }

            refreshList();
        });


    }

    static void allocAction(int num){
        memory.allocation(num);
    }

    static void collecAction(int num){
        memory.collection(num);
    }

    static void refreshList(){
        String[] strings = new String[100];
        System.arraycopy(a,0,strings,0,strings.length);
        int[] ID = memory.getZonesID();
        for(int i=0;i<ID.length;i++){
            strings[ID[i]] = "分区号"+i;
        }
        jList.setListData(strings);
        SwingUtilities.updateComponentTreeUI(jList);

        int[] n = memory.getNotfreeZone();
        jList.setSelectedIndices(n);
    }
}
