package com.lkm.menu.util;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.Version;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class WordUtils {

    /**
     * 生成 word 文档方法
     *
     * @param dataMap      要填充的数据
     * @param templateName 模版名称
     * @param fileName     要输出的文件路径
     * @throws Exception 抛出的异常
     */
    public static void generateWord(Map<String, Object> dataMap, String templateName, String fileName) throws Exception {

        // 设置FreeMarker的版本和编码格式
        Configuration configuration = new Configuration(new Version("2.3.28"));
        configuration.setDefaultEncoding("UTF-8");

        // 设置FreeMarker生成Word文档所需要的模板的路径
        // configuration.setDirectoryForTemplateLoading(new File("/Users/xxx/Desktop/"));
        // 此处把模版文件都放在 resources 下的 templates 中
        configuration.setClassForTemplateLoading(WordUtils.class, "/templates");

        // 设置FreeMarker生成Word文档所需要的模板
        Template tem = configuration.getTemplate(templateName, "UTF-8");
        // 创建一个Word文档的输出流
        Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(fileName)), StandardCharsets.UTF_8));
        // FreeMarker使用Word模板和数据生成Word文档
        tem.process(dataMap, out);
        out.flush();
        out.close();
    }

    public static void generateWord2(HttpServletResponse response) throws Exception {
        Map<String, Object> dataMap = initData();
        // 设置FreeMarker的版本和编码格式
        Configuration configuration = new Configuration(new Version("2.3.28"));
        configuration.setDefaultEncoding("UTF-8");

        // 设置FreeMarker生成Word文档所需要的模板的路径
        // configuration.setDirectoryForTemplateLoading(new File("/Users/xxx/Desktop/"));
        // 此处把模版文件都放在 resources 下的 templates 中
        configuration.setClassForTemplateLoading(WordUtils.class, "/templates");

        // 设置FreeMarker生成Word文档所需要的模板
        Template tem = configuration.getTemplate("zbgzh.ftl", "UTF-8");
        // 创建一个Word文档的输出流
        Writer out = new BufferedWriter(new OutputStreamWriter(response.getOutputStream(), StandardCharsets.UTF_8));

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/msword");//doc
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("工作函.doc", "UTF-8"));
        // FreeMarker使用Word模板和数据生成Word文档
        tem.process(dataMap, out);
        out.flush();
        out.close();
    }

    public static Map<String,Object> initData(){
        Map<String, Object> data = new HashMap<>();
        data.put("name", "张三");
        data.put("org_name", "广东分公司");
        data.put("dept_name", "企信部");
        data.put("temptrans_dept_name", "人力资源部");
        data.put("begindate", "2022-11-08");
        data.put("enddate", "2022-11-09");
        data.put("billmaker_name", "王五");
        data.put("mobile", "13322222222");
        data.put("create_date", "2022-11-07");
        return data;
    }

    public static void main(String[] args) {
        Map<String, Object> data = WordUtils.initData();
        String templateName = "zbgzh.ftl";
        try {
            WordUtils.generateWord(data, templateName, "C:\\Users\\s8758\\Desktop\\aaa.docx");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
