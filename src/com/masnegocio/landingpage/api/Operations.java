package com.masnegocio.landingpage.api;

import com.caronte.json.JSONObject;
import com.caronte.rest.annotatios.RESTContentParam;
import com.caronte.rest.annotatios.RESTController;
import com.caronte.rest.annotatios.RESTHeaderParam;
import com.caronte.rest.annotatios.RESTMethod;
import com.caronte.rest.enums.CharsetType;
import com.caronte.rest.enums.ContentParamType;
import com.caronte.rest.enums.ContentType;
import com.caronte.rest.enums.MethodType;
import com.masnegocio.landingpage.ontrollers.OperationsController;

@RESTController("/landing")
public class Operations
{
    @RESTMethod(path="/validar", method=MethodType.POST, contentType=ContentType.APPLICATION_JSON, produces=ContentType.APPLICATION_JSON, producesCharset=CharsetType.UTF_8)
    public JSONObject validar(@RESTContentParam(ContentParamType.JSON) JSONObject content) throws Exception
    {
        OperationsController op = new OperationsController();
        return op.validar(content);
    }

    @RESTMethod(path="/crearcliente", method=MethodType.POST, contentType=ContentType.APPLICATION_JSON, produces=ContentType.APPLICATION_JSON, producesCharset=CharsetType.UTF_8)
    public JSONObject crearcliente(@RESTContentParam(ContentParamType.JSON) JSONObject content) throws Exception
    {
        OperationsController op = new OperationsController();
        return op.crearCliente(content);
    }

    @RESTMethod(path="/crearparametros", method=MethodType.POST, contentType=ContentType.APPLICATION_JSON, produces=ContentType.APPLICATION_JSON, producesCharset=CharsetType.UTF_8)
    public JSONObject crearparametros(@RESTContentParam(ContentParamType.JSON) JSONObject content) throws Exception
    {
        OperationsController op = new OperationsController();
        return op.crearParametros(content);
    }

    @RESTMethod(path="/crearschema", method=MethodType.POST, contentType=ContentType.APPLICATION_JSON, produces=ContentType.APPLICATION_JSON, producesCharset=CharsetType.UTF_8)
    public JSONObject crearschema(@RESTContentParam(ContentParamType.JSON) JSONObject content) throws Exception
    {
        OperationsController op = new OperationsController();
        return op.crearSchema(content);
    }

    @RESTMethod(path="/crearartefacto", method=MethodType.POST, contentType=ContentType.APPLICATION_JSON, produces=ContentType.APPLICATION_JSON, producesCharset=CharsetType.UTF_8)
    public JSONObject crearartefacto(@RESTContentParam(ContentParamType.JSON) JSONObject content) throws Exception
    {
        OperationsController op = new OperationsController();
        return op.crearArtefactoConexion(content);
    }

    @RESTMethod(path="/creararnotificacion", method=MethodType.POST, contentType=ContentType.APPLICATION_JSON, produces=ContentType.APPLICATION_JSON, producesCharset=CharsetType.UTF_8)
    public JSONObject crearNotificacion(@RESTContentParam(ContentParamType.JSON) JSONObject content) throws Exception
    {
        OperationsController op = new OperationsController();
        return op.crearProcesoNoticiacion(content);
    }

    @RESTMethod(path="/crearproperties", method=MethodType.POST, contentType=ContentType.APPLICATION_JSON, produces=ContentType.APPLICATION_JSON, producesCharset=CharsetType.UTF_8)
    public JSONObject crearproperties(@RESTContentParam(ContentParamType.JSON) JSONObject content) throws Exception
    {
        OperationsController op = new OperationsController();
        return op.crearProperties(content);
    }

    @RESTMethod(path="/copiarwar", method=MethodType.POST, contentType=ContentType.APPLICATION_JSON, produces=ContentType.APPLICATION_JSON, producesCharset=CharsetType.UTF_8)
    public JSONObject copiarwar(@RESTContentParam(ContentParamType.JSON) JSONObject content) throws Exception
    {
        OperationsController op = new OperationsController();
        return op.copiarWar(content);
    }

    @RESTMethod(path="/usuario", method=MethodType.POST, contentType=ContentType.APPLICATION_JSON, produces=ContentType.APPLICATION_JSON, producesCharset=CharsetType.UTF_8)
    public JSONObject usuario(@RESTContentParam(ContentParamType.JSON) JSONObject content) throws Exception
    {
        OperationsController op = new OperationsController();
        return op.crearUsuario(content);
    }

    @RESTMethod(path="/reset", method=MethodType.POST, contentType=ContentType.APPLICATION_JSON, produces=ContentType.APPLICATION_JSON, producesCharset=CharsetType.UTF_8)
    public JSONObject reset(@RESTContentParam(ContentParamType.JSON) JSONObject content) throws Exception
    {
        OperationsController op = new OperationsController();
        return op.resetPwd(content);
    }

    @RESTMethod(path="/test", method=MethodType.POST, contentType=ContentType.APPLICATION_JSON, produces=ContentType.APPLICATION_JSON, producesCharset=CharsetType.UTF_8)
    public JSONObject test(@RESTContentParam(ContentParamType.JSON) JSONObject content, @RESTHeaderParam("Authorization") String productoMN) throws Exception
    {
        System.out.println( content );
        System.out.println( productoMN );

        return new JSONObject();
    }
}
