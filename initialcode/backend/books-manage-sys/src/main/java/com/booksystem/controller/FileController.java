package com.booksystem.controller;

import com.booksystem.utils.IdFactoryUtil;
import com.booksystem.utils.PathUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
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
    @Value("${file.upload-path}")
    private String uploadPath; // 從 application.yml 讀取

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
            if (saveToFile(multipartFile, fileName)) {
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
            if (saveToFile(multipartFile, fileName)) {
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
//    public boolean uploadFile(MultipartFile multipartFile, String fileName) throws IOException {
//        return fileName(multipartFile, fileName);
//    }
//
//    public static boolean fileName(MultipartFile multipartFile, String fileName) throws IOException {
//        File fileDir = new File(PathUtils.getClassLoadRootPath() + "/pic");  //產生一個表示 /pic 資料夾的 File 物件(取得專案的根目錄)    D:/booksystem/initialcode/BackEnd/books-manage-sys/pic
//        if (!fileDir.exists()) { // 如果 /pic 資料夾還沒被建立 就呼叫 mkdirs() 嘗試建立它（包含中間的路徑也會一起建） 如果建立失敗，回傳 false 表示發生錯誤
//            if (!fileDir.mkdirs()) {
//                System.out.println("呼叫 mkdirs() 嘗試建立它");
//                return false;
//            }
//        }
//        File file = new File(fileDir.getAbsolutePath() + "/" + fileName);//建立一個 File 物件，代表你準備儲存檔案的「完整路徑」 D:/booksystem/initialcode/BackEnd/books-manage-sys/pic/55cee0bpic_4.jpg
//        if (file.exists()) {//如果該檔案已經存在，就刪除它（避免舊檔衝突）
//            if (!file.delete()) {//嘗試刪除這個 File 物件所代表的檔案或資料夾
//                return false;
//            }
//        }
//        if (file.createNewFile()) { //若指定的檔案不存在，就建立它，建立一個實體檔案（空的），true（建立成功）
//            multipartFile.transferTo(file);//將你上傳的檔案內容實際「寫入」磁碟檔案中
//            System.out.println("有到寫入multipartFile.transferTo(file)");
//            System.out.println("寫進磁碟裡的檔案路徑)"+fileDir.getAbsolutePath() + "/" + fileName);
//            return true;
//        }
//        return false;
//    }

    /**
     * 共用邏輯：儲存檔案
     */
    private boolean saveToFile(MultipartFile multipartFile, String fileName) throws IOException {
        File dir = new File(uploadPath);
        if (!dir.exists() && !dir.mkdirs()) {
            System.out.println(" 建立上傳目錄失敗: " + uploadPath);
            return false;
        }

        File file = new File(dir, fileName);
        if (file.exists() && !file.delete()) {
            System.out.println(" 刪除舊檔失敗: " + file.getAbsolutePath());
            return false;
        }

        multipartFile.transferTo(file);
        System.out.println(" 圖片寫入磁碟：" + file.getAbsolutePath());
        ///app/pic/3e3d681123.jfif
        return true;
    }

    /**
     * 查看圖片資源
     *
     * @param imageName 文件名
     * @param response  響應
     * @throws IOException 異常
     */
//    @GetMapping("/getFile")
//    public void getImage(@RequestParam("fileName") String imageName,
//                         HttpServletResponse response) throws IOException {
////        File fileDir = new File(PathUtils.getClassLoadRootPath() + "/pic"); //取得pic 資料夾的 File 物件(取得專案的根目錄) RENDER  file:/app/target/app.jar!/BOOT-INF/classes!
////        File image = new File(fileDir.getAbsolutePath() + "/" + imageName); //建立一個 File 物件，代表你準備讀取檔案的「完整路徑」指向磁碟中圖片實體的 Java File 物件 D:/booksystem/initialcode/BackEnd/books-manage-sys/pic/55cee0bpic_4.jpg
//        File image = new File(uploadPath, imageName);
//        if (image.exists()) { //如果存在就寫資料流傳到前端
//            FileInputStream fileInputStream = new FileInputStream(image);
//            byte[] bytes = new byte[fileInputStream.available()];
//            if (fileInputStream.read(bytes) > 0) {
//                OutputStream outputStream = response.getOutputStream();
//                outputStream.write(bytes);
//                outputStream.close();
//            }
//            fileInputStream.close();
//        }
//    }


// 建立一個 GET 請求的圖片 API，接收 fileName 參數
@GetMapping("/getFile")
public void getImage(@RequestParam("fileName") String fileName,
                     HttpServletResponse response) throws IOException {

    // 🔍 建立一個 File 物件，代表圖片在硬碟上的完整路徑：uploadPath + fileName
    File image = new File(uploadPath, fileName);

    // ❗ 如果檔案不存在，回傳 404 並寫出錯訊息
    if (!image.exists()) {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND); // 設定 HTTP 回應狀態為 404
        response.getWriter().write("圖片不存在: " + fileName); // 回傳錯誤訊息給前端
        return; // 中斷執行
    }

    // ✅ 判斷副檔名，設定正確的 Content-Type，讓瀏覽器知道這是什麼類型的圖片
    if (fileName.endsWith(".png")) {
        response.setContentType("image/png"); // PNG 格式
    } else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".jfif")) {
        response.setContentType("image/jpeg"); // JPEG 或 JFIF 格式（其實是一樣）
    } else if (fileName.endsWith(".gif")) {
        response.setContentType("image/gif"); // GIF 動圖格式
    } else {
        response.setContentType("application/octet-stream"); // 不明類型 ➜ 通用二進位資料流
    }

    // ✅ 使用 try-with-resources，自動關閉檔案與輸出串流，避免記憶體洩漏
    try (
            InputStream is = new FileInputStream(image);               // 開啟圖片檔案作為輸入流
            OutputStream os = response.getOutputStream()               // 取得回應的輸出流（要把資料寫回前端）
    ) {
        byte[] buffer = new byte[1024];                            // 建立緩衝區，每次讀取 1024 bytes
        int len;
        while ((len = is.read(buffer)) != -1) {                    // 只要還有資料，就不斷讀取與寫出
            os.write(buffer, 0, len);                              // 寫入剛剛讀到的資料給前端
        }
    }
}


}

