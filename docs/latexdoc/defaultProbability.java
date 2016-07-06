
package firstAbm;


package com.aliasi.stats;

import repast.simphony.context.Context;
import firstAbm.Bank;
import firstAbm.Consumer;


import java.io.FileWriter;
import java.io.IOException;

import com.aliasi.matrix.DenseVector;
import com.aliasi.matrix.Vector;

import com.aliasi.stats.AnnealingSchedule;
import com.aliasi.stats.LogisticRegression;
import com.aliasi.stats.RegressionPrior;

public class defaultProbability {
    
    public Vector[] INPUTS = Vector[] {
    x[0] = 1;
    x[1] = public double Consumer.wealth;
    //leverage si deve aggiungere e calcolare in Consumer class
    x[2] = public double Consumer.leverage;
    x[3] = public boolean Consumer.employed;
}

    public static void main(String[] args) {
        System.out.println("Computing Household Default Probability Logistic Regression");


//estimate method
        LogisticRegression regression
            = LogisticRegression.estimate(INPUTS,
                                            //Default = output della regressione
                                          Default,
                                          RegressionPrior.noninformative(),
                                          AnnealingSchedule.inverse(.05,100),
                                          null, // reporter with no feedback
                                          0.000000001, // min improve
                                          1, // min epochs
                                          5000); // max epochs
        
        Vector[] betas = regression.weightVectors();
        for (int outcome = 0; outcome < betas.length; ++outcome) {
            System.out.print("Outcome=" + outcome);
            for (int i = 0; i < betas[outcome].numDimensions(); ++i)
                System.out.printf(" %6.2f",betas[outcome].value(i));
            System.out.println();
        }

	System.out.println("\nInput Vector         Outcome Conditional Probabilities");
        for (Vector testCase : TEST_INPUTS) {
            double[] conditionalProbs = regression.classify(testCase);
            for (int i = 0; i < testCase.numDimensions(); ++i) {
                System.out.printf("%3.1f ",testCase.value(i));
            }
            for (int k = 0; k < conditionalProbs.length; ++k) {
                System.out.printf(" p(%d|input)=%4.2f ",k,conditionalProbs[k]);
            }
            System.out.println();
        }

    }
}
