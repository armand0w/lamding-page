package com.masnegocio.landingpage.ontrollers;

import com.caronte.json.JSONObject;
import com.caronte.rest.enums.CharsetType;
import com.caronte.rest.enums.ContentType;
import com.caronte.rest.enums.MethodType;
import com.caronte.rest.exceptions.AuthorizationException;
import com.caronte.rest.exceptions.OperationExecutionException;
import com.caronte.rest.http.RESTClient;
import com.masnegocio.landingpage.utils.Utils;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static com.masnegocio.landingpage.listeners.MNServletContextListener.LOGGER;

public class OperationsController
{
    public JSONObject validar(JSONObject content) throws Exception
    {
        JSONObject response = new JSONObject();

        Connection connect = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try
        {
            String appDir = Utils.getString( content, "/p_contexto" );
            String appWar = appDir + ".war";
            boolean existApp = false;
            boolean existSchema = false;

            File dir = new File( Utils.getString(Utils.appProperties, "/tomcat-webapps") + File.separator + appDir );
            File war = new File( Utils.getString(Utils.appProperties, "/tomcat-webapps") + File.separator + appWar );
            existApp = dir.exists() || war.exists();


            connect = Utils.getConnection();
            preparedStatement = connect.prepareStatement("SELECT COUNT(1) FROM information_schema.schemata WHERE SCHEMA_NAME = ?");
            preparedStatement.setString(1, Utils.getString(Utils.appProperties, "/webapp-prefix") + "_" + appDir);

            resultSet = preparedStatement.executeQuery();
            resultSet.next();
            existSchema = resultSet.getInt(1) == 1 ? Boolean.TRUE : Boolean.FALSE;
            resultSet.close();

            response.addPair("continue", !Boolean.logicalOr(existApp, existSchema));
            response.addPair("war", existApp);
            response.addPair("schema", existSchema);

            if( existSchema || existApp )
            {
                throw new OperationExecutionException("El aplicativo ya existe en el sistema");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if( preparedStatement != null ) preparedStatement.close();
            if( connect != null ) connect.close();
        }

        return response;
    }

    public JSONObject crearCliente(JSONObject content) throws Exception
    {
        JSONObject response = new JSONObject();

        try
        {
            RESTClient.execute(
                    Utils.getString(Utils.appProperties, "/url-acl-newclient"),
                    MethodType.POST,
                    ContentType.APPLICATION_JSON,
                    CharsetType.UTF_8,
                    content,
                    null,
                    ContentType.APPLICATION_JSON);

            response.addPair("continue", true);
        }
        catch (AuthorizationException | OperationExecutionException aoe)
        {
            LOGGER.info( aoe.getData() );
            response.addPair("continue", false);
            response.addPair("message", Utils.getString((JSONObject) aoe.getData(), "/data/message"));
        }
        catch (Exception e)
        {
            response.addPair("continue", false);
            LOGGER.error(this.getClass().getName(), e);
        }

        return response;
    }

    public JSONObject crearSchema(JSONObject content)
    {
        return new JSONObject();
    }

    public JSONObject crearArtefactoConeccion(JSONObject content)
    {
        return new JSONObject();
    }

    public JSONObject crearProperties(JSONObject content)
    {
        return new JSONObject();
    }

    public JSONObject copiarWars(JSONObject content)
    {
        return new JSONObject();
    }
}
