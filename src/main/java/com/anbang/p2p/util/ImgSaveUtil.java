package com.anbang.p2p.util;

import com.anbang.p2p.conf.VerifyConfig;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * @Description:
 */
public class ImgSaveUtil {
    static String PATH = VerifyConfig.IMG_PATH;

    //base64字符串转化成图片
    public static boolean generateImage(String imgStr, String imgName)
    {   //对字节数组字符串进行Base64解码并生成图片
        if (imgStr == null) //图像数据为空
            return false;
        BASE64Decoder decoder = new BASE64Decoder();
        try
        {
            //Base64解码
            byte[] b = decoder.decodeBuffer(imgStr);
            for(int i=0;i<b.length;++i)
            {
                if(b[i]<0)
                {//调整异常数据
                    b[i]+=256;
                }
            }
            //生成jpeg图片
            String imgFilePath = PATH + imgName;//新生成的图片(.jpg)
            OutputStream out = new FileOutputStream(imgFilePath);
            out.write(b);
            out.flush();
            out.close();
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }

    public static void getImg (String imgName, HttpServletResponse response) {
        String imgPath = PATH + imgName;
        File file = new File(imgPath);
        try {
            BufferedImage image = ImageIO.read(new File(imgPath));

            if (file.exists()) {
                ImageIO.write(image, "jpg", response.getOutputStream());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
