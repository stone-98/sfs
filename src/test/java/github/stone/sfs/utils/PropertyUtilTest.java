package github.stone.sfs.utils;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author shikui@tidu.com
 * @date 2021/10/22
 */
@Slf4j
public class PropertyUtilTest {
    @Test
    public void readPropertyTest() {
        log.info(PropertyUtil.getProperty("sfs.server.port"));
        log.info(PropertyUtil.getProperty("sfs.directory"));
    }
}
