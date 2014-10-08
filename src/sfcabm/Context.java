package sfcabm;

import repast.simphony.dataLoader.ContextBuilder;
//import repast.simphony.engine.environment.RunEnvironment;
//import repast.simphony.random.RandomHelper;
//import sfcabm.LaborMkt;
import sfcabm.Consumer;
//import sfcabm.Firm;

public class Context implements ContextBuilder<Object> {
	public static boolean verbousFlag=true;
		private static final int NumConsumers = 5;
		private static final int NumFirm = 3;
		
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
		
		
	
		//public static boolean verbouseFlag=true;
		public repast.simphony.context.Context<Object> build(
				repast.simphony.context.Context<Object> context) {
	
			//int maxIter=30;
			
			for (int i = 0; i< NumConsumers; i++){
				context.add(new Consumer(i));
			}
				
				
			for (int f = 0; f<NumFirm; f++){
				context.add(new Firm(f));
			}
				
			//LaborMkt theLaborMkt=new LaborMkt();
			//context.add(theLaborMkt);
			
			
			
			/* if (RunEnvironment.getInstance().isBatch())
		        {
		            RunEnvironment.getInstance().endAt(10);
		        }
		        
			*/
			return context;
			
			
		}

		

	}
