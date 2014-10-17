package sfcabm;

import java.util.ArrayList;

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
	
	/*
	public double desiredOutput;
	public double expectedSales;
	public double potentialOutput;
	public int ActualCapital;
	public double sigma=0.3;
	public double initialOutput;
	public double initialCapitalStock;
	public double firmTech=1;
	public double averageAbilityFirm=0.8;
	*/

	int senderID;
	double senderFirmReservationWage;
	double wageBase=300;
	int degree;
	
	public int sumAbility0, sumAbility1, sumAbility2, sumAbility3, sumAbility4, sumAbility5, sumAbility6;
	public double avgAbility0, avgAbility1, avgAbility2, avgAbility3, avgAbility4, avgAbility5, avgAbility6;
	public double senderFirmReservationWage_0, senderFirmReservationWage_1, senderFirmReservationWage_2, senderFirmReservationWage_3, senderFirmReservationWage_4, senderFirmReservationWage_5, senderFirmReservationWage_6; 
	 repast.simphony.context.Context<Object> myContext;

	ArrayList<Curriculum> applicationList = new ArrayList<Curriculum>();
	ArrayList<Consumer> workersList = new ArrayList<Consumer>();

	Curriculum aCurriculum;
	Consumer aConsumer;
	IndexedIterable<Object> consumersList;
	Firm aFirm;	
	LaborMarket myLaborMarket;
	LaborOffer myOffer;


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
			try{
				consumersList=myContext.getObjects(Class.forName("sfcabm.Consumer"));
			}
			catch(ClassNotFoundException e){
				System.out.println("Class not found");
			}


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
		}
		else{
			System.out.println("  Firm "+identity+" NO WORKERS");
		}
		production=Math.round(sumOfWorkersProductivity*Context.parameterOfProductivityInProductionFuncion);
	//	productAbsoluteRank=1;
		productAbsoluteRank=RandomHelper.nextIntFromTo(1,10);
		if(Context.verbousFlag){
			System.out.println("  Firm "+identity+" sum of productivity "+sumOfWorkersProductivity+ " production "+production);
		}
}
	
	//CALCOLARE WAGE OFFER FIRM
	//CASO 1: PERFECT FORESIGHT
	
//public void wageOffer(){
//		senderFirmReservationWage=wageBase*aConsumer.getProductivity();
//	}
	
	
	//CASO 2: ASYMMETRIC INFO: WORKERS' AVERAGE ABILITY
 public void wageOffer(){
	 	sumAbility0= (int) aConsumer.getDegree(degree=0);
		sumAbility1=(int) aConsumer.getDegree(degree=1);
		sumAbility2=(int) aConsumer.getDegree(degree=2);
		sumAbility3=(int) aConsumer.getDegree(degree=3);
		sumAbility4=(int) aConsumer.getDegree(degree=4);
		sumAbility5=(int) aConsumer.getDegree(degree=5);
		sumAbility6=(int) aConsumer.getDegree(degree=6);
		
	for (int w=0; w < workersList.size(); w++){
		switch (degree){
		case 1: degree = 0;
		avgAbility0=sumAbility0/workersList.size();
		senderFirmReservationWage_0=wageBase*avgAbility0;
		break;
		
		case 2: degree = 1;
		avgAbility1=sumAbility1/workersList.size();
		senderFirmReservationWage_1=wageBase*avgAbility1;
		break;
		
		case 3: degree = 2;
		avgAbility2=sumAbility2/workersList.size();
		senderFirmReservationWage_2=wageBase*avgAbility2;
		break;
		
		case 4: degree = 3;
		avgAbility3=sumAbility3/workersList.size();
		senderFirmReservationWage_3=wageBase*avgAbility3;
		break;
		
		case 5: degree = 4;
		avgAbility4=sumAbility4/workersList.size();
		senderFirmReservationWage_4=wageBase*avgAbility4;
		break;
		
		case 6: degree = 5;
		avgAbility5=sumAbility5/workersList.size();
		senderFirmReservationWage_5=wageBase*avgAbility5;
		break;
		
		case 7: degree = 6;
		avgAbility6=sumAbility6/workersList.size();
		senderFirmReservationWage_6=wageBase*avgAbility6;
		break;
		}
	}
 }
	
	// SEND LABOR DEMAND TO LABOR MARKET
	 
	public void sendLaborDemand(){
		myOffer = new LaborOffer(identity,senderFirmReservationWage);
		try{
			myLaborMarket=(LaborMarket)(myContext.getObjects(Class.forName("sfcabm.LaborMarket"))).get(0);
		}
		catch(ClassNotFoundException e){
			System.out.println("Class not found");
		}
	
	myLaborMarket.receiveLaborDemand(myOffer);
	}

	//questo viene dopo
	
	
	
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
	
	public int getProductAbsoluteRank(){
		return productAbsoluteRank;
	}
	
	public long getProduction(){
		return production;
	}
}
