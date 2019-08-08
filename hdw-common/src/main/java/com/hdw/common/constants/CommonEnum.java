package com.hdw.common.constants;

/**
 * @Description 常量
 * @Author TuMinglong
 * @Date 2018年4月19日 下午3:57:46
 */
public class CommonEnum {

    /**
     * 菜单类型
     */
    public enum MenuType {
        /**
         * 目录
         */
        CATALOG(0),
        /**
         * 菜单
         */
        MENU(1),
        /**
         * 按钮
         */
        BUTTON(2);

        private int value;

        MenuType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    /**
     * 定时任务状态
     */
    public enum ScheduleStatus {
        /**
         * 正常
         */
        NORMAL(0),
        /**
         * 暂停
         */
        PAUSE(1);

        private int value;

        ScheduleStatus(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    /**
     * 文件存储
     */
    public enum CloudService {
        /**
         * 本地
         */
        LOCAL(0),
        /**
         * fastdfs
         */
        FASTDFS(1),
        /**
         * 七牛云
         */
        QINIU(2),
        /**
         * 阿里云
         */
        ALIYUN(3),
        /**
         * 腾讯云
         */
        QCLOUD(4);

        private int value;

        CloudService(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

}
