import com.mysql.cj.protocol.Resultset;

import java.awt.Color;
import java.awt.Font;
import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

/**
 * creation de la classe fenetrecreationfichefraisforfait
 */
public class Fenetrecreationfichefraisforfait extends JFrame {
    /**
     * initialisation des label et champ texte ainsi que les boutons
     */
    JLabel labtitre, lablibelle, labmontant;
    JTextField jtflibelle, jtfmontant;
    JButton btajout, btnretour;
    JComboBox<String> comboBoxFicheFrais; // Sélecteur de fiches frais

    public void newFenetrecreationfichefraisforfait(String user_id) {
        /**
         * creation de la methode en reprenant en parametre l'user_id
         * creation de la fenetre
         */
        this.setTitle("Création frais forfait");
        this.setSize(400, 480);
        this.setLocationRelativeTo(null);
        JPanel pan = new JPanel();
        pan.setLayout(null);
        pan.setBackground(Color.gray);
        add(pan);
        /**
         * creation du label de la fenetre, du libelle et du montant ainsi que ses champs textes
         */
        labtitre = new JLabel("Formulaire frais forfait");
        labtitre.setBounds(200, 10, 300, 30);
        labtitre.setFont(new Font("Arial", Font.BOLD, 22));
        labtitre.setForeground(Color.white);
        pan.add(labtitre);

        lablibelle = new JLabel("Libellé");
        lablibelle.setBounds(20, 60, 300, 30);
        lablibelle.setFont(new Font("Arial", Font.BOLD, 18));
        lablibelle.setForeground(Color.white);
        pan.add(lablibelle);

        jtflibelle = new JTextField();
        jtflibelle.setBounds(200, 60, 200, 25);
        pan.add(jtflibelle);

        labmontant = new JLabel("Montant");
        labmontant.setBounds(20, 100, 300, 30);
        labmontant.setFont(new Font("Arial", Font.BOLD, 18));
        labmontant.setForeground(Color.white);
        pan.add(labmontant);

        jtfmontant = new JTextField();
        jtfmontant.setBounds(200, 100, 200, 25);
        pan.add(jtfmontant);

        // Ajout du sélecteur de fiches frais
        comboBoxFicheFrais = new JComboBox<>();
        comboBoxFicheFrais.setBounds(200, 140, 200, 25);
        pan.add(comboBoxFicheFrais);

        /**
         * creation du bouton enregistrer
         */
        btajout = new JButton("Enregistrer");
        btajout.setBounds(150, 260, 150, 30);
        btajout.setBackground(Color.green);
        btajout.setFont(new Font("Arial", Font.BOLD, 18));
        btajout.setForeground(Color.white);
        pan.add(btajout);

        /**
         * action du bouton enregistrer
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
                    String query = "INSERT INTO fraisforfait (libelle, montant) VALUES (?, ?)";
                    PreparedStatement statement = connection.prepareStatement(query);


                    /**
                     * remplacement des valeurs dynamique
                     */
                    String libelle = jtflibelle.getText();
                    String montant = jtfmontant.getText();


                    statement.setString(1, libelle);
                    statement.setString(2, montant);

                    /**
                     * execution de la requete
                     */
                    statement.executeUpdate();
                    String update = "INSERT INTO lignefraisforfait (FraisForfait_id, quantite, montant, date) VALUES (?,?,?,?)";
                    PreparedStatement statementupdate = connection.prepareStatement(update);

                    String queryid = "SELECT id FROM fraisforfait WHERE libelle = ?";
                    PreparedStatement statementid = connection.prepareStatement(queryid);
                    statementid.setString(1,libelle);
                    ResultSet idfrais = statementid.executeQuery();

                    String fraisforfaitid = "";
                    if (idfrais.next()){
                        fraisforfaitid = idfrais.getString("id");
                        statementupdate.setString(1,fraisforfaitid);
                    }
                    int count = 1;
                    while (idfrais.next()){
                        count++;
                    }

                    statementupdate.setInt(2,count);
                    statementupdate.setString(3,montant);
                    statementupdate.setString(4, String.valueOf(new Timestamp(new java.util.Date().getTime())));
                    statementupdate.executeUpdate();
                    String updateliaison = "INSERT INTO fichefrais_lignefraisforfait (FicheFrais_id, LigneFraisForfait_id) VALUES (?,?)";
                    PreparedStatement statementliaison = connection.prepareStatement(updateliaison);
                    statementliaison.setString(1,(String) comboBoxFicheFrais.getSelectedItem() );
                    String queryliaison = "SELECT id FROM lignefraisforfait WHERE FraisForfait_id = ?";
                    PreparedStatement statementqueryliaison = connection.prepareStatement(queryliaison);
                    statementqueryliaison.setString(1,fraisforfaitid);
                    ResultSet idlignefraisforfaits = statementqueryliaison.executeQuery();
                    if (idlignefraisforfaits.next()){
                        statementliaison.setString(2,idlignefraisforfaits.getString("id"));

                    }
                    statementliaison.executeUpdate();
                    /**
                     * affichage d'un message de confirmation
                     */
                    JFrame frame = new JFrame();
                    JOptionPane.showMessageDialog(frame, "Fiche enregistrée avec succès!");

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

        // Remplissage du sélecteur avec les fiches frais existantes
        remplirComboBoxFicheFrais(user_id);

        setVisible(true);
    }

    // Méthode pour remplir le sélecteur avec les fiches frais existantes
    private void remplirComboBoxFicheFrais(String user_id) {
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
