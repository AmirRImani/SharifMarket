package sharedClasses;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;

public class FileOperator <E> {
    private File file;
    private FileWriter fileWriter;
    private BufferedReader bufferedReader;
    private GsonBuilder builder = new GsonBuilder();

    public File getFile() {
        return file;
    }

    public FileOperator(String fileName) {
        file = new File("resources\\"+fileName);
        try {
            if(file.createNewFile()){
                //TODO
            } else{

                //TODO
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void saveFile(File file,HashSet<E> hashSet,boolean append) throws IOException {
        Gson gson = builder.create();
        fileWriter = new FileWriter(file);
        fileWriter.write(gson.toJson(hashSet));
        fileWriter.close();
    }

    public void saveFile(File file,ArrayList<E> hashSet) throws IOException {
        Gson gson = builder.create();
        fileWriter = new FileWriter(file);
        fileWriter.write(gson.toJson(hashSet));
        fileWriter.close();
    }

    public HashSet<E> loadFile(File file) {
        HashSet<E> hashSet = new HashSet<>();
        Gson gson = builder.create();
        try {
            bufferedReader = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        hashSet = gson.fromJson(bufferedReader, HashSet.class);
        try {
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(hashSet == null)
            hashSet = new HashSet<>();
        return hashSet;
    }

    public ArrayList<E> loadFile(File file, int array) {
        ArrayList<E> arrayList = new ArrayList<>();
        Gson gson = builder.create();
        try {
            bufferedReader = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        arrayList = gson.fromJson(bufferedReader, ArrayList.class);
        try {
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(arrayList == null)
            arrayList = new ArrayList<>();
        return arrayList;
    }
}
