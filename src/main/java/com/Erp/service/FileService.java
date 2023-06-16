package com.Erp.service;

import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

@Service
@Log
public class FileService {

    // 이미지 저장을 위한 메소드
    public String uploadFile(String upLoadPath, String originalFileName,byte[] fileData) throws Exception{
        UUID uuid = UUID.randomUUID();
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String savedFileName=uuid.toString()+extension;

        String fileUploadFullUrl= upLoadPath+"/"+savedFileName;

        FileOutputStream fos = new FileOutputStream(fileUploadFullUrl);

        fos.write(fileData);

        fos.close();

        return savedFileName;
    }
}
