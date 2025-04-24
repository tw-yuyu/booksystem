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
    private String API; //  /api/book-manage-sys-api/v1.0     上線Render後要改成 https://booksystem-jgi1.onrender.com/api/book-manage-sys-api/v1.0

    /**
     * 文件上傳
     *
     * @param multipartFile 文件流
     * @return 響應
     */
    @PostMapping("/upload")
    public Map<String, Object> uploadFile(@RequestParam("file") MultipartFile multipartFile) {
        String uuid = IdFactoryUtil.getFileId();//產生唯一檔案名（避免檔名衝突）
        String fileName = uuid + multipartFile.getOriginalFilename();//uuid+取得使用者原始上傳檔案的檔名
        Map<String, Object> rep = new HashMap<>();
        try {
            if (uploadFile(multipartFile, fileName)) {
                rep.put("code", 200);
                rep.put("data", API+ "/file/getFile?fileName=" + fileName);//System.out.println("回傳前端JSON: " +API+ "/file/getFile?fileName=" + fileName);     /api/book-manage-sys-api/v1.0/file/getFile?fileName=55cee0bpic_4.jpg
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
                rep.put("data", API+ "/file/getFile?fileName=" + fileName); //  /api/book-manage-sys-api/v1.0/file/getFile?fileName=55cee0bpic_4.jpg
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
        File fileDir = new File(PathUtils.getClassLoadRootPath() + "/pic");  //產生一個表示 /pic 資料夾的 File 物件(取得專案的根目錄)    D:/booksystem/initialcode/BackEnd/books-manage-sys/pic
        if (!fileDir.exists()) { // 如果 /pic 資料夾還沒被建立 就呼叫 mkdirs() 嘗試建立它（包含中間的路徑也會一起建） 如果建立失敗，回傳 false 表示發生錯誤
            if (!fileDir.mkdirs()) {
                System.out.println("呼叫 mkdirs() 嘗試建立它");
                return false;
            }
        }
        File file = new File(fileDir.getAbsolutePath() + "/" + fileName);//建立一個 File 物件，代表你準備儲存檔案的「完整路徑」 D:/booksystem/initialcode/BackEnd/books-manage-sys/pic/55cee0bpic_4.jpg
        if (file.exists()) {//如果該檔案已經存在，就刪除它（避免舊檔衝突）
            if (!file.delete()) {//嘗試刪除這個 File 物件所代表的檔案或資料夾
                return false;
            }
        }
        if (file.createNewFile()) { //若指定的檔案不存在，就建立它，建立一個實體檔案（空的），true（建立成功）
            multipartFile.transferTo(file);//將你上傳的檔案內容實際「寫入」磁碟檔案中
            System.out.println("有到寫入multipartFile.transferTo(file)");
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
        File fileDir = new File(PathUtils.getClassLoadRootPath() + "/pic"); //取得pic 資料夾的 File 物件(取得專案的根目錄)
        File image = new File(fileDir.getAbsolutePath() + "/" + imageName); //建立一個 File 物件，代表你準備讀取檔案的「完整路徑」指向磁碟中圖片實體的 Java File 物件 D:/booksystem/initialcode/BackEnd/books-manage-sys/pic/55cee0bpic_4.jpg
        if (image.exists()) { //如果存在就寫資料流傳到前端
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

