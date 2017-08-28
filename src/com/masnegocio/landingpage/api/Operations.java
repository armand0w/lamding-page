package com.masnegocio.landingpage.api;

import com.caronte.json.JSONObject;
import com.caronte.rest.annotatios.RESTContentParam;
import com.caronte.rest.annotatios.RESTController;
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
        return op.crearArtefactoConeccion(content);
    }

    @RESTMethod(path="/crearproperties", method=MethodType.POST, contentType=ContentType.APPLICATION_JSON, produces=ContentType.APPLICATION_JSON, producesCharset=CharsetType.UTF_8)
    public JSONObject crearproperties(@RESTContentParam(ContentParamType.JSON) JSONObject content) throws Exception
    {
        OperationsController op = new OperationsController();
        return op.crearProperties(content);
    }

    @RESTMethod(path="/copiarwars", method=MethodType.POST, contentType=ContentType.APPLICATION_JSON, produces=ContentType.APPLICATION_JSON, producesCharset=CharsetType.UTF_8)
    public JSONObject copiarwars(@RESTContentParam(ContentParamType.JSON) JSONObject content) throws Exception
    {
        OperationsController op = new OperationsController();
        return op.copiarWars(content);
    }
}
