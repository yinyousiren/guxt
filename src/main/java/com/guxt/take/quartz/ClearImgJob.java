package com.guxt.take.quartz;


import com.fasterxml.jackson.databind.annotation.JsonAppend;
import com.guxt.take.common.RedisConstant;
import com.guxt.take.utils.QiniuUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.quartz.QuartzJobBean;


import javax.annotation.Resource;
import java.util.Set;

public class ClearImgJob extends QuartzJobBean {

    @Autowired
    private RedisTemplate redisTemplate;


    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        Set<String> set = redisTemplate.opsForSet().difference(RedisConstant.FOOD_PIC_RESOURCES, RedisConstant.FOOD_PIC_DB_RESOURCES);

        if (set!=null){
            for (String picName : set){
                QiniuUtils.deleteFileFromQiniu(picName);
                redisTemplate.opsForSet().remove(RedisConstant.FOOD_PIC_RESOURCES,picName);
                System.out.println("定时任务执行成功！清理垃圾图片："+picName);
            }
        }

    }
}
