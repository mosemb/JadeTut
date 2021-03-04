package bookBuyer;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.core.behaviours.TickerBehaviour;
//import jade.domain.introspection.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.core.behaviours.*;
import jade.core.AID;
import java.util.*;








public class BookBuyerAgent extends Agent {
	// This is the book that is being looked for
	private String targetBookTitle; 
	
	// The list of known sellers 
	private AID [] sellerAgents = {new AID("seller1", AID.ISLOCALNAME), 
			                       new AID("seller2",AID.ISLOCALNAME)};
	
	protected void setup() {
		// This is the greeting message
		System.out.println("Hello Buyer Agent "+getAID().getName()+ "is ready!"); 
		
		// Get the title of the books to buy as arguments from command line. 
		Object args [] = getArguments();
		
		if(args!=null && args.length >0) {
			
			targetBookTitle = (String)args[0];
			System.out.println("Trying to buy "+targetBookTitle);
			
			// Add a scheduled behavior where by the agent will be checking every minute for seller
			
			addBehaviour(new TickerBehaviour(this,6000) {
				
				@Override
				protected void onTick() {
					// TODO Auto-generated method stub
					myAgent.addBehaviour(new RequestPerformer());
					
				}
			});
		} else {
			// if no book title is specified then delete the agent
			System.out.println("No book title Specified");
			doDelete();
			
		}
		
	}
	
	// And finally do the clean up
	protected void takeDown() {
		
		System.out.println("Buyer Agent "+getAID().getName()+ " is terminating"); 
		
	}
	
	// Buyer agents checking in with Sellers for the target book 
	private class RequestPerformer extends Behaviour {
		private AID bestSeller;  // Best seller so far 
		private int bestPrice;  // Best price so far 
		private int repliesCnt=0;  // Count of replies for sellers 
		private MessageTemplate mt; // Template to recieve messages 
		private int step = 0;
		
		public void action() {
			
			switch(step) {
			case 0:
				// Send cfp to all sellers, A CFP is a call for proposal
				ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
				for(int i = 0; i <sellerAgents.length; ++i) {
					cfp.addReceiver(sellerAgents[i]);
				}
				cfp.setContent(targetBookTitle);
				cfp.setConversationId("book-trade");
				cfp.setReplyWith("cfp "+System.currentTimeMillis());
				myAgent.send(cfp);
				
				// Prepare the templates to recieve the proposals
				mt = MessageTemplate.and(MessageTemplate.MatchConversationId("book-trade"),
						MessageTemplate.MatchInReplyTo(cfp.getReplyWith()));
				
				step = 1;
				break; 
				
			case 1:
				// Recieve all proposals or refusals from seller agents 
				ACLMessage reply = myAgent.receive(mt);
				if(reply == null) {
					
					if(reply.getPerformative() == ACLMessage.PROPOSE) {
						
						int price = Integer.parseInt(reply.getContent());
						if(bestSeller == null || price < bestPrice) {
							bestPrice = price;
							bestSeller = reply.getSender();
						}
						
					}
					repliesCnt++;
					if(repliesCnt>=sellerAgents.length) {
						
						// All replies have been recieved
						step = 2;
						
						
					}
					
					
				}else {
					block(); 
				}
				break;
				
			case 2:
				// Send the purchase order to the seller that provided the best price
				ACLMessage order = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
				order.addReceiver(bestSeller);
				order.setContent(targetBookTitle);
				order.setConversationId("book-trade");
				order.setReplyWith("order "+System.currentTimeMillis());
				myAgent.send(order);
				step = 3; 
				break;
				
			case 3:
				// Recieve purcharse order by reply 
				reply = myAgent.receive(mt);
				if(reply != null) {
					
					// Purchase order recieved 
					if(reply.getPerformative()== reply.INFORM) {
						System.out.println(targetBookTitle + " Has been sucessfully purchased! " 
					+ reply.getSender().getName());
						
						System.out.println(bestPrice + " with as the best price ");
						myAgent.doDelete();
					}else {
						System.out.print("Requested book has already been purchased ");
						
					}
					
					step = 4; 
					
					
					
				}else {
					block();
				}
				
				
			}
				
		}
		
		
        public boolean done() {
        	
        	if(step ==2 && bestSeller==null) {
        		
        		System.out.println("Attempt failed "+ targetBookTitle + " Cannot be found!"); 
        	}
        	
        	return ((step ==2 && bestSeller == null) || step == 4);
                                    }		
	}
}



