package com.nchu.lecturerplatform.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ReadFile {
    /*
     * 读取指定路径下的文件名和目录名
     */
    public List<String> getFileNameList(Long id,String dateType) {//课程数据目录文件名称列表

        List<String> fileNameList=new ArrayList<>();

        File file = new File("D:\\LecturerPlatform\\courses\\"+dateType+"\\"+id+"\\");

        if (!file.exists()) {
            file.mkdirs();
        }

        File[] fileList = file.listFiles();

        for (int i = 0; i < fileList.length; i++) {
            if (fileList[i].isFile()) {
                String fileName = fileList[i].getName();
                fileNameList.add(fileName);
                System.out.println("文件：" + fileName);
            }

            /*if (fileList[i].isDirectory()) {
                String fileName = fileList[i].getName();
                System.out.println("目录：" + fileName);
            }*/
        }

        return fileNameList;
    }

}
