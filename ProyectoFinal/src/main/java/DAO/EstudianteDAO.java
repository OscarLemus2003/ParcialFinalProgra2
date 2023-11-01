package DAO;

import DTO.CursoDTO;
import DTO.EstudianteDTO;
import entidad.Curso;
import entidad.Estudiante;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.ArrayList;
import java.util.List;

public class EstudianteDAO {
    private SessionFactory sessionFactory;

    public EstudianteDAO() {
        Configuration configuration = new Configuration().configure("hibernate.cfg.xml");
        sessionFactory = configuration.buildSessionFactory();
    }

    /*public void guardarCurso(Curso curso) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.save(curso);
        transaction.commit();
        session.close();
    }*/



    public List<EstudianteDTO> obtenerEstudiantes() {
        Session session = sessionFactory.openSession();
        List<Estudiante> estudiantes = session.createQuery("FROM Estudiante", Estudiante.class).list();
        session.close();

        List<EstudianteDTO> estudiantesDTO = new ArrayList<>();

        for (Estudiante estudiante : estudiantes) {
            EstudianteDTO estudianteDTO = new EstudianteDTO();
            estudianteDTO.setId_estudiante(estudiante.getId_estudiante());
            estudianteDTO.setNombre(estudiante.getNombre());
            estudianteDTO.setApellido(estudiante.getApellido());
            estudianteDTO.setEmail(estudiante.getEmail());
            estudiantesDTO.add(estudianteDTO);
        }

        return estudiantesDTO;
    }

}
