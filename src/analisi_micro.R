dati<-read.csv(file="zdata_micro_consumers_run_1.csv", header = TRUE, sep = ";",as.is=T)
ba1<-read.csv(file="zdata_micro_consumersbankaccounts01_run_1.csv", header = F, sep = "|",as.is=T)
ba2<-read.csv(file="zdata_micro_consumersbankaccounts02_run_1.csv", header = F, sep = "|",as.is=T)
ba3<-read.csv(file="zdata_micro_consumersbankaccounts03_run_1.csv", header = F, sep = "|",as.is=T)
ba4<-read.csv(file="zdata_micro_consumersbankaccounts04_run_1.csv", header = F, sep = "|",as.is=T)
ba5<-read.csv(file="zdata_micro_consumersbankaccounts05_run_1.csv", header = F, sep = "|",as.is=T)
ba6<-read.csv(file="zdata_micro_consumersbankaccounts06_run_1.csv", header = F, sep = "|",as.is=T)


startp<-21
endp<-21
print(dati[startp:endp,])
print("begin of payback")
print(matrix(as.numeric(unlist(strsplit(ba1[startp:endp,1],","))),ncol=7,byrow=T))
print("end of payback")
print(matrix(as.numeric(unlist(strsplit(ba2[startp:endp,1],","))),ncol=7,byrow=T))
print("begin stepConsumption")
print(matrix(as.numeric(unlist(strsplit(ba3[startp:endp,1],","))),ncol=7,byrow=T))
print("end stepConsumption")
print(matrix(as.numeric(unlist(strsplit(ba4[startp:endp,1],","))),ncol=7,byrow=T))
print("begin adjustConsumptionAccordingTo extendedCredit")
print(matrix(as.numeric(unlist(strsplit(ba5[startp:endp,1],","))),ncol=7,byrow=T))
print("end of update bank account")
print(matrix(as.numeric(unlist(strsplit(ba6[startp:endp,1],","))),ncol=7,byrow=T))
