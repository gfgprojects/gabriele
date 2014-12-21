dati<-read.csv(file="zdata_macro_run_1.csv", header = TRUE, sep = ";")
dati1<-dati[20:520,]

par(mfrow=c(2,2))

plot(dati1$AC,type="l",xlab="time",ylab="GDP")
#plot(dati1$AS,type="o",xlab="time",ylab="Aggregate consumption (red) & production (blue)",col="blue")
#lines(dati1$AC,col="red")
#points(dati1$AC,col="red")

plot(dati1$LHd,type="l",xlab="time",ylab="consumer credit asked (black) allowed (red)")
lines(dati1$LHs,col="red")

unemployed<-dati1$NC-dati1$NS-dati1$NW

max_graph<-max(c(dati1$NW,unemployed,dati1$NS))
min_graph<-min(c(dati1$NW,unemployed,dati1$NS))
plot(dati1$NW,type="l",xlab="time",ylab="#W (black) #S (blue)",ylim=c(min_graph,max_graph))
#lines(unemployed,col="red")
lines(dati1$NS,col="blue")

#plot(dati1$NFE,type="l",xlab="time",ylab="# of bailouts")

#max_graph<-max(c(dati1$L,dati1$D))
#min_graph<-min(c(dati1$L,dati1$D))
#plot(dati1$L,type="o",ylim=c(min_graph,max_graph),xlab="time",ylab="deposits (red) & loans (blue)",col="blue")
#lines(dati1$D,col="red")
#points(dati1$D,col="red")

unemployed<-dati1$NC-dati1$NS-dati1$NW

dole=c(numeric(length=100)+10,numeric(length=100)+10);
plot(unemployed*dole,type="l",xlab="time",ylab="public exprenditure",col="red")

#plot(dati1$NS,type="l",xlab="time",ylab="# of students",col="red")
#plot(dati1$NFE,type="l",xlab="time",ylab="# of bailouts",col="red")


