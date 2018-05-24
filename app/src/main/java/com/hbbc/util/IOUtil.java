package com.hbbc.util;

import java.io.Closeable;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 文件写入，读取，输入输出流工具类
 */
public class IOUtil {


    /**
     * 保存到日志文件
     */
    public static synchronized void write2SD(String content, String filename) {
        // 写入内容
        try {
            FileWriter writer = new FileWriter(filename, true);
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    /**
     * 关流
     */
    public static void closeStream(Closeable closeable) {

        try {
            if (closeable != null)
                closeable.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
