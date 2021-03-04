package bookBuyer;

import java.util.Hashtable;


import jade.content.onto.basic.Action;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class BookSellerAgent extends Agent{
	
	// Catalogue of books 
	private Hashtable catalogue; 
	
	// Gui to store the books 
	private BookSellerGui myGui; 
	
	// Agent intializations 
	public void setup() {
		
		// Create the new catalogue 
		catalogue = new Hashtable();
		
		// Create GUI 
		myGui = new BookSellerGui(this);
		
		// Register the book serving example in the Directory Facilitor
		DFAgentDescription dfd = new DFAgentDescription(); 
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType("book-selling");
		sd.setName("Jade-book-trading");
		dfd.addServices(sd);
		
		try {
			
			DFService.register(this, dfd);
			
		} catch ( FIPAException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		// Behavior of buyers inquiring about book sales
		addBehaviour(new PurchaseOrderServer());
		
		// Fulfilling purchase orders from sellers 
		addBehaviour(new OfferRequestsServer());
	}
	
	/*
	 * Adding books to the catalogue using a oneShotBehavior
	 * */
	public void updateCatalogue(final String title, final int price) {
		 
		addBehaviour(new OneShotBehaviour() {
			
			@Override
			public void action() {
				// TODO Auto-generated method stub
				catalogue.put(title, new Integer(price));
				System.out.println(title + " Added into the catalogue with price "+ price);
				
			}
		});
	}
	
	// Agent Clean up. 
	public void takeDown() {
		// This is the degregestration 
		try {
			
			DFService.deregister(this);
			
			
			
		} catch (FIPAException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		// Dispose off the GUI
		myGui.dispose();
		
		System.out.println("Seller Agent ID "+getAID().getName()+ " is terminating.");
		
	}
	
	
	
	/*
	 * This class accepts messages from buyers inquiring about the books that are available
	 * For sale. And then 
	 * 
	 * */
	public class OfferRequestsServer extends CyclicBehaviour{	
		public void action() {
			MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.CFP);
			ACLMessage msg = myAgent.receive(mt);
			
			if(msg!=null) {
				String title = msg.getContent(); 
				ACLMessage reply = msg.createReply(); 
				Integer price = (Integer) catalogue.get(title);
				if(price != null) {
					// The requested book is available for sale then reply with a message
					reply.setPerformative(ACLMessage.PROPOSE);
					reply.setContent(String.valueOf(price.intValue()));
				}else {
					reply.setPerformative(ACLMessage.REFUSE);
					reply.setContent("Not available");
				}
				myAgent.send(reply);
			}else {
				block();
			}

		}
		
	}
	/*
	 * This class fulfills purchase orders from buyers and the title of the book is removed from 
	 * the catalog after a sale. 
	 * */
	
	private class PurchaseOrderServer extends CyclicBehaviour {
		
		public void action() {
			MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);
			ACLMessage msg = myAgent.receive(mt);
			if(msg!=null) {
				// Accept proposal message and process it. 
				String title = msg.getContent(); 
				ACLMessage reply = msg.createReply();
				
				Integer price = (Integer) catalogue.remove(title);
				
				if(price!= null) {
					reply.setPerformative(ACLMessage.INFORM);
					
					System.out.println(title + " sold to agent "+msg.getSender().getName());
				}else {
					reply.setPerformative(ACLMessage.FAILURE);
					reply.setContent("not - available");
				}
				myAgent.send(reply);
				
			} else {
				
				block();
			}
			
		}
		
	}
	
}
