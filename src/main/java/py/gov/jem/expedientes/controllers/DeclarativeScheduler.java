/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gov.jem.expedientes.controllers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.poi.util.IOUtils;
import org.primefaces.shaded.json.JSONArray;
import org.primefaces.shaded.json.JSONException;
import org.primefaces.shaded.json.JSONObject;
import py.gov.jem.expedientes.controllers.util.JsfUtil;
import py.gov.jem.expedientes.datasource.RespuestaConsultaActuacionesCausa;
import py.gov.jem.expedientes.models.DocumentosJudicialesCorte;
import py.gov.jem.expedientes.models.ExpActuaciones;
import py.gov.jem.expedientes.models.ExpConexiones;
import py.gov.jem.expedientes.models.ExpEnviosEmail;
import py.gov.jem.expedientes.models.ParametrosSistema;

@Singleton
public class DeclarativeScheduler {

    @Inject
    private ExpActuacionesController actuacionesController;
    @Inject
    private ExpEnviosEmailController enviosEmailController;

    private EntityManagerFactory emf;
    private EntityManager em;
    private ParametrosSistema par;
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    static private boolean procesando = false;

    public DeclarativeScheduler() {
    }
    
    
    @PostConstruct
    public void initParams() {
        conexion();
        par = em.createNamedQuery("ParametrosSistema.findById", ParametrosSistema.class).setParameter("id", Constantes.PARAMETRO_ID_ANTECEDENTE).getSingleResult();
    }

    // @Schedule(second = "0", minute = "*/5", hour = "*", persistent = false)
    @Schedule(second = "0", minute = "*", hour = "*", persistent = false)
    public void atSchedule() throws InterruptedException {

        //if (!procesando) {
            procesando = true;
            try {
                enviarTodosEmails();
            } finally {
                procesando = false;
            }
        //}
        consultarOficios();

        System.out.println("DeclarativeScheduler:: In atSchedule()");
    }
    
    private void conexion(){
        emf = Persistence.createEntityManagerFactory("gestionstarticPU");
        em = emf.createEntityManager();
    }

    private void enviarTodosEmails() {
        
        Date fecha = getSystemDate();
        
        em.getEntityManagerFactory().getCache().evictAll();
        
        List<ExpEnviosEmail> lista = em.createNamedQuery("ExpEnviosEmail.findByFechaHoraEnvioProgramadoEnviado", ExpEnviosEmail.class).setParameter("fechaHoraEnvioProgramado", fecha).setParameter("enviado", false).getResultList();
        for (ExpEnviosEmail env : lista) {

            BodyPart texto = new MimeBodyPart();
            try {
                fecha = getSystemDate();
                texto.setContent(env.getMensaje(), "text/html; charset=utf-8");

                Utils.sendEmailAsync(par.getIpServidorEmail(),
                        par.getPuertoServidorEmail(),
                        par.getUsuarioServidorEmail(),
                        par.getContrasenaServidorEmail(),
                        par.getUsuarioServidorEmail(),
                        env.getEmail(),
                        env.getAsunto(),
                        texto);
                
                actualizarEnviosEmail(env.getId(), fecha);
            } catch (MessagingException ex) {
                ex.printStackTrace();
                return;
            }

        }
    }
    
