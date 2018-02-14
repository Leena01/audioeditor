import java.awt.Color;
import matlabcontrol.*;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import matlabcontrol.MatlabInvocationException;
import view.elements.Label;

import java.sql.ResultSet;

public class LFrame extends JFrame implements ActionListener{
    public Component previous;
    public Object proxy;

    private int i;
    private int w=50;
    private JLabel slabel;
    private JLabel lfilename;
    private JLabel duration;
    private JLabel background;
    private JLabel lbackground;
    private JButton back;
    private JButton heart;
    private JButton play;
    private JButton pause;
    private Font font1;
    private Font font2;

    public LFrame(Object ref, Object stm, Object link, Object proxy, int count)
    {
        this.proxy = proxy;
        ResultSet rs = null;
        try {
            rs = ((Statement) stm).executeQuery("SELECT * FROM fav");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        previous = (Component)ref;
        font1 = new Font("Arial", Font.BOLD,13);
        font2 = new Font("Arial", Font.ITALIC,10);
        setLayout(null);
        setUndecorated(true);
        setBackground(new Color(0,0,0,0));
        setTitle("Apple Pie!");
        setLocation(500,250);
        setSize(240,340);
        setResizable(false);

        background = new Label(new ImageIcon("C:/Users/Livia/Desktop/audioeditor/images/background.png"));

        ImageIcon backi = new ImageIcon("C:/Users/Livia/Desktop/audioeditor/images/library.png");
        back = new JButton("",backi);
        back.addActionListener(this);
        back.setBorderPainted(false);
        add(background);
        background.add(back);

        for(i=1;i<=count;i++){
            w = w+5;
            try {
                rs.next();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            slabel = new Label();
            slabel.setOpaque(true);
            slabel.setBackground(Color.lightGray);


            lbackground = new Label(new ImageIcon("C:/Users/Livia/Desktop/audioeditor/images/background.png"));

            lfilename = new Label();
            try {
                lfilename.setText(rs.getString("song_name"));
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            lfilename.setOpaque(false);
            lfilename.setForeground(Color.red);

            duration = new Label();
            duration.setText("Song Duration");
            duration.setVisible(true);
            duration.setForeground(Color.black);

            ImageIcon hearti = new ImageIcon("C:/Users/Livia/Desktop/audioeditor/images/heart_red_lib.png");
            heart = new JButton("",hearti);
            heart.setBorderPainted(false);

            ImageIcon playi = new ImageIcon("C:/Users/Livia/Desktop/audioeditor/images/play_lib.png");

            play = new JButton("",playi);
            play.addActionListener(this);
            play.setBorderPainted(false);

            ImageIcon pausei = new ImageIcon("C:/Users/Livia/Desktop/audioeditor/images/pause.png");
            pause = new JButton("",pausei);
            pause.addActionListener(this);
            pause.setBorderPainted(false);
            pause.setVisible(false);
            background.add(slabel);
            slabel.add(lbackground);
            lbackground.add(lfilename);
            lbackground.add(duration);
            lbackground.add(heart);
            lbackground.add(play);
            lbackground.add(pause);
            slabel.setBounds(30,w,180,40);
            w=w+40;
            lbackground.setBounds(0,0,180,40);
            lfilename.setBounds(5,5,180,15);
            lfilename.setFont(font1);
            duration.setBounds(5,20,180,15);
            duration.setFont(font2);
            heart.setBounds(132,10,15,20);
            play.setBounds(154,10,20,20);
            pause.setBounds(154,10,20,20);
        }


        background.setBounds(0,0,240,340);
        back.setBounds(25,25,15,15);
    }


    public void actionPerformed(ActionEvent ae1) {

        if(ae1.getSource()== back)
        {
            this.setVisible(false);
            previous.setVisible(true) ;
        }
        else if(ae1.getSource() == pause)
        {
            {
                play.setVisible(true);
                pause.setVisible(false);
                try {
                    ((MatlabOperations)proxy).eval("pause(song);");
                } catch (MatlabInvocationException e)
                {
                    e.printStackTrace();
                }
            }}
        else if(ae1.getSource()==play)
        //if(result.equals("off"))
        {
            play.setVisible(false);
            pause.setVisible(true);

            try {
                ((MatlabOperations)proxy).eval("resume(song);");
            } catch (MatlabInvocationException e)
            {
                e.printStackTrace();
            }
        }

    }

}


/*
create table fav
(
	song_id INT AUTO_INCREMENT,
	song_name VARCHAR(255) NOT NULL,
	song_path VARCHAR(255) NOT NULL,
	PRIMARY KEY(song_id)

);
*/

