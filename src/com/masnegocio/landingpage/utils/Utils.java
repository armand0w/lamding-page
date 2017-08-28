package com.masnegocio.landingpage.utils;

import com.caronte.jpath.JPATH;
import com.caronte.json.JSON;
import com.caronte.json.JSONObject;
import com.caronte.json.JSONValue;
import com.caronte.json.JSONValueType;

import java.sql.Connection;
import java.sql.DriverManager;

public class Utils
{
    public static JSONObject appProperties;

    public static Connection getConnection() throws Exception
    {
        String ip = (String) JPATH.find(appProperties, "ip").getValue();
        String puerto = (String) JPATH.find(appProperties, "puerto").getValue();
        //String esquema = (String) JPATH.find(appProperties, "esquema").getValue();
        String usuario = (String) JPATH.find(appProperties, "Usuario").getValue();
        String password = (String) JPATH.find(appProperties, "password").getValue();
        String tamBloquePool = (String) JPATH.find(appProperties, "tam_bloque_pool").getValue();
        String maxTamPool = (String) JPATH.find(appProperties, "max_tam_pool").getValue();

        return DriverManager.getConnection("jdbc:mysql://" + ip + ":" + puerto, usuario, password);
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
}
