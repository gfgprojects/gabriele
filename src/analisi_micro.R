datic<-read.csv(file="zdata_micro_consumers_run_1.csv", header = TRUE, sep = ";",as.is=T)
datif<-read.csv(file="zdata_micro_firms_run_1.csv", header = TRUE, sep = ";",as.is=T)

bac1<-read.csv(file="zdata_micro_consumersbankaccounts01_run_1.csv", header = F, sep = "|",as.is=T)
bac1m<-matrix(as.numeric(unlist(strsplit(bac1[,1],","))),ncol=7,byrow=T)
bac2<-read.csv(file="zdata_micro_consumersbankaccounts02_run_1.csv", header = F, sep = "|",as.is=T)
bac3<-read.csv(file="zdata_micro_consumersbankaccounts03_run_1.csv", header = F, sep = "|",as.is=T)
bac4<-read.csv(file="zdata_micro_consumersbankaccounts04_run_1.csv", header = F, sep = "|",as.is=T)
bac5<-read.csv(file="zdata_micro_consumersbankaccounts05_run_1.csv", header = F, sep = "|",as.is=T)
bac6<-read.csv(file="zdata_micro_consumersbankaccounts06_run_1.csv", header = F, sep = "|",as.is=T)
bac6m<-matrix(as.numeric(unlist(strsplit(bac6[,1],","))),ncol=7,byrow=T)

baf1<-read.csv(file="zdata_micro_firmsbankaccounts01_run_1.csv", header = F, sep = "|",as.is=T)
baf1m<-matrix(as.numeric(unlist(strsplit(baf1[,1],","))),ncol=7,byrow=T)
baf2<-read.csv(file="zdata_micro_firmsbankaccounts02_run_1.csv", header = F, sep = "|",as.is=T)
baf3<-read.csv(file="zdata_micro_firmsbankaccounts03_run_1.csv", header = F, sep = "|",as.is=T)
baf4<-read.csv(file="zdata_micro_firmsbankaccounts04_run_1.csv", header = F, sep = "|",as.is=T)
baf5<-read.csv(file="zdata_micro_firmsbankaccounts05_run_1.csv", header = F, sep = "|",as.is=T)
baf6<-read.csv(file="zdata_micro_firmsbankaccounts06_run_1.csv", header = F, sep = "|",as.is=T)
baf6m<-matrix(as.numeric(unlist(strsplit(baf6[,1],","))),ncol=7,byrow=T)

ba6m<-rbind(bac6m,baf6m)

#periodo<-3
for(periodo in 1:10){
selba6m<-ba6m[which(ba6m[,1]==periodo),]
borrowers<-which(selba6m[,4]<=0)
depositants<-which(selba6m[,4]>0)

#print(selba6m)
#print(paste("periodo",periodo,"prestiti",sum(selba6m[borrowers,4]),"depositi",sum(selba6m[depositants,4])))
}

consumersFlag=T
firmsFlag=F

#startp<-2
#endp<-5
endp<-startp

if(consumersFlag){
print(datic[startp:endp,])
print("begin of payback")
print(matrix(as.numeric(unlist(strsplit(bac1[startp:endp,1],","))),ncol=7,byrow=T))
print("end of payback")
print(matrix(as.numeric(unlist(strsplit(bac2[startp:endp,1],","))),ncol=7,byrow=T))
print("begin stepDesiredConsumption")
print(matrix(as.numeric(unlist(strsplit(bac3[startp:endp,1],","))),ncol=7,byrow=T))
print("end stepDesiredConsumption")
print(matrix(as.numeric(unlist(strsplit(bac4[startp:endp,1],","))),ncol=7,byrow=T))
print("begin adjustConsumptionAccordingTo extendedCredit")
print(matrix(as.numeric(unlist(strsplit(bac5[startp:endp,1],","))),ncol=7,byrow=T))
print("end of updateBankAccountAccordingToEffectiveConsumption")
print(matrix(as.numeric(unlist(strsplit(bac6[startp:endp,1],","))),ncol=7,byrow=T))
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
