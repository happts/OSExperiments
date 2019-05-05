import javax.swing.*;

public class UtilView {

    public static JFrame createFrame(String title){
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        return frame;
    }

    public static int stringToInt(String string){
        int num=-1;
        try{
            num = Integer.valueOf(string).intValue();
        }catch (NumberFormatException e){
            e.printStackTrace();
        }

        return num;
    }

    public static JTextField createKeyValue(Box box,String key){
        JPanel panel = new JPanel();
        panel.add(new JLabel(key));
        JTextField valueView = new JTextField(10);
        panel.add(valueView);
        box.add(panel);
        return valueView;
    }

    public static void clearTextView(JTextField... textFields){
        for(JTextField JTF:textFields){
            JTF.setText("");
        }
    }

    public static JTextArea createCMD(Box box){
        JTextArea cmdArea = new JTextArea();
        cmdArea.setEditable(false);

        box.add(cmdArea);
        return cmdArea;
    }


}
