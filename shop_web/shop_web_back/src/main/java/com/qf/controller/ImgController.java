package com.qf.controller;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Controller
@RequestMapping("/imgs")
public class ImgController {

  /*  private static final String PATH="C:\\tup\\";*/
    @Autowired
    private FastFileStorageClient fastFileStorageClient;

    @RequestMapping("/uploader")
    @ResponseBody
    //参数名只能叫file,因为前台默认传的就是file
    public String upload(MultipartFile file){
        //获得点这个符号的下标
        int index=file.getOriginalFilename().lastIndexOf(".");
        //获得后缀,从.的后一位开始截取
        String houzhui=file.getOriginalFilename().substring(index+1);
        System.out.println("后缀为"+houzhui);
        try {
            //上传到dfs上
            StorePath storePath = fastFileStorageClient.uploadImageAndCrtThumbImage(file.getInputStream(), file.getSize(), houzhui, null);
            //获得上传的地址
            String url=storePath.getFullPath();
            System.out.println("上传的地址为"+url);
            //以json字符串的形式返回
            return "{\"loadpath\":\""+url+"\"}";

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
