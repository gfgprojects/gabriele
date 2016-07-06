 package gabriele.bargaining;

import gabriele.agents.Consumer;

public class Curriculum {
	int senderDegree;
	int senderID;
	int senderAge;
	double senderReservationWage;
	double senderProduction;
	Consumer theSender;

	public Curriculum(Consumer sender,int dg,int id,int age,double production, double srw){
		theSender=sender;
		senderDegree=dg;
		senderID=id;
		senderAge=age;
		senderProduction=production;
		senderReservationWage=srw;
	}
	public int getSenderDegree(){
		return senderDegree;
	}
	public int getSenderID(){
		return senderID;
	}
	public int getSenderAge(){
		return senderAge;
	}
	public double getSenderProduction(){
		return senderProduction;
	}
	public double getSenderReservationWage(){
		return senderReservationWage;
	}
	public Consumer getSender(){
		return theSender;
	}
}
