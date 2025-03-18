package cn.com.wenyl.bs.dlt698.constants;

public class DLT698Def {

    // 开始和结束标识
    public static final byte START_MARK = 0x68;
    public static final byte END_MARK = 0x16;

    // 基础请求/响应类型
    public static final int LINK_REQUEST = 1;
    public static final int LINK_RESPONSE = 129;
    public static final int CONNECT_REQUEST = 2;
    public static final int RELEASE_REQUEST = 3;
    public static final int GET_REQUEST = 5;
    public static final int GET_REQUEST_RESPONSE = 133;
        public static final int GET_REQUEST_NORMAL = 1;
        public static final int GET_REQUEST_NORMAL_LIST = 2;
        public static final int GET_REQUEST_RECORD = 3;
        public static final int GET_REQUEST_RECORD_LIST = 4;
        public static final int GET_REQUEST_RECORD_NEXT = 5;

    public static final int SET_REQUEST = 6;
        public static final int SET_REQUEST_NORMAL = 1;
        public static final int SET_REQUEST_NORMAL_LIST = 2;
        public static final int SET_THEN_GET_REQUEST_NORMAL_LIST = 3;

    public static final int ACTION_REQUEST = 7;
        public static final int ACTION_REQUEST_1 = 1;
        public static final int ACTION_REQUEST_LIST = 2;
        public static final int ACTION_THEN_GET_REQUEST_NORMAL_LIST = 3;

    public static final int REPORT_RESPONSE = 8;
    public static final int PROXY_REQUEST = 9;
        public static final int PROXY_GET_REQUEST_LIST = 1;
        public static final int PROXY_GET_REQUEST_RECORD = 2;
        public static final int PROXY_SET_REQUEST_LIST = 3;
        public static final int PROXY_SET_THEN_GET_REQUEST_LIST = 4;
        public static final int PROXY_ACTION_REQUEST_LIST = 5;
        public static final int PROXY_ACTION_THEN_GET_REQUEST_LIST = 6;
        public static final int PROXY_TRANS_COMMAND_REQUEST = 7;
        public static final int F209_TRANS_COMMAND_ACTION = 8; // F29设备的发送127号透传指令

    public static final int CONNECT_RESPONSE = 130;
    public static final int RELEASE_RESPONSE = 131;
    public static final int RELEASE_NOTIFICATION = 132;
    public static final int GET_RESPONSE = 133;

    public static final int SET_RESPONSE = 134;
    public static final int ACTION_RESPONSE = 135;
        public static final int ACTION_RESPONSE_NORMAL = 1;
        public static final int ACTION_RESPONSE_NORMAL_LIST = 2;
        public static final int ACTION_THEN_GET_RESPONSE_NORMAL_LIST = 3;
    public static final int REPORT_NOTIFICATION = 136;
        public static final int REPORT_NOTIFICATION_LIST = 1;
        public static final int REPROT_NOTIFICATION_RECORD_LIST = 2;
        public static final int REPORT_NOTIFICATION_TRANS_DATA = 3;
    public static final int PROXY_RESPONSE = 137;
        public static final int PROXY_GET_RESPONSE_LIST = 1;
        public static final int PROXY_GET_RESPONSE_RECORD = 2;
        public static final int PROXY_SET_RESPONSE_LIST = 3;
        public static final int PROXY_SET_THEN_GET_RESPONSE_LIST = 4;
        public static final int PROXY_ACTION_RESPONSE_LIST = 5;
        public static final int PROXY_ACTION_THEN_GET_RESPONSE_LIST = 6;
        public static final int PROXY_TRANS_COMMAND_RESPONSE = 7;
        public static final int F209_TRANS_COMMAND_ANSWER = 8; // F29设备的127号响应结果

    public static final int SECURITY_REQUEST = 16;
    public static final int SECURITY_RESPONSE = 144;


}
