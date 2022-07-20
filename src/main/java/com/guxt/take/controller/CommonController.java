package com.guxt.take.controller;


import com.guxt.take.common.R;
import com.guxt.take.utils.QiniuUtils;
import com.guxt.take.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("/common")
public class CommonController {
    @Autowired
    private RedisTemplate redisTemplate;
    @Resource
    private RedisUtils redisUtils;


    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){
        //原始文件名
        String originalFilename = file.getOriginalFilename();
        assert originalFilename != null;
        int index = originalFilename.lastIndexOf(".");
        //.jpg
        String extention = originalFilename.substring(index - 1);
        String fileName = UUID.randomUUID().toString() + extention;
        try {
            //将文件上传到七牛云服务器

            QiniuUtils.upload2Qiniu(file.getBytes(),fileName);
            //jedisPool.getResource().sadd(RedisConstant.FOOD_PIC_RESOURCES,fileName);
            redisUtils.save2Qiniu(fileName);
            //redisTemplate.opsForSet().add(RedisConstant.FOOD_PIC_RESOURCES,fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return R.error("图片上传失败！");
        }
        return R.success(fileName);
    }
}
