package sfcabm;

import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.util.collections.IndexedIterable;
//import repast.simphony.engine.environment.RunEnvironment;
//import repast.simphony.random.RandomHelper;
//import sfcabm.LaborMkt;
import sfcabm.Consumer;
import sfcabm.Firm;
import sfcabm.LaborMarket;
import sfcabm.OfficeForStatistics;

public class Context implements ContextBuilder<Object> {
	public static boolean verbousFlag=true;
		public static int NumConsumers = 10;
		public static int NumFirms = 3;
		public static int consumerExitAge=50;
		public static int parameterOfProductivityInProductionFuncion=100;
		/*
		double initialProbabilityToBeEmployed=0.7;
		VARIABILI USATE IN LAB MKT
		double reservationWageWorker;
		double workerWage;
		int numApplic;
		EMPLOYMENT STATUS: DECIDERE SE TENERE O RIMUOVERE IN BASE A LAB MKT
		int status;
		 */
		
		
		 //propensioni rivedere calibration
		public static double alpha = 0.4;
		public static double beta = 0.4;
		//taxes on consumption, tax rate evolves??...T = \phi c
		public static double taxRate = 0.1;


		Consumer aConsumer;
		Firm aFirm;

		IndexedIterable consumersList,firmsList;
		OfficeForStatistics officeForStatistics;

	
		//public static boolean verbouseFlag=true;
		public repast.simphony.context.Context<Object> build(
				repast.simphony.context.Context<Object> context) {
	
			//int maxIter=30;
			
			for (int i = 0; i< NumConsumers; i++){
				aConsumer=new Consumer(i,context);
				aConsumer.initialize();
				context.add(aConsumer);
			}
				
				
			for (int f = 0; f<NumFirms; f++){
				context.add(new Firm(f,context));
			}
				
			LaborMarket theLaborMarket=new LaborMarket();
			context.add(theLaborMarket);
			
			try{
				consumersList=context.getObjects(Class.forName("sfcabm.Consumer"));
				firmsList=context.getObjects(Class.forName("sfcabm.Firm"));
			}
			catch(ClassNotFoundException e){
				System.out.println("Class not found");
			}
			for(int i=0;i<consumersList.size();i++){
				aConsumer=(Consumer)consumersList.get(i);
				aConsumer.sendInitialJobApplication();
			}
			for(int i=0;i<firmsList.size();i++){
				aFirm=(Firm)firmsList.get(i);
				aFirm.setInitialWorkers();
			}

			officeForStatistics=new OfficeForStatistics(context);
			officeForStatistics.computeVariables();
			officeForStatistics.publishIndustriesStats();

			
			
			/* if (RunEnvironment.getInstance().isBatch())
		        {
		            RunEnvironment.getInstance().endAt(10);
		        }
		        
			*/
			return context;
			
			
		}

		

	}
