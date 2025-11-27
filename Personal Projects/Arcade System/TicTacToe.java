import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class TicTacToe extends JPanel implements ActionListener{ //implements an interface
	
	//initialising a panel, and label
	Random random = new Random();
	JPanel title_panel = new JPanel();
	JPanel button_panel = new JPanel();
	JLabel text = new JLabel();
	JButton[] buttons = new JButton[9];
	JButton Quit = new JButton("Quit");
	boolean P1;  //if P1 is false then it will be P2's turn 
	
	
	public TicTacToe(){
		setSize(800, 500);
		setLayout(new BorderLayout());
		
		text.setBackground(new Color(25,25,25));
		// text.setForeground(new Color(36,255,20));
		text.setForeground(Color.decode("#FDE8D3"));
		text.setFont(new Font("Ink Free", Font.BOLD,75));
		text.setHorizontalAlignment(JLabel.CENTER);
		text.setText("Tic Tac Toe!");
		text.setOpaque(true);
		
		title_panel.setLayout(new BorderLayout());
		title_panel.setBounds(0,0,800,100);
		
		button_panel.setLayout(new GridLayout(3,3));
		button_panel.setBackground(new Color(150,150,150));
		
		Quit.setBounds(10, 10, 70, 30);
		Quit.setFont(new Font("MV Boli", Font.BOLD, 15));
        Quit.setBackground(Color.BLACK);
        Quit.setForeground(Color.WHITE);
		Quit.setBorderPainted(false);
        Quit.setOpaque(false);
        title_panel.add(Quit);

		//creates buttons and styles them
		for (int x = 0; x < 9; x++){
			buttons[x] = new JButton();
			button_panel.add(buttons[x]);
			buttons[x].setFont(new Font ("MV Boli", Font.BOLD,120));
			buttons[x].setFocusable(false); //buttons cannot gain focus
			buttons[x].addActionListener(this);
		}
		
		
		title_panel.add(text);
		text.setFocusable(true);
		add(title_panel,BorderLayout.NORTH); //sticks title panel to the top of the border
		add(button_panel);
		
		fTurn();
	}

	public void newGame() {
		buttons[0].setText("");
		buttons[0].setBackground(Color.WHITE);
		buttons[1].setText("");
		buttons[1].setBackground(Color.WHITE);
		buttons[2].setText("");
		buttons[2].setBackground(Color.WHITE);
		buttons[3].setText("");
		buttons[3].setBackground(Color.WHITE);
		buttons[4].setText("");
		buttons[4].setBackground(Color.WHITE);
		buttons[5].setText("");
		buttons[5].setBackground(Color.WHITE);
		buttons[6].setText("");
		buttons[6].setBackground(Color.WHITE);
		buttons[7].setText("");
		buttons[7].setBackground(Color.WHITE);
		buttons[8].setText("");
		buttons[8].setBackground(Color.WHITE);
		for (int a = 0; a < 9; a++){
			buttons[a].setEnabled(true);
		}
		text.setText("Tic Tac Toe!");
	}
	
	@Override
	public void actionPerformed(ActionEvent e){ //Determines which player just put their sign down then hands over the turn to the other player
		for (int y = 0; y < 9; y++){
			if (e.getSource() == buttons[y]){
				if (P1){
					if(buttons[y].getText() == ""){
						// buttons[y].setForeground(new Color(77,225,250));
						buttons[y].setForeground(Color.decode("#406483"));
						buttons[y].setText("X");
						P1 = false;
						text.setText("O's turn"); 
						check();
					}
				} else{
					if(buttons[y].getText() == ""){
						// buttons[y].setForeground(new Color(254,1,154));
						buttons[y].setForeground(Color.decode("#89847b"));
						buttons[y].setText("O");
						P1 = true;
						text.setText("X's turn");
						check();
				}
			}
		}
	}
}
	 
	public void fTurn(){  //determines which player starts first
		if(random.nextInt(2) == 0) {
			P1 = true;
			text.setText("X's turn");
		} else {
			P1 = false;
			text.setText("O's turn");
		}
	}
	
	public void check(){//checks to see which player has the winning combination
		if((buttons[0].getText() == "X") &&  //checks if the first row has matching X's
		   (buttons[1].getText() == "X") && 
		   (buttons[2].getText() == "X")){
			   xWins(0,1,2);//winning combo
			   }
			   
		if((buttons[3].getText() == "X") &&  //checks if the second row has matching X's
		   (buttons[4].getText() == "X") && 
		   (buttons[5].getText() == "X")){
				xWins(3,4,5);//winning combo
				}
				
		if((buttons[6].getText() == "X") &&  //checks if the third row has matching X's
		   (buttons[7].getText() == "X") && 
		   (buttons[8].getText() == "X")){
			   xWins(6,7,8);//winning combo
			   }
		
		if((buttons[1].getText() == "X") && 
		   (buttons[4].getText() == "X") && 
		   (buttons[7].getText() == "X")){
			   xWins(1,4,7);//winning combo
			   }
		
		if((buttons[0].getText() == "X") &&  
		   (buttons[3].getText() == "X") && 
		   (buttons[6].getText() == "X")){
			   xWins(0,3,6);//winning combo
			   }
			   
		if((buttons[2].getText() == "X") &&  
		   (buttons[5].getText() == "X") && 
		   (buttons[8].getText() == "X")){
			   xWins(2,5,8);//winning combo
			   }
		
		
		if((buttons[0].getText() == "X") &&  
		   (buttons[4].getText() == "X") && 
		   (buttons[8].getText() == "X")){
			   xWins(0,4,8);//winning combo
			   }
			   
		if((buttons[2].getText() == "X") &&  
		   (buttons[4].getText() == "X") && 
		   (buttons[6].getText() == "X")){
			   xWins(2,4,6);//winning combo
			   }
			   
        if((buttons[0].getText() == "O") &&  
		   (buttons[1].getText() == "O") && 
		   (buttons[2].getText() == "O")){
			   oWins(0,1,2);//winning combo
			   }
			   
		if((buttons[3].getText() == "O") &&  
		   (buttons[4].getText() == "O") && 
		   (buttons[5].getText() == "O")){
				oWins(3,4,5);//winning combo
				}
				
		if((buttons[6].getText() == "O") &&  
		   (buttons[7].getText() == "O") && 
		   (buttons[8].getText() == "O")){
			   oWins(6,7,8);//winning combo
			   }
		
		if((buttons[1].getText() == "O") && 
		   (buttons[4].getText() == "O") && 
		   (buttons[7].getText() == "O")){
			   oWins(1,4,7);//winning combo
			   }
		
		if((buttons[0].getText() == "O") &&  
		   (buttons[3].getText() == "O") && 
		   (buttons[6].getText() == "O")){
			   oWins(0,3,6);//winning combo
			   }
			   
		if((buttons[2].getText() == "O") &&  
		   (buttons[5].getText() == "O") && 
		   (buttons[8].getText() == "O")){
			   oWins(2,5,8);//winning combo
			   }
		
		
		if((buttons[0].getText() == "O") &&  
		   (buttons[4].getText() == "O") && 
		   (buttons[8].getText() == "O")){
			   oWins(0,4,8);//winning combo
			   }
			   
		if((buttons[2].getText() == "O") &&  
		   (buttons[4].getText() == "O") && 
		   (buttons[6].getText() == "O")){
			   oWins(2,4,6);//winning combo
			   }
			    
		
	}
	
	public void xWins(int x, int y, int z){
		buttons[x].setBackground(Color.decode("#89847b"));
		buttons[y].setBackground(Color.decode("#89847b"));
		buttons[z].setBackground(Color.decode("#89847b"));
		
		for (int a = 0; a < 9; a++){
			buttons[a].setEnabled(false);
		}
		text.setText("X Wins!");
	}
	
	
	
	public void oWins(int x, int y, int z){
		buttons[x].setBackground(Color.decode("#406483"));
		buttons[y].setBackground(Color.decode("#406483"));
		buttons[z].setBackground(Color.decode("#406483"));
		
		for (int a = 0; a < 9; a++){
			buttons[a].setEnabled(false);
		}
		text.setText("O Wins!");
	}
	 
}
