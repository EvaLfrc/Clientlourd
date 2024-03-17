import java.awt.Color;
import java.awt.Font;
import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

/**
 * creation de la classe fenetrecreationfichefraishorsforfait
 */
public class Fenetrecreationfichefraishorsforfait extends JFrame {
    JLabel lablibelle, labmontant, labtitre;
    JTextField jtflibelle, jtfmontant;
    JButton btajout, btnretour;
    JComboBox<String> comboBoxFicheFrais;

    public void newFenetrecreationfichefraishorsforfait(String user_id) {
        /**
         * creation de methode reprenant en @param l'user_id
         */

        /**
         * creation de la fenetre
         */
        this.setTitle("Création");
        this.setSize(400, 480);
        this.setLocationRelativeTo(null);
        JPanel pan = new JPanel();
        pan.setLayout(null);
        pan.setBackground(Color.gray);
        add(pan);

        /**
         * creation du titre de la fenetre
         */

        labtitre = new JLabel("Formulaire frais hors forfait");
        labtitre.setBounds(200, 10, 300, 30);
        labtitre.setFont(new Font("Arial", Font.BOLD, 22));
        labtitre.setForeground(Color.white);
        pan.add(labtitre);

        /**
         * Ajout du sélecteur de fiches frais hf
         */

        comboBoxFicheFrais = new JComboBox<>();
        comboBoxFicheFrais.setBounds(200, 140, 200, 25);
        pan.add(comboBoxFicheFrais);

        /**
         * creation de la fenetre montant et de son champ de texte avec les elements de stylistique
         */
        labmontant = new JLabel("Montant");
        labmontant.setBounds(20, 100, 300, 30);
        labmontant.setFont(new Font("Arial", Font.BOLD, 18));
        labmontant.setForeground(Color.white);
        pan.add(labmontant);

        jtfmontant = new JTextField();
        jtfmontant.setBounds(200, 100, 200, 25);
        pan.add(jtfmontant);

        /**
         * creation de la fenetre libelle et de son champ de texte avec les elements de stylistique
         */
        lablibelle = new JLabel("libelle");
        lablibelle.setBounds(20, 60, 300, 30);
        lablibelle.setFont(new Font("Arial", Font.BOLD, 18));
        lablibelle.setForeground(Color.white);
        pan.add(lablibelle);

        jtflibelle = new JTextField();
        jtflibelle.setBounds(200, 60, 200, 25);
        pan.add(jtflibelle);

        /**
         * creation du bouton Enregistrer avec ses elements de stylistique
         */
        btajout = new JButton("Enregistrer");
        btajout.setBounds(150, 260, 150, 30);
        btajout.setBackground(Color.green);
        btajout.setFont(new Font("Arial", Font.BOLD, 18));
        btajout.setForeground(Color.white);
        pan.add(btajout);

        /**
         * Action du bouton enregistrer
         */
        btajout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                /**
                 * connexion à la bdd
                 */
                String url = "jdbc:mysql://localhost:3306/mgsb";
                String user = "root";
                String password = "0000";
                Connection connection = null;
                try {
                    connection = DriverManager.getConnection(url, user, password);

                    /**
                     * preparation de la requete
                     */
                    String query = "INSERT INTO lignefraishorsforfait (libelle, montant, date) VALUES (?,?,?)";
                    PreparedStatement statement = connection.prepareStatement(query);


                    /**
                     * remplacement des valeurs dynamique
                     */
                    String libelle = jtflibelle.getText();
                    String montant = jtfmontant.getText();



                    statement.setString(1, libelle);
                    statement.setString(2, montant);
                    statement.setString(3, String.valueOf(new Timestamp(new java.util.Date().getTime())));


                    /**
                     * execution de la requete
                     */
                    statement.executeUpdate();
                    String updateliaison = "INSERT INTO fichefrais_lignefraishorsforfait (FicheFrais_id, LigneFraisHorsForfait_id) VALUES (?,?)";
                    PreparedStatement statementliaison = connection.prepareStatement(updateliaison);
                    statementliaison.setString(1,(String) comboBoxFicheFrais.getSelectedItem() );
                    String queryliaison = "SELECT id FROM lignefraishorsforfait WHERE libelle = ?";
                    PreparedStatement statementqueryliaison = connection.prepareStatement(queryliaison);
                    statementqueryliaison.setString(1,libelle);
                    ResultSet idlignefraishorsforfaits = statementqueryliaison.executeQuery();
                    if (idlignefraishorsforfaits.next()){
                        statementliaison.setString(2,idlignefraishorsforfaits.getString("id"));

                    }
                    statementliaison.executeUpdate();
                    /**
                     * affichage d'un message de confirmation
                     */
                    JFrame frame = new JFrame();
                    JOptionPane.showMessageDialog(frame, "Fiche hors forfait enregistré avec succès!");

                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    try {
                        if (connection != null) {
                            connection.close();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        /**
         * creation du bouton retour
         */
        btnretour = new JButton("Retour");
        btnretour.setBounds(150, 290, 150, 30);
        btnretour.setBackground(Color.red);
        btnretour.setFont(new Font("Arial", Font.BOLD, 18));
        btnretour.setForeground(Color.white);
        pan.add(btnretour);

        /**
         * action du bouton retour
         */
        btnretour.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> {
                new MenuGSB().LanceMenuGSB(user_id);
                this.dispose();
            });
        });

        /**
         * Remplissage du sélecteur avec les fiches frais existantes
          */

        remplirComboBoxFicheFraishf(user_id);

        setVisible(true);

    }

    /**
     * Méthode pour remplir le sélecteur avec les fiches frais existantes
      */

    private void remplirComboBoxFicheFraishf(String user_id) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mgsb", "root", "0000");

            String query = "SELECT id FROM fichefrais WHERE user_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, user_id);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String ficheId = resultSet.getString("id");
                comboBoxFicheFrais.addItem(ficheId);
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
    }
}
