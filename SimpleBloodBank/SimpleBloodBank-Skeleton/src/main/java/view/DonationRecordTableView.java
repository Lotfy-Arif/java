package view;

import entity.DonationRecord;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import logic.DonationRecordLogic;
import logic.LogicFactory;

/**
 *
 * @author Wontaek Oh
 */
@WebServlet(name = "DonationRecordTable", urlPatterns = {"/DonationRecordTable"})
public class DonationRecordTableView extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>DonationRecordTable</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<table style=\"margin-left: auto; margin-right: auto;\" border=\"1\">");
            out.println("<caption>Donation Records</caption>");

            DonationRecordLogic recordLogic = LogicFactory.getFor("DonationRecord");

            // Table Header
            out.println("<tr>");
            recordLogic.getColumnNames().forEach(c -> out.printf("<th>%s</th>", c));
            out.println("</tr>");

            // Table body
            recordLogic.getAll().forEach(e -> out.printf("<tr><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td></tr>",
                    recordLogic.extractDataAsList(e).toArray()));

            // Table Footer
            out.println("<tr>");
            recordLogic.getColumnNames().forEach(c -> out.printf("<th>%s</th>", c));
            out.println("</tr>");
            
            out.println("</table>");
            out.printf("<div style=\"text-align: center;\"><pre>%s</pre></div>", toStringMap(request.getParameterMap()));
            out.println("</body>");
            out.println("</html>");
        }
    }
    
    private String toStringMap(Map<String, String[]> parameterMap) {
        StringBuilder builder = new StringBuilder();
        for(String k: parameterMap.keySet()) {
            builder.append("Key=").append(k)
                    .append(", ")
                    .append("Value/s=").append( Arrays.toString( parameterMap.get(k)))
                    .append(System.lineSeparator());
        }
        return builder.toString();
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log("GET");
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log("POST");
        DonationRecordLogic recordLogic = LogicFactory.getFor("DonationRecord");
        DonationRecord records = recordLogic.updateEntity(request.getParameterMap());
        recordLogic.update(records);
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "DonationRecord View Model";
    }// </editor-fold>
    
    private static final boolean DEBUG = true;
    
    public void log(String msg) {
        if(DEBUG){
            String message = String.format( "[%s] %s", getClass().getSimpleName(), msg );
            getServletContext().log(message);
        }
    }

    public void log( String msg, Throwable t ) {
        String message = String.format( "[%s] %s", getClass().getSimpleName(), msg );
        getServletContext().log(message, t);
    }

}
