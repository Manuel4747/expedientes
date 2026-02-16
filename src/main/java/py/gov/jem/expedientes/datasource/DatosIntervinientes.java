package py.gov.jem.expedientes.datasource;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class DatosIntervinientes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "codPersona")
    private Integer codPersona;
    @Basic(optional = true)
    @Column(name = "codCasoJudicial")
    private Integer codCasoJudicial;
    @Basic(optional = true)
    @Column(name = "tipoInterviniente")
    private String tipoInterviniente;
    @Basic(optional = true)
    @Column(name = "nombresApellidos")
    private String nombresApellidos;
    @Basic(optional = true)
    @Column(name = "descripcionTipoDocumento")
    private String descripcionTipoDocumento;
    @Basic(optional = true)
    @Column(name = "nroDocumento")
    private String nroDocumento;
    @Basic(optional = true)
    @Column(name = "fechaNacimiento")
    private String fechaNacimiento;
    @Basic(optional = true)
    @Column(name = "sexo")
    private String sexo;
    @Basic(optional = true)
    @Column(name = "descripcionTipoPersona")
    private String descripcionTipoPersona;
    @Basic(optional = true)
    @Column(name = "descripcionOcupacion")
    private String descripcionOcupacion;
    @Basic(optional = true)
    @Column(name = "descripcionEstadoInterviniente")
    private String descripcionEstadoInterviniente;

    public DatosIntervinientes(){
        
    }
    public DatosIntervinientes(Integer codPersona, Integer codCasoJudicial, String tipoInterviniente, String nombresApellidos, String descripcionTipoDocumento, String nroDocumento, String fechaNacimiento, String sexo, String descripcionTipoPersona, String descripcionOcupacion, String descripcionEstadoInterviniente) {
        this.codPersona = codPersona;
        this.codCasoJudicial = codCasoJudicial;
        this.tipoInterviniente = tipoInterviniente;
        this.nombresApellidos = nombresApellidos;
        this.descripcionTipoDocumento = descripcionTipoDocumento;
        this.nroDocumento = nroDocumento;
        this.fechaNacimiento = fechaNacimiento;
        this.sexo = sexo;
        this.descripcionTipoPersona = descripcionTipoPersona;
        this.descripcionOcupacion = descripcionOcupacion;
        this.descripcionEstadoInterviniente = descripcionEstadoInterviniente;
    }

    public Integer getCodPersona() {
        return codPersona;
    }

    public void setCodPersona(Integer codPersona) {
        this.codPersona = codPersona;
    }

    public Integer getCodCasoJudicial() {
        return codCasoJudicial;
    }

    public void setCodCasoJudicial(Integer codCasoJudicial) {
        this.codCasoJudicial = codCasoJudicial;
    }

    public String getTipoInterviniente() {
        return tipoInterviniente;
    }

    public void setTipoInterviniente(String tipoInterviniente) {
        this.tipoInterviniente = tipoInterviniente;
    }

    public String getNombresApellidos() {
        return nombresApellidos;
    }

    public void setNombresApellidos(String nombresApellidos) {
        this.nombresApellidos = nombresApellidos;
    }

    public String getDescripcionTipoDocumento() {
        return descripcionTipoDocumento;
    }

    public void setDescripcionTipoDocumento(String descripcionTipoDocumento) {
        this.descripcionTipoDocumento = descripcionTipoDocumento;
    }

    public String getNroDocumento() {
        return nroDocumento;
    }

    public void setNroDocumento(String nroDocumento) {
        this.nroDocumento = nroDocumento;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getDescripcionTipoPersona() {
        return descripcionTipoPersona;
    }

    public void setDescripcionTipoPersona(String descripcionTipoPersona) {
        this.descripcionTipoPersona = descripcionTipoPersona;
    }

    public String getDescripcionOcupacion() {
        return descripcionOcupacion;
    }

    public void setDescripcionOcupacion(String descripcionOcupacion) {
        this.descripcionOcupacion = descripcionOcupacion;
    }
    
    public String getDescripcionEstadoInterviniente() {
        return descripcionEstadoInterviniente;
    }

    public void setDescripcionEstadoInterviniente(String descripcionEstadoInterviniente) {
        this.descripcionEstadoInterviniente = descripcionEstadoInterviniente;
    }

}
