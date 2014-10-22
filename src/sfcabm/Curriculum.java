 package sfcabm;

public class Curriculum {
	int senderDegree;
	int senderID;
	int senderAge;
	double senderReservationWage;
	Consumer theSender;

	public Curriculum(Consumer sender,int dg,int id,int age,double productivity, double srw){
		theSender=sender;
		senderDegree=dg;
		senderID=id;
		senderAge=age;
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
	public double getSenderReservationWage(){
		return senderReservationWage;
	}
	public Consumer getSender(){
		return theSender;
	}
}
