package sfcabm;

import java.util.ArrayList;
import java.util.Iterator;

import repast.simphony.random.RandomHelper;
import repast.simphony.util.collections.IndexedIterable;

//import java.util.ArrayList;
//import sfcabm.LaborMkt;
import sfcabm.Curriculum;
import sfcabm.LaborOffer;
import sfcabm.LaborMarket;
import sfcabm.Context;

public class Firm {
	long production;
	int FirmID;
	int identity;
	double reservationWageFirmSum;
	int vacancySum;
	int totalJobs;
	double firmWageSum;
	int numVacancy;
	int numJobs;
	double sumOfWorkersProductivity=0;
	int productAbsoluteRank;
	int demand;
	double productionCapacityAfterWorkforceAdjustment;
	
		/*
	   public double desiredOutput;
	   public double expectedSales;
	   public double potentialOutput;
	   public int ActualCapital;
	   public double sigma=0.3;
	   */

	double senderProductivity;
	int senderID;
	double senderFirmReservationWage;


	public double initialOutput;
	public double initialCapitalStock;
	public double firmTech=1;
	public double averageAbilityFirm=0.8;
	


	repast.simphony.context.Context<Object> myContext;

	ArrayList<Curriculum> applicationList = new ArrayList<Curriculum>();
	ArrayList<Consumer> workersList = new ArrayList<Consumer>();

	Curriculum aCurriculum;
	Consumer aConsumer;
	IndexedIterable<Object> consumersList;
	Firm aFirm;	
	LaborMarket myLaborMarket;
	LaborOffer myOffer;

	int[] numberOfWokersInADegree;
	double[] totalProductivityOfWorkersInADegree;
	double[] averageProductivityOfWorkersInADegree;

	Iterator<Consumer> workersListIterator;
	Iterator<Curriculum> applicationListIterator;

	public Firm(int FirmID) {
		super();
		identity = FirmID;
	}

	public Firm(int FirmID,repast.simphony.context.Context<Object> con) {
		super();
		identity = FirmID;
		myContext=con;
	}

	public int getID(){
		return identity;
	}
	public void printID(){
		System.out.println("I am firm "+identity);
	}

	public void receiveCurriculum(Curriculum aCV){
		applicationList.add(aCV);
		if(Context.verbousFlag){
			System.out.println("  Firm "+identity+" received CV from consumer "+aCV.getSenderID()+" degree "+aCV.getSenderDegree());
		}
	}

	public void setInitialWorkers(){
		if(applicationList.size()>0){
			/*
			   try{
			   consumersList=myContext.getObjects(Class.forName("sfcabm.Consumer"));
			   }
			   catch(ClassNotFoundException e){
			   System.out.println("Class not found");
			   }
			   */
			for(int i=0;i<applicationList.size();i++){
				aCurriculum=(Curriculum)applicationList.get(i);
				aConsumer=aCurriculum.getSender();
				workersList.add(aConsumer);
				aConsumer.jobObtained(this);
				sumOfWorkersProductivity=sumOfWorkersProductivity+aConsumer.getProductivity();
			}
			/*
			   for(int i=0;i<applicationList.size();i++){
			   aCurriculum=(Curriculum)applicationList.get(i);
			   int curriculumSenderID=aCurriculum.getSenderID();
			   for(int j=0;j<consumersList.size();j++){
			   aConsumer=(Consumer)consumersList.get(j);
			   if(aConsumer.getIdentity()==curriculumSenderID){
			   workersList.add(aConsumer);
			   aConsumer.jobObtained(this);
			   sumOfWorkersProductivity=sumOfWorkersProductivity+aConsumer.getProductivity();
			   }
			   }
			   }
			   */
		}
		else{
			if(Context.verbousFlag){
				System.out.println("  Firm "+identity+" NO WORKERS");
			}
		}
		production=Math.round(sumOfWorkersProductivity*Context.parameterOfProductivityInProductionFuncion);
		//	productAbsoluteRank=1;
		productAbsoluteRank=RandomHelper.nextIntFromTo(1,10);
		if(Context.verbousFlag){
			System.out.println("  Firm "+identity+" sum of productivity "+sumOfWorkersProductivity+ " production "+production);
		}
	}

