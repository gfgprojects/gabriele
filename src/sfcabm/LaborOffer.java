package sfcabm;

public class LaborOffer {

	double senderProductivity;
	int senderID;
	double senderFirmReservationWage;

	public LaborOffer(double firmprod,int id, double sfrw){
		senderProductivity=firmprod;
		senderID=id;
		senderFirmReservationWage=sfrw;
	}
	public double getSenderProductivity(){
		return senderProductivity;
	}
	
	public int getSenderID(){
		return senderID;
	}
	public double senderFirmReservationWage(){
		return senderFirmReservationWage;
	}
}

