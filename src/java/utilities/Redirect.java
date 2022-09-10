//
//import configuration.Settings;
//import com.emudhra.esign.ReturnDocument;
//import com.emudhra.esign.eSign;
//import com.emudhra.esign.eSignServiceReturn;
//import esign.text.pdf.codec.Base64;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.PrintWriter;
//import java.net.URLDecoder;
//import java.util.ArrayList;
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.xml.xpath.XPath;
//import javax.xml.xpath.XPathFactory;
//import org.w3c.dom.Document;
//
///**
// *
// * @author 21701
// */
//@WebServlet("/Redirect")
//public class Redirect extends HttpServlet {
//
//    /**
//     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
//     * methods.
//     *
//     * @param request servlet request
//     * @param response servlet response
//     * @throws ServletException if a servlet-specific error occurs
//     * @throws IOException if an I/O error occurs
//     */
//    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        response.setContentType("text/html;charset=UTF-8");
//        InputStream xmlStream;
//        String xml = "";
//        String txnref = null;
//        try {
//            // Get response from gateway
//            xmlStream = request.getInputStream();
//            txnref = Utilities.getStringFromInputStream(xmlStream);
//
//            String txns = URLDecoder.decode(txnref).substring(7).split("&")[0];
//
//            String txn = new String(java.util.Base64.getDecoder().decode(txns.getBytes()));
//            String[] strArr = txn.split("\\|");
//            String txnid = strArr[0];
//
//            // Configurations
//            String TempPath = Settings.TEMP_FOLDER;
//            String OutputPath = Settings.OUTPUT_FOLDER;
//            String licenceFilePath = Settings.ESIGN_LICENCE;
//            String pfxPath = Settings.PFX_PATH;
//            String pfxPassword = Settings.PFX_PASSWORD;
//            String pfxAlias = Settings.PFX_ALIAS;
//            eSign eSign = new eSign(licenceFilePath, pfxPath, pfxPassword, pfxAlias);
//            eSignServiceReturn returnService = eSign.getStatus(txnid);
//            xml = returnService.getResponseXML();
//
//            Document doc = Utilities.convertStringToDocument(xml);
//            
//            
//            XPath xPath = XPathFactory.newInstance().newXPath();
//            String status = Utilities.GetXpathValue(xPath, "/EsignResp/@status", doc);
//            String txnId = Utilities.GetXpathValue(xPath, "/EsignResp/@txn", doc);
//            
//            
//            if ("1".equals(status)) {
//
//                // complete signing
//                eSignServiceReturn serviceReturn = eSign.getSigedDocument(xml, TempPath + File.separator + txnId + ".sig");
//
//                // To convert signed pdf from base 64 encoded string to pdf file
//                if (serviceReturn.getStatus() == 1) {
//                    ArrayList<ReturnDocument> returnDocuments = serviceReturn.getReturnDocuments();
//                    int i = 0;
//                    for (ReturnDocument returnDocument : returnDocuments) {
//                        String pdfBase64 = returnDocument.getSignedDocument();
//                        byte[] signedBytes = Base64.decode(pdfBase64);
//                        String pdfPath = OutputPath + File.separator + txnId + "_" + i + ".pdf";
//                        try (FileOutputStream fos = new FileOutputStream(pdfPath)) {
//                            fos.write(signedBytes);
//                        }
//                        i++;
//                    }
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        try (PrintWriter out = response.getWriter()) {
//            /* TODO output your page here. You may use following sample code. */
//            out.println("<!DOCTYPE html>");
//            out.println("<html>");
//            out.println("<head>");
//            out.println("<title>Servlet Redirect</title>");
//            out.println("</head>");
//            out.println("<body>");
//            out.println("<h4>Servlet Redirect at " + "" + "</h4>");
//            out.println("<h4>ResponseXML: </h4>" + "<p>" + xml + "</p>");
//            out.println("</body>");
//            out.println("</html>");
//        }
//    }
//
//    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
//    /**
//     * Handles the HTTP <code>GET</code> method.
//     *
//     * @param request servlet request
//     * @param response servlet response
//     * @throws ServletException if a servlet-specific error occurs
//     * @throws IOException if an I/O error occurs
//     */
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        processRequest(request, response);
//    }
//
//    /**
//     * Handles the HTTP <code>POST</code> method.
//     *
//     * @param request servlet request
//     * @param response servlet response
//     * @throws ServletException if a servlet-specific error occurs
//     * @throws IOException if an I/O error occurs
//     */
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        processRequest(request, response);
//    }
//
//    /**
//     * Returns a short description of the servlet.
//     *
//     * @return a String containing servlet description
//     */
//    @Override
//    public String getServletInfo() {
//        return "Short description";
//    }// </editor-fold>
//
//}
