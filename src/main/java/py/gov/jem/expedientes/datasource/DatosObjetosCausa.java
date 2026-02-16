package py.gov.jem.expedientes.datasource;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class DatosObjetosCausa implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "CodObjetoCausa")
    private Integer codObjetoCausa;
    @Basic(optional = true)
    @Column(name = "CodCasoJudicial")
    private Integer codCasoJudicial;
    @Basic(optional = true)
    @Column(name = "DescripcionObjetoCausa")
    private String descripcionObjetoCausa;
    @Basic(optional = true)
    @Column(name = "GradoRealizacion")
    private String gradoRealizacion;
    @Basic(optional = true)
    @Column(name = "Principal")
    private String principal;
    @Basic(optional = true)
    @Column(name = "Ley")
    private String ley;
    @Basic(optional = true)
    @Column(name = "ArticuloDelCodigo")
    private String articuloDelCodigo;

    public DatosObjetosCausa() {
    }

    public DatosObjetosCausa(Integer codObjetoCausa, Integer codCasoJudicial, String descripcionObjetoCausa, String gradoRealizacion, String principal, String ley, String articuloDelCodigo) {
        this.codObjetoCausa = codObjetoCausa;
        this.codCasoJudicial = codCasoJudicial;
        this.descripcionObjetoCausa = descripcionObjetoCausa;
        this.gradoRealizacion = gradoRealizacion;
        this.principal = principal;
        this.ley = ley;
        this.articuloDelCodigo = articuloDelCodigo;
    }

    public Integer getCodObjetoCausa() {
        return codObjetoCausa;
    }

    public void setCodObjetoCausa(Integer codObjetoCausa) {
        this.codObjetoCausa = codObjetoCausa;
    }

    public Integer getCodCasoJudicial() {
        return codCasoJudicial;
    }

    public void setCodCasoJudicial(Integer codCasoJudicial) {
        this.codCasoJudicial = codCasoJudicial;
    }

    public String getDescripcionObjetoCausa() {
        return descripcionObjetoCausa;
    }

    public void setDescripcionObjetoCausa(String descripcionObjetoCausa) {
        this.descripcionObjetoCausa = descripcionObjetoCausa;
    }

    public String getGradoRealizacion() {
        return gradoRealizacion;
    }

    public void setGradoRealizacion(String gradoRealizacion) {
        this.gradoRealizacion = gradoRealizacion;
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public String getLey() {
        return ley;
    }

    public void setLey(String ley) {
        this.ley = ley;
    }

    public String getArticuloDelCodigo() {
        return articuloDelCodigo;
    }

    public void setArticuloDelCodigo(String articuloDelCodigo) {
        this.articuloDelCodigo = articuloDelCodigo;
    }

}
