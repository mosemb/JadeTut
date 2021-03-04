package bookBuyer;

import jade.core.AID;

import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class BookSellerGui extends JFrame {
	
	private BookSellerAgent myAgent;
	private JTextField priceField, textField; 
	
	public BookSellerGui(BookSellerAgent a) {
		// TODO Auto-generated constructor stub
		super(a.getLocalName());
		myAgent = a;
		
		JPanel p = new JPanel();
		p.setLayout(new GridLayout(2,2));
		
		
		
		
		JPanel p = new JPanel();
		p.setLayout(new GridLayout(2, 2));
		p.add(new JLabel("Book title:"));
		titleField = new JTextField(15);
		p.add(titleField);
		p.add(new JLabel("Price:"));
		priceField = new JTextField(15);
		p.add(priceField);
		getContentPane().add(p, BorderLayout.CENTER);
		
	}

}
