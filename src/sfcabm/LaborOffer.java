package sfcabm;

public class LaborOffer {
//inteso come post vacancy
	//double senderProductivity;
	int senderID;
	double senderFirmReservationWage;

	public LaborOffer(int id, double sfrw){
		senderID=id;
		senderFirmReservationWage=sfrw;
	}

	
	public int getSenderID(){
		return senderID;
	}
	public double senderFirmReservationWage(){
		return senderFirmReservationWage;
	}
}

