package com.keywordstone.contractmanagement.contract.api.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
public class FileUtil {

    @Value("${fastdfs.httpurl}")
    private String confighttpurl;

    /**
     * Description: 文件批量下载
     *
     * @param filename
     * @return
     * @see
     */
    public static File newFiles(byte[] buffer,String filename) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        String storagePath = System.getProperty("java.io.tmpdir") + File.separator;
                try {
                    // docx模板的存储路径
                    File dir = new File(storagePath);
                    if (!dir.exists() && dir.isDirectory()) {// 判断文件目录是否存在
                        dir.mkdirs();
                    }
                    file = new File(storagePath + "\\" + filename);
                    fos = new FileOutputStream(file);
                    bos = new BufferedOutputStream(fos);
                    bos.write(buffer);
                    return file;
                } catch (IOException e) {
                    log.error(e.getMessage());
                } finally {
                    if (bos != null) {
                        try {
                            bos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
        return null;
    }



    /**
     * Description: 获取文件后缀名
     *
     * @param fileName
     * @return
     * @see
     */
    public static String getExtensionName(String fileName) {
        String prefix = fileName.substring(fileName.lastIndexOf(".") + 1);
        return prefix;
    }

    /**
     * 根据path获取文件名
     *
     * @param filename
     * @return
     * @author kokJuis
     * @version 1.0
     * @date 2016-12-12
     */
    public static String getOriginalFilename(String filename) {
        if (filename == null) {
            return "";
        }
        int pos = filename.lastIndexOf(".");
        if (pos == -1) {
            pos = filename.lastIndexOf("\\");
        }
        if (pos != -1) {
            return filename.substring(pos);
        } else {
            return filename;
        }
    }

    public static String getHttpUrl(String groupFileId){
        int pos = groupFileId.indexOf("/");
        String filepath = groupFileId.substring(pos);
        return filepath;
    }

    public static String getGroupUrl(String url){
        String filepath = url.substring(0,5);
        return filepath;
    }

    public static String getUUID(){
        UUID uuid=UUID.randomUUID();
        String str = uuid.toString();
        String uuidStr=str.replace("-", "");
        return uuidStr;
    }

    /**
     * 功能:压缩多个文件成一个zip文件
     * @param srcfile：源文件列表
     * @param zipfile：压缩后的文件
     */
    public static byte[] zipFiles(List<File> srcfile, File zipfile){
        byte[] buf=new byte[1024*10];
        try {
            //ZipOutputStream类：完成文件或文件夹的压缩
            ZipOutputStream out=new ZipOutputStream(new FileOutputStream(zipfile));
            for(int i=0;i<srcfile.size();i++){
                FileInputStream in=new FileInputStream(srcfile.get(i));
                out.putNextEntry(new ZipEntry(srcfile.get(i).getName()));
                int len;
                while((len=in.read(buf))>0){
                    out.write(buf,0,len);
                }
                out.closeEntry();
                in.close();
            }
            out.close();
            return buf;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 功能:list去重
     * @param arlList：去重list
     */
    public static List<String> getclearlist(List arlList){
        Set set = new HashSet();
        List newList = new ArrayList();
        for (Iterator iter = arlList.iterator(); iter.hasNext(); )
        {
            Object element = iter.next();
            if (set.add(element)) newList.add(element);
        }
        arlList.clear();
        arlList.addAll(newList);
        return arlList;
    }
}
