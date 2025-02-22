package com.example.evaluation.utils;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

import java.util.Random;
/*
验证码图片 = 位图 =
                验证码(随机生成不同的验证码)
                + 文本样式(随机生成不同的文本样式)
                + 间距(随机生成不同的间距)
                + 干扰线(随机生成不同的干扰线)
 */
public class CodeUtils {
    private static final char[] chars = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
    };
    private static CodeUtils codeUtils;
    private int paddingLeft, paddingTop;
    private StringBuilder builder = new StringBuilder();
    private Random random = new Random();

    private static final int DEFAULT_CODE_LENGTH = 6;   //验证码的位数
    private static final int DEFAULT_TEXT_SIZE = 60;    //字体大小
    private static final int DEFAULT_LINE_LENGTH = 3;   //干扰线的条数
    private static final int BASE_PADDING_LEFT = 20;    //左边距
    private static final int RANGE_PADDING_LEFT = 30;   //左边距的范围值
    private static final int BASE_PADDING_TOP = 70;     //上边距
    private static final int RANGE_PADDING_TOP = 15;    //上边距的范围值
    private static final int DEFAULT_WIDTH = 300;       //图片宽度
    private static final int DEFAULT_HEIGHT = 100;      //图片高度
    private static final int DEFAULT_COLOR = 0xDF;      //背景颜色值
    private String code;                                //存储验证码

    public String getCode() {
        return code;
    }

    public static CodeUtils getInstance() {
        if (codeUtils == null) {
            codeUtils = new CodeUtils();
        }
        return codeUtils;
    }

    //生成验证码图片(位图)
    public Bitmap createBitmap() {
        //创建位图 -> 根据位图创建画布 -> 设置画布的颜色 -> 创建画笔 -> 设置不规则的验证码(样式、间距) -> 添加干扰线
        paddingLeft = 0;    //初始化
        paddingTop = 0;     //初始化
        //创建位图
        Bitmap bitmap = Bitmap.createBitmap(DEFAULT_WIDTH , DEFAULT_HEIGHT, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);                                     //创建画布
        RectF rectF = new RectF(0,0,DEFAULT_WIDTH,DEFAULT_HEIGHT);     //构建RectF对象存放矩形坐标
        Paint paint = new Paint();                                              //创建画笔
        paint.setTextSize(DEFAULT_TEXT_SIZE);                                   //设置字体大小
        paint.setColor(Color.rgb(DEFAULT_COLOR,DEFAULT_COLOR,DEFAULT_COLOR));   //设置画笔颜色
        canvas.drawRoundRect(rectF, 18, 18, paint);                      //绘制圆角矩形
//        canvas.drawColor(Color.rgb(DEFAULT_COLOR,DEFAULT_COLOR,DEFAULT_COLOR)); //设置画布颜色

        code = createCode();                    //创建验证码
        //绘制验证码
        for (int i = 0; i < code.length(); i ++) {
            randomTextStyle(paint);     //获取随机的字体样式
            randomPadding();            //获取随机的字体间距
            canvas.drawText(code.charAt(i)+"",paddingLeft,paddingTop,paint); //绘制验证码
        }
        //绘制干扰线
        for (int i = 0; i < DEFAULT_LINE_LENGTH; i ++) {
            drawLine(canvas , paint);   //调用随机干扰线方法，绘制不规则干扰线
        }

        //二者配套使用
        canvas.save();      //保存
        canvas.restore();   //恢复,防止原canvas上的图，影响新的canvas上的图
        return bitmap;
    }

    //生成验证码
    private String createCode() {
        builder.delete(0,builder.length()); //使用之前首先清空内容
        for (int i = 0; i < DEFAULT_CODE_LENGTH; i ++) { //利用chars字符数组随机产生含有数字与字母的验证码
            builder.append(chars[ random.nextInt(chars.length) ]);
        }
        return builder.toString();
    }

    //随机干扰线
    private void drawLine(Canvas canvas, Paint paint) {
        int color = randomColor();                          //调用随机颜色方法，生成随机颜色
        int startX = random.nextInt(DEFAULT_WIDTH);         //随机开始位置(X1)
        int startY = random.nextInt(DEFAULT_HEIGHT);        //随机开始位置(Y1)
        int stopX = random.nextInt(DEFAULT_WIDTH);          //随机结束位置(X2)
        int stopY = random.nextInt(DEFAULT_HEIGHT);         //随机结束位置(Y2)
        paint.setColor(color);                              //设置干扰线颜色
        paint.setStrokeWidth(1);                            //设置线条大小
        canvas.drawLine(startX,startY,stopX,stopY,paint);   //绘画线条
    }

    //随机文本样式(单个字体的样式)
    private void randomTextStyle(Paint paint) {
        //随机颜色、粗体、倾斜
        int color = randomColor();                      //调用随机颜色方法,生成随机颜色
        paint.setColor(color);                          //设置字体颜色
        paint.setFakeBoldText(random.nextBoolean());    //随机true,false, true粗体,false非粗体
        int SkewX = random.nextInt(11)/10;       //随机倾斜度
        SkewX = random.nextBoolean() ? SkewX:-SkewX;    //随机倾斜方向
        paint.setTextSkewX(SkewX);  //float类型参数，负数表示右斜，整数左斜
    }

    //随机颜色
    private int randomColor() {
        builder.delete(0, builder.length());
        String haxString;
        for (int i = 0; i < 3; i ++) {
            //随机生成16进制的数，转换成16进制的字符串
            haxString = Integer.toHexString(random.nextInt(0xFF));
            if (haxString.length() == 1) {  //不足两位补'0',保证每次随机产生两位字符
                haxString = "0" + haxString;
            }
            builder.append(haxString); //将产生的随机字符连接在一起组成从而表示16进制的颜色码
        }
        return Color.parseColor("#"+builder.toString()); //用颜色解析器将颜色字符串解析成颜色码
    }

    //随机间距(让验证码不规则排列)
    private void randomPadding() {
        paddingLeft += BASE_PADDING_LEFT + random.nextInt(RANGE_PADDING_LEFT);
        paddingTop = BASE_PADDING_TOP + random.nextInt(RANGE_PADDING_TOP);
    }
}

