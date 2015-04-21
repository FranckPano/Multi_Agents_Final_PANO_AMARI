import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


public class Simulator extends JFrame{
	
	Environment environment;
	Operator operator;
	Observator observator;
	
	static JTextArea operatorText;
	static JTextArea observatorText;
	
	static JTextArea clockText;
	
	public Simulator() {

	    /**************************************Partie Graphique***************************************/
		this.setTitle("Système Multi-Agents : Operator / Observator");
	    this.setSize(1200, 700);
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    this.setLocationRelativeTo(null);
	   
	    JPanel operatorPic = new JPanel();
	    operatorPic.setPreferredSize(new Dimension(280, 280));
	    ImagePanel imgpan = new ImagePanel("src/operator.png");
	    operatorPic.setLayout(new BorderLayout());
	    operatorPic.add(imgpan, BorderLayout.CENTER);
	    
	    operatorText = new JTextArea();
	    operatorText.setFont(new Font("Serif", Font.BOLD, 15));
	    JScrollPane scrollPaneOp = new JScrollPane( operatorText );
	    scrollPaneOp.setPreferredSize(new Dimension(280,280));
	    
	    JPanel house = new JPanel();
	    house.setPreferredSize(new Dimension(550, 550));
	    ImagePanel imgpan2 = new ImagePanel("src/maison_plan.png");
	    house.setLayout(new BorderLayout());
	    house.add(imgpan2, BorderLayout.CENTER);
	    
	    JPanel observatorPic = new JPanel();
	    observatorPic.setPreferredSize(new Dimension(280, 280));
	    ImagePanel imgpan3 = new ImagePanel("src/observator.png");
	    observatorPic.setLayout(new BorderLayout());
	    observatorPic.add(imgpan3, BorderLayout.CENTER);
	    
	    observatorText = new JTextArea();
	    observatorText.setFont(new Font("Serif", Font.BOLD, 15));
	    JScrollPane scrollPaneOp2 = new JScrollPane( observatorText );
	    scrollPaneOp2.setPreferredSize(new Dimension(280,280));
	    
	    JButton button = new JButton("Next Step");
	    button.setMinimumSize(new Dimension(300, 100));
	    button.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				next();
			}
	    });
	    
	    clockText = new JTextArea();
	    clockText.setFont(new Font("Serif", Font.BOLD, 25));
	    JScrollPane scrollPaneClock = new JScrollPane( clockText );
	    scrollPaneClock.setPreferredSize(new Dimension(60,40));
	    
	    JPanel content = new JPanel();
	    content.setPreferredSize(new Dimension(1200, 700));
	    content.setLayout(new GridBagLayout());
	    GridBagConstraints gbc = new GridBagConstraints();
	        
	    gbc.gridx = 0;
	    gbc.gridy = 0;
	    gbc.gridheight = 1;
	    gbc.gridwidth = 1;
	    content.add(operatorPic, gbc);
	    
	    gbc.gridy = 1;
	    content.add(scrollPaneOp, gbc);
	    
	    gbc.gridx = 3;
	    gbc.gridy = 1;
	    content.add(scrollPaneOp2, gbc);
	    
	    gbc.gridy = 0;
	    content.add(observatorPic, gbc);
	    
	    gbc.gridx = 1;
	    gbc.gridheight = 2;
	    gbc.gridwidth = 2;
	    content.add(house, gbc);
	    
	    gbc.gridx = 2;
	    gbc.gridy = 2;
	    gbc.gridheight = 1;
	    gbc.gridwidth = 1;
	    content.add(button,gbc);
	    
	    gbc.gridx = 3;
	    content.add(scrollPaneClock,gbc);
	    
	    this.setContentPane(content);
	    this.setVisible(true); 
	    
	    
	    /**************************************Partie Graphique***************************************/

		environment = new Environment();
		operator = new Operator(environment);
		observator = new Observator(environment);
		
		/*for(int i =0; i<1000; i++)
			next();
		*/
	}
	
	
	public void next(){
		operator.Update();
		observator.Update();
		environment.Update();
	}
	
    
    public class ImagePanel extends JPanel {
    	
    	public String path;
    	public ImagePanel(String p){path = p;}
    	  public void paintComponent(Graphics g){
    	    try {
    	      Image img = ImageIO.read(new File(path));
    	      g.drawImage(img, 0, 0, this);
    	    } catch (IOException e) {
    	      e.printStackTrace();
    	    }                
    	  }               
    }
	
    static public void main(String args[]) {
    	Simulator sim = new Simulator();
    }

}
