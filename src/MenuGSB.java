import javax.swing.*;

/**
 * Il s'agit là d'une classe reprenant le menu principale qui est MenuGSB et affichant les différentes options
 */
public class MenuGSB {

    public void LanceMenuGSB(String user_id) {
        /**
         * Ici, on créer une fenetre swing et on recupère en @param le user_id qui nous sera utile
         */
        JFrame fenetre = new JFrame("Menu GSB");
        fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Création d'un JPanel
        JPanel panneau = new JPanel();

        /**
         * création du bouton ajouter
         */
        JButton btnadd = new JButton("Ajouter une fiche frais");
        // Ajout de composants au JPanel
        panneau.add(btnadd);
        // Ajout du JPanel à la fenêtre
        fenetre.add(panneau);
        // Définir la taille de la fenêtre
        fenetre.setSize(400, 300);
        btnadd.addActionListener(e -> {
            /**
             * On appelle la classe : fenetrecreationfiche en reprenant l'user_id et on ferme la fenêtre en cours
             */
                    SwingUtilities.invokeLater(() -> {
                        new Fenetrecreationfiche().Fenetrecreationfiche(user_id);
                        fenetre.dispose();
                    });
                });
        /**
         * On rend la fenêtre visible
         */
        fenetre.setVisible(true);


        /**
         * création du bouton "Voir les fiches" en l'ajoutant au panel et en définissant sa taille
         */
        JButton btnview = new JButton("Voir les fiches");
        // Ajout de composants au JPanel
        panneau.add(btnview);
        // Ajout du JPanel à la fenêtre
        fenetre.add(panneau);
        // Définir la taille de la fenêtre
        fenetre.setSize(400, 300);
        btnview.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> {
                /**
                 * appel de la classe fenetrevuefiche en reprenant en @param l'user_id
                 */
                new Fenetrevuefiche().MaFenetrevuefiche(user_id);
                fenetre.dispose();
            });
        });
        /**
         * on rend la fenêtre visible
         */
        fenetre.setVisible(true);

        /**
         * création du bouton "Voir les fiches frais" en l'ajoutant au panel et en définissant sa taille
         */
        JButton btnviewff = new JButton("Voir les fiches frais");
        // Ajout de composants au JPanel
        panneau.add(btnviewff);
        // Ajout du JPanel à la fenêtre
        fenetre.add(panneau);
        // Définir la taille de la fenêtre
        fenetre.setSize(400, 300);
        btnviewff.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> {
                /**
                 * appel de la classe fenetrevuefiche en reprenant en @param l'user_id
                 */
                new voirff().voirff(user_id);
                fenetre.dispose();
            });
        });
        /**
         * on rend la fenêtre visible
         */
        fenetre.setVisible(true);

        /**
         * création du bouton "Voir les fiches frais hors forfait" en l'ajoutant au panel et en définissant sa taille
         */
        JButton btnviewfhf = new JButton("Voir les fiches frais hors forfait");
        // Ajout de composants au JPanel
        panneau.add(btnviewfhf);
        // Ajout du JPanel à la fenêtre
        fenetre.add(panneau);
        // Définir la taille de la fenêtre
        fenetre.setSize(400, 300);
        btnviewfhf.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> {
                /**
                 * appel de la classe fenetrevuefiche en reprenant en @param l'user_id
                 */
                new voirfhf().voirfhf(user_id);
                fenetre.dispose();
            });
        });
        /**
         * on rend la fenêtre visible
         */
        fenetre.setVisible(true);

        /**
         * création du bouton ajouter un frais forfait avec l'ajout du composant au Jpanel et à la fenêtre
         * on défini la taille
         */
        JButton btnaddfraisforfait = new JButton("Ajouter un frais forfait");
        panneau.add(btnaddfraisforfait);
        fenetre.add(panneau);
        fenetre.setSize(400, 300);
        btnaddfraisforfait.addActionListener(e -> {
            /**
             * creation de l'action du bouton qui renverra sur une autre fenetre en prenant en @param l'user id
             * on ferme la fenêtre en cour
             */
            SwingUtilities.invokeLater(() -> {
                new Fenetrecreationfichefraisforfait().newFenetrecreationfichefraisforfait(user_id);
                fenetre.dispose();
            });
        });
        /**
         * On rend la fenêtre visible
         */
        fenetre.setVisible(true);

        /**
         * creation du bouton ajouter un frais hors forfait avec l'ajout du composant au panel
         * l'ajout du panel a la fenetre , definition de la fenetre
         */
        JButton btnaddfraishorsforfait = new JButton("Ajouter un frais hors forfait");
        // Ajout de composants au JPanel
        panneau.add(btnaddfraishorsforfait);
        // Ajout du JPanel à la fenêtre
        fenetre.add(panneau);
        // Définir la taille de la fenêtre
        fenetre.setSize(400, 300);
        btnaddfraishorsforfait.addActionListener(e -> {
            /**
             * ajout de l'action au bouton en reprenant en @param l'user_id
             */
            SwingUtilities.invokeLater(() -> {
                new Fenetrecreationfichefraishorsforfait().newFenetrecreationfichefraishorsforfait(user_id);
                fenetre.dispose();
            });
        });
        /**
         * On rend la fenetre visible
         */
        fenetre.setVisible(true);

        /**
         * creation du bouton de deconnexion, ajout du composant au panel, du panel a la fenetre et
         * definition de la fenetre
         */
        JButton btndisconnect = new JButton("Se deconnecter");
        panneau.add(btndisconnect);
        fenetre.add(panneau);
        fenetre.setSize(400, 300);
        btndisconnect.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> {

                FenetreConnexion fenetreConnexion = new FenetreConnexion();
                fenetre.dispose(); // Ferme la fenêtre actuelle
                fenetreConnexion.setVisible(true);
            });
        });
        // Rendre la fenêtre visible
        fenetre.setVisible(true);
    }

}
