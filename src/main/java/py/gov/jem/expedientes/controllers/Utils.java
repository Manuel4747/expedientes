/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gov.jem.expedientes.controllers;

import com.sun.mail.smtp.SMTPTransport;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.xml.bind.DatatypeConverter;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import py.gov.jem.expedientes.models.DocumentosJudiciales;
import py.gov.jem.expedientes.models.ExpFeriados;

/**
 *
 * @author eduardo
 */
public class Utils {

    public static final String politicasContrasena = "La contraseña debe tener un mínimo de 7 caracteres, contener al menos una letra, al menos un número o caracteres especial y no debe tener más de 2 caracteres iguales simultáneos. Además, se recomienda no utilizar palabras de uso común o nombre propios. ";

    private EntityManagerFactory emf;
    private EntityManager em;
    private final FiltroURL filtroURL = new FiltroURL();

    public static void timeStamp(String mensaje, Calendar fecha1, Calendar fecha2) {
        /*
        long diff = fecha2.getTime().getTime() - fecha1.getTime().getTime();
        System.out.println("TIMESTAMP-" + mensaje + diff);
        */
    }
    
    static public String capitalize(final String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }

    public static void sendEmailAsync(String host, String port, String username, String password, String remitente, String mail, String asunto, BodyPart mensaje) {
        sendEmailAsync(host, port, username, password, remitente, mail, asunto, mensaje, null, null);
    }

    public static void sendEmailAsync(String host, String port, String username, String password, String remitente, String mail, String asunto, BodyPart mensaje, String path, String filename) {

        new Thread(() -> {
            sendEmail(host, port, username, password, remitente, mail, asunto, mensaje, path, filename);
        }).start();
    }

    public static void sendEmail(String host, String port, String username, String password, String remitente, String mail, String asunto, BodyPart mensaje) {
        sendEmail(host, port, username, password, remitente, mail, asunto, mensaje, null, null);
    }

    public static void sendEmail(String host, String port, String username, String password, String remitente, String mail, String asunto, BodyPart mensaje, String path, String filename) {
        try {
            Properties props = new Properties();
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.socketFactory.port", port);
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.ssl.enable", "true");
            props.put("mail.smtp.port", port);
            // props.put("mail.smtp.starttls.enable", "true");

            Session session = Session.getInstance(props,
                    new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(remitente));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mail));
            message.setSubject(asunto);

            MimeMultipart multiParte = new MimeMultipart();
            multiParte.addBodyPart(mensaje);

            if (filename != null && path != null) {
                MimeBodyPart messageBodyPart = new MimeBodyPart();
                DataSource source = new FileDataSource(path + File.separator + filename);
                messageBodyPart.setDataHandler(new DataHandler(source));
                messageBodyPart.setFileName(filename);
                multiParte.addBodyPart(messageBodyPart);
            }

            message.setContent(multiParte);

            Transport.send(message);
            /*
            SMTPTransport t = (SMTPTransport) session.getTransport("smtp");
            Socket socket = new Socket(host, Integer.valueOf(port));
            t.connect(socket);
            t.sendMessage(message, message.getAllRecipients());
            */

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static Long cantDiasEnjuiciamiento(DocumentosJudiciales doc, Date now, EntityManager em) {
        Long dias = 0L;
        if (doc != null) {

            if (doc.getFechaInicioEnjuiciamiento() != null) {
                
                
                LocalDate localDate = now.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate localDateInicio = doc.getFechaInicioEnjuiciamiento().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                
                if(localDate.isAfter(localDateInicio)){
                    while(!localDate.equals(localDateInicio)){

                        if (localDate.getDayOfWeek() != DayOfWeek.SATURDAY &&
                            localDate.getDayOfWeek() != DayOfWeek.SUNDAY){

                            Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                            List<ExpFeriados> listaFeriados = em.createNamedQuery("ExpFeriados.findByFecha", ExpFeriados.class).setParameter("fecha", date).getResultList();

                            if (listaFeriados.isEmpty()) {
                                dias++;
                            }
                        }
                        localDate = localDate.minusDays(1);
                    }
                }
            }
        }

        return dias;
    }

