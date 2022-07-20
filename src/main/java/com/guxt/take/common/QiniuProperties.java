package com.guxt.take.common;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Component
@ConfigurationProperties(prefix = "qiniu")
@Data
public class QiniuProperties {
    private String accessKey;

    private String secretKey;

    private String bucket;

}
