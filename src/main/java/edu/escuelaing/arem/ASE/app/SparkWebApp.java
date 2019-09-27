/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.escuelaing.arem.ASE.app;

/**
 *
 * @author 2137516
 */


import static spark.Spark.*;
import edu.escuelaing.arem.ASE.app.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.LinkedList;
import spark.Request;
import spark.Response;

public class SparkWebApp {

    public LinkedList<Double> lista = new LinkedList<Double>();
    public static Double media = 0.0;
    public static Double desviacion = 0.0;    

    public static void main(String[] args) {
        port(getPort());        
        //get("/", (req, res) -> indexPage(req, res));
        get("/calculos", (req, res) -> calculosPage(req, res));
        get("/respuesta", (req, res) -> logicaPage(req, res));

    }

    static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 4567; //returns default port if heroku-port isn't set (i.e.on localhost)
    }

   
    private static String calculosPage(Request req, Response res) throws IOException {

        String calculosHtml = "<!DOCTYPE html>\n"
                + "<html>\n"
                + "   \n"
                + "   <head>\n"
                + "      <title>Calculadora</title>\n"
                + "   </head>\n"
                + "	\n"
                + "<body>"
                + "<center>"
                + "      <h2>Ingrese el numero</h2>\n"
                + "      <p>Ingrese un numero para calcular el cuadrado.</p>\n"
                + "       <form action=\"/respuesta\">\n"                
                + "           <input type=\"text\" placeholder=\"Numero\" name=\"inputData\" ><br><br>\n"
                + "           <input type=\"submit\" value=\"Calcular\">\n"
               // + "      <a href = \"/respuesta\" target = \"_self\">Ver Respuesta</a>\n"
                + "       </form>\n"
                + "</center>\n"
                + "</body>\n"
                + "</html>";        
        return calculosHtml;
    }

    private static String logicaPage(Request req, Response res) throws IOException {        
        URL aws = new URL("https://i6no5gfav7.execute-api.us-east-1.amazonaws.com/Beta?value=" + req.queryParams("inputData"));     
        String data = "";
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(aws.openStream()))) {
			String inputLine = null;
			while ((inputLine = reader.readLine()) != null) {
				data+=inputLine;
			}
		} catch (IOException x) {
			System.err.println(x);
		}
        
        DecimalFormat round= new DecimalFormat("#.00");                 
        String c = "<!DOCTYPE html>\n"
                + "<html>\n"
                + "<head>\n"
                + "<title> Resultados </title>\n"
                + "</head>\n"
                + "<body>\n"
                + "<center>\n"
                + "<h1> Resultados </h1>\n"
                + "<p> El resultado es:</p> <br>\n"
                + "<p> Cuadrado: " + round.format(Integer.parseInt(data)) + "</p>\n"                
                + "<a href = \"/calculos\" target = \"_self\">Volver a la calculadora</a>\n"
                + "</center>\n"
                + "</body>\n"
                + "</html>";
        return c;
    }

}
