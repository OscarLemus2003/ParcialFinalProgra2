package DTO;

import entidad.Estudiante;

import javax.persistence.Column;

public class InscripcionDTO {
    private int id_inscripcion;
    private EstudianteDTO estudiante;
    private CursoDTO curso;
    private String fecha_inscripcion;


    public int getId_inscripcion() {
        return id_inscripcion;
    }

    public void setId_inscripcion(int id_inscripcion) {
        this.id_inscripcion = id_inscripcion;
    }

    public EstudianteDTO getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(EstudianteDTO estudiante) {
        this.estudiante = estudiante;
    }

    public CursoDTO getCurso() {
        return curso;
    }

    public void setCurso(CursoDTO curso) {
        this.curso = curso;
    }

    public String getFecha_inscripcion() {
        return fecha_inscripcion;
    }

    public void setFecha_inscripcion(String fecha_inscripcion) {
        this.fecha_inscripcion = fecha_inscripcion;
    }

    public Object[] mostrarDatos(){
        return new Object[]{id_inscripcion, estudiante.getNombre()+" "+estudiante.getApellido(), estudiante.getEmail(), curso.getNombre_curso(), curso.getProfesor(), fecha_inscripcion};
    }
}
