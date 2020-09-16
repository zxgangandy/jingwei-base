package io.jingwei.common.lock;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * UID 的配置
 *
 * @author Andy
 * @date 2019.11.15 10:31
 */
@ConfigurationProperties(prefix = "dblock")
public class LockProperties {

    private int expire = 60;

    public int getExpire() {
        return expire;
    }

    public void setExpire(int expire) {
        this.expire = expire;
    }
}
