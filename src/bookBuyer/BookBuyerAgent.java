package bookBuyer;
import jade.core.Agent;
import jade.core.AID;






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
	
	

}


