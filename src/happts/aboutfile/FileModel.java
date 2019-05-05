package happts.aboutfile;

import java.io.*;

public class FileModel {

    String name;
    int protectioncode;
    int lenth;
    User user;
    String fileLocation;

    FileModel(User user, String...KV){
        this.user = user;
        name = KV[0];
        protectioncode = stringToint(KV[1]);
        lenth = stringToint(KV[2]);
        this.fileLocation = user.FDpointer+"/"+name;
    }

    FileModel(User user,String name,int protectioncode,int lenth){
        this.user = user;
        this.name = name;
        this.protectioncode = protectioncode;
        this.lenth = lenth;
        this.fileLocation = user.FDpointer+"/"+name;
    }

    private static int stringToint(String string){
        int b=-1;
        try {
            b = Integer.valueOf(string).intValue();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return b;
    }

    public int getLenth() {
        return lenth;
    }

    public int getProtectioncode() {
        return protectioncode;
    }

    public String getName() {
        return name;
    }

    public String getFileLocation() {
        return fileLocation;
    }

    public void setName(String name){
        try {
            user.changeFilename(this,name);
            this.name = name;
        }catch (Exception e){

        }

    }

    public void setProtectioncode(int protectioncode) {
        try {
            user.changeProtectioncode(this, protectioncode);
            this.protectioncode = protectioncode;
        }catch (Exception e){

        }
    }

    public void setText(String text){
        File file = new File(fileLocation);
        this.lenth = text.length();
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(text);
            fileWriter.flush();
            fileWriter.close();

            user.changeLength(this,lenth);

        }catch (Exception e){

        }
    }

    public String getText() throws Exception{
        File file = new File(fileLocation);
        if(file.exists()){
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            StringBuffer stringBuffer = new StringBuffer();
            String line;
            while ((line = bufferedReader.readLine())!=null){
                stringBuffer.append(line);
            }

            return stringBuffer.toString();
        }else {
            return "文件丢失";
        }
    }
}
