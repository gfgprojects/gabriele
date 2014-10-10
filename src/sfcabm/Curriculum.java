 package sfcabm;

public class Curriculum {
	int senderDegree;
	int senderID;
	int senderAge;
	double senderProductivity;
	double senderReservationWage;

	public Curriculum(int dg,int id,int age,double productivity,double srw){
		senderDegree=dg;
		senderID=id;
		senderAge=age;
		senderProductivity=productivity;
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
	public double getSenderProductivity(){
		return senderProductivity;
	}
	public double getSenderReservationWage(){
		return senderReservationWage;
	}
}
