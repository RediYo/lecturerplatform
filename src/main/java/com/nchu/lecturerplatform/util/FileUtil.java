package com.nchu.lecturerplatform.util;

import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;
import ws.schild.jave.EncoderException;
import ws.schild.jave.MultimediaInfo;
import ws.schild.jave.MultimediaObject;

import java.io.File;

@Slf4j
public class FileUtil {

    private static ImmutableList<String> videoSuffixList = ImmutableList.of("mp4", "mov", "avi", "mkv", "m4v", "wmv",
            "asf", "asx", "rm", "rmvb", "3gp", "dat", "flv", "vob");

    /**
     * 获取视频时长 * @param file 视频文件
     *
     * @return 时长（秒）
     */
    public static String getVideoDuration(File dest) {
        int duration = 0;
        MultimediaObject multimediaObject = new MultimediaObject(dest);
        MultimediaInfo info = null;
        try {
            info = multimediaObject.getInfo();
        } catch (EncoderException e) {
            e.printStackTrace();
        }
        duration = (int) Math.ceil((double) info.getDuration() / 1000);
        return secToTime(duration);
    }

    public static String getFileExtension(String fileName) {

        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0){
            return fileName.substring(fileName.lastIndexOf("."));
        } else {
            return "";
        }

    }


    /**
     * 判断文件是否是视频 * @param file 文件
     *
     * @return 是否是视频
     */
    public static boolean isVideo(MultipartFile file) {
        return videoSuffixList.contains(FilenameUtils.getExtension(file.getOriginalFilename()));
    }

    public static String secToTime(int time) {
        String timeStr = null;

        int hour = 0;

        int minute = 0;

        int second = 0;

        if (time <= 0)

            return "00:00";

        else {
            minute = time / 60;

            if (minute < 60) {
                second = time % 60;

                timeStr = unitFormat(minute) + ":" + unitFormat(second);

            } else {
                hour = minute / 60;

                if (hour > 99)

                    return "99:59:59";

                minute = minute % 60;

                second = time - hour * 3600 - minute * 60;

                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);

            }

        }

        return timeStr;

    }

    public static String unitFormat(int i) {
        String retStr = null;

        if (i >= 0 && i < 10)

            retStr = "0" + Integer.toString(i);

        else

            retStr = "" + i;

        return retStr;

    }
}
