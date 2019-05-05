package happts.aboutfile;

import java.io.*;
import java.util.LinkedList;

public class User {
    String username;
    String FDpointer;

    LinkedList<FileModel> UFD;
    File UFDFile;

    User(String... KV) throws Exception{
        this.username = KV[0];
        this.FDpointer = KV[1];
    }

    public void initUFD() {
        UFD = new LinkedList<>();
        try {
            UFDFile = new File(FDpointer + "/UserFileDictionary.txt");
            if (UFDFile.exists()) {
                BufferedReader MFDBufferedReader = new BufferedReader(new FileReader(UFDFile));
                String line;
                String[] kV;
                while ((line = MFDBufferedReader.readLine()) != null) {
                    kV = line.split(" ");
                    UFD.add(new FileModel(this, kV));
                }
                MFDBufferedReader.close();
            } else {
                UFDFile.createNewFile();
            }
        }catch (Exception e){

        }
    }

    public Object[][] showUFD(){
        Object[][] result = new Object[UFD.size()][3];
        for(FileModel fileModel: UFD){
            System.out.println(fileModel.name+" "+fileModel.protectioncode+" "+fileModel.lenth);
        }
        for(int i=0;i<UFD.size();i++){
            FileModel fileModel = UFD.get(i);
            result[i] = new Object[] {fileModel.name,fileModel.protectioncode,fileModel.lenth,"打开"};
        }
        return result;

    }

    public Boolean createFile(String filename,int protectioncode,String text){
        for(FileModel fileModel: UFD){
            if(fileModel.name.equals(filename)){
                return false;
            }
        }

        FileModel fileModel = new FileModel(this,filename,protectioncode,text.length());

        File file = new File(FDpointer+"/"+fileModel.name);

        try {
            file.createNewFile();
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(text);
            fileWriter.flush();
            fileWriter.close();


            FileWriter UFDWriter = new FileWriter(UFDFile, true);
            UFDWriter.write(fileModel.name + " " + fileModel.protectioncode + " " + fileModel.lenth + "\n");
            UFDWriter.flush();
            UFDWriter.close();
        }catch (Exception e){

        }

        UFD.add(fileModel);

        return true;
    }
    public void deleteFile(FileModel fileModel){
        File file = new File(fileModel.fileLocation);
        file.delete();

        UFDFile.delete();
        UFDFile = new File(FDpointer + "/UserFileDictionary.txt");

        try {
            FileWriter UFDWriter = new FileWriter(UFDFile);
            for(int i=0;i<UFD.size();i++){
                FileModel fm = UFD.get(i);
                if(fm == fileModel){
                    UFD.remove(i);
                    continue;
                }
                UFDWriter.write(fm.name + " " + fm.protectioncode + " " + fm.lenth + "\n");
                UFDWriter.flush();
            }
            UFDWriter.close();
        }catch (Exception e){

        }

    }

    public Boolean changeLength(FileModel fileModel,int length) throws IOException{
        RandomAccessFile raf = new RandomAccessFile(UFDFile,"rwd");
        long now;
        String[] KV;
        String oneline;
        do{
            now = raf.getFilePointer();
            oneline= raf.readLine();
            KV = oneline.split(" ");
            if(fileModel.name.equals(KV[0])){
                raf.seek(now);
                String updata = fileModel.name+" "+fileModel.protectioncode+" "+length;
                raf.write(updata.getBytes());
                for(int i=0;i<oneline.length()-updata.length();i++) {
                    raf.write(" ".getBytes());
                }
                raf.write("\n".getBytes());
                return true;
            }
        }while (oneline!=null);

        return false;
    }
    public Boolean changeFilename(FileModel fileModel, String newname) throws IOException{
        File file = new File(FDpointer+"/"+fileModel.name);
        file.renameTo(new File(FDpointer+"/"+newname));
        //修改UFD
        RandomAccessFile raf = new RandomAccessFile(UFDFile,"rwd");
        long now;
        String[] KV;
        String oneline;
        do{
           now = raf.getFilePointer();
           oneline= raf.readLine();
           KV = oneline.split(" ");
           if(fileModel.name.equals(KV[0])){
               raf.seek(now);
               String updata = newname+" "+fileModel.protectioncode+" "+fileModel.lenth;
               raf.write(updata.getBytes());
               for(int i=0;i<oneline.length()-updata.length();i++) {
                   raf.write(" ".getBytes());
               }
               raf.write("\n".getBytes());
               return true;
           }
        }while (oneline!=null);

        return false;
    }

    public Boolean changeProtectioncode (FileModel fileModel,int newcode)throws IOException{
        RandomAccessFile raf = new RandomAccessFile(UFDFile,"rwd");
        long now;
        String[] KV;
        String oneline;
        do{
            now = raf.getFilePointer();
            oneline=raf.readLine();
            KV = oneline.split(" ");
            if(KV[0].equals(fileModel.name)){
                raf.seek(now);
                String updata = fileModel.name+" "+newcode+" "+fileModel.lenth+"\n";
                raf.write(updata.getBytes());
                return true;
            }
        }while (oneline!=null);

        return false;
    }

    public FileModel selectFileModel(int index){
        return UFD.get(index);
    }

    public String getUsername() {
        return username;
    }

    public String getFDpointer() {
        return FDpointer;
    }
}
