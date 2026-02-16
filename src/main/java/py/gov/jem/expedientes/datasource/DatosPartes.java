package py.gov.jem.expedientes.datasource;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class DatosPartes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "codParteCasoJudicial")
    private Integer codParteCasoJudicial;
    @Basic(optional = true)
    @Column(name = "codCasoJudicial")
    private Integer codCasoJudicial;
    @Basic(optional = true)
    @Column(name = "nombresApellidos")
    private String nombresApellidos;
    @Basic(optional = true)
    @Column(name = "nroDocumento")
    private String nroDocumento;
    @Basic(optional = true)
    @Column(name = "descripcionTipoDocumento")
    private String descripcionTipoDocumento;
    @Basic(optional = true)
    @Column(name = "descripcionOcupacion")
    private String descripcionOcupacion;
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
    @Column(name = "descripcionEstadoCivil")
    private String descripcionEstadoCivil;
    @Basic(optional = true)
    @Column(name = "descripcionTipoParte")
    private String descripcionTipoParte;
    @Basic(optional = true)
    @Column(name = "localidad")
    private String localidad;
    @Basic(optional = true)
    @Column(name = "calle")
    private String calle;
    @Basic(optional = true)
    @Column(name = "telefono")
    private String telefono;
    @Basic(optional = true)
    @Column(name = "email")
    private String email;
    @Basic(optional = true)
    @Column(name = "tipoProfesional")
    private String tipoProfesional;
    @Basic(optional = true)
    @Column(name = "nombreProfesional")
    private String nombreProfesional;
    @Basic(optional = true)
    @Column(name = "representacion")
    private String representacion;
    @Basic(optional = true)
    @Column(name = "profesionalHabilitado")
    private String profesionalHabilitado;
    @Basic(optional = true)
    @Column(name = "descripcionTipoDomicilio")
    private String descripcionTipoDomicilio;
    
    public DatosPartes(){
        
    }

    public DatosPartes(Integer codParteCasoJudicial, Integer codCasoJudicial, String nombresApellidos,
            String nroDocumento, String descripcionTipoDocumento, String descripcionOcupacion, String fechaNacimiento,
            String sexo, String descripcionTipoPersona, String descripcionEstadoCivil, String descripcionTipoParte,
            String localidad, String calle, String telefono, String email, String tipoProfesional,
            String nombreProfesional, String representacion, String profesionalHabilitado, String descripcionTipoDomicilio) {
        this.codParteCasoJudicial = codParteCasoJudicial;
        this.codCasoJudicial = codCasoJudicial;
        this.nombresApellidos = nombresApellidos;
        this.nroDocumento = nroDocumento;
        this.descripcionTipoDocumento = descripcionTipoDocumento;
        this.descripcionOcupacion = descripcionOcupacion;
        this.fechaNacimiento = fechaNacimiento;
        this.sexo = sexo;
        this.descripcionTipoPersona = descripcionTipoPersona;
        this.descripcionEstadoCivil = descripcionEstadoCivil;
        this.descripcionTipoParte = descripcionTipoParte;
        this.localidad = descripcionTipoParte;
        this.calle = calle;
        this.telefono = telefono;
        this.email = email;
        this.tipoProfesional = tipoProfesional;
        this.nombreProfesional = nombreProfesional;
        this.representacion = representacion;
        this.profesionalHabilitado = profesionalHabilitado;
        this.descripcionTipoDomicilio = descripcionTipoDomicilio;
    }

    public Integer getCodParteCasoJudicial() {
        return codParteCasoJudicial;
    }

    public void setCodParteCasoJudicial(Integer codParteCasoJudicial) {
        this.codParteCasoJudicial = codParteCasoJudicial;
    }

    public Integer getCodCasoJudicial() {
        return codCasoJudicial;
    }

    public void setCodCasoJudicial(Integer codCasoJudicial) {
        this.codCasoJudicial = codCasoJudicial;
    }

    public String getNombresApellidos() {
        return nombresApellidos;
    }

    public void setNombresApellidos(String nombresApellidos) {
        this.nombresApellidos = nombresApellidos;
    }

    public String getNroDocumento() {
        return nroDocumento;
    }

    public void setNroDocumento(String nroDocumento) {
        this.nroDocumento = nroDocumento;
    }

    public String getDescripcionTipoDocumento() {
        return descripcionTipoDocumento;
    }

    public void setDescripcionTipoDocumento(String descripcionTipoDocumento) {
        this.descripcionTipoDocumento = descripcionTipoDocumento;
    }

    public String getDescripcionOcupacion() {
        return descripcionOcupacion;
    }

    public void setDescripcionOcupacion(String descripcionOcupacion) {
        this.descripcionOcupacion = descripcionOcupacion;
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

    public String getDescripcionEstadoCivil() {
        return descripcionEstadoCivil;
    }

    public void setDescripcionEstadoCivil(String descripcionEstadoCivil) {
        this.descripcionEstadoCivil = descripcionEstadoCivil;
    }

    public String getDescripcionTipoParte() {
        return descripcionTipoParte;
    }

    public void setDescripcionTipoParte(String descripcionTipoParte) {
        this.descripcionTipoParte = descripcionTipoParte;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTipoProfesional() {
        return tipoProfesional;
    }

    public void setTipoProfesional(String tipoProfesional) {
        this.tipoProfesional = tipoProfesional;
    }

    public String getNombreProfesional() {
        return nombreProfesional;
    }

    public void setNombreProfesional(String nombreProfesional) {
        this.nombreProfesional = nombreProfesional;
    }

    public String getRepresentacion() {
        return representacion;
    }

    public void setRepresentacion(String representacion) {
        this.representacion = representacion;
    }

    public String getProfesionalHabilitado() {
        return profesionalHabilitado;
    }

    public void setProfesionalHabilitado(String profesionalHabilitado) {
        this.profesionalHabilitado = profesionalHabilitado;
    }

    public String getDescripcionTipoDomicilio() {
        return descripcionTipoDomicilio;
    }

    public void setDescripcionTipoDomicilio(String descripcionTipoDomicilio) {
        this.descripcionTipoDomicilio = descripcionTipoDomicilio;
    }

}
