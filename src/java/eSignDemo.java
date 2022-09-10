/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import configuration.Settings;
import com.emudhra.esign.eSign;
import com.emudhra.esign.eSign.Coordinates;
import com.emudhra.esign.eSign.PageTobeSigned;
import com.emudhra.esign.eSignInput;
import com.emudhra.esign.eSignServiceReturn;
import esign.text.pdf.codec.Base64;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author 21701
 */
public class eSignDemo extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String pageLevelCoordinates = Settings.DEMO_COORDINATES;
        String appearenceContent = request.getParameter("content");
        if (appearenceContent == null || appearenceContent.isEmpty()) {
            appearenceContent = Settings.DEMO_SAMPLE_CONTENT;
        }

        eSign.eSignAPIVersion type = eSign.eSignAPIVersion.V3;
        eSign.AuthMode authMode = eSign.AuthMode.OTP;
        response.setContentType("text/html;charset=UTF-8");

        // Configurations
        String tempFolder = Settings.TEMP_FOLDER;
        String licenceFilePath = Settings.ESIGN_LICENCE;
        String pfxPath = Settings.PFX_PATH;
        String pfxPassword = Settings.PFX_PASSWORD;
        String pfxAlias = Settings.PFX_ALIAS;
        String pdfURL = Settings.PDF_URL;
        String resposeURL = (type == eSign.eSignAPIVersion.V2) ? Settings.RESPONSE_AADHAAR_URL : Settings.RESPONSE_URL;
        String redirectUrl = Settings.REDIRECT_URL;
        String pdfPath_1 = Settings.SAMPLE_PDF_PATH;
        String pdfPath_2 = Settings.PDF_2;
        String pdfPath_3 = Settings.PDF_3;
        String pdfPath_4 = Settings.PDF_4;
        String pdfPath_5 = Settings.PDF_5;

//        Initializing eSign object
        eSign eSignObj = new eSign(licenceFilePath, pfxPath, pfxPassword, pfxAlias);
//        eSign eSignObj = new eSign(licenceFilePath, pfxPath, pfxPassword, pfxAlias, false,
//                "", 0, 0, eSignSettings.LogType.AllLog);

        //To prepare data to be signed 
        //To convert PDF in base 64 encoded byte array
        byte[] array_1 = Files.readAllBytes(new File(pdfPath_1).toPath());
        String pdfBase64_1 = Base64.encodeBytes(array_1);
        byte[] array_2 = Files.readAllBytes(new File(pdfPath_2).toPath());
        String pdfBase64_2 = Base64.encodeBytes(array_2);
        byte[] array_3 = Files.readAllBytes(new File(pdfPath_3).toPath());
        String pdfBase64_3 = Base64.encodeBytes(array_3);
        byte[] array_4 = Files.readAllBytes(new File(pdfPath_4).toPath());
        String pdfBase64_4 = Base64.encodeBytes(array_4);
        byte[] array_5 = Files.readAllBytes(new File(pdfPath_5).toPath());
        String pdfBase64_5 = Base64.encodeBytes(array_5);
//            eSignInput eSignInput1 = new eSignInput(pdfBase64_1, "INFO 1", pdfURL, "Bengaluru", "Test Signing", "", true, pageLevelCoordinates, appearenceContent);
        eSignInput eSignInput1 = new eSignInput(pdfBase64_1, "test", pdfURL, "Bengaluru", "Test Signing", "", true, PageTobeSigned.Last, Coordinates.BottomRight, appearenceContent);
        eSignInput eSignInput2 = new eSignInput(pdfBase64_2, "INFO 2", pdfURL, "", "", "", true, pageLevelCoordinates, appearenceContent);
        eSignInput eSignInput3 = new eSignInput(pdfBase64_3, "INFO 3", pdfURL, "", "", "", true, pageLevelCoordinates, appearenceContent);
        eSignInput eSignInput4 = new eSignInput(pdfBase64_4, "INFO 4", pdfURL, "", "", "", true, pageLevelCoordinates, appearenceContent);
        eSignInput eSignInput5 = new eSignInput(pdfBase64_5, "INFO 5", pdfURL, "", "", "", true, pageLevelCoordinates, appearenceContent);
        ArrayList<eSignInput> eSignInputs = new ArrayList<>();
        eSignInputs.add(eSignInput1);
//            eSignInputs.add(eSignInput2);
//            eSignInputs.add(eSignInput3);
//            eSignInputs.add(eSignInput4);
//            eSignInputs.add(eSignInput5);

        //To sign pdfs
        eSignServiceReturn serviceReturn = eSignObj.getGatewayParameter(eSignInputs, "sales", "", resposeURL, redirectUrl, tempFolder, type, authMode);
        String gatewayParam = serviceReturn.getGatewayParameter();
        String gatewayURL = (type == eSign.eSignAPIVersion.V2)
                ? "https://authenticate.sandbox.emudhra.com/AadhaareSign.jsp"
                : "https://authenticate.sandbox.emudhra.com/index.jsp";

        // Form to send to gate way
        String form = "<center><h1>Gateway Parameter</h1><br><form action=\"" + gatewayURL + "\"\n"
                + "		method=\"post\">\n"
                + "		<input type=\"text\" name=\"txnref\"  value=\"" + gatewayParam + "\"/> \n"
                + "		<input type=\"submit\"\n"
                + "			value=\"GateWay Redirect\" >\n"
                + "	</form>"
                + "     <p>Error: \"" + serviceReturn.getErrorMessage() + "\"</p></center>";
        response.getWriter().print(form);
    }

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
