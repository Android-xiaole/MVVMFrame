package com.lee.base.utils;

/**
 * Author ：lee
 * Date ：2022/5/21 19:22
 * Description ：判断是否时候防快点工具类
 */
public class FastClickUtils {
    //防重复点击间隔(毫秒)
    public static final int MIN_CLICK_DELAY_TIME = 1000;
    private static long lastClickTime;

    /**
     * 判断是否是防快点
     *
     * @return true:是快点 false:不是快点
     */
    public static boolean isFastClick() {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) < MIN_CLICK_DELAY_TIME) {
            flag = true;
        }
        lastClickTime = curClickTime;
        return flag;
    }
} 
