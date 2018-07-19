package com.jtd.web.service.adx.tanx;

import org.apache.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class TanxUtil {

    static Logger logger = Logger.getLogger("errorLog");

    private static final Properties tanxProps = new Properties();

    public static final String APPKEY;
    public static final String APPSEC;
    public static final String TOKEN; // 不是价格解密的那个key
    public static final Boolean isIgnore;
    public static final String DSPID;
    public static final String SERVER_URL;

    static {

        try {

            Thread thread = Thread.currentThread();

            ClassLoader classLoder  = thread.getContextClassLoader();

            InputStream is = classLoder.getResourceAsStream("tanx.properties");

            tanxProps.load(is);

        } catch (FileNotFoundException e) {
            logger.error(e.getMessage(), e);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        DSPID = tanxProps.getProperty("DSPID");
        APPKEY = tanxProps.getProperty("APPKEY");
        APPSEC = tanxProps.getProperty("APPSEC");
        TOKEN = tanxProps.getProperty("TOKEN");
        SERVER_URL = tanxProps.getProperty("SERVER_URL");
        isIgnore = Boolean.valueOf(tanxProps.getProperty("TANX_IS_IGNORE"));

    }


}
