package view;

import entity.BloodBank;
import entity.BloodDonation;
import entity.DonationRecord;
import entity.Person;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import logic.BloodBankLogic;
import logic.BloodDonationLogic;
import logic.DonationRecordLogic;
import logic.LogicFactory;
import logic.PersonLogic;

/**
 *
 * @author Wontaek
 */
@WebServlet(name = "DonateBloodFrom", urlPatterns = {"/DonateBloodFrom"})
public class DonateBloodFrom extends HttpServlet {

    // Dependency Fields
    private String errorMessage = null;
    private PersonLogic personLogic = LogicFactory.getFor("Person");
    private BloodDonationLogic donationLogic = LogicFactory.getFor("BloodDonation");
    private BloodBankLogic bankLogic = LogicFactory.getFor("BloodBank");
    private DonationRecordLogic recordLogic = LogicFactory.getFor( "DonationRecord" );
    
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
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Donate Blood Form</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println( "<div style=\"text-align: center;\">" );
            out.println( "<div style=\"display: inline-block; text-align: left;\">" );
            out.println( "<form method=\"post\">" );

            out.println("<h1>Blood Donation Form</h1>");   
            out.println("<h2>Person</h2>");   
            out.println( "First Name:<br>" );
            out.printf( "<input type=\"text\" name=\"%s\" value=\"\"><br>", PersonLogic.FIRST_NAME );
            out.println( "<br>" );
            out.println( "Last Name:<br>" );
            out.printf( "<input type=\"text\" name=\"%s\" value=\"\"><br>", PersonLogic.LAST_NAME );
            out.println( "<br>" );
            out.println( "PHONE:<br>" );
            out.printf( "<input type=\"text\" name=\"%s\" value=\"\"><br>", PersonLogic.PHONE );
            out.println( "<br>" );
            out.println( "ADDRESS:<br>" );
            out.printf( "<input type=\"Address\" name=\"%s\" value=\"\"><br>", PersonLogic.ADDRESS );
            out.println( "<br>" );
            out.println( "BIRTH:<br>" );
            out.printf( "<input type=\"datetime-local\" step=\"1\" name=\"%s\" value=\"\"><br>", PersonLogic.BIRTH );
            out.println( "<br>" );
            
            out.println("<h2>Blood</h2>");   
            out.println("AMOUNT:<br>");
            out.printf("<input type=\"text\" name=\"%s\" value=\"\"><br>", BloodDonationLogic.MILLILITERS);
            out.println("<br>");
            out.println("<label for=\"Blood group\">Blood Group: </label><br>");
            out.printf("<select name=\"%s\" id=\"Blood Group\">", BloodDonationLogic.BLOOD_GROUP);
            out.println("<option value=\"A\"> A </option>"); 
            out.println("<option value=\"B\"> B </option>");
            out.println("<option value=\"AB\"> AB </option>");
            out.println("<option value=\"O\"> O </option>");
            out.println("</select><br>");
            out.println("<br><label for=\"RHESUS\">RHESUS FACTOR: </label><br>");
            out.printf("<select name=\"%s\" id=\"RHESUS\">", BloodDonationLogic.RHESUS_FACTOR);
            out.println("<option value=\"+\">Positive</option>");
            out.println("<option value=\"-\">Negative</option>");
            out.println("</select><br>");
            out.println("<br><label for=\"TESTED\">TESTED: </label><br>");
            out.printf("<select name=\"%s\" id=\"TESTED\">", DonationRecordLogic.TESTED);
            out.println("<option value=\"true\">Positive</option>");
            out.println("<option value=\"false\">Negative</option>");
            out.println("</select><br><br>");

            out.println("<h2>Administration</h2>");    
            out.println( "ADMINISTRATOR:<br>" );
            out.printf( "<input type=\"text\" name=\"%s\" value=\"\"><br>", DonationRecordLogic.ADMINISTRATOR );
            out.println( "<br>" );
            out.println( "HOSPITAL:<br>" );
            out.printf( "<input type=\"text\" name=\"%s\" value=\"\"><br>", DonationRecordLogic.HOSPITAL );
            out.println( "<br>" );

