package github.stone.sfs.model;

import github.stone.sfs.common.Constants;
import github.stone.sfs.utils.PropertyUtil;
import io.netty.util.internal.StringUtil;
import lombok.Data;

/**
 * @author candy
 * @date 2021/10/22
 */
@Data
public class SfsConfig {
    private int port;
    private String directory;

    public SfsConfig() {
        this.port = StringUtil.isNullOrEmpty(PropertyUtil.getProperty(Constants.PORT)) ? 0 : new Integer(PropertyUtil.getProperty(Constants.PORT));
        this.directory = PropertyUtil.getProperty(Constants.DIRECTORY);
    }
}