    private void actualizarEnviosEmail(Integer id, Date fecha){
        try{
            em.joinTransaction();
            // em.createQuery("update ExpEnviosEmail set enviado = true, fechaHoraEnvio = :fechaHoraEnvio where id = :id").setParameter("fechaHoraEnvio", fecha).setParameter("id", id).executeUpdate();
     
            ExpEnviosEmail env = em.createQuery("select a from ExpEnviosEmail a where a.id = :id", ExpEnviosEmail.class).setParameter("id", id).getSingleResult();
       
            env.setEnviado(true);
            env.setFechaHoraEnvio(fecha);
            
            em.persist(env);
            em.flush();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private Date getSystemDate() {
        return getSystemDate(0);
    }

    private Date getSystemDate(Integer cantDias) {
        Query query = em.createNativeQuery("SELECT DATE_ADD(CURRENT_TIMESTAMP, INTERVAL " + cantDias + " DAY) AS DATE_VALUE FROM DUAL", DateItem.class);
        DateItem dateItem = (DateItem) query.getSingleResult();
        return dateItem.getDate();
    }

    public void consultarOficios() {
        // List<ExpActuaciones> lista = em.createNamedQuery("ExpActuaciones.findByEnvioCorteANDProveido", ExpActuaciones.class).setParameter("envioCorte", true).setParameter("proveidoCorte", false).getResultList();
        List<DocumentosJudicialesCorte> lista = em.createNamedQuery("DocumentosJudicialesCorte.findByEnvioCorteANDProveido", DocumentosJudicialesCorte.class).setParameter("envioCorte", true).setParameter("proveidoCorte", false).getResultList();

        for (DocumentosJudicialesCorte doc : lista) {
            System.out.println("Doc: " + doc.getId());
            obtenerConsultarOficios(doc);
        }
    }

    private void actualizarOficio(Integer codActuacionCaso, Integer codActuacionRelacionada) {

        List<ExpActuaciones> ac = em.createNamedQuery("ExpActuaciones.findByCodActuacionCaso", ExpActuaciones.class).setParameter("codActuacionCaso", codActuacionCaso).getResultList();

        if (!ac.isEmpty()) {
            if (ac.size() > 1) {
                JsfUtil.addErrorMessage("Se encontro mas de una actuacion asociada al oficio");
                System.out.println("CodActuacionCaso duplicado: " + codActuacionCaso);
            } else {
                System.out.println("Act: " + ac.get(0).getId());
                Date fecha = getSystemDate();
                ac.get(0).setProveidoCorte(true);
                ac.get(0).setFechaHoraProveido(fecha);
                ac.get(0).setCodActuacionRelacionada(codActuacionRelacionada);
                actuacionesController.setSelected(ac.get(0));
                actuacionesController.save(null);
            }
        }
    }

    private void obtenerConsultarOficios(DocumentosJudicialesCorte doc) {

        if (doc != null) {
            try {

                List<ExpConexiones> ws = em.createNamedQuery("ExpConexiones.findById", ExpConexiones.class).setParameter("id", Constantes.CONEXION_CORTE_CONSULTAR_OFICIOS_ID).getResultList();

                if (ws.isEmpty()) {
                    JsfUtil.addErrorMessage("No se encuentran parametros para conexion con la Corte");
                    return;
                }

                CloseableHttpClient CLIENT = Utils.createAcceptSelfSignedCertificateClient();
                HttpPost request = new HttpPost(ws.get(0).getIpServidor() + ":" + ws.get(0).getPuertoServidor() + ws.get(0).getUri());

                JSONObject json = new JSONObject();
                json.put("codCasoJudicial", doc.getCodCasoJudicial());
                json.put("codCircunscripcion", doc.getCodCircunscripcion());
                json.put("codDespachoJudicial", doc.getCodDespachoJudicial());
                json.put("codCasoJEM", doc.getDocumentoJudicial().getId());
                json.put("usuario", ws.get(0).getUsuario());

                try {
                    ByteArrayEntity params = new ByteArrayEntity(Utils.encryptMsg(json.toString(), Utils.generateKey()));
                    request.addHeader("content-type", "application/octet-stream;charset=UTF-8");
                    request.addHeader("charset", "UTF-8");
                    request.setEntity(params);

                    HttpResponse response = (HttpResponse) CLIENT.execute(request);
                    if (response != null) {
                        HttpEntity entity = response.getEntity();

                        byte[] bytes = IOUtils.toByteArray(entity.getContent());

                        String respuesta = Utils.decryptMsg(bytes, Utils.generateKey());

                        if (respuesta != null) {
                            JSONObject obj = null;
                            try {
                                obj = new JSONObject(respuesta);

                                if (obj.getInt("codigo") == 0) {

                                    JSONArray lista = obj.getJSONArray("lista");

                                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                                    RespuestaConsultaActuacionesCausa resp = null;
                                    for (int j = 0; j < lista.length(); j++) {
                                        JSONObject rs = lista.getJSONObject(j);

                                        if (rs.getInt("codActuacionRelacionada") != 0) {
                                            actualizarOficio(rs.getInt("codActuacionRelacionada"), rs.getInt("codActuacionCaso"));
                                        }

                                    }

                                } else {
                                    JsfUtil.addErrorMessage(obj.getString("descripcion"));
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }

                } catch (InvalidKeySpecException ex) {
                    ex.printStackTrace();
                    JsfUtil.addErrorMessage("No se pudo obtener actuaciones");
                    return;
                } catch (NoSuchPaddingException ex) {
                    ex.printStackTrace();
                    JsfUtil.addErrorMessage("No se pudo obtener actuaciones");
                    return;
                } catch (InvalidKeyException ex) {
                    ex.printStackTrace();
                    JsfUtil.addErrorMessage("No se pudo obtener actuaciones");
                    return;
                } catch (InvalidParameterSpecException ex) {
                    ex.printStackTrace();
                    JsfUtil.addErrorMessage("No se pudo obtener actuaciones");
                    return;
                } catch (IllegalBlockSizeException ex) {
                    ex.printStackTrace();
                    JsfUtil.addErrorMessage("No se pudo obtener actuaciones");
                    return;
                } catch (BadPaddingException ex) {
                    ex.printStackTrace();
                    JsfUtil.addErrorMessage("No se pudo obtener actuaciones");
                    return;
                } catch (UnsupportedEncodingException ex) {
                    ex.printStackTrace();
                    JsfUtil.addErrorMessage("No se pudo obtener actuaciones");
                    return;
                } catch (InvalidAlgorithmParameterException ex) {
                    ex.printStackTrace();
                    JsfUtil.addErrorMessage("No se pudo obtener actuaciones");
                    return;
                }
            } catch (IOException | KeyManagementException | NoSuchAlgorithmException | KeyStoreException ex) {
                JsfUtil.addErrorMessage("No se pudo obtener actuaciones");
                ex.printStackTrace();
            }
        }

    }
}
