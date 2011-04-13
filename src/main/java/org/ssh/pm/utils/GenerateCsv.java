package org.ssh.pm.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;

public class GenerateCsv {
    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        //generateCsvFile();

        System.out.println("配对写读:");
        randWrite("c:\\test.csv");

        System.out.println(randRead("c:\\test.csv"));

        generateCsvFile("c:\\test2.csv");
    }

    private static void generateCsvFile(String sFileName) {
        try {
            OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(sFileName), "utf-8");

            writer.append("姓名");
            writer.append(',');
            writer.append("Age");
            writer.append('\n');

            writer.append("MKYONG");
            writer.append(',');
            writer.append("26");
            writer.append('\n');

            writer.append("YOUR NAME");
            writer.append(',');
            writer.append("29");
            writer.append('\n');

            //generate whatever data you want
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //配对写,读
    public static void randWrite(String sFileName) {
        try {
            RandomAccessFile writer = new RandomAccessFile( sFileName, "rw");
            writer.writeUTF("中国你好");
            writer.writeUTF("姓名");
            writer.writeUTF(",");
            writer.writeUTF("Age");
            writer.writeUTF("\n");

            writer.writeUTF("MKYONG");
            writer.writeUTF(",");
            writer.writeUTF("26");
            writer.writeUTF("\n");

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String randRead(String sFileName) {
        try {
            RandomAccessFile raf = new RandomAccessFile( sFileName, "r");
            String str = raf.readUTF();
            str = str + "," + raf.readUTF();
            raf.close();

            return str;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
