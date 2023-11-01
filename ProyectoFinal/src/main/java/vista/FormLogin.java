package vista;

import DAO.InscripcionDAO;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FormLogin extends JFrame{
    private JPanel panelLista;
    private JButton btnIngresar;
    private JButton btnSalir;
    private JTextField txtUsuario;
    private JPasswordField txtPass;
    private String usuarioActual;
    private String passActual;

    public FormLogin(){
        this.setSize(650,450);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.add(panelLista);
        passActual="admin";
        usuarioActual="admin";

        btnIngresar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ingresarSistema();
            }
        });
        btnSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int opcion = JOptionPane.showConfirmDialog(null, "¿Desea salir realmente del programa?", "Salir programa", JOptionPane.YES_NO_OPTION);

                if (opcion == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });
    }

    public void ingresarSistema(){
        String pass = new String(txtPass.getPassword());
        String uss= txtUsuario.getText();
        if(uss.equalsIgnoreCase(usuarioActual) && pass.equals(passActual)){
            dispose();
            new FormInscripcion().setVisible(true);

        }else{
            JOptionPane.showMessageDialog(this,"Usuario y/o contraseña incorrecto","Login",JOptionPane.WARNING_MESSAGE);
        }
    }
}
