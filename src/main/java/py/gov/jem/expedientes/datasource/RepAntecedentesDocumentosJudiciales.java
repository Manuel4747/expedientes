/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gov.jem.expedientes.datasource;

/**
 *
 * @author eduardo
 */
public class RepAntecedentesDocumentosJudiciales {
    private String nroCausa;
    private String caratula;
    private String ano;
    private String motivoProceso;
    private String label;
    private String texto;
    
    public RepAntecedentesDocumentosJudiciales(){
        
    }
    
    public RepAntecedentesDocumentosJudiciales(String label, String texto){
        this.label = label;
        this.texto = texto;
    }

    public String getNroCausa() {
        return nroCausa;
    }

    public void setNroCausa(String nroCausa) {
        this.nroCausa = nroCausa;
    }

    public String getCaratula() {
        return caratula;
    }

    public void setCaratula(String caratula) {
        this.caratula = caratula;
    }

    public String getAno() {
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public String getMotivoProceso() {
        return motivoProceso;
    }

    public void setMotivoProceso(String motivoProceso) {
        this.motivoProceso = motivoProceso;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
    
}
