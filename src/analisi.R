dati1<-read.csv(file="zdata_macro_run_1.csv", header = TRUE, sep = ";")
plot(dati1$AS,type="o",xlab="time",ylab="Aggregate consumption (red) & production (blue)",col="blue")
lines(dati1$AC,col="red")
points(dati1$AC,col="red")

