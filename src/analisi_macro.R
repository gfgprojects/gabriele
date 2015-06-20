dati<-read.csv(file="zdata_macro_run_1.csv", header = TRUE, sep = ";")
#dati1<-dati[20:520,]
dati1<-dati

par(mfrow=c(3,3))

I<-c(0,dati1$AOI[-dim(dati1)[1]])
C<-dati1$AC

plot(C+I,type="l",xlab="time",main="aggregate demand\nC (red) C+I (black)",cex.main=0.8,ylab="")
#plot(dati1$AS,type="o",xlab="time",ylab="Aggregate consumption (red) & production (blue)",col="blue")
lines(C,col="red")
#points(dati1$AC,col="red")

plot(dati1$LHd,type="l",xlab="time",main="Consumers credit\nasked (black), allowed (red)",cex.main=0.8,ylab="")
lines(dati1$LHs,col="red")

unemployed<-dati1$NC-dati1$NS-dati1$NW
u<-(dati1$NC-dati1$NS-dati1$NW)/(dati1$NC-dati1$NS)

max_graph<-max(c(dati1$NW,unemployed,dati1$NS))
min_graph<-min(c(dati1$NW,unemployed,dati1$NS))
plot(dati1$NW,type="l",xlab="time",main="workers (black), students (blue)\nunemployed (red)",cex.main=0.8,ylab="",ylim=c(min_graph,max_graph))
lines(unemployed,col="red")
lines(dati1$NS,col="blue")

plot(u,type="l",main="unemployment rate",cex.main=0.8,ylab="")

plot(dati1$NR,type="l",main="retired workers",cex.main=0.8,ylab="")

#plot(dati1$NFE,type="l",xlab="time",ylab="# of bailouts")

#max_graph<-max(c(dati1$L,dati1$D))
#min_graph<-min(c(dati1$L,dati1$D))
#plot(dati1$L,type="o",ylim=c(min_graph,max_graph),xlab="time",ylab="deposits (red) & loans (blue)",col="blue")
#lines(dati1$D,col="red")
#points(dati1$D,col="red")

unemployed<-dati1$NC-dati1$NS-dati1$NW

#dole=c(numeric(length=100)+10,numeric(length=100)+10);
#plot(unemployed*dole,type="l",xlab="time",ylab="public exprenditure",col="red")

#plot(dati1$NS,type="l",xlab="time",ylab="# of students",col="red")
#plot(dati1$NFE,type="l",xlab="time",ylab="# of bailouts",col="red")

plot(dati1$L,type="l",main="Loans\ntotal",cex.main=0.8,ylab="")

plot(dati1$D,type="l",main="Deposits\ntotal",cex.main=0.8,ylab="")

