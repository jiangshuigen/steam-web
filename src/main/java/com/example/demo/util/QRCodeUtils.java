package com.example.demo.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class QRCodeUtils {

    /**
     * 黑色
     */
    private static final int BLACK = 0xFF000000;
    /**
     * 白色
     */
    private static final int WHITE = 0xFFFFFFFF;
    /**
     * 宽
     */
    private static final int WIDTH = 2000;
    /**
     * 高
     */
    private static final int HEIGHT = 2000;

    /**
     * 图片高度增加60
     */
    private static final int PIC_HEIGHT = HEIGHT + 120;

    /**
     * 二维码传图片
     *
     * @param matrix
     * @return
     */
    public static BufferedImage toBufferedImage(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();

        BufferedImage image = new BufferedImage(width, PIC_HEIGHT, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < PIC_HEIGHT; y++) {
                image.setRGB(x, y, WHITE);
            }
        }
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
            }
        }
        return image;
    }

    /**
     * 生成二维码
     *
     * @param content 扫描二维码的内容
     * @param format  图片格式 jpg
     *                文件
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public static BufferedImage generateQrCode(String content, String format) throws Exception {

        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        @SuppressWarnings("rawtypes")
        Map hints = new HashMap();
        // 设置UTF-8， 防止中文乱码
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        // 设置二维码四周白色区域的大小
        hints.put(EncodeHintType.MARGIN, 5);
        // 设置二维码的容错性
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        // 画二维码
        BitMatrix bitMatrix = multiFormatWriter.encode(content, BarcodeFormat.QR_CODE, WIDTH, HEIGHT, hints);
        BufferedImage image = toBufferedImage(bitMatrix);
        return image;
    }


    /**
     * 把生成的图片写到指定路径
     *
     * @param qrcFile       路径
     * @param qrCodeContent 二维码内容
     * @param pressText     增加的文字
     * @throws Exception
     */
    public static void generateQrCodeByPath(File qrcFile, String qrCodeContent, String pressText) throws Exception {


        BufferedImage image = generateQrCode(qrCodeContent, "jpg");

        Graphics g = image.getGraphics();
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        //设置字体，大小
        Font font = new Font("黑体", Font.PLAIN, 150);
        g.setFont(font);
        g.setColor(Color.black);
        FontMetrics metrics = g.getFontMetrics(font);
        // 文字在图片中的坐标 这里设置在中间
        int startX = (WIDTH - metrics.stringWidth(pressText)) / 2;
        //  int startY=HEIGHT+(PIC_HEIGHT-HEIGHT)/2;  //文字在二维码上面
        int startY = PIC_HEIGHT - HEIGHT;  //文字在二维码下面
        g.drawString(pressText, startX, startY);

        g.dispose();
        image.flush();
        try {
            ImageIO.write(image, "jpg", qrcFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 把生成的图片返回到前端
     *
     * @param qrCodeContent 二维码内容
     * @param pressText     增加的文字
     * @throws Exception
     */
    public static BufferedImage generateQrCodeBack(String qrCodeContent, String pressText) throws Exception {


        BufferedImage image = generateQrCode(qrCodeContent, "jpg");

        Graphics g = image.getGraphics();
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        //设置字体
        Font font = new Font("黑体", Font.PLAIN, 150);
        g.setFont(font);
        g.setColor(Color.black);
        FontMetrics metrics = g.getFontMetrics(font);
        // 文字在图片中的坐标 这里设置在中间
        int startX = (WIDTH - metrics.stringWidth(pressText)) / 2;
        //  int startY=HEIGHT+(PIC_HEIGHT-HEIGHT)/2;  //文字在二维码下面
        int startY = PIC_HEIGHT - HEIGHT;  //文字在二维码上面
        g.drawString(pressText, startX, startY);

        g.dispose();
        image.flush();
        return image;

    }


}



