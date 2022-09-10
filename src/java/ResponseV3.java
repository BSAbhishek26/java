/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.emudhra.esign.ReturnDocument;
import com.emudhra.esign.eSign;
import com.emudhra.esign.eSignServiceReturn;
import configuration.Settings;
import esign.text.pdf.codec.Base64;
//import jakarta.servlet.annotation.WebServlet;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;


@WebServlet("/Response")
public class ResponseV3 extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String xmlString = "";
        try {
            InputStream xmlStream = request.getInputStream();

           xmlString = Utilities.getStringFromInputStream(xmlStream);
            
            Document doc = Utilities.convertStringToDocument(xmlString);
            XPath xPath = XPathFactory.newInstance().newXPath();
            String status = Utilities.GetXpathValue(xPath, "/EsignResp/@status", doc);
            String txnId = Utilities.GetXpathValue(xPath, "/EsignResp/@txn", doc);
            if ("1".equals(status)) {
                String TempPath = "//Users//abhi//Downloads//eSignAspLibDemo//resources//Temp";
                String licenceFilePath = Settings.ESIGN_LICENCE;
                String pfxPath = Settings.PFX_PATH;
                String pfxPassword = Settings.PFX_PASSWORD;
                String pfxAlias = Settings.PFX_ALIAS;
                eSign eSign = new eSign(licenceFilePath, pfxPath, pfxPassword, pfxAlias);
                
                eSignServiceReturn serviceReturn = eSign.getSigedDocument(xmlString, TempPath + "/" + txnId + ".sig");
                if (serviceReturn.getStatus() == 1) {
                    ArrayList<ReturnDocument> returnDocuments = serviceReturn.getReturnDocuments();
                    int i = 0;
                    for (ReturnDocument returnDocument : returnDocuments) {
                        String pdfBase64 = returnDocument.getSignedDocument();
//                        byte[] signedBytes = Base64.decode(pdfBase64);
                        byte[] signedBytes = Base64.decode(pdfBase64);
                        String pdfPath = Settings.OUTPUT_FOLDER + File.separator  + txnId + "_" + i + ".pdf";
                        try (FileOutputStream fos = new FileOutputStream(pdfPath)) {
                            fos.write(signedBytes);
//                            fos.flush();
                        }
                        i++;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try (PrintWriter out = response.getWriter()) {
            out.println("<h4>Response page</h4><br>");
            out.println("ResponseXML: \n");
            out.print(xmlString);
        }
    }// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">

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
    }// </editor-fold>
}
