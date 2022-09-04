package org.example.reggie.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.reggie.common.R;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * 文件上传和下载
 */
@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {

    @Value("${reggie.path}")
    private String basePath;

    /**
     * 文件上传
     *
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) {
        log.info("上传文件：{}", file.getOriginalFilename());

//        原始文件名
        String originalFilename = file.getOriginalFilename();
//        获取原文件后缀
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
//        使用uuid重新生成文件名，防止重名文件覆盖
        String filename = UUID.randomUUID() + suffix;

//        若目录不存在，创建
        File dir = new File(basePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

//        转存文件
        try {
//            file.transferTo(new File(basePath + filename));
            file.transferTo(new File(dir.getAbsoluteFile(), filename));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        log.info("新文件名：{}", filename);
        return R.success(filename);
    }


    @GetMapping("/download")
    public void download(String name, HttpServletResponse response, HttpServletRequest request) {
        try {
//            FileInputStream fileInputStream = new FileInputStream(new File(basePath + name));
            File file = new File(basePath + name).getAbsoluteFile();
            FileInputStream fileInputStream = new FileInputStream(file);
//            FileInputStream fileInputStream = new FileInputStream(new File(request.getServletContext().getRealPath(basePath+name)));
//            InputStream fileInputStream = ReggieApplication.class.getClassLoader().getResourceAsStream(basePath + name);
            ServletOutputStream outputStream = response.getOutputStream();

            response.setContentType("image/jpeg");

            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = fileInputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
                outputStream.flush();
            }
//            关闭资源
            outputStream.close();
            fileInputStream.close();
        } catch (IOException e) {
            System.out.println();
            throw new RuntimeException(e);
        }

    }
}
