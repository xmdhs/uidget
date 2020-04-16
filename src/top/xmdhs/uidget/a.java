package top.xmdhs.uidget;

import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class a {
    public static void main(String[] args) {
        papapa[] pa = new papapa[3];
        pa[0] = new papapa("2147483643", "1", "139294");
        pa[1] = new papapa("2147483642", "1075817", "1216079");
        pa[2] = new papapa("2147483641", "2151633", "2289654");
        ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(5);
        exec.scheduleAtFixedRate(() -> pa[0].run(), 1000, 500, TimeUnit.MILLISECONDS);
        exec.scheduleAtFixedRate(() -> pa[1].run(), 2000, 500, TimeUnit.MILLISECONDS);
        exec.scheduleAtFixedRate(() -> pa[2].run(), 3000, 500, TimeUnit.MILLISECONDS);

    }
}

class papapa {
    private final String uid;
    private final String start;
    private final String end;
    sqlite s = new sqlite();

    public papapa(String uid, String start, String end) {
        this.uid = uid;
        this.start = start;
        this.end = end;
    }

    static Logger logger = Logger.getLogger("LoggerPropreties");
    static LogManager logManager = LogManager.getLogManager();

    public void run() {
        int i = s.getUid(uid);
        if (i == -1) {
            s.creatSql();
            s.insertsql(Integer.parseInt(uid), "0", Integer.parseInt(start), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, "0", "0");
        }
        try {
            InputStream in = papapa.class.getResourceAsStream("/logging.properties");
            logManager.readConfiguration(in);
            logManager.addLogger(logger);
            if (i <= Integer.parseInt(end) && s.getUid(Integer.toString(i)) == -1) {
                i = s.getUid(uid);
                URL url = new URL("https://www.mcbbs.net/api/mobile/index.php?module=profile&uid=" + i);
                http h = new http(url);
                if (h.getJson().equals("1") || !h.testjson(h.getJson()).Charset.equals("UTF-8")) {
                    logger.warning("网络似乎有什么问题");
                    Thread.sleep(10000);
                } else {
                    if (h.json2Class(h.getJson()) == null) {
                        if(h.getJson().contains("messageval")){
                            logger.warning("此用户大概有什么问题，uid：" + i);
                            i++;
                        }else {
                            logger.warning("网络似乎有什么问题");
                            Thread.sleep(10000);
                        }
                    } else {
                        uidapi u = h.json2Class(h.getJson());
                        String username = u.Variables.space.username.replace("'", "''");
                        logger.warning("漏掉的用户，用户名：" + username + "，uid：" + u.Variables.space.uid);
                        if (u.Variables.space.group != null) {
                            s.insertsql(u.Variables.space.uid, username, u.Variables.space.credits, u.Variables.space.extcredits1,
                                    u.Variables.space.extcredits2, u.Variables.space.extcredits3, u.Variables.space.extcredits4,
                                    u.Variables.space.extcredits5, u.Variables.space.extcredits6, u.Variables.space.extcredits7,
                                    u.Variables.space.extcredits8, u.Variables.space.oltime, u.Variables.space.groupid,
                                    u.Variables.space.posts, u.Variables.space.threads, u.Variables.space.friends,
                                    u.Variables.space.views, u.Variables.space.adminid, u.Variables.space.emailstatus, u.Variables.space.group.grouptitle, u.Variables.space.extgroupids);
                        }
                        i = u.Variables.space.uid;
                        i++;
                    }
                    s.setUid(i, uid);
                }
            } else {
                i++;
                s.setUid(i, uid);
                if (i > Integer.parseInt(end)) {
                    System.out.println("结束了");
                    Thread.sleep(10000);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

