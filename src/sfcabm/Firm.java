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
			System.out.println("NO WORKERS");
		}
		production=Math.round(sumOfWorkersProductivity*Context.parameterOfProductivityInProductionFuncion);
	//	productAbsoluteRank=1;
		productAbsoluteRank=RandomHelper.nextIntFromTo(1,10);
		if(Context.verbousFlag){
			System.out.println("  Firm "+identity+" sum of productivity "+sumOfWorkersProductivity+ " production "+production);
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
	
	public int getProductAbsoluteRank(){
		return productAbsoluteRank;
	}
	
	public long getProduction(){
		return production;
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
