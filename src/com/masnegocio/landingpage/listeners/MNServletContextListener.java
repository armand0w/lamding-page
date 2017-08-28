package com.masnegocio.landingpage.listeners;

import java.io.FileInputStream;
import java.util.Enumeration;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.log4j.Logger;

import com.caronte.json.JSONObject;
import com.masnegocio.landingpage.utils.Utils;

@WebListener
public class MNServletContextListener implements ServletContextListener
{
    private String projectName;
    private String osName = "";
    private String pathWindows = "C:\\usr\\local\\apps\\properties\\";
    private String pathLinux = "/usr/local/apps/properties/";
    private String propertiesFile = "";

    public static final Logger LOGGER = Logger.getLogger("mn-landing-page-logger");

    @Override
    public void contextInitialized(ServletContextEvent event)
    {
        projectName = event.getServletContext().getContextPath().replaceAll("/", "");
        osName = System.getProperty("os.name");
        propertiesFile = (osName.toUpperCase().contains("WINDOWS") ? pathWindows : pathLinux) + projectName + ".properties";

        LOGGER.info("Starting service " + projectName + " in OS " + osName);
        LOGGER.info("Loading properties file " + propertiesFile);

        try
        {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Properties properties = new Properties();
            FileInputStream fileInputStream = new FileInputStream(propertiesFile);

            Utils.appProperties = new JSONObject();

            if (fileInputStream != null)
            {
                properties.load(fileInputStream);

                Enumeration<Object> propertiesEnumeration = properties.keys();

                while(propertiesEnumeration.hasMoreElements())
                {
                    String key = (String)propertiesEnumeration.nextElement();
                    String value = properties.getProperty(key);
                    Utils.appProperties.addPair(key, value);
                }

                fileInputStream.close();
            }
        }
        catch(Exception e)
        {
            LOGGER.error("Error loading configuration file " + propertiesFile);
            LOGGER.error(e.getMessage());
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent event)
    {
        LOGGER.info("Stopping " + projectName);
    }
}
