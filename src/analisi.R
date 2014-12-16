dati1<-read.csv(file="zdata_macro_run_1.csv", header = TRUE, sep = ";")

par(mfrow=c(2,2))

plot(dati1$AS,type="o",xlab="time",ylab="Aggregate consumption (red) & production (blue)",col="blue")
lines(dati1$AC,col="red")
points(dati1$AC,col="red")


max_graph<-max(c(dati1$L,dati1$D))
min_graph<-min(c(dati1$L,dati1$D))
plot(dati1$L,type="o",ylim=c(min_graph,max_graph),xlab="time",ylab="deposits (red) & loans (blue)",col="blue")
lines(dati1$D,col="red")
points(dati1$D,col="red")

plot(dati1$NS,type="l",,xlab="time",ylab="# of students",col="red")
#plot(dati1$AC,col="red")
plot(dati1$NW,type="l",,xlab="time",ylab="# of workers",col="red")