	public void laborForceDownwardAdjustment(){
		if(Context.verbousFlag){
			System.out.println("  Firm "+identity+" downward labor force adjustment ");
		}
		productionCapacityAfterWorkforceAdjustment=0;
		workersListIterator=workersList.iterator();
		while(workersListIterator.hasNext()){
			aConsumer=workersListIterator.next();
			if(aConsumer.getAge()<Context.consumerExitAge){
				productionCapacityAfterWorkforceAdjustment=productionCapacityAfterWorkforceAdjustment+aConsumer.getProductivity()*Context.parameterOfProductivityInProductionFuncion;
			}
			else{
				aConsumer.receiveRetirementNew();
				workersListIterator.remove();
			}
		}
//		if((productionCapacityAfterWorkersRetire-demand)>(OfficeForStatistics.averageProductivity*Context.parameterOfProductivityInProductionFuncion)){
		if((productionCapacityAfterWorkforceAdjustment-demand)>0){
			while(productionCapacityAfterWorkforceAdjustment>demand){
				int latestWorkerPosition=workersList.size()-1;
				aConsumer=workersList.get(latestWorkerPosition);
				productionCapacityAfterWorkforceAdjustment=productionCapacityAfterWorkforceAdjustment-aConsumer.getProductivity()*Context.parameterOfProductivityInProductionFuncion;
				aConsumer.receiveFiredNew();
				workersList.remove(latestWorkerPosition);
			}
		}

	}


	public void laborForceUpwardAdjustment(){
			switch(Context.firmsWorkersMatching){
				case 0:
					hireUsingReceivedCV();
					break;
				case 1:
					hireUsingReceivedCV();
					sendVacancies();
					break;
				case 2:
					sendVacancies();
					break;
				default: System.out.println("Unknown workers firms matching mechanism");
					 break;
			}
	}

	public void hireUsingReceivedCV(){
		applicationListIterator=applicationList.iterator();
		if(Context.verbousFlag){
			System.out.println("     firm "+identity+" application list size "+applicationList.size()+" prodcap "+productionCapacityAfterWorkforceAdjustment+" dem "+demand);
		}
		while(productionCapacityAfterWorkforceAdjustment<demand && applicationListIterator.hasNext()){
			//		while(applicationListIterator.hasNext()){
			aCurriculum=applicationListIterator.next();
			aConsumer=aCurriculum.getSender();
			if(aConsumer.getIsWorkingFlag()){
				applicationListIterator.remove();
				if(Context.verbousFlag){
					System.out.println("     the sender was hired by another firm");
				}
			}
			else{
				aConsumer.receiveHiredNew(this);
				productionCapacityAfterWorkforceAdjustment=productionCapacityAfterWorkforceAdjustment+aConsumer.getProductivity()*Context.parameterOfProductivityInProductionFuncion;
				workersList.add(aConsumer);
				applicationListIterator.remove();
			}
			if(Context.verbousFlag){
				System.out.println("     firm "+identity+" application list size "+applicationList.size()+" prodcap "+productionCapacityAfterWorkforceAdjustment+" dem "+demand);
			}
			}


		}

	public void hire(Consumer newWorker){
				workersList.add(newWorker);
				newWorker.receiveHiredNew(this);
	}




