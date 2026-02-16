package py.gov.jem.expedientes.datasource;

import java.util.List;

public class Respuesta {
	private Integer codigo;
	private String descripcion;
	List<RespuestaConsultaCausas> lista;
	public Integer getCodigo() {
		return codigo;
	}
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public List<RespuestaConsultaCausas> getLista() {
		return lista;
	}
	public void setLista(List<RespuestaConsultaCausas> lista) {
		this.lista = lista;
	}
	
	public Respuesta() {
	}
	
	public Respuesta(Integer codigo, String descripcion) {
		this.codigo = codigo;
		this.descripcion = descripcion;
	}
	
}
