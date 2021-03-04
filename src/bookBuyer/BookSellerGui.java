package bookBuyer;

import jade.core.AID;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.print.attribute.standard.PDLOverrideSupported;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.sun.tools.sjavac.comp.dependencies.PublicApiCollector;


public class BookSellerGui extends JFrame {
	
	private BookSellerAgent myAgent;
	private JTextField priceField, textField;
	private JTextField titleField; 
	
	public BookSellerGui(BookSellerAgent a) {
		// TODO Auto-generated constructor stub
		super(a.getLocalName());
		this.myAgent = a;
		
		JPanel p = new JPanel();
		p.setLayout(new GridLayout(2,2));
		p.add(new JLabel("Book title"));
		titleField = new JTextField(15); 
		p.add(titleField); 
		p.add(new JLabel("Price"));
		priceField = new JTextField(15); 
		p.add(priceField); 
		getContentPane().add(p,BorderLayout.CENTER);
		
		JButton addButton = new JButton("Add");
		addButton.addActionListener( new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				try {
					
					String title = titleField.getText().trim(); 
					String price = priceField.getText().trim();
					myAgent.updateCatalogue(title, Integer.parseInt(price));
					titleField.setText("");
					priceField.setText("");
						
				} catch (Exception e) {
					// TODO: handle exception
				}
				
			}
		}
				
			    
				);
		
		
	
		
	
		
	}

}
