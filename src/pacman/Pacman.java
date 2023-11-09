/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package pacman;
import javax.swing.JFrame;

/**
 *
 * @author MJAm
 */
public class Pacman extends JFrame{

    public Pacman(){
        add(new Model());
    }
    public static void main(String[] args) {
        Pacman pac = new Pacman();
	pac.setVisible(true);
	pac.setTitle("Pacman");
	pac.setSize(380,420);
	pac.setDefaultCloseOperation(EXIT_ON_CLOSE);
	pac.setLocationRelativeTo(null);
        
        
    }
    
}
