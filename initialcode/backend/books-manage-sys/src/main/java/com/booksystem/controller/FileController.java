package com.booksystem.controller;

import com.booksystem.utils.IdFactoryUtil;
import com.booksystem.utils.PathUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 文件前端控制器
 *
 */
@RestController
@RequestMapping("/file")
public class FileController {

    @Value("${my-server.api-context-path}")
    private String API;

    /**
     * 文件上傳
     *
     * @param multipartFile 文件流
     * @return 響應
     */
    @PostMapping("/upload")
    public Map<String, Object> uploadFile(@RequestParam("file") MultipartFile multipartFile) {
        String uuid = IdFactoryUtil.getFileId();
        String fileName = uuid + multipartFile.getOriginalFilename();
        Map<String, Object> rep = new HashMap<>();
        try {
            if (uploadFile(multipartFile, fileName)) {
                rep.put("code", 200);
                rep.put("data", API+ "/file/getFile?fileName=" + fileName);
//                System.out.println("回傳前端JSON: " +API+ "/file/getFile?fileName=" + fileName);
                return rep;
            }
        } catch (IOException e) {
            rep.put("code", 400);
            rep.put("msg", "文件上傳異常");
            return rep;
        }
        rep.put("code", 400);
        rep.put("msg", "文件上傳異常");
        return rep;
    }

    /**
     * 視頻上傳
     *
     * @param multipartFile 文件流
     * @return 響應
     */
    @PostMapping("/video/upload")
    public Map<String, Object> videoUpload(@RequestParam("file") MultipartFile multipartFile) {
        String uuid = IdFactoryUtil.getFileId();
        String fileName = uuid + multipartFile.getOriginalFilename();
        Map<String, Object> rep = new HashMap<>();

        try {
            if (uploadFile(multipartFile, fileName)) {
                rep.put("code", 200);
                rep.put("data", API+ "/file/getFile?fileName=" + fileName);
                return rep;
            }
        } catch (IOException e) {
            rep.put("code", 400);
            rep.put("msg", "文件上傳異常");
            return rep;
        }
        rep.put("code", 400);
        rep.put("msg", "文件上傳異常");
        return rep;
    }

    /**
     * 上傳文件
     *
     * @param multipartFile 文件流
     * @param fileName      文件名
     * @return boolean
     * @throws IOException 異常
     */
    public boolean uploadFile(MultipartFile multipartFile, String fileName) throws IOException {
        return fileName(multipartFile, fileName);
    }

    public static boolean fileName(MultipartFile multipartFile, String fileName) throws IOException {
        File fileDir = new File(PathUtils.getClassLoadRootPath() + "/pic");
        if (!fileDir.exists()) {
            if (!fileDir.mkdirs()) {
                return false;
            }
        }
        File file = new File(fileDir.getAbsolutePath() + "/" + fileName);
        if (file.exists()) {
            if (!file.delete()) {
                return false;
            }
        }
        if (file.createNewFile()) {
            multipartFile.transferTo(file);
            return true;
        }
        return false;
    }

    /**
     * 查看圖片資源
     *
     * @param imageName 文件名
     * @param response  響應
     * @throws IOException 異常
     */
    @GetMapping("/getFile")
    public void getImage(@RequestParam("fileName") String imageName,
                         HttpServletResponse response) throws IOException {
        File fileDir = new File(PathUtils.getClassLoadRootPath() + "/pic");
        File image = new File(fileDir.getAbsolutePath() + "/" + imageName);
        if (image.exists()) {
            FileInputStream fileInputStream = new FileInputStream(image);
            byte[] bytes = new byte[fileInputStream.available()];
            if (fileInputStream.read(bytes) > 0) {
                OutputStream outputStream = response.getOutputStream();
                outputStream.write(bytes);
                outputStream.close();
            }
            fileInputStream.close();
        }
    }

}

