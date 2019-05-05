
import happts.aboutfile.*;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FileOSUI_4 {
    static JTable userTable;
    static JTable userFileTable;

    static JButton addUserbtn ;
    static JButton addFilebtn ;

    static JButton saveFilebtn;
    static JButton deleteFilebtn;

    static JTextField fileNameFiled;
    static JTextField protectionCodeFiled;
    static JTextArea textArea;

    static FileOS fileOS;
    static User tmpUser;

    public static void main(String[] args) throws Exception{
        fileOS  = new FileOS();
        SwingUtilities.invokeLater(()->initView());
    }

    static void initView(){
        JFrame userlistFrame = UtilView.createFrame("用户列表");

        Box vbox = Box.createVerticalBox();

        addUserbtn = new JButton("创建新用户");

        vbox.add(addUserbtn);
        vbox.add(createUserTable());

        userlistFrame.setContentPane(vbox);
        userlistFrame.pack();
        userlistFrame.setLocationRelativeTo(null);
        userlistFrame.setVisible(true);

        addUserbtn.addActionListener(e -> {
            String name = JOptionPane.showInputDialog(userlistFrame,
                    "用户名:",
                    "").trim();
            if(name!=null){
                try {
                    fileOS.createUser(name);
                    refreshUserTable();
                }catch (Exception e1){

                }
            }
        });
    }

    static JPanel createUserTable(){
        JPanel panel = new JPanel(new BorderLayout());

        final Object[] columnName = {"用户名","路径"," "};
        final Object[][] rowData = fileOS.showMFD();

        userTable = new JTable(new AbstractTableModel() {
            @Override
            public int getRowCount() {
                return rowData.length;
            }

            @Override
            public int getColumnCount() {
                return columnName.length;
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                return rowData[rowIndex][columnIndex];
            }

            @Override
            public String getColumnName(int column){
                return columnName[column].toString();
            }

            @Override
            public boolean isCellEditable(int rowIndex,int columnIndex){
                return false;
            }
        });

        userTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int row = userTable.getSelectedRow();
                int column = userTable.getSelectedColumn();
                if(column>1){
                    tmpUser = fileOS.selectUser(row);
                    tmpUser.initUFD();
                    createUserFileTable();
                }
            }
        });

        panel.add(userTable.getTableHeader(),BorderLayout.NORTH);
        panel.add(userTable,BorderLayout.CENTER);
        return panel;
    }

    static void createUserFileTable(){
        JPanel panel = new JPanel(new BorderLayout());

        final  Object[] columnNames = {"文件名","权限码","长度"," "};
        final Object[][] rowData = tmpUser.showUFD();

        userFileTable = new JTable(new AbstractTableModel() {
            @Override
            public int getRowCount() {
                return rowData.length;
            }

            @Override
            public int getColumnCount() {
                return columnNames.length;
            }

            @Override
            public String getColumnName(int column){
                return columnNames[column].toString();
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                return rowData[rowIndex][columnIndex];
            }

            @Override
            public boolean isCellEditable(int rowIndex,int columnIndex){
                return columnIndex<2;
            }

            @Override
            public void setValueAt(Object newValue,int rowIndex,int columnIndex){
                rowData[rowIndex][columnIndex] = newValue;
                fireTableCellUpdated(rowIndex,columnIndex);
            }
        });
        panel.add(userFileTable.getTableHeader(),BorderLayout.NORTH);
        panel.add(userFileTable,BorderLayout.CENTER);

        TableModel model = userFileTable.getModel();
        model.addTableModelListener(e -> {
            int firstRow = e.getFirstRow();
            int lastRow = e.getLastRow();

            int column = e.getColumn();

            int type = e.getType();

            if(type == TableModelEvent.UPDATE){
                if(column==0){
                    tmpUser.selectFileModel(firstRow).setName(model.getValueAt(firstRow,column).toString());
                }else {
                    tmpUser.selectFileModel(firstRow).setProtectioncode( UtilView.stringToInt(model.getValueAt(firstRow,column).toString()));
                }
            }
        });

        userFileTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e)  {
                super.mouseClicked(e);
                int row = userFileTable.getSelectedRow();
                int column = userFileTable.getSelectedColumn();
                if(column>=2){
                    openFileFrame(tmpUser.selectFileModel(row));
                }
            }
        });

        Box vbox = Box.createVerticalBox();
        addFilebtn = new JButton("新建文件");
        vbox.add(addFilebtn);
        vbox.add(panel);

        JFrame frame = new JFrame("文件列表");
        frame.setLocationRelativeTo(null);
        frame.setContentPane(vbox);
        frame.pack();
        frame.setVisible(true);

        addFilebtn.addActionListener(e -> {
            createFileFrame();
        });
    }

    static JFrame createFileFrame(){
        JFrame frame = new JFrame("new file");
        textArea = new JTextArea();
        Box hBox = Box.createHorizontalBox();

        fileNameFiled = UtilView.createKeyValue(hBox,"文件名");
        protectionCodeFiled = UtilView.createKeyValue(hBox,"权限码");
        saveFilebtn = new JButton("保存");
        deleteFilebtn = new JButton("删除");

        hBox.add(deleteFilebtn);
        hBox.add(saveFilebtn);

        Box vBox = Box.createVerticalBox();
        vBox.add(textArea);
        vBox.add(hBox);

        frame.setContentPane(vBox);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        deleteFilebtn.setEnabled(false);
        saveFilebtn.addActionListener(e -> {
            String filename = fileNameFiled.getText();
            int protectionCode = UtilView.stringToInt(protectionCodeFiled.getText());
            String text = textArea.getText();

            tmpUser.createFile(filename,protectionCode,text);

            frame.dispose();
            refreshUserFileTable();
        });

        return frame;
    }

    static void openFileFrame(FileModel file){
        JFrame frame = createFileFrame();
        frame.setTitle(file.getName());

        fileNameFiled.setText(file.getName());
        protectionCodeFiled.setText(file.getProtectioncode()+"");
        protectionCodeFiled.setEnabled(false);
        fileNameFiled.setEnabled(false);
        if(file.getProtectioncode()==0){
            textArea.setEditable(false);
            saveFilebtn.setEnabled(false);
        }

        try {
            textArea.setText(file.getText());
        }catch (Exception e){

        }
        deleteFilebtn.setEnabled(true);
        saveFilebtn.removeActionListener(saveFilebtn.getActionListeners()[0]);
        saveFilebtn.addActionListener(e -> {
            file.setText(textArea.getText());
            frame.dispose();
            refreshUserFileTable();
        });

        deleteFilebtn.addActionListener(e -> {
            tmpUser.deleteFile(file);
            frame.dispose();
            refreshUserFileTable();
        });
    }

    static void refreshUserTable(){
        final Object[] columnName = {"用户名","路径"," "};
        final Object[][] rowData = fileOS.showMFD();
        TableModel model = new AbstractTableModel() {
            @Override
            public int getRowCount() {
                return rowData.length;
            }

            @Override
            public int getColumnCount() {
                return columnName.length;
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                return rowData[rowIndex][columnIndex];
            }

            @Override
            public String getColumnName(int column){
                return columnName[column].toString();
            }

            @Override
            public boolean isCellEditable(int rowIndex,int columnIndex){
                return false;
            }
        };

        userTable.setModel(model);
        userTable.setEnabled(true);
    }

    static void refreshUserFileTable(){
        final  Object[] columnName = {"文件名","权限码","长度"," "};
        final Object[][] rowData = tmpUser.showUFD();
        TableModel model = new AbstractTableModel() {
            @Override
            public int getRowCount() {
                return rowData.length;
            }

            @Override
            public int getColumnCount() {
                return columnName.length;
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                return rowData[rowIndex][columnIndex];
            }

            @Override
            public String getColumnName(int column){
                return columnName[column].toString();
            }

            @Override
            public boolean isCellEditable(int rowIndex,int columnIndex){
                return columnIndex<2;
            }
        };


        userFileTable.setModel(model);
        userFileTable.setEnabled(true);
        userFileTable.getModel().addTableModelListener(e -> {
            int firstRow = e.getFirstRow();

            int column = e.getColumn();

            int type = e.getType();

            if(type == TableModelEvent.UPDATE){
                if(column==0){
                    tmpUser.selectFileModel(firstRow).setName(model.getValueAt(firstRow,column).toString());
                }else {
                    tmpUser.selectFileModel(firstRow).setProtectioncode( UtilView.stringToInt(model.getValueAt(firstRow,column).toString()));
                }
            }
        });
    }

}
