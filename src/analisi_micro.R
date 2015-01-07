dati<-read.csv(file="zdata_micro_consumers_run_1.csv", header = TRUE, sep = ";",as.is=T)
datif<-read.csv(file="zdata_micro_firms_run_1.csv", header = TRUE, sep = ";",as.is=T)
ba1<-read.csv(file="zdata_micro_consumersbankaccounts01_run_1.csv", header = F, sep = "|",as.is=T)
ba2<-read.csv(file="zdata_micro_consumersbankaccounts02_run_1.csv", header = F, sep = "|",as.is=T)
ba3<-read.csv(file="zdata_micro_consumersbankaccounts03_run_1.csv", header = F, sep = "|",as.is=T)
ba4<-read.csv(file="zdata_micro_consumersbankaccounts04_run_1.csv", header = F, sep = "|",as.is=T)
ba5<-read.csv(file="zdata_micro_consumersbankaccounts05_run_1.csv", header = F, sep = "|",as.is=T)
ba6<-read.csv(file="zdata_micro_consumersbankaccounts06_run_1.csv", header = F, sep = "|",as.is=T)

baf1<-read.csv(file="zdata_micro_firmsbankaccounts01_run_1.csv", header = F, sep = "|",as.is=T)
baf2<-read.csv(file="zdata_micro_firmsbankaccounts02_run_1.csv", header = F, sep = "|",as.is=T)
baf3<-read.csv(file="zdata_micro_firmsbankaccounts03_run_1.csv", header = F, sep = "|",as.is=T)
baf4<-read.csv(file="zdata_micro_firmsbankaccounts04_run_1.csv", header = F, sep = "|",as.is=T)
baf5<-read.csv(file="zdata_micro_firmsbankaccounts05_run_1.csv", header = F, sep = "|",as.is=T)
baf6<-read.csv(file="zdata_micro_firmsbankaccounts06_run_1.csv", header = F, sep = "|",as.is=T)


consumersFlag=T
firmsFlag=F

#startp<-2
#endp<-5
endp<-startp

if(consumersFlag){
print(dati[startp:endp,])
print("begin of payback")
print(matrix(as.numeric(unlist(strsplit(ba1[startp:endp,1],","))),ncol=7,byrow=T))
print("end of payback")
print(matrix(as.numeric(unlist(strsplit(ba2[startp:endp,1],","))),ncol=7,byrow=T))
print("begin stepDesiredConsumption")
print(matrix(as.numeric(unlist(strsplit(ba3[startp:endp,1],","))),ncol=7,byrow=T))
print("end stepDesiredConsumption")
print(matrix(as.numeric(unlist(strsplit(ba4[startp:endp,1],","))),ncol=7,byrow=T))
print("begin adjustConsumptionAccordingTo extendedCredit")
print(matrix(as.numeric(unlist(strsplit(ba5[startp:endp,1],","))),ncol=7,byrow=T))
print("end of updateBankAccountAccordingToEffectiveConsumption")
print(matrix(as.numeric(unlist(strsplit(ba6[startp:endp,1],","))),ncol=7,byrow=T))
}

if(firmsFlag){
print(datif[startp:endp,])
print("begin of payback")
print(matrix(as.numeric(unlist(strsplit(baf1[startp:endp,1],","))),ncol=7,byrow=T))
print("end of payback")
print(matrix(as.numeric(unlist(strsplit(baf2[startp:endp,1],","))),ncol=7,byrow=T))
print("begin stepDesiredCredit")
print(matrix(as.numeric(unlist(strsplit(baf3[startp:endp,1],","))),ncol=7,byrow=T))
print("end stepDesiredCredit")
print(matrix(as.numeric(unlist(strsplit(baf4[startp:endp,1],","))),ncol=7,byrow=T))
print("begin adjustProductionCapitalAndBankAccount")
print(matrix(as.numeric(unlist(strsplit(baf5[startp:endp,1],","))),ncol=7,byrow=T))
print("end of adjustProductionCapitalAndBankAccount")
print(matrix(as.numeric(unlist(strsplit(baf6[startp:endp,1],","))),ncol=7,byrow=T))
}
