/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.awt.Color;
import java.util.ArrayList;

/**
 *
 * @author Rafael
 */
final public class ListaCores {

    final static ArrayList<Color> cores = new ArrayList();

    public static ArrayList<Color> getColors() {
        cores.add(new Color(75, 0, 130, 100));
        cores.add(new Color(123, 104, 238, 100));
        cores.add(new Color(0, 0, 255, 100));
        cores.add(new Color(128, 0, 0, 100));
        cores.add(new Color(0, 0, 128, 100));
        cores.add(new Color(70, 130, 180, 100));
        cores.add(new Color(250, 128, 114, 100));
        cores.add(new Color(255, 165, 0, 100));
        cores.add(new Color(0, 206, 209, 100));
        cores.add(new Color(0, 128, 0, 100));
        cores.add(new Color(154, 205, 50, 100));
        cores.add(new Color(128, 128, 0, 100));
        cores.add(new Color(218, 165, 32, 100));
        cores.add(new Color(210, 105, 30, 100));
        cores.add(new Color(255, 0, 0, 100));
        cores.add(new Color(210, 105, 30, 100));
        cores.add(new Color(178, 34, 34, 100));
        cores.add(new Color(30, 144, 255, 100));
        cores.add(new Color(148, 0, 211, 100));
        cores.add(new Color(220, 20, 60, 100));
        return cores;
    }
}