    public static String passwordToHash(String password) {

        String myHash = "";
        if (true) {
            MessageDigest md;
            try {
                md = MessageDigest.getInstance("MD5");
                md.update(password.getBytes());
                byte[] digest = md.digest();
                myHash = DatatypeConverter.printHexBinary(digest).toUpperCase();
            } catch (NoSuchAlgorithmException ex) {
                ex.printStackTrace();
            }
        } else {
            myHash = password;
        }

        return myHash;
    }
    
    public static String politicasContrasena(String contrasena) {

        if (contrasena == null) {
            return "La contraseña debe tener un mínimo de 7 caracteres";
        }

        if ("".equals(contrasena)) {
            return "La contraseña debe tener un mínimo de 7 caracteres";
        }

        if (contrasena.length() < 7) {
            return "La contraseña debe tener un mínimo de 7 caracteres";
        }
        
        Pattern letter = Pattern.compile("[a-zA-z]");
        Pattern digit = Pattern.compile("[0-9]");
        Pattern special = Pattern.compile("[!@#$%&*()_+=|<>?{}\\[\\]~-]");
        //Pattern eight = Pattern.compile (".{8}");

        Matcher hasLetter = letter.matcher(contrasena);
        Matcher hasDigit = digit.matcher(contrasena);
        Matcher hasSpecial = special.matcher(contrasena);

        if(!hasLetter.find()){
            return "La contraseña debe contener al menos una letra";
        }
        
        if(!(hasDigit.find() || hasSpecial.find())){
            return "Contener al menos un número o un caracter especial";
        }
        
        int cantRep = 0;
        char actual;
        char anterior = 0;
        for (int i = 0; i < contrasena.length(); i++) {
            actual = contrasena.charAt(i);
            if(anterior == actual){
                cantRep++;
            }else{
                cantRep = 0;
            }
            
            if(cantRep > 2){
                break;
            }
            
            anterior = actual;
        }

        if (cantRep > 2) {
            return "No debe tener más de 2 caracteres iguales simultáneos";
        }
        
        return "";
    }
    
    public static String generarHash(Date fecha, Integer id) throws NoSuchAlgorithmException {

        DateFormat format2 = new SimpleDateFormat("yyyyMMddhhmmss");
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update((id + "_" + format2.format(fecha)).getBytes());
        byte[] digest = md.digest();
        return DatatypeConverter.printHexBinary(digest).toUpperCase();
    }

    public static SecretKey generateKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        return new SecretKeySpec("mm4kj3n8tjnkxmei".getBytes(), "AES");
    }

    public static SecretKey generateKey(String clave) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return new SecretKeySpec(clave.getBytes(), "AES");
    }

    public static String decryptMsg(byte[] cipherText, SecretKey secret) throws NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidParameterSpecException, InvalidAlgorithmParameterException,
            InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {
        /*
		 * Decrypt the message, given derived encContentValues and initialization
		 * vector.
         */
        Cipher cipher = null;
        cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secret);
        String decryptString = new String(cipher.doFinal(cipherText), "UTF-8");
        return decryptString;
    }

    public static byte[] encryptMsg(String message, SecretKey secret)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidParameterSpecException,
            IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        /* Encrypt the message. */
        Cipher cipher = null;
        cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secret);
        byte[] cipherText = cipher.doFinal(message.getBytes("UTF-8"));
        return cipherText;
    }

    public static CloseableHttpClient createAcceptSelfSignedCertificateClient()
            throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {

        // use the TrustSelfSignedStrategy to allow Self Signed Certificates
        SSLContext sslContext = SSLContextBuilder.create().loadTrustMaterial(new TrustSelfSignedStrategy()).build();

        // we can optionally disable hostname verification.
        // if you don't want to further weaken the security, you don't have to
        // include this.
        HostnameVerifier allowAllHosts = new NoopHostnameVerifier();

        // create an SSL Socket Factory to use the SSLContext with the trust
        // self signed certificate strategy
        // and allow all hosts verifier.
        SSLConnectionSocketFactory connectionFactory = new SSLConnectionSocketFactory(sslContext, allowAllHosts);

        // finally create the HttpClient using HttpClient factory methods and
        // assign the ssl socket factory
        return HttpClients.custom().setSSLSocketFactory(connectionFactory).build();
    }

}
