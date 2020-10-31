package me.weekbelt.community.infra.fileUtils;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.UUID;

public class ImageFile {
    public static String makeImageFileName(String originalName) {
        UUID uuid = UUID.randomUUID();
        return uuid.toString() + "_" + originalName;
    }


    public static void saveImageFileToLocal(MultipartFile image, String saveAddr) {
        try(FileOutputStream fileOutputStream = new FileOutputStream(saveAddr);
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
                FileInputStream fileInputStream = (FileInputStream) image.getInputStream();
                BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream)) {

            int readCount;
            byte[] buffer = new byte[1024];

            while((readCount = bufferedInputStream.read(buffer)) != -1) {
                bufferedOutputStream.write(buffer, 0, readCount);
            }

        } catch (IOException e) {
            throw new RuntimeException("File Save Error");
        }

    }
}
