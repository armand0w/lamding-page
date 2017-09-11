package com.masnegocio.landingpage.controllers;

import com.caronte.encoding.Base64;
import com.caronte.json.JSONObject;
import com.caronte.rest.enums.CharsetType;
import com.caronte.rest.enums.ContentType;
import com.caronte.rest.enums.MethodType;
import com.caronte.rest.exceptions.AuthorizationException;
import com.caronte.rest.exceptions.OperationExecutionException;
import com.caronte.rest.http.RESTClient;
import com.masnegocio.landingpage.utils.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Properties;

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

            File dir = new File( Utils.getString(Utils.appProperties, "/tomcat-webapps") + File.separator + appDir );
            File war = new File( Utils.getString(Utils.appProperties, "/tomcat-webapps") + File.separator + appWar );
            boolean existApp = dir.exists() || war.exists();
            boolean existSchema = false;

            connect = Utils.getConnection();
            preparedStatement = connect.prepareStatement("SELECT COUNT(1) FROM information_schema.schemata WHERE SCHEMA_NAME = ?");
            preparedStatement.setString(1, Utils.getString(content, "/schema_name"));

            resultSet = preparedStatement.executeQuery();
            resultSet.next();
            existSchema = resultSet.getInt(1) == 1 ? Boolean.TRUE : Boolean.FALSE;
            resultSet.close();

            response.addPair("continue", !Boolean.logicalOr(existApp, existSchema));

            if( existSchema || existApp )
            {
                response.addPair("message", "El aplicativo ya existe en el sistema.");
            }
            else
            {
                response.addPair("message", "Puede crear el aplicativo.");
            }
        }
        catch (Exception e)
        {
            LOGGER.error(this.getClass().getName(), e);
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
        LOGGER.info(content);

        try
        {
            HashMap<String, String> headers = new HashMap<>();
            headers.put("ProductoMN", Utils.getString(content, "/p_clave_producto"));

            RESTClient.execute(
                    Utils.getString(Utils.appProperties, "/url-acl-newclient"),
                    MethodType.POST,
                    ContentType.APPLICATION_JSON,
                    CharsetType.UTF_8,
                    content,
                    headers,
                    ContentType.APPLICATION_JSON);

            response.addPair("continue", true);
            response.addPair("message", "Cliente creado correctamente.");
        }
        catch (AuthorizationException | OperationExecutionException aoe)
        {
            response.addPair("continue", false);
            response.addPair("message", "Error al crear cliente en ACL");

            LOGGER.error( aoe.getData() );
            LOGGER.error(this.getClass().getName(), aoe);
        }
        catch (Exception e)
        {
            response.addPair("continue", false);
            response.addPair("message", "Error al crear cliente en ACL");
            LOGGER.error(this.getClass().getName(), e);
        }

        return response;
    }

    public JSONObject crearParametros(JSONObject content) throws Exception
    {
        JSONObject response = new JSONObject();
        JSONObject parametro = new JSONObject();

        try
        {
            String uri = Utils.getString(Utils.appProperties, "/url-acl-param");
            HashMap<String, String> headers = new HashMap<>();
            headers.put("ProductoMN", Utils.getString(content, "/p_clave_producto"));
            headers.put("Token", Utils.getToken(
                    Utils.getString(content, "/p_clave_usuario"),
                    Utils.getString(content, "/p_clave_usuario"),
                    Utils.getString(content, "/p_clave_producto"),
                    Utils.getString(content, "/p_clave_cliente")
            ));

            parametro.addPair("p_codigo", "SEG001");
            parametro.addPair("p_decripcion", "Máximo número de intentos para autenticarse");
            parametro.addPair("p_i_valor", 3);
            parametro.addPair("p_v_valor", null);
            parametro.addPair("p_clave_cliente", Utils.getString(content, "/p_clave_cliente"));
            parametro.addPair("p_clave_producto", "MN-EXPENSES-ENTERPRISE-V30");
            RESTClient.execute(uri, MethodType.POST, ContentType.APPLICATION_JSON, CharsetType.UTF_8, parametro, headers, ContentType.APPLICATION_JSON);

            parametro = new JSONObject();
            parametro.addPair("p_codigo", "SEG002");
            parametro.addPair("p_decripcion", "Tiempo en minutos de bloqueo de una cuenta por exceder el número de intentos");
            parametro.addPair("p_i_valor", 30);
            parametro.addPair("p_v_valor", null);
            parametro.addPair("p_clave_cliente", Utils.getString(content, "/p_clave_cliente"));
            parametro.addPair("p_clave_producto", "MN-EXPENSES-ENTERPRISE-V30");
            RESTClient.execute(uri, MethodType.POST, ContentType.APPLICATION_JSON, CharsetType.UTF_8, parametro, headers, ContentType.APPLICATION_JSON);

            parametro = new JSONObject();
            parametro.addPair("p_codigo", "SEG003");
            parametro.addPair("p_decripcion", "Tiempo en minutos de la vigencia de un token");
            parametro.addPair("p_i_valor", 30);
            parametro.addPair("p_v_valor", null);
            parametro.addPair("p_clave_cliente", Utils.getString(content, "/p_clave_cliente"));
            parametro.addPair("p_clave_producto", "MN-EXPENSES-ENTERPRISE-V30");
            RESTClient.execute(uri, MethodType.POST, ContentType.APPLICATION_JSON, CharsetType.UTF_8, parametro, headers, ContentType.APPLICATION_JSON);

            response.addPair("message", "Parametros creados correctamente: " + uri);
            response.addPair("continue", true);
        }
        catch (OperationExecutionException | AuthorizationException aoe)
        {
            response.addPair("message", "Error al crear parametros del sistema");
            response.addPair("continue", false);

            LOGGER.error(this.getClass().getName(), aoe);
            LOGGER.error(parametro);
            LOGGER.error(aoe.getData());
        }
        catch (Exception e)
        {
            response.addPair("message", "Error al crear parametros del sistema");
            response.addPair("continue", false);

            LOGGER.error(this.getClass().getName(), e);
        }

        return  response;
    }

    public JSONObject crearSchema(JSONObject content) throws Exception
    {
        JSONObject response = new JSONObject();
        try
        {
            String schema = Utils.getString(content, "/schema_name");

            Connection connect = Utils.getConnection();
            connect.createStatement().execute("CREATE SCHEMA " + schema);
            connect.close();

            Connection connSchema = Utils.getConnection(schema);
            File sqlScripts = new File(Utils.getString(Utils.appProperties, "/schema-files"));
            if ( sqlScripts.exists() && sqlScripts.isDirectory() )
            {
                File[] dir = sqlScripts.listFiles();
                if ( dir != null )
                {
                    for( File d : dir )
                    {
                        if ( d.isDirectory() )
                        {
                            File[] files = d.listFiles();
                            if ( files != null )
                            {
                                for ( File f : files )
                                {
                                    if( f.isFile() )
                                    {
                                        try
                                        {
                                            String script = new String( Files.readAllBytes( Paths.get( f.getPath() ) ) );
                                            connSchema.createStatement().execute( script );
                                        }
                                        catch (Exception ex)
                                        {
                                            LOGGER.error(this.getClass().getName(), ex);
                                        }
                                    }
                                }

                                response = new JSONObject();
                                response.addPair("message", "Termino el creado de SCHEMA.");
                                response.addPair("continue", true);
                            }
                            else
                            {
                                LOGGER.info("CrearSchema: NO SE ENCONTRARON SCRIPTS SQL PARA CREAR SCHEMA.");
                            }
                        }
                    }
                }
                else
                {
                    LOGGER.info("CrearSchema: NO SE ENCONTRO CARPETA CON LOS SCRIPTS.");
                }

            }

        }
        catch (Exception e)
        {
            response.addPair("message", "Error al crera parametros del sistema");
            response.addPair("continue", false);
            LOGGER.error(this.getClass().getName(), e);
        }

        return response;
    }

    public JSONObject crearArtefactoConexion(JSONObject content) throws Exception
    {
        JSONObject response = new JSONObject();

        try
        {
            String connection = new String( Files.readAllBytes( Paths.get(Utils.getString(Utils.appProperties, "/artifact_conn")) ) );
            connection = connection.replace("_SCHEMA_", Utils.getString(content, "/schema_name"));
            connection = connection.replace("_ID_ARTIFACT_", Utils.getString(content, "/artifact_conn"));

            connection  = Base64.encode( connection.getBytes("UTF-8") );

            JSONObject artifact = new JSONObject();
            artifact.addPair("xmlB64", connection);

            RESTClient.execute(
                    Utils.getString(Utils.appProperties, "/url-radt-connection"),
                    MethodType.POST,
                    ContentType.APPLICATION_JSON,
                    CharsetType.UTF_8,
                    artifact,
                    null,
                    ContentType.APPLICATION_JSON);

            response.addPair("message", "Artefacto de conexion creado correctamente.");
            response.addPair("continue", true);
        }
        catch (OperationExecutionException | AuthorizationException aoe)
        {
            response.addPair("message", "Error al crear artefacto de conexion en RADT.");
            response.addPair("continue", false);

            LOGGER.error(this.getClass().getName(), aoe);
            LOGGER.error(aoe.getData());
        }
        catch (Exception e)
        {
            response.addPair("message", "Error al crear artefacto de conexion en RADT.");
            response.addPair("continue", false);

            LOGGER.error(this.getClass().getName(), e);
        }

        return response;
    }

    public JSONObject crearProcesoNoticiacion(JSONObject content) throws Exception
    {
        JSONObject response = new JSONObject();

        try
        {
            JSONObject payload = new JSONObject();
            payload.addPair("p_schema", Utils.getString(content, "/schema_name"));
            payload.addPair("p_referencia_conexion", "cnnExpensesSemillaV10");
            payload.addPair("p_sp_reservar_notificacion", "SP_ReservarNotificacion");
            payload.addPair("p_extractor_notificaciones", "extNotificacionesExpensesSemillaV10");
            payload.addPair("p_sp_actualizar_estatus_notificacion", "SP_ActualizarEstatusNotificacion");
            payload.addPair("p_connections", Base64.encode(
                    Utils.getJSONObject(content, "/connections").toString().getBytes("UTF-8") ));

            response = (JSONObject) RESTClient.execute(
                    Utils.getString(Utils.appProperties, "/url-radt-notify"),
                    MethodType.POST,
                    ContentType.APPLICATION_JSON,
                    CharsetType.UTF_8,
                    payload,
                    null,
                    ContentType.APPLICATION_JSON);

            LOGGER.info(response);

            response.addPair("message", "Proceso de noticicación agregado correctamente.");
            response.addPair("continue", true);
        }
        catch (OperationExecutionException | AuthorizationException aoe)
        {
            response.addPair("message", "Error al crear proceso de noticicación.");
            response.addPair("continue", false);

            LOGGER.error(this.getClass().getName(), aoe);
            LOGGER.error(aoe.getData());
        }
        catch (Exception e)
        {
            response.addPair("message", "Error al crear proceso de noticicación.");
            response.addPair("continue", false);

            LOGGER.error(this.getClass().getName(), e);
        }

        return response;
    }

    public JSONObject crearProperties(JSONObject content) throws Exception
    {
        JSONObject response = new JSONObject();
        try
        {
            FileOutputStream fos = new FileOutputStream( Utils.getString(Utils.appProperties, "/properties-dir") + Utils.getString(content, "/p_contexto") + ".properties");
            FileInputStream fis = new FileInputStream(Utils.getString(Utils.appProperties, "/properties-file"));
            Properties properties = new Properties();
            properties.load(fis);

            properties.setProperty("CLIENTEMN", Utils.getString(content, "/p_clave_cliente"));
            properties.setProperty("CONNECTION", Utils.getString(content, "/artifact_conn"));
            properties.setProperty("CONNECTIONS", Base64.encode(
                    Utils.getJSONObject(content, "/connections").toString().getBytes("UTF-8") ));

            properties.store(fos, null);

            fis.close();
            fos.close();

            response.addPair("message", "Archivo de propiedades creado correctamente.");
            response.addPair("continue", true);
        }
        catch (Exception e)
        {
            response.addPair("message", "Error al crear archivo de propiedades.");
            response.addPair("continue", false);

            LOGGER.error(this.getClass().getName(), e);
        }

        return response;
    }

    public JSONObject copiarWar(JSONObject content) throws Exception
    {
        JSONObject response = new JSONObject();
        try
        {
            byte[] war = Files.readAllBytes( Paths.get(Utils.getString(Utils.appProperties, "war-file")) );
            Files.write( Paths.get(Utils.getString(Utils.appProperties, "/tomcat-webapps") + Utils.getString(content, "/p_contexto") + ".war"), war );

            response.addPair("message", "Archivo WAR copiado correctamente.");
            response.addPair("continue", true);
        }
        catch (Exception e)
        {
            response.addPair("message", "Error al copiar WAR.");
            response.addPair("continue", false);

            LOGGER.error(this.getClass().getName(), e);
        }

        return response;
    }

    public JSONObject crearUsuario(JSONObject content) throws Exception
    {
        JSONObject response = new JSONObject();

        try
        {
            JSONObject payload = new JSONObject();
            JSONObject data = new JSONObject();
            JSONObject roles = new JSONObject();

            data.addPair("c_empleado_interno", "S");
            data.addPair("c_director_general", "N");

            data.addPair("v_nombre", Utils.getString(content, "/p_nombre_usuario"));
            data.addPair("v_primer_apellido", Utils.getString(content, "/apellidop"));
            data.addPair("v_segundo_apellido", Utils.getString(content, "/apellidom"));
            data.addPair("v_usuario_acl", Utils.getString(content, "/p_clave_usuario"));
            data.addPair("v_correo_electronico", Utils.getString(content, "/p_correo_electronico"));

            roles.resetArray();
            roles.addToArray("2");
            roles.saveArray("checked");
            data.addPair("mc_roles", roles);

            payload.addPair("id", "frmUsuarioExpensesSemillaV10");
            payload.addPair("connection_reference", "cnnExpensesSemillaV10");
            payload.addPair("procedure", "SP_SalvarUsuario");
            payload.addPair("mode", "CREATE");
            payload.addPair("item_id", null);
            payload.addPair("data", data);

            payload.addPair("connections", Utils.getJSONObject(content, "/connections"));

            response = (JSONObject) RESTClient.execute(
                    Utils.getString(Utils.appProperties, "/url-radt-datasave"),
                    MethodType.POST,
                    ContentType.APPLICATION_JSON,
                    CharsetType.UTF_8,
                    payload,
                    null,
                    ContentType.APPLICATION_JSON);

            response = new JSONObject();
            response.addPair("message", "Usuario creado correctamente.");
            response.addPair("continue", true);
        }
        catch (Exception e)
        {
            LOGGER.error(response);
            response.addPair("message", "Error al crear Usuario Administrador en RADT.");
            response.addPair("continue", false);

            LOGGER.error(this.getClass().getName(), e);
        }

        return response;
    }

    public JSONObject resetPwd(JSONObject content) throws Exception
    {
        JSONObject response = new JSONObject();

        try
        {
            HashMap<String, String> headers = new HashMap<>();
            headers.put("ProductoMN", Utils.getString(content,"/p_clave_producto"));
            headers.put("ClienteMN", Utils.getString(content,"/p_clave_cliente"));

            JSONObject payload = new JSONObject();
            payload.addPair("p_mail", Utils.getString(content, "/p_correo_electronico"));
            payload.addPair("p_template", "/p_template");

            response = (JSONObject) RESTClient.execute(
                    Utils.getString(Utils.appProperties, "/url-acl-reset"),
                    MethodType.POST,
                    ContentType.APPLICATION_JSON,
                    CharsetType.UTF_8,
                    payload,
                    headers,
                    ContentType.APPLICATION_JSON);

            response = new JSONObject();
            response.addPair("message", "Se enviaron sus nuevas credenciales.");
            response.addPair("continue", true);
        }
        catch (Exception e)
        {
            LOGGER.error(response);
            response.addPair("message", "Error al crear sus nuevas credenciales.");
            response.addPair("continue", false);

            LOGGER.error(this.getClass().getName(), e);
        }

        return response;
    }
}
