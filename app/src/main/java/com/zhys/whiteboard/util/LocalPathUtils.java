package com.zhys.whiteboard.util;

import com.zhys.whiteboard.WhiteBoardApplication;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @author zhouys
 * @date 2022/9/13
 * @description
 */
public class LocalPathUtils {
    private static String PATH = WhiteBoardApplication.sContext.getFilesDir().getPath();// 学生答题记录文件中的路径
    private static String pathFileName = "path.txt";// 本类输出的文件名称

    public static void saveObject(Object obj) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = new FileOutputStream(PATH + pathFileName);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(obj);
            oos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
                if (oos != null) {
                    oos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Object readObject() {
        FileInputStream fis = null;  //文件输入流
        ObjectInputStream ois = null;
        try {
            fis = new FileInputStream(PATH + pathFileName);
            ois = new ObjectInputStream(fis);
            return ois.readObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                if (ois != null) {
                    ois.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //所有异常返回null
        return null;
    }
}
