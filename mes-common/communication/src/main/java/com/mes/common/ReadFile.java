package com.mes.common;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import java.io.*;

/**
 * 读取文件类
 *
 * @author yzx
 * @version 1.0
 */
public class ReadFile {
    /**
     * 读取Json文件内容
     * @param fileUrl
     * @return
     * @throws IOException
     */
    public static JSONObject readJson(String fileUrl) throws IOException {
        // 资源路径（相对于resources根目录）

        String resourcePath = fileUrl;
        // 获取类加载器

        try (InputStream inputStream = new FileInputStream(resourcePath)) {
            if (inputStream == null) {
                throw new IOException("资源未找到: " + resourcePath);
            }
            StringBuilder content = new StringBuilder();
            // 读取文本内容
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
            String str = content.toString();

            return JSONObject.parseObject(str);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
