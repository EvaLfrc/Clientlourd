import java.awt.Color;
import java.awt.Font;
import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Timestamp;

/**
 * creation de la classe Fenetrecreationfiche qui est un formulaire permettant l'insertion de différentes données
 * par l'utilisateur
 */

public class Fenetrecreationfiche extends JFrame {
    JLabel labtitre,labnbJustificatif, labmontantValide, labmois, labannee;
    JTextField jtfnbJustificatif, jtfmontantValide, jtfmois, jtfannee;
    JButton btajout, btnretour;


    public void Fenetrecreationfiche(String user_id){
        /**
         * creation de la fenetre qui a pour titre Creation en définissant la taille, et des elements de style
         */
        //titre fenêtre
        this.setTitle("Création");
        this.setSize(600,680);
        this.setLocationRelativeTo(null);
        JPanel pan=new JPanel();
        pan.setLayout(null);
        pan.setBackground(Color.gray);
        add(pan);

        /**
         * ajout du label titre, placement et elements de stylistique
         */
        labtitre=new JLabel("Formulaire");
        labtitre.setBounds(200,10,300,30);
        labtitre.setFont(new Font("Arial",Font.BOLD,22));
        labtitre.setForeground(Color.white);
        pan.add(labtitre);
        /**
         * ajout du label nombre justificatif ainsi que son champs de texte
         */
        labnbJustificatif=new JLabel("Nombre Justificatif :");
        labnbJustificatif.setBounds(20,60,300,30);
        labnbJustificatif.setFont(new Font("Arial",Font.BOLD,18));
        labnbJustificatif.setForeground(Color.white);
        pan.add(labnbJustificatif);

        jtfnbJustificatif=new JTextField();
        jtfnbJustificatif.setBounds(200,60,200,25);
        pan.add(jtfnbJustificatif);

        /**
         * Ajout du label et du champ texte Montant Valide, placement et stylistique
         */

        labmontantValide=new JLabel("Montant Valide");
        labmontantValide.setBounds(20,100,300,30);
        labmontantValide.setFont(new Font("Arial",Font.BOLD,18));
        labmontantValide.setForeground(Color.white);
        pan.add(labmontantValide);

        jtfmontantValide=new JTextField();
        jtfmontantValide.setBounds(200,100,200,25);
        pan.add(jtfmontantValide);

        /**
         * ajout du label et champs de texte Mois ainsi que les elements de stylistique
         */

        labmois=new JLabel("Mois :");
        labmois.setBounds(20,140,300,30);
        labmois.setFont(new Font("Arial",Font.BOLD,18));
        labmois.setForeground(Color.white);
        pan.add(labmois);

        jtfmois=new JTextField();
        jtfmois.setBounds(200,140,90,25);
        pan.add(jtfmois);

        /**
         * Ajout du label et du champs de texte Année avec ses éléments de stylistique
         */

        labannee=new JLabel("Année :");
        labannee.setBounds(20,180,300,30);
        labannee.setFont(new Font("Arial",Font.BOLD,18));
        labannee.setForeground(Color.white);
        pan.add(labannee);

        jtfannee=new JTextField();
        jtfannee.setBounds(200,180,90,25);
        pan.add(jtfannee);


        /**
         * ajout du bouton enregistrer ainsi que ses elements de style
         */
        btajout=new JButton("Enregistrer");
        btajout.setBounds(150,300,150,30);
        btajout.setBackground(Color.green);
        btajout.setFont(new Font("Arial",Font.BOLD,18));
        btajout.setForeground(Color.white);
        pan.add(btajout);

        /**
         * action du bouton enregistrer
         */
        btajout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                /**
                 * connexion à la base de donnée
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
                    String query = "INSERT INTO fichefrais (nbjustificatif, montantvalide, datemotif, mois, annee, user_id) VALUES (?, ?, ?, ?, ?,?)";
                    PreparedStatement statement = connection.prepareStatement(query);


                    /**
                     * remplacement des elements dans la bdd
                     */
                    String nbJustificatif = jtfnbJustificatif.getText();
                    String montantValide = jtfmontantValide.getText();
                    String mois = jtfmois.getText();
                    String annee = jtfannee.getText();


                    statement.setString(1, nbJustificatif);
                    statement.setString(2, montantValide);
                    statement.setString(3, String.valueOf(new Timestamp(new java.util.Date().getTime())));
                    statement.setString(4, mois);
                    statement.setString(5, annee);
                    statement.setString(6, user_id);

                    /**
                     * execution de la requete
                     */
                    statement.executeUpdate();

                    /**
                     * affichage d'un message de confirmation
                     */
                    JFrame frame = new JFrame();
                    JOptionPane.showMessageDialog(frame,"Fiche enregistré avec succès!");
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
         * création d'un bouton retour avec ses elements de stylistique
         */
        btnretour=new JButton("Retour");
        btnretour.setBounds(150,340,150,30);
        btnretour.setBackground(Color.red);
        btnretour.setFont(new Font("Arial",Font.BOLD,18));
        btnretour.setForeground(Color.white);
        pan.add(btnretour);

        /**
         * action du bouton retour qui renvoi au MenuGSB en reprenant en @param le user_id
         */
        btnretour.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> {
                new MenuGSB().LanceMenuGSB(user_id);
                this.dispose();
            });
        });
        setVisible(true);

    }

}

