package com.hemaapp.luna_demo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.hemaapp.luna_demo.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import xtom.frame.util.XtomFileUtil;

/**
 * Created by HuHu on 2016-06-30.
 */
public class ReadDocxActivity extends Activity {
    private String filePath = "/sdcard/hehe.docx";
    private String htmlPath = "/sdcard/hehe.html";
    private FileOutputStream output;
    private WebView webView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        webView = (WebView) findViewById(R.id.webView);
        readDocx();

    }

    /**
     * 读取docx
     */
    private void readDocx() {
        String river = "";
        try {
            File file = new File(htmlPath);
            output = new FileOutputStream(file);
            String head = "<!DOCTYPE><html><meta charset=\"utf-8\"><body>";// 定义头文件,我在这里加了utf-8,不然会出现乱码
            String end = "</body></html>";
            String tagBegin = "<p>";// 段落开始,标记开始?
            String tagEnd = "</p>";// 段落结束
            String tableBegin = "<table style=\"border-collapse:collapse\" border=1 bordercolor=\"black\">";
            String tableEnd = "</table>";
            String rowBegin = "<tr>";
            String rowEnd = "</tr>";
            String colBegin = "<td>";
            String colEnd = "</td>";
            output.write(head.getBytes());// 写如头部
            ZipFile xlsxFile = new ZipFile(new File(this.filePath));
            ZipEntry sharedStringXML = xlsxFile.getEntry("word/document.xml");
            InputStream inputStream = xlsxFile.getInputStream(sharedStringXML);
            XmlPullParser xmlParser = Xml.newPullParser();
            xmlParser.setInput(inputStream, "utf-8");
            int evtType = xmlParser.getEventType();
            boolean isColor = false; // 颜色状态
            boolean isCenter = false; // 居中状态
            boolean isRight = false; // 居右状态
            boolean isItalic = false; // 是斜体
            boolean isUnderline = false; // 是下划线
            boolean isBold = false; // 加粗
            boolean isR = false; // 在那个r中
            boolean isBack = false;
            int pictureIndex = 1; // docx 压缩包中的图片名 image1 开始 所以索引从1开始

            String style = "";//保存文本的样式表
            while (evtType != XmlPullParser.END_DOCUMENT) {
                switch (evtType) {
                    // 开始标签
                    case XmlPullParser.START_TAG:
                        String tag = xmlParser.getName();
                        System.out.println(tag);

                        if (tag.equalsIgnoreCase("p")) {//检测到p标签，即开启新段落
                            this.output.write(tagBegin.getBytes());
                        }
                        if (tag.equalsIgnoreCase("r")) {//检测到新的文本集，要清空样式表
                            isR = true;
                            style = "";
                        }
                        if (tag.equalsIgnoreCase("u")) {//判断下划线
                            String mode = xmlParser.getAttributeValue(0);
                            if ("single".equals(mode))//其他模式都不划线
                                isUnderline = true;
                        }
                        if (tag.equalsIgnoreCase("jc")) {//判断对齐方式
                            String align = xmlParser.getAttributeValue(0);
                            if (align.equals("center")) {
                                this.output.write("<center>".getBytes());
                                isCenter = true;
                            }
                            if (align.equals("right")) {
                                this.output.write("<div align=\"right\">".getBytes());
                                isRight = true;
                            }
                        }
                        if (tag.equalsIgnoreCase("rFonts")) {//改字体
                            String fontFamily = xmlParser.getAttributeValue(0);
                            style += "font-family:" + fontFamily + ";";
//                            style += "font-family:Microsoft Yahei;";
                        }
                        if (tag.equalsIgnoreCase("color")) { // 判断颜色
                            String color = xmlParser.getAttributeValue(0);
                            style += "color:#" + color + ";";
                        }
                        if (tag.equalsIgnoreCase("sz")) { // 判断大小
                            int size = Integer.valueOf(xmlParser.getAttributeValue(0)) / 2;
                            style += "font-size:" + size + "px;";
                        }
                        if (tag.equalsIgnoreCase("highlight")) {//判断高亮背景色
                            String color = xmlParser.getAttributeValue(0);
                            style += "background:" + color + ";";
                        }
                        // 下面是表格处理
                        if (tag.equalsIgnoreCase("tbl")) { // 检测到tbl 表格开始
                            this.output.write(tableBegin.getBytes());
                        }
                        if (tag.equalsIgnoreCase("tr")) { // 行
                            this.output.write(rowBegin.getBytes());
                        }
                        if (tag.equalsIgnoreCase("tc")) { // 列
                            this.output.write(colBegin.getBytes());
                        }
                        if (tag.equalsIgnoreCase("pic")) { // 检测到标签 pic 图片
                            String entryName_jpeg = "word/media/image"
                                    + pictureIndex + ".jpeg";
                            String entryName_png = "word/media/image"
                                    + pictureIndex + ".png";
                            String entryName_gif = "word/media/image"
                                    + pictureIndex + ".gif";
                            ZipEntry sharePicture = null;
                            InputStream pictIS = null;
                            sharePicture = xlsxFile.getEntry(entryName_jpeg);
                            // 一下为读取docx的图片 转化为流数组
                            if (sharePicture == null) {
                                sharePicture = xlsxFile.getEntry(entryName_png);
                            }
                            if (sharePicture == null) {
                                sharePicture = xlsxFile.getEntry(entryName_gif);
                            }
                            pictIS = xlsxFile.getInputStream(sharePicture);
                            ByteArrayOutputStream pOut = new ByteArrayOutputStream();
                            byte[] bt = null;
                            byte[] b = new byte[1024];
                            int len = 0;
                            while ((len = pictIS.read(b)) != -1) {
                                pOut.write(b, 0, len);
                            }
                            pictIS.close();
                            pOut.close();
                            bt = pOut.toByteArray();
                            Log.i("byteArray", "" + bt);
                            if (pictIS != null)
                                pictIS.close();
                            if (pOut != null)
                                pOut.close();
                            /*writeDOCXPicture(bt);*/
                            pictureIndex++; // 转换一张后 索引+1
                        }
                        if (tag.equalsIgnoreCase("b")) { // 检测到加粗标签
                            isBold = true;
                        }
                        if (tag.equalsIgnoreCase("i")) { // 斜体
                            isItalic = true;
                        }
                        // 检测到值 标签
                        if (tag.equalsIgnoreCase("t")) {
                            if (isBold == true) { // 加粗
                                this.output.write("<b>".getBytes());
                            }
                            if (isUnderline == true) { // 检测到下划线标签,输入<u>
                                this.output.write("<u>".getBytes());
                            }
                            if (isItalic == true) { // 检测到斜体标签,输入<i>
                                output.write("<i>".getBytes());
                            }

                            river = xmlParser.nextText();
                            if (isR == true) { // 文本集结束
                                String span = "<span style=\"" + style + "\">" + river + "</span>";
                                this.output.write(span.getBytes());
                                isR = false;
                            }

                            if (isItalic == true) { // 检测到斜体标签,在输入值之后,输入</i>,并且斜体状态=false
                                this.output.write("</i>".getBytes());
                                isItalic = false;
                            }
                            if (isUnderline == true) {// 检测到下划线标签,在输入值之后,输入</u>,并且下划线状态=false
                                this.output.write("</u>".getBytes());
                                isUnderline = false;
                            }
                            if (isBold == true) { // 加粗
                                this.output.write("</b>".getBytes());
                                isBold = false;
                            }
                            if (isColor == true) { // 检测到颜色设置存在,输入结束标签
                                this.output.write("</span>".getBytes());
                                isColor = false;
                            }
                            if (isCenter == true) { // 检测到居中,输入结束标签
                                this.output.write("</center>".getBytes());
                                isCenter = false;
                            }
                            if (isRight == true) { // 居右不能使用<right></right>,使用div可能会有状况,先用着
                                this.output.write("</div>".getBytes());
                                isRight = false;
                            }
                            if (isBack) {
                                this.output.write("</span>".getBytes());
                                isColor = false;
                            }
                        }
                        break;
                    // 结束标签
                    case XmlPullParser.END_TAG:
                        String tag2 = xmlParser.getName();
                        if (tag2.equalsIgnoreCase("tbl")) { // 检测到表格结束,更改表格状态
                            this.output.write(tableEnd.getBytes());
                        }
                        if (tag2.equalsIgnoreCase("tr")) { // 行结束
                            this.output.write(rowEnd.getBytes());
                        }
                        if (tag2.equalsIgnoreCase("tc")) { // 列结束
                            this.output.write(colEnd.getBytes());
                        }
                        if (tag2.equalsIgnoreCase("p")) { // p结束,如果在表格中就无视
                            this.output.write(tagEnd.getBytes());
                        }
                        if (tag2.equalsIgnoreCase("r")) {
                            isR = false;
                        }
                        break;
                    default:
                        break;
                }
                evtType = xmlParser.next();
            }
            this.output.write(end.getBytes());
        } catch (ZipException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        if (river == null) {
            river = "解析文件出现问题";
        }

        webView.loadUrl("file:" + htmlPath);
    }
}
