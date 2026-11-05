package edu.upc.eetac.dsa.services;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Serveix la Swagger UI carregant els recursos des del CDN oficial.
 * Accessible a: http://localhost:8080/swagger-ui
 */
@Path("/swagger-ui")
public class SwaggerUiResource {

    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response swaggerUi() {
        String html = "<!DOCTYPE html>\n" +
            "<html lang=\"ca\">\n" +
            "<head>\n" +
            "  <meta charset=\"UTF-8\">\n" +
            "  <title>Dron Manager API - Swagger UI</title>\n" +
            "  <link rel=\"stylesheet\" href=\"https://unpkg.com/swagger-ui-dist@5/swagger-ui.css\">\n" +
            "</head>\n" +
            "<body>\n" +
            "  <div id=\"swagger-ui\"></div>\n" +
            "  <script src=\"https://unpkg.com/swagger-ui-dist@5/swagger-ui-bundle.js\"></script>\n" +
            "  <script>\n" +
            "    window.onload = function() {\n" +
            "      SwaggerUIBundle({\n" +
            "        url: '/openapi.json',\n" +
            "        dom_id: '#swagger-ui',\n" +
            "        presets: [SwaggerUIBundle.presets.apis, SwaggerUIBundle.SwaggerUIStandalonePreset],\n" +
            "        layout: 'BaseLayout'\n" +
            "      });\n" +
            "    };\n" +
            "  </script>\n" +
            "</body>\n" +
            "</html>";
        return Response.ok(html).build();
    }
}
