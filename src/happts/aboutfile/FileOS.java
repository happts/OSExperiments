package happts.aboutfile;

import java.io.*;
import java.util.LinkedList;

public class FileOS {
    final private String rootpath = "/Users/happts/Documents/javaProjects/OSE/src/fileOS";
    final private File  MFDFile = new File(rootpath+"/MainFileDictionary.txt");
    LinkedList<User> MFD = new LinkedList<>();

    public FileOS()throws Exception{
        readMFD(MFD);
    }

    private void readMFD(LinkedList<User> MFD)throws Exception{
        if (MFDFile.exists()){
            BufferedReader MFDBufferedReader = new BufferedReader(new FileReader(MFDFile));
            String line;
            String[] kV ;
            while ((line = MFDBufferedReader.readLine())!=null){
                kV = line.split(" ");
                MFD.add(new User(kV));
            }
            MFDBufferedReader.close();
        }else {
            MFDFile.createNewFile();
        }
    }

    public Boolean createUser(String username) throws Exception{
        for(User user:MFD){
            if(user.username.equals(username)){
                return false;
            }
        }

        User user = new User(username,rootpath+"/"+username);

        FileWriter MFDWriter = new FileWriter(MFDFile,true);
        MFDWriter.write(user.username+" "+user.FDpointer+"\n");
        MFDWriter.flush();
        MFDWriter.close();

        new File(user.FDpointer).mkdir();
        user.initUFD();
        MFD.add(user);

        return true;
    }

    public Object[][] showMFD(){
        Object[][] result = new Object[MFD.size()][3];
        for(User user: MFD){
            System.out.println(user.username+" "+user.FDpointer);
        }

        for(int i=0;i<MFD.size();i++){
            User user = MFD.get(i);
            result[i] = new Object[]{user.getUsername(),user.getFDpointer(),"打开"};
        }
        return result;
    }

    public User selectUser(int index){
        return MFD.get(index);
    }
}
