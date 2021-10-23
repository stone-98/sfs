package github.stone.sfs.reader;

import github.stone.sfs.model.ReadContent;
import io.netty.util.internal.StringUtil;

import java.io.File;
import java.util.Arrays;

/**
 * @author stone
 * @date 2021/10/22
 */
public class FileReader {
    public ReadContent read(String path) {
        assert StringUtil.isNullOrEmpty(path);
        File file = new File(path);
        if (!file.exists()) {
            throw new IllegalStateException("The uri illegal.");
        }
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            ReadContent readContent = new ReadContent();
            readContent.setDirectory(Boolean.TRUE);
            readContent.setFiles(Arrays.asList(files));
            return readContent;
        } else {
            ReadContent readContent = new ReadContent();
            readContent.setFiles(Arrays.asList(file));
            readContent.setDirectory(Boolean.FALSE);
            return readContent;
        }
    }

}
