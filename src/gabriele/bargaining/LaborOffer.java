/*
GABRIELE: the General Agent Based Repast Implemented Extensible Laboratory for Economics
Copyright (C) 2018  Gianfranco Giulioni
This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.
This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.
You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package gabriele.bargaining;

import gabriele.agents.Firm;

public class LaborOffer {
//inteso come post vacancy
	//double senderProductivity;
	int senderID;
	double neededProductionCapacity;
	double[] senderFirmReservationWage;
	Firm sender;

	public LaborOffer(Firm send,int id,double npc ,double[] sfrw){
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
	public double[] getSenderFirmReservationWage(){
		return senderFirmReservationWage;
	}
	public double getNeededProductionCapacity(){
		return neededProductionCapacity;
	}
	public void decreaseNeededProductionCapacityAfterHiring(double capacityIncrease){
		neededProductionCapacity=neededProductionCapacity-capacityIncrease;
	}
}

