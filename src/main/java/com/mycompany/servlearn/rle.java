/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.servlearn;

import java.util.Date;
import java.util.UUID;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import java.security.MessageDigest;

/**
 *
 * @author 21701
 */

public class rle {

    public static String ts() {
        long input = new Date().getTime();
        DateTime dateTimeUtc = new DateTime(input, DateTimeZone.UTC);
        DateTimeZone timeZoneIndia = DateTimeZone.forID("Asia/Kolkata");
        DateTime dateTimeIndia = dateTimeUtc.withZone(timeZoneIndia);
        String[] st = dateTimeIndia.toString().split("\\.");
        String s = st[0] + st[1].substring(3);
//        System.out.println("TS: " + s + "\n");
        String date = s.substring(0, 10);
        return s;
    }

    public static String txn() {
        String txns = UUID.randomUUID().toString().replace("-", "");
//        System.out.println("TXN: " + txns + "\n");
        return txns;
    }

    public static String hash() {
        String par = "emudhra123";
        StringBuffer hexString = new StringBuffer();
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(par.getBytes("UTF-8"));
            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return hexString.toString();

    }

    public static String check() throws Exception {
        String urls = "https://qaserver-int.emudhra.net:18006/KYCExternal/enrolment/CheckEnrolmentStatus";
        String req = "<CheckEnrolmentStatusReq accessKey=\""+hash()+"\" aspID=\"MASP\" requestType=\"1\" requestValue=\"abhishek26\" ts=\""+ts()+"\" txn=\""+txn()+"\" ver=\"1.0\" />";
        String Resp = apicall.executePostParameters(urls, req);
        System.out.println(Resp);
        return Resp;
    }
//    public static void main(String[] args) throws Exception {
//        check();
//       }
}
