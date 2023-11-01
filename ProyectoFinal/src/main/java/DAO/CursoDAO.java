package DAO;

import DTO.CursoDTO;
import entidad.Curso;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.ArrayList;
import java.util.List;

public class CursoDAO {
    private SessionFactory sessionFactory;

    public CursoDAO() {
        Configuration configuration = new Configuration().configure("hibernate.cfg.xml");
        sessionFactory = configuration.buildSessionFactory();
        System.out.println(sessionFactory);
    }

    public void guardarCurso(Curso curso) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.save(curso);
        transaction.commit();
        session.close();
    }

    /*public CursoDTO obtenerCursos(int productId) {
        Session session = sessionFactory.openSession();
        Curso product = session.get(Curso.class, productId);
        session.close();

        CursoDTO productDTO = new CursoDTO();
        productDTO.setName(product.getName());
        productDTO.setPrice(product.getPrice());

        return productDTO;
    }*/

    public List<CursoDTO> obtenerCursos() {
        Session session = sessionFactory.openSession();
        List<Curso> cursos = session.createQuery("FROM Curso", Curso.class).list();
        session.close();

        List<CursoDTO> cursosDTO = new ArrayList<>();

        for (Curso curso : cursos) {
            CursoDTO cursoDTO = new CursoDTO();
            cursoDTO.setId_curso(curso.getId_curso());
            cursoDTO.setNombre_curso(curso.getNombre_curso());
            cursoDTO.setProfesor(curso.getProfesor());
            cursosDTO.add(cursoDTO);
        }

        return cursosDTO;
    }
}
