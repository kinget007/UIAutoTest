package com.xxxx.uiautotest.util;

/**
 * Created by yuyilong on 15/9/24.
 */
public class StartAppiumServer extends Thread {
    long bPort;
    long chromeport;
    private String port;
    private String udid;
    private String appiumPath;

    /**
     * @param port appium service port
     */
    public StartAppiumServer(String port, String udid) {
        this.udid = udid;
        this.port = port;
        this.bPort = Long.parseLong(port) + 2;
        this.chromeport = Long.parseLong(port) + 4792;
        this.appiumPath = "/usr/local/lib/node_modules/appium/bin/";
    }

    /**
     * Description: override method run to start appium service.
     * 启动Appium服务。
     */
    public void run() {
//        int count = 1;
//        String out;
//        String result;

        //如果appium －p xxx －bp xxx 端口被占用则杀掉进程
//        out = Tools.cmdInvoke("ps -A|grep " + port + "|grep -v grep|awk 'NR=1 {print $1}'");

//        while (null != out && !out.equals("")) {
//            if (count > 6)
//                break;
//            Log.logInfo("The " + count + " times try to kill the appium");
//            Tools.cmdInvoke("ps -A|grep " + port + "|grep -v grep|awk 'NR=1 {print $1}'|xargs kill -9");
//            out = Tools.cmdInvoke("ps -A|grep " + port + "|grep -v grep|awk 'NR=1 {print $1}'");
//            count++;
//        }

//        result = (count > 6) ? "error >> killed appium's process failure!" : "kill appium's processes success";
//        Log.logInfo(result);
        //--command-timeout 8400

        String command = "node " + appiumPath + "appium.js -a 127.0.0.1 -p " + port + " -bp " + bPort + " --session-override --chromedriver-port " + chromeport + " -U " + udid;
        //启动appium服务
//        Log.logInfo("start appium >> appium -p " + port + " -bp " + bPort + " --session-override" + " -U " + udid);
//        Tools.cmdInvoke("appium -p " + port + " -bp " + bPort + " --session-override" + " -U " + udid);
        Log.logInfo("run " + udid + " Appium Server in port " + port + "......");
        Log.logInfo("start appium >> " + command);
        Tools.cmdInvoke(command);

    }
}
