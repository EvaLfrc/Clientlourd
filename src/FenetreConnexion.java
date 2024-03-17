import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;


/**
 * Cette classe concerne la connexion de l'utilisateur pour acceder au reste des service.
 * Elle contient une première methode public FenetreConnexion qui se connecte a la base donnée pour pouvoir
 * par la suite effectuer des lecture, modification, ajout ou suppression
 */

public class FenetreConnexion extends JFrame {

    /**
     * Cette méthode reprend plusieurs variable pour effectuer ultérieurement une connexion a la bdd
     * avec comme @param l'URL, l'Utilisateur, le Mot_de_passe de la base de donnée qui est en lien avec le logiciel
     */
    private static final String URL = "jdbc:mysql://localhost:3306/mgsb";
    private static final String UTILISATEUR = "root";
    private static final String MOT_DE_PASSE = "0000";

    public FenetreConnexion() {
        /**
         * Cette méthode affiche visuellement les différents éléments tel que les champs utilisateur et mot de passe en
         * ajoutant le bouton se connecter. il y a également le placement des différent label et champ ainsi que
         * le bouton de connexion
         */
        this.setTitle("Connexion");
        JLabel labelTitre = new JLabel("Connexion");
        JTextField champUtilisateur = new JTextField(20);
        JPasswordField champMotDePasse = new JPasswordField(20);
        JButton boutonConnexion = new JButton("Se connecter");
        this.setLayout(new BorderLayout());
        this.add(labelTitre, "North");
        JPanel panelCentre = new JPanel(new GridLayout(2, 2));
        panelCentre.add(new JLabel("Utilisateur : "));
        panelCentre.add(champUtilisateur);
        panelCentre.add(new JLabel("Mot de passe : "));
        panelCentre.add(champMotDePasse);
        this.add(panelCentre, "Center");
        this.add(boutonConnexion, "South");
        boutonConnexion.addActionListener((e) -> {
            /**
             * Dans cet action listenner on recupère les différentes valeurs comme le champUtilisateur et mot de passe
             * afin d'y tenter une connexion et on affiche un message si la connexion a réussi ou non
             */
            String username = champUtilisateur.getText();
            String password = new String(champMotDePasse.getPassword());
            boolean connexionReussie = this.tenterConnexion(username, password);
            if (connexionReussie) {
                JOptionPane.showMessageDialog(this, "Connexion réussie !");
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Échec de la connexion !");
            }

        });
        this.pack();
        this.setDefaultCloseOperation(3);
        this.setLocationRelativeTo((Component)null);
        this.setVisible(true);
    }

    private boolean tenterConnexion(String username, String password) {
        boolean connexionReussie = false;
        /**
         * On teste la connexion au serveur et on reprend le username et password de l'utilisateur afin de le comparer
         * dans la bdd
         */
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/mgsb", "root", "0000");
            PreparedStatement statement = connexion.prepareStatement("SELECT password,id FROM users WHERE username = ?");
            statement.setString(1,username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                /**
                 * On teste si les informations sont correctes des deux côtés
                 * si les informations sont bonnes, connexion reussi, sinon, on informe l'utilisateur
                 * quel paramètre est incorrecte
                 */

                String passwordFromDB = resultSet.getString("password");
                String useridFromDB = resultSet.getString("id");
                if (passwordFromDB.equals(password)) {
                    JFrame frame = new JFrame();
                    JOptionPane.showMessageDialog(frame,"Connexion réussi bienvenue " + username + "!");
                    connexionReussie = true;
                    /**
                     * redirige si connexion reussi
                     */
                    SwingUtilities.invokeLater(() -> {
                        new MenuGSB().LanceMenuGSB(useridFromDB);
                        this.dispose();
                    });
                } else {
                    /**
                     * affichage d'un message d'erreur pour le mdp
                     */
                    JFrame frame = new JFrame();
                    JOptionPane.showMessageDialog(frame,"Mot de passe incorrect !");
                }
            } else {
                /**
                 * affichage d'un message d'erreur si l'utilisateur est non trouvé
                 */
                JFrame frame = new JFrame();
                JOptionPane.showMessageDialog(frame,"Utilisateur non trouvé !");
            }

            resultSet.close();
            statement.close();
            connexion.close();
        } catch (SQLException | ClassNotFoundException var9) {
            var9.printStackTrace();
        }

        return connexionReussie;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FenetreConnexion fenetreConnexion = new FenetreConnexion();
            fenetreConnexion.setVisible(true);
        });
    }
}