            out.println("<label for=\"bank\">BLOOD BANK:</label><br>");
            out.printf("<select name=\"%s\" id=\"bank\">", BloodBankLogic.NAME);
            out.println("<option value=\"BloddyBank\">BloddyBank</option>");
            out.println("<option value=\"Bank\">Bank</option>");
            out.println("</select><br><br>");
            
            DonationRecordLogic drLogic = LogicFactory.getFor( "DonationRecord" );
            String date = drLogic.convertDateToString(Calendar.getInstance().getTime());
            
            out.println( "CREATED:<br>" );
            out.printf( "<input type=\"text\" name=\"%s\" value=\"%s\"><br>", DonationRecordLogic.CREATED, date );
            out.println( "<br>" );
            

            out.println( "<input type=\"submit\" name=\"add\" value=\"Add\">" );
            out.println( "</form>" );
            if( errorMessage != null && !errorMessage.isEmpty() ){
                out.println( "<p color=red>" );
                out.println( "<font color=red size=4px>" );
                out.println( errorMessage );
                out.println( "</font>" );
                out.println( "</p>" );
            }
            out.println( "<pre>" );
            out.println( "Submitted keys and values:" );
            out.println( toStringMap( request.getParameterMap() ) );
            out.println( "</pre>" );
            out.println( "</div>" );
            out.println( "</div>" );
            out.println( "</body>" );
            out.println( "</html>" );
        }
    }
    
    private String toStringMap( Map<String, String[]> values ) {
        StringBuilder builder = new StringBuilder();
        values.forEach( ( k, v ) -> builder.append( "Key=" ).append( k )
                .append( ", " )
                .append( "Value/s=" ).append( Arrays.toString( v ) )
                .append( System.lineSeparator() ) );
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
    protected void doGet( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException {
        log( "GET" );
        processRequest( request, response );
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
    protected void doPost( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException {
        log( "POST" );

        try {
            // Entities
            Person PERSON = personLogic.createEntity( request.getParameterMap());
            BloodDonation DONATION = donationLogic.createEntity( request.getParameterMap());
            DonationRecord RECORD = recordLogic.createEntity( request.getParameterMap());
//            BloodBank BANK = bankLogic.createEntity( request.getParameterMap());

//            // get IDs
//            String person_id = request.getParameter( PersonLogic.ID );
//            String donation_id = request.getParameter( BloodDonationLogic.ID );
//            String bank_id = request.getParameter( BloodBankLogic.ID );
//            String record_id = request.getParameter( DonationRecordLogic.ID );
//            
//            // use the logic of the dependency to get the entity form DB using dependency id
//            Person person = personLogic.getWithId(Integer.parseInt(person_id));
//            BloodDonation bloodDonation = donationLogic.getWithId(Integer.parseInt(donation_id));
//            DonationRecord donationRecord = recordLogic.getWithId(Integer.parseInt(record_id));
//            BloodBank bloodBank = bankLogic.getWithId(Integer.parseInt(bank_id));
//            
//            // set the dependencies on the objects
//            RECORD.setPerson(person);
//            RECORD.setBloodDonation(bloodDonation);
//            BANK.setOwner(person);
//            DONATION.setBloodBank(bloodBank);

            // add the objects on DB
            personLogic.add(PERSON);
            recordLogic.add(RECORD);
            donationLogic.add(DONATION);
//            bankLogic.add(BANK);

        } catch( Exception ex ) {
            log("Exception", ex);
        }

        if( request.getParameter( "add" ) != null ){
            //if add button is pressed return the same page
            processRequest( request, response );
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Create a DonationRecord Entity";
    }// </editor-fold>

    private static final boolean DEBUG = true;

    public void log( String msg ) {
        if( DEBUG ){
            String message = String.format( "[%s] %s", getClass().getSimpleName(), msg );
            getServletContext().log( message );
        }
    }

    public void log( String msg, Throwable t ) {
        String message = String.format( "[%s] %s", getClass().getSimpleName(), msg );
        getServletContext().log( message, t );
    }

}