		//compute average productivity for each degree of education
		public void computeAverageProductivityForEachDegreeOfEducation(){
			numberOfWokersInADegree=new int[7];
			totalProductivityOfWorkersInADegree=new double[7];
			averageProductivityOfWorkersInADegree=new double[7];
			for(int i=0;i<workersList.size();i++){
				aConsumer=(Consumer)workersList.get(i);
				int degree=aConsumer.getDegree();
				numberOfWokersInADegree[degree]++;
				totalProductivityOfWorkersInADegree[degree]=totalProductivityOfWorkersInADegree[degree]+aConsumer.getProductivity();
				averageProductivityOfWorkersInADegree[degree]=totalProductivityOfWorkersInADegree[degree]/numberOfWokersInADegree[degree];
			}
		if(Context.verbousFlag){
			for(int j=0;j<7;j++){
				System.out.println("Firm "+identity+" degree "+j+" n "+numberOfWokersInADegree[j]+" tp "+totalProductivityOfWorkersInADegree[j]+" ap "+averageProductivityOfWorkersInADegree[j]);
			}
		}
		}

		public void setWorkersWage(){
			for(int i=0;i<workersList.size();i++){
				aConsumer=workersList.get(i);
				int degree=aConsumer.getDegree();
				double aWage;
				switch(Context.wageSettingRule){
					case 0: aConsumer.setWage(Context.parameterOfProductivityInProductionFuncion*aConsumer.getProductivity()); 
						break;
					case 1: aConsumer.setWage(Context.parameterOfProductivityInProductionFuncion*averageProductivityOfWorkersInADegree[degree]);
						break;
					case 2: aConsumer.setWage(Context.parameterOfProductivityInProductionFuncion*OfficeForStatistics.averageProductivityOfWorkersInADegree[degree]);
						break;
					default: System.out.println("Unknown wage setting rule");
						 break;
				}
			}
		}

		// SEND LABOR DEMAND TO LABOR MARKET

		public void sendVacancies(){
			if(productionCapacityAfterWorkforceAdjustment<demand){
				myOffer = new LaborOffer(this,identity,(demand-productionCapacityAfterWorkforceAdjustment),senderFirmReservationWage);
				try{
					myLaborMarket=(LaborMarket)(myContext.getObjects(Class.forName("sfcabm.LaborMarket"))).get(0);
				}
				catch(ClassNotFoundException e){
					System.out.println("Class not found");
				}

				myLaborMarket.receiveVacancies(myOffer);
			}
		}

		/*
		   public void hire(){

		   }
		   questo viene dopo
		   */


		//INIZIALIZZARE OUTPUT LEVEL:
		//PUO SERVIRE PER QUANDO SI INTRODUCE IL CAPITALE? SERVE UNA COMBINAZIONE DI K ED L?
		/*
		   public void setInitialProduction(){
		   initialOutput=(Context.NumConsumers/Context.NumFirms)*Math.min(firmTech,averageAbilityFirm);
		   initialCapitalStock=initialOutput/Math.min(firmTech,averageAbilityFirm);
		   }

		//non so calcolare il numero dei workers di ogni firm
		//si deve inizializzare past sales: si inizia con un valore a caso e poi si lega al consumo dei worker? 
		 * 
		 * 
		 public void productionDecision(){
		 potentialOutput=(1-sigma)*ActualCapital*Math.min(sumOfWorkersProductivity,averageAbilityFirm);
		 expectedSales=???
		 desiredOutput=expectedSales;

		 if (potentialOutput<desiredOutput){
		 laborDemand++;
		 }
		 else laborDemand=0;
		 }

*/

		public void setDemand(double industryProduction,double industryDemand){
			demand=(int)Math.round(production/industryProduction*industryDemand);
			if(Context.verbousFlag){
				System.out.println("  Firm "+identity+" production "+production+" demand "+demand);
			}
		}
		public void jettisoningCurricula(){
			applicationList = new ArrayList<Curriculum>();
		}


		public int getProductAbsoluteRank(){
			return productAbsoluteRank;
		}

		public long getProduction(){
			return production;
		}
		public int[] getnumberOfWokersInADegree(){
			return numberOfWokersInADegree;
		}
		public double[] getTotalProductivityOfWorkersInADegree(){
			return totalProductivityOfWorkersInADegree;
		}






		//reset the firm each time step 
		/*public void reset(){
		  numJobs=0;
		  firmWageSum=0;
		  applicationList.clear();
		  jobList.clear();
		}
		*/
}
