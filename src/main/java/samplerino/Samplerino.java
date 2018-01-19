/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package samplerino;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UnsupportedLookAndFeelException;
import view.forms.FramePrincipal;
import view.forms.LogoAbertura;

/**
 *
 * @author Rafael
 */
public class Samplerino {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            FramePrincipal framePrincipal;
            LogoAbertura splashPanel = new LogoAbertura();
            splashPanel.setVisible(true);
            framePrincipal = new FramePrincipal();
            framePrincipal.setVisible(true);
            while (!framePrincipal.isShowing()) {}
            splashPanel.dispose();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException | IOException ex) {
            Logger.getLogger(Samplerino.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
