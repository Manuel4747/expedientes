package py.gov.jem.expedientes.datasource;

public class RespuestaEnviarCorte {

    private Integer codActuacionCaso;

    public RespuestaEnviarCorte() {
    }

    public RespuestaEnviarCorte(Integer codActuacionCaso) {
        this.codActuacionCaso = codActuacionCaso;
    }

    public Integer getCodActuacionCaso() {
        return codActuacionCaso;
    }

    public void setCodActuacionCaso(Integer codActuacionCaso) {
        this.codActuacionCaso = codActuacionCaso;
    }

}
