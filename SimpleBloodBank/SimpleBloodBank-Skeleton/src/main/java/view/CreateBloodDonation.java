package view;

import com.mysql.cj.log.Log;
import common.ValidationException;
import entity.Account;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import entity.BloodDonation;
import java.util.Calendar;
import logic.AccountLogic;
import logic.BloodDonationLogic;
import logic.LogicFactory;

/**
 * @author lev
 */
@WebServlet(name = "CreateBloodDonation", urlPatterns = {"/CreateBloodDonation"})
public class CreateBloodDonation extends HttpServlet {
    private String errorMessage = null;
    private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Create BloodDonation</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<div style=\"text-align: center;\">");
            out.println("<div style=\"display: inline-block; text-align: left;\">");
            out.println("<form method=\"post\">");

            //instead of typing the name of column manualy use the static vraiable in logic
            //use the same name as column id of the table. will use this name to get date
            //from parameter map.
            //add a row for the bloodbank id
            out.println("Milliliters:<br>");
            out.printf("<input type=\"text\" name=\"%s\" value=\"\"><br>", BloodDonationLogic.MILLILITERS);
            out.println("<br>");
            out.println("Blood Group:<br>");
//            out.printf("<input type=\"text\" name=\"%s\" value=\"\"><br>", BloodDonationLogic.BLOOD_GROUP);
            out.printf("<select name=\"%s\" id=\"Blood Group\">", BloodDonationLogic.BLOOD_GROUP);
            out.println("<option value=\"A\"> A </option>"); 
            out.println("<option value=\"B\"> B </option>");
            out.println("<option value=\"AB\"> AB </option>");
            out.println("<option value=\"O\"> O </option>");
            out.println("</select><br>");
            out.println("<br>");
            out.println("RHESUS FACTOR:<br>");
//            out.printf("<input type=\"text\" name=\"%s\" value=\"\"><br>", BloodDonationLogic.RHESUS_FACTOR);
            out.printf("<select name=\"%s\" id=\"RHESUS\">", BloodDonationLogic.RHESUS_FACTOR);
            out.println("<option value=\"+\">Positive</option>");
            out.println("<option value=\"-\">Negative</option>");
            out.println("</select><br>");
            out.println("<br>");
            
            BloodDonationLogic drLogic = LogicFactory.getFor( "BloodDonation" );
            String date = drLogic.convertDateToString(Calendar.getInstance().getTime());
            
            out.println( "CREATED DATE:<br>" );
            out.printf( "<input type=\"text\" name=\"%s\" value=\"%s\"><br>", BloodDonationLogic.CREATED, date );
            out.println( "<br>" );
            
            out.println("<input type=\"submit\" name=\"view\" value=\"Add and View\">");
            out.println("<input type=\"submit\" name=\"add\" value=\"Add\">");
            out.println("</form>");

            //
            if (errorMessage != null && !errorMessage.isEmpty()) {
                out.println("<p color=red>");
                out.println("<font color=red size=4px>");
                out.println(errorMessage);
                out.println("</font>");
                out.println("</p>");
            }
            out.println("<pre>");
            out.println("Submitted keys and values:");
            out.println(toStringMap(request.getParameterMap()));
            out.println("</pre>");
            out.println("</div>");
            out.println("</div>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    private String toStringMap(Map<String, String[]> values) {
        StringBuilder builder = new StringBuilder();
        values.forEach((k, v) -> builder.append("Key=").append(k)
                .append(", ")
                .append("Value/s=").append(Arrays.toString(v))
                .append(System.lineSeparator()));
        return builder.toString();
    }


    /**
     * Handles the HTTP <code>GET</code> method.
     * <p>
     * get method is called first when requesting a URL. since this servlet will create a host this method simple
     * delivers the html code. creation will be done in doPost method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log("GET");
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     * <p>
     * this method will handle the creation of entity. as it is called by user submitting data through browser.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log("POST");
        BloodDonationLogic bLogic = LogicFactory.getFor("BloodDonation");
//        String username = request.getParameter(AccountLogic.USERNAME);

        String date = request.getParameter(BloodDonationLogic.CREATED);
        Date dateCreated = null;
        try {
            dateCreated = FORMATTER.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

 

            try {

                BloodDonation bloodDonation = bLogic.createEntity(request.getParameterMap());
//           setting  bllodbank logic dependency here
//              bloodDonation.setBloodBank(bloodBank);
                bLogic.add(bloodDonation);
            } catch (Exception ex) {
                log("", ex);
            }
//        } else {
            //if duplicate print the error message
//        errorMessage = "Username: \"" + username + "\" already exists";
//            errorMessage = "ID: \"" + date + "\" already exists";
//        }
        
        if (request.getParameter("add") != null) {
            //if add button is pressed return the same page
            processRequest(request, response);
        } else if (request.getParameter("view") != null) {
            //if view button is pressed redirect to the appropriate table
            response.sendRedirect("BloodDonationTable");
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Create a BloodDonation Entity";
    }

    private static final boolean DEBUG = true;

    public void log(String msg) {
        if (DEBUG) {
            String message = String.format("[%s] %s", getClass().getSimpleName(), msg);
            getServletContext().log(message);
        }
    }

    public void log(String msg, Throwable t) {
        String message = String.format("[%s] %s", getClass().getSimpleName(), msg);
        getServletContext().log(message, t);
    }


}