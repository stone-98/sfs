package github.stone.sfs.model;

import lombok.Data;

import java.io.File;
import java.util.List;

/**
 * @author stone
 * @date 2021/10/23 22:37
 */
@Data
public class ReadContent {
    private boolean isDirectory;
    private List<File> files;
}
