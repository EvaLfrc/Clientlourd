import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

/**
 * creation de la classe fenetrevuefiche
 */
public class Fenetrevuefiche extends JFrame {
    /**
     * creation de la classe fenetrevuefiche et initialisation des variable locales et boutons
     */
    private DefaultTableModel modele;
    private String user_id;
    JButton btnRetour, btnSupprimer;

    public void MaFenetrevuefiche(String user_id) {
        this.user_id = user_id;
        /**
         * recuperation des variables
         */
        JFrame fenetre = new JFrame("Exemple JTable");
        fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panneau = new JPanel();
        String[] entetes = {"id", "nbjustificatif", "montantvalide", "datemotif", "mois", "annee"};
        modele = new DefaultTableModel(entetes, 0);

        /**
         * chargement des données initiales
         */
        actualiserDonnees();

        /**
         * creation du tableau
         */
        JTable tableau = new JTable(modele);
        tableau.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                /**
                 * action du clique de la souris pour modifier
                 */
                if (e.getClickCount() == 1) {
                    int row = tableau.getSelectedRow();
                    int column = tableau.getSelectedColumn();
                    Object ancienneValeur = modele.getValueAt(row, column);
                    /**
                     * ouverture d'une fenetre de dialogue pour la modification de valeur
                     */
                    String nouvelleValeur = JOptionPane.showInputDialog(fenetre, "Nouvelle valeur :", ancienneValeur);

                    if (nouvelleValeur != null) {
                        /**
                         * si la valeur a été modifiée, on mets a jour la bdd
                         */
                        modele.setValueAt(nouvelleValeur, row, column);
                        mettreAJourBaseDeDonnees(row, column, nouvelleValeur);
                    }
                }
            }
        });

        /**
         * creation du bouton supprimer fiche
         */
        btnSupprimer = new JButton("Supprimer Fiche");
        styleBouton(btnSupprimer);
        btnSupprimer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                demanderEtSupprimerFiche();
            }
        });

        /**
         * creation du bouton retour
         */
        btnRetour = new JButton("Retour");
        btnRetour.setBackground(Color.yellow);
        btnRetour.setFont(new Font("Arial",Font.BOLD,18));
        btnRetour.setForeground(Color.black);
        btnRetour.addActionListener(e -> {
            /**
             * bouton d'action qui ferme la fenetre actuelle
             */
            fenetre.dispose();
            new MenuGSB().LanceMenuGSB(user_id);
        });

        /**
         * ajout des composants à la fenetre
         */
        panneau.add(btnSupprimer);
        panneau.add(btnRetour);
        panneau.add(new JScrollPane(tableau));
        fenetre.add(panneau);

        /**
         * configuration de la taille de la fenetre
         */
        fenetre.setSize(400, 300);
        fenetre.setVisible(true);
    }

    private void actualiserDonnees() {
        modele.setRowCount(0);
        /**
         * methode d'actualisation de la fenetre
         */
        try {
            /**
             * protocole de connexion a la bdd
             */
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/mgsb", "root", "0000");
            Statement statement = connexion.createStatement();

            /**
             * Préparation de la requête pour n'avoir que les fiches de l'utilisateur connecté avec '"+ user_id+ "'"
             */
            ResultSet resultSet = statement.executeQuery("SELECT id, nbjustificatif, montantvalide, datemotif, mois, annee FROM fichefrais WHERE user_id ='" + user_id + "'");

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int nbjustificatif = resultSet.getInt("nbjustificatif");
                int montantvalide = resultSet.getInt("montantvalide");
                String datemotif = resultSet.getString("datemotif");
                String mois = resultSet.getString("mois");
                int annee = resultSet.getInt("annee");

                modele.addRow(new Object[]{id, nbjustificatif, montantvalide, datemotif, mois, annee});
            }

            resultSet.close();
            statement.close();
            connexion.close();

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void demanderEtSupprimerFiche() {
        /**
         * creation de la méthode pour demander a supprimer la fiche en entrant son id
         */
        String idFicheASupprimer = JOptionPane.showInputDialog(this, "Entrez l'ID de la fiche à supprimer:");

        /**
         * dialogue verification de l'utilisateur sur l'action
         */
        if (idFicheASupprimer != null && !idFicheASupprimer.isEmpty()) {
            int confirmation = JOptionPane.showConfirmDialog(this, "Êtes-vous sûr de vouloir supprimer la fiche ?", "Confirmation de suppression", JOptionPane.YES_NO_OPTION);

            if (confirmation == JOptionPane.YES_OPTION) {
                supprimerFiche(Integer.parseInt(idFicheASupprimer));
            }
        }
    }

    private void supprimerFiche(int idFiche) {
        /**
         * creation de la méthode pour supprimer la fiche
         */
        try {
            /**
             * connexion à la bdd
             */
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/mgsb", "root", "0000");

            /**
             * requete de suppression
             */
            String deleteQuery = "DELETE FROM fichefrais WHERE id = ?";
            PreparedStatement preparedStatement = connexion.prepareStatement(deleteQuery);

            preparedStatement.setInt(1, idFiche);
            int rowsAffected = preparedStatement.executeUpdate();

            preparedStatement.close();
            connexion.close();

            // Fin de la requête

            /**
             * verification si la fiche existe ou non avec une condition
             */
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Fiche supprimée avec succès !");
                actualiserDonnees(); // Actualisation des données après la suppression
            } else {
                // La fiche n'est pas trouvée
                JOptionPane.showMessageDialog(this, "Aucune fiche trouvée avec cet ID.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors de la suppression de la fiche : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mettreAJourBaseDeDonnees(int row, int column, String nouvelleValeur) {
        /**
         * methode pour mettre à jour la base de donnée en se reconnectant a la bdd
         */
        try {
            /*
            protocole de connexion
             */
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/mgsb", "root", "0000");

            int idLigne = (int) modele.getValueAt(row, 0);
            String nomColonne = modele.getColumnName(column);

            /**
             * requete et mise a jour de la bdd
             */
            String updateQuery = "UPDATE fichefrais SET " + nomColonne + " = ? WHERE id = ?";
            PreparedStatement preparedStatement = connexion.prepareStatement(updateQuery);

            /**
             * preparation de la requete sur la ligne en question
             */
            preparedStatement.setString(1, nouvelleValeur);
            preparedStatement.setInt(2, idLigne);

            preparedStatement.executeUpdate();

            preparedStatement.close();
            connexion.close();

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * fonction pour definir le style du bouton
     * @param bouton
     */
    private void styleBouton(JButton bouton) {
        bouton.setBounds(150, 290, 150, 30);
        bouton.setBackground(Color.red);
        bouton.setFont(new Font("Arial", Font.BOLD, 18));
        bouton.setForeground(Color.white);
    }


}
