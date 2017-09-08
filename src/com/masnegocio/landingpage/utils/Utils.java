package com.masnegocio.landingpage.utils;

import com.caronte.encoding.Base64;
import com.caronte.jpath.JPATH;
import com.caronte.json.JSON;
import com.caronte.json.JSONObject;
import com.caronte.json.JSONValue;
import com.caronte.json.JSONValueType;
import com.caronte.rest.enums.CharsetType;
import com.caronte.rest.enums.ContentType;
import com.caronte.rest.enums.MethodType;
import com.caronte.rest.http.RESTClient;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;

public class Utils
{
    public static JSONObject appProperties;

    public static Connection getConnection() throws Exception
    {
        String ip = (String) JPATH.find(appProperties, "ip").getValue();
        String puerto = (String) JPATH.find(appProperties, "puerto").getValue();
        String usuario = (String) JPATH.find(appProperties, "usuario").getValue();
        String password = (String) JPATH.find(appProperties, "password").getValue();

        return DriverManager.getConnection("jdbc:mysql://" + ip + ":" + puerto, usuario, password);
    }

    public static Connection getConnection( String schema ) throws Exception
    {
        String ip = (String) JPATH.find(appProperties, "ip").getValue();
        String puerto = (String) JPATH.find(appProperties, "puerto").getValue();
        String usuario = (String) JPATH.find(appProperties, "usuario").getValue();
        String password = (String) JPATH.find(appProperties, "password").getValue();

        return DriverManager.getConnection("jdbc:mysql://" + ip + ":" + puerto + "/" + schema, usuario, password);
    }

    public static String getString(JSONObject jsonObject, String path)
    {
        try
        {
            JSONValue jsonValue = JPATH.find(jsonObject, path);

            if (jsonValue != null && jsonValue.getValue() != null && jsonValue.getType() == JSONValueType.STRING)
            {
                return jsonValue.getValue().toString();
            }
        }
        catch (Exception e)
        {
            return "";
        }

        return "";
    }

    public static Integer getInteger(JSONObject jsonObject, String path)
    {
        try
        {
            JSONValue jsonValue = JPATH.find(jsonObject, path);

            if (jsonValue != null && jsonValue.getValue() != null && (jsonValue.getType()==JSONValueType.INTEGER || jsonValue.getType()==JSONValueType.STRING))
            {
                return Integer.parseInt(jsonValue.getValue().toString());
            }
        }
        catch (Exception e)
        {
            return 0;
        }

        return 0;
    }

    public static JSONObject getJSONObject(JSONObject jsonObject, String path)
    {
        try
        {
            JSONValue jsonValue = JPATH.find(jsonObject, path);

            if (jsonValue != null && jsonValue.getValue() != null && jsonValue.getType() == JSONValueType.OBJECT)
            {
                return JSON.parse( jsonValue.getValue().toString() );
            }
        }
        catch (Exception e)
        {
            return new JSONObject();
        }

        return new JSONObject();
    }

    public static String getToken(String usuario, String pwd, String producto, String cliente)
    {
        try
        {
            HashMap<String, String> headers = new HashMap<>();
            headers.put("ProductoMN", producto);
            headers.put("ClienteMN", cliente);
            headers.put("Authorization", "Basic " + Base64.encode( (usuario + ":" + pwd).getBytes("UTF-8") ));

            JSONObject token = (JSONObject) RESTClient.execute(getString(appProperties, "/url-acl-login"), MethodType.POST, ContentType.APPLICATION_JSON, CharsetType.UTF_8, null, headers, ContentType.APPLICATION_JSON);

            return getString(token, "/data/p_token");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return "";
        }
    }
}
