package sfcabm;

public class LaborOffer {
//inteso come post vacancy
	//double senderProductivity;
	int senderID;
	double neededProductionCapacity,senderFirmReservationWage;
	Firm sender;

	public LaborOffer(Firm send,int id,double npc ,double sfrw){
		sender=send;
		senderID=id;
		neededProductionCapacity=npc;
		senderFirmReservationWage=sfrw;
	}

	public Firm getSender(){
		return sender;
	}
	public int getSenderID(){
		return senderID;
	}
	public double getSenderFirmReservationWage(){
		return senderFirmReservationWage;
	}
	public double getNeededProductionCapacity(){
		return neededProductionCapacity;
	}
	public void decreaseNeededProductionCapacityAfterHiring(double capacityIncrease){
		neededProductionCapacity=neededProductionCapacity-capacityIncrease;
	}
}

