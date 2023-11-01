package DAO;

import DTO.CursoDTO;
import DTO.EstudianteDTO;
import DTO.InscripcionDTO;
import entidad.Estudiante;
import entidad.Inscripcion;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.Query;
import java.util.ArrayList;
import java.util.List;

public class InscripcionDAO {
    private SessionFactory sessionFactory;

    public InscripcionDAO() {
        Configuration configuration = new Configuration().configure("hibernate.cfg.xml");
        sessionFactory = configuration.buildSessionFactory();
    }

    public boolean guardarInscripcion(Inscripcion inscripcion) {
        try{
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            session.save(inscripcion);
            transaction.commit();
            session.close();
            return true;
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
        return false;
    }

    public boolean modificarInscripcion(Inscripcion inscripcion) {
        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            session.update(inscripcion);
            transaction.commit();
            session.close();
            return true;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return false;
    }

    public boolean eliminarInscripcion(int idInscripcion) {
        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();

            Inscripcion inscripcion = session.get(Inscripcion.class, idInscripcion);

            if (inscripcion != null) {
                session.delete(inscripcion);
                transaction.commit();
                session.close();
                return true;
            } else {
                System.out.println("No se encontró la inscripción con el ID especificado.");
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return false;
    }

    public InscripcionDTO obtenerInscrip(int id_inscripcion) {
        Session session = sessionFactory.openSession();

        String hql = "SELECT i.id_inscripcion, e.nombre, e.apellido, e.email, c.nombre_curso, c.profesor, i.fecha_inscripcion, e.id_estudiante, c.id_curso " +
                "FROM Inscripcion i " +
                "INNER JOIN i.estudiante e " +
                "INNER JOIN i.curso c " +
                "WHERE i.id_inscripcion = :idInscripcion";

        Query query = session.createQuery(hql);
        query.setParameter("idInscripcion", id_inscripcion);

        List<Object[]> results = query.list();
        session.close();

        if (!results.isEmpty()) {
            Object[] result = results.get(0); // Tomamos la primera fila, ya que solo esperamos una

            InscripcionDTO inscripcionDTO = new InscripcionDTO();
            inscripcionDTO.setId_inscripcion((Integer) result[0]);
            EstudianteDTO estudianteDTO = new EstudianteDTO();
            estudianteDTO.setNombre((String) result[1]);
            estudianteDTO.setApellido((String) result[2]);
            estudianteDTO.setId_estudiante((int)result[7]);
            estudianteDTO.setEmail((String) result[3]);
            CursoDTO cursoDTO = new CursoDTO();
            cursoDTO.setNombre_curso((String) result[4]);
            cursoDTO.setProfesor((String) result[5]);
            cursoDTO.setId_curso((int)result[8]);
            inscripcionDTO.setEstudiante(estudianteDTO);
            inscripcionDTO.setCurso(cursoDTO);
            inscripcionDTO.setFecha_inscripcion((String) result[6]);

            return inscripcionDTO;
        } else {
            return null; // No se encontraron resultados para el ID dado
        }
    }


    public List<InscripcionDTO> buscarInscripciones(String filtro) {
        Session session = sessionFactory.openSession();

        String hql = "SELECT i.id_inscripcion, e.nombre, e.apellido, e.email, c.nombre_curso, c.profesor, i.fecha_inscripcion " +
                "FROM Inscripcion i " +
                "INNER JOIN i.estudiante e " +
                "INNER JOIN i.curso c " +
                "WHERE e.nombre LIKE :filtro " +
                "OR e.apellido LIKE :filtro " +
                "OR c.nombre_curso LIKE :filtro " +
                "OR c.profesor LIKE :filtro " +
                "OR i.fecha_inscripcion LIKE :filtro";

        Query query = session.createQuery(hql);
        query.setParameter("filtro", "%" + filtro + "%"); // Agrega % para buscar coincidencias parciales

        List<Object[]> results = query.list();
        session.close();

        List<InscripcionDTO> inscripcionesDTO = new ArrayList<>();

        for (Object[] result : results) {
            InscripcionDTO inscripcionDTO = new InscripcionDTO();
            inscripcionDTO.setId_inscripcion((Integer) result[0]);
            EstudianteDTO estudianteDTO = new EstudianteDTO();
            estudianteDTO.setNombre((String) result[1]);
            estudianteDTO.setApellido((String) result[2]);
            estudianteDTO.setEmail((String) result[3]);
            CursoDTO cursoDTO = new CursoDTO();
            cursoDTO.setNombre_curso((String) result[4]);
            cursoDTO.setProfesor((String) result[5]);
            inscripcionDTO.setEstudiante(estudianteDTO);
            inscripcionDTO.setCurso(cursoDTO);
            inscripcionDTO.setFecha_inscripcion((String) result[6]);
            inscripcionesDTO.add(inscripcionDTO);
        }

        return inscripcionesDTO;
    }



    public List<InscripcionDTO> obtenerInscripciones() {
        Session session = sessionFactory.openSession();

        String hql = "SELECT i.id_inscripcion, e.nombre,e.apellido, e.email, c.nombre_curso, c.profesor, i.fecha_inscripcion " +
                "FROM Inscripcion i " +
                "INNER JOIN i.estudiante e " +
                "INNER JOIN i.curso c";

        Query query = session.createQuery(hql);

        List<Object[]> results = query.list();
        session.close();

        List<InscripcionDTO> inscripcionesDTO = new ArrayList<>();

        for (Object[] result : results) {
            InscripcionDTO inscripcionDTO = new InscripcionDTO();
            inscripcionDTO.setId_inscripcion((Integer) result[0]);
            EstudianteDTO estudianteDTO = new EstudianteDTO();
            estudianteDTO.setNombre((String) result[1]);
            estudianteDTO.setApellido((String) result[2]);
            estudianteDTO.setEmail((String) result[3]);
            CursoDTO cursoDTO = new CursoDTO();
            cursoDTO.setNombre_curso((String) result[4]);
            cursoDTO.setProfesor((String) result[5]);
            inscripcionDTO.setEstudiante(estudianteDTO);
            inscripcionDTO.setCurso(cursoDTO);
            inscripcionDTO.setFecha_inscripcion((String) result[6]);
            inscripcionesDTO.add(inscripcionDTO);
        }

        return inscripcionesDTO;
    }



}
