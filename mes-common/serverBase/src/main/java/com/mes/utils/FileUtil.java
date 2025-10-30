package com.mes.utils;

import org.springframework.core.io.FileSystemResource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * @Author : zhoush
 * @Date: 2025/8/12 11:02
 * @Description:
 */
public class FileUtil {
    /**
     * 使用FileSystemResource同步文件
     *
     * @param sourcePath    源文件完整路径
     * @param targetDirPath 目标目录路径
     * @throws IOException 当文件操作失败时抛出
     */
    public static void syncUsingResource(String sourcePath, String targetDirPath) throws IOException {
        // 1. 创建FileSystemResource对象
        FileSystemResource sourceResource = new FileSystemResource(sourcePath);

        // 2. 准备目标路径
        Path targetDir = Paths.get(targetDirPath);

        // 3. 检查源文件是否存在
        if (!sourceResource.exists()) {
            throw new IOException("源文件不存在: " + sourcePath);
        }

        // 4. 构建目标文件路径
        Path targetPath = targetDir.resolve(sourceResource.getFilename());

        // 5. 确保目标目录存在
        if (!Files.exists(targetDir)) {
            Files.createDirectories(targetDir);
        }

        // 6. 执行文件复制
        Files.copy(
                // 获取源文件的输入流
                sourceResource.getInputStream(),
                // 目标路径
                targetPath,
                // 覆盖已存在的文件
                StandardCopyOption.REPLACE_EXISTING
        );
    }
}
