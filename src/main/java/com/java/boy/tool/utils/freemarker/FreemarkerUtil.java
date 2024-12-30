package com.java.boy.tool.utils.freemarker;

import com.java.boy.tool.utils.SpringContextUtils;
import freemarker.template.Configuration;
import freemarker.template.Template;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.Map;

/**
 * @author 王青玄
 * @Contact 1121586359@qq.com
 * @create 2024年12月30日 14:53
 * @Description Freemarker工具类
 * @Version V1.0
 */
public class FreemarkerUtil {

    private static Configuration configuration;

    private static final String SUFFIX = ".doc";

    /**
     * 静态方法初始化Configuration
     */
    static {
        configuration = new Configuration(Configuration.VERSION_2_3_23);
        configuration.setDefaultEncoding("UTF-8");
        configuration.setClassForTemplateLoading(FreemarkerUtil.class, "/template/word");
    }

    public static void exportWord(Map map, String title, String ftlName) {
        File file = null;
        InputStream is = null;
        ServletOutputStream os = null;

        try {
            Template template = configuration.getTemplate(ftlName);
            file = createDocFile(map, template);
            is = new FileInputStream(file);
            String fileName = title + SUFFIX;
            HttpServletResponse response = SpringContextUtils.getHttpServletResponse();
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/msword");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "utf-8"));
            os = response.getOutputStream();
            byte[] buffer = new byte[512];
            int bytesToRead = -1;
            while ((bytesToRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesToRead);
            }
            if (is != null) {
                is.close();
            }
            if (os != null) {
                os.close();
            }
            if (file != null) {
                file.delete();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private static File createDocFile(Map map, Template template) throws Exception {
        File file = new File("init.doc");
        OutputStreamWriter os = new OutputStreamWriter(new FileOutputStream(file), "utf-8");
        template.process(map, os);
        os.close();
        return file;
    }

}
