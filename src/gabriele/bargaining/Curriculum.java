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
