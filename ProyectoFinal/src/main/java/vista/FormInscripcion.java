package vista;

import DAO.CursoDAO;
import DAO.EstudianteDAO;
import DAO.InscripcionDAO;
import DTO.CursoDTO;
import DTO.EstudianteDTO;
import DTO.InscripcionDTO;
import entidad.Curso;
import entidad.Estudiante;
import entidad.Inscripcion;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class FormInscripcion extends JFrame{
    private JPanel panelLista;
    private JTextField txtProfesor;
    private JComboBox<CursoDTO> cbCurso;
    private JComboBox<EstudianteDTO> cbEstudiante;
    private JTextField txtEmail;
    private JTextField txtBuscar;
    private JButton btnBuscar;
    private JButton btnNuevo;
    private JButton btnActualizar;
    private JButton btnEditar;
    private JButton btnEliminar;
    private JButton btnLimpiar;
    private JButton btnSalir;
    private JTable table1;
    private JTextField txtFecha;

    private InscripcionDTO inscripcionDTO;

    public FormInscripcion(){
        this.setSize(1000,450);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.add(panelLista);

        java.lang.String[] datos = new java.lang.String[]{"Id Inscripción", "Estudiante","Email","Curso","Profesor","Fecha Inscripción"};
        table1.setModel(new DefaultTableModel(
                null,
                datos
        ));


        cbCurso.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(cbCurso.getItemCount()>0){
                    CursoDTO curso= (CursoDTO)cbCurso.getSelectedItem();
                    txtProfesor.setText(curso.getProfesor());
                }

            }
        });

        cbEstudiante.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(cbEstudiante.getItemCount()>0){
                    EstudianteDTO estudiante= (EstudianteDTO) cbEstudiante.getSelectedItem();
                    txtEmail.setText(estudiante.getEmail());
                }

            }
        });

        cargarCursos();
        cargarEstudiantes();
        cargarInscripciones(table1, new InscripcionDAO().obtenerInscripciones());

        btnNuevo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nuevaInscripcion(table1);
            }
        });
        btnActualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarInscripcion(table1);
            }
        });
        btnEditar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modificarInscripcion(table1);
            }
        });
        btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarInscripcion(table1);
            }
        });
        btnLimpiar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnEditar.setEnabled(false);
                btnActualizar.setEnabled(true);
                btnNuevo.setEnabled(true);
                btnEliminar.setEnabled(true);
                txtFecha.setText("");
                txtBuscar.setText("");
                if(cbEstudiante.getItemCount()!=0) cbEstudiante.setSelectedIndex(0);
                if(cbCurso.getItemCount()!=0)  cbCurso.setSelectedIndex(0);
            }
        });
        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarInscripcion(table1);
            }
        });
        btnSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new FormLogin().setVisible(true);
            }
        });
    }



    private void cargarCursos(){
        CursoDAO pDO= new CursoDAO();
        List<CursoDTO> cursos= pDO.obtenerCursos();

        for(int i=0; i<cursos.size(); i++){
            cbCurso.addItem(cursos.get(i));
        }
    }

    private void cargarEstudiantes(){
        EstudianteDAO pDO= new EstudianteDAO();
        List<EstudianteDTO> estudiantes= pDO.obtenerEstudiantes();

        for(int i=0; i<estudiantes.size(); i++){
            cbEstudiante.addItem(estudiantes.get(i));
        }
    }

    private void cargarInscripciones(JTable tabla, List<InscripcionDTO>  inscripciones){
        DefaultTableModel modelo=(DefaultTableModel)tabla.getModel();
        modelo.setRowCount(0);
        InscripcionDAO pDO= new InscripcionDAO();
        for(int i=0; i<inscripciones.size(); i++){
            modelo.addRow(inscripciones.get(i).mostrarDatos());
        }
    }

    public void nuevaInscripcion(JTable tabla){
        String msj=validarCamposVacios();
        if(msj.isEmpty()){//si algun campo esta vacio
            //validar la fecha
            if(validarFecha(txtFecha.getText())){
                InscripcionDAO dao= new InscripcionDAO();
                Inscripcion inscripcion= new Inscripcion();
                Curso curso= new Curso();
                curso.setId_curso(((CursoDTO)cbCurso.getSelectedItem()).getId_curso());
                inscripcion.setCurso(curso);
                Estudiante estudiante =new Estudiante();
                estudiante.setId_estudiante(((EstudianteDTO)cbEstudiante.getSelectedItem()).getId_estudiante());
                inscripcion.setEstudiante(estudiante);
                inscripcion.setFecha_inscripcion(txtFecha.getText());
                if(dao.guardarInscripcion(inscripcion)){
                    cargarInscripciones(tabla,dao.obtenerInscripciones());
                    JOptionPane.showMessageDialog(this,"Nueva inscripción realizada","Nueva Inscripción",JOptionPane.INFORMATION_MESSAGE);
                    cbEstudiante.setSelectedIndex(0);
                    cbCurso.setSelectedIndex(0);
                    txtFecha.setText("");
                }else{
                    JOptionPane.showMessageDialog(this,"Error al registrar nueva inscripción","Nueva Inscripción",JOptionPane.WARNING_MESSAGE);
                }
            }else{
                JOptionPane.showMessageDialog(this,"La fecha es inválida, ingrese en formato: yyyy-MM-dd","Nueva Inscripción",JOptionPane.WARNING_MESSAGE);
            }
        }else{
            JOptionPane.showMessageDialog(this,msj,"Nueva Inscripción",JOptionPane.WARNING_MESSAGE);
        }
    }

    public void actualizarInscripcion(JTable tabla){
        int filaSeleccionada = tabla.getSelectedRow();
        if (filaSeleccionada != -1) { // Verificar si se ha seleccionado una fila
            // Obtener datos de la fila seleccionada
            int id_inscripcion = (int) tabla.getValueAt(filaSeleccionada, 0);

            InscripcionDAO dao= new InscripcionDAO();
            inscripcionDTO =dao.obtenerInscrip(id_inscripcion);
            if(inscripcionDTO!=null){
                for(int i=0; i<cbCurso.getItemCount(); i++){
                    if(((CursoDTO)cbCurso.getItemAt(i)).getId_curso()==inscripcionDTO.getCurso().getId_curso()){
                        cbCurso.setSelectedIndex(i);
                        break;
                    }
                }

                for(int i=0; i<cbEstudiante.getItemCount(); i++){
                    if(((EstudianteDTO)cbEstudiante.getItemAt(i)).getId_estudiante()==inscripcionDTO.getEstudiante().getId_estudiante()){
                        cbEstudiante.setSelectedIndex(i);
                        break;
                    }
                }

                txtFecha.setText(inscripcionDTO.getFecha_inscripcion());
                btnEditar.setEnabled(true);
                btnActualizar.setEnabled(false);
                btnNuevo.setEnabled(false);
                btnEliminar.setEnabled(false);
            }
        } else {
            JOptionPane.showMessageDialog(this, "No se ha seleccionado ninguna fila.");
        }
    }

    public void modificarInscripcion(JTable tabla){
        String msj=validarCamposVacios();
        if(msj.isEmpty()){//si algun campo esta vacio
            //validar la fecha
            if(validarFecha(txtFecha.getText())){
                InscripcionDAO dao= new InscripcionDAO();
                Inscripcion inscripcion= new Inscripcion();
                inscripcion.setId_inscripcion(this.inscripcionDTO.getId_inscripcion());
                Curso curso= new Curso();
                curso.setId_curso(((CursoDTO)cbCurso.getSelectedItem()).getId_curso());
                inscripcion.setCurso(curso);
                Estudiante estudiante =new Estudiante();
                estudiante.setId_estudiante(((EstudianteDTO)cbEstudiante.getSelectedItem()).getId_estudiante());
                inscripcion.setEstudiante(estudiante);
                inscripcion.setFecha_inscripcion(txtFecha.getText());
                if(dao.modificarInscripcion(inscripcion)){
                    cargarInscripciones(tabla,dao.obtenerInscripciones());
                    JOptionPane.showMessageDialog(this,"La inscripción se ha modificado","Modificar Inscripción",JOptionPane.INFORMATION_MESSAGE);
                    cbEstudiante.setSelectedIndex(0);
                    cbCurso.setSelectedIndex(0);
                    txtFecha.setText("");
                    btnEditar.setEnabled(false);
                    btnActualizar.setEnabled(true);
                    btnNuevo.setEnabled(true);
                    btnEliminar.setEnabled(true);

                }else{
                    JOptionPane.showMessageDialog(this,"Error al modificar inscripción","Modificar Inscripción",JOptionPane.WARNING_MESSAGE);
                }
            }else{
                JOptionPane.showMessageDialog(this,"La fecha es inválida, ingrese en formato: yyyy-MM-dd","Modificar Inscripción",JOptionPane.WARNING_MESSAGE);
            }
        }else{
            JOptionPane.showMessageDialog(this,msj,"Modificar Inscripción",JOptionPane.WARNING_MESSAGE);
        }
    }

    public void eliminarInscripcion(JTable tabla){
        int filaSeleccionada = tabla.getSelectedRow();
        if (filaSeleccionada != -1) { // Verificar si se ha seleccionado una fila
            // Obtener datos de la fila seleccionada
            int id_inscripcion = (int) tabla.getValueAt(filaSeleccionada, 0);

            int opcion = JOptionPane.showConfirmDialog(null, "¿Estás seguro de eliminar la inscripción con ID: "+id_inscripcion+"?", "Confirmar Eliminar", JOptionPane.YES_NO_OPTION);

            if (opcion == JOptionPane.YES_OPTION) {
                InscripcionDAO dao= new InscripcionDAO();
                if(dao.eliminarInscripcion(id_inscripcion)){
                    cargarInscripciones(tabla,dao.obtenerInscripciones());
                    JOptionPane.showMessageDialog(this, "Se eliminó la inscripción","Eliminar Inscripción",JOptionPane.INFORMATION_MESSAGE);

                }else{
                    JOptionPane.showMessageDialog(this, "Error al eliminar inscripción","Eliminar Inscripción",JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "No se ha seleccionado ninguna fila.");
        }
    }

    public void buscarInscripcion(JTable tabla){
        String buscar= txtBuscar.getText();
        InscripcionDAO dao= new InscripcionDAO();
        List<InscripcionDTO>inscrpciones= dao.buscarInscripciones(buscar);
        cargarInscripciones(tabla,inscrpciones);
    }

    public String validarCamposVacios(){
        String msj="";
        if(cbEstudiante.getItemCount()==0) msj+="Seleccione un estudiante\n";
        if(cbCurso.getItemCount()==0) msj+="Seleccione un curso\n";
        if(txtEmail.getText().isEmpty()) msj+="Ingrese fecha\n";
        return msj;

    }

    public boolean validarFecha(String fecha) {
        try {
            LocalDate.parse(fecha, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            return true; // La fecha es válida
        } catch (DateTimeParseException e) {
            return false; // La fecha no es válida
        }
    }
}

