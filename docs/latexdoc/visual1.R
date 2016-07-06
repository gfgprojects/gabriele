#this file creates a pdf with the visual representation of the model (visual.pdf).
#it takes as input the file visual.txt that contains the first part of the metapost code
#This R code creates a metapost file, compiles it and transaforms the resulting eps in a pdf.
#R, metapost or context, and epstopdf must be installed in your computer.


steps<-readLines("items_in_main_loop.txt")

items<-length(steps)
delta<-360/items

start<-90-delta/2

radius_in_cm<-5
filename<-"visual1"
system(paste("cp ",filename,".txt ",filename,".mp",sep=""))


for(i in 1:(items)){
direction<-(start+i*delta)%%360
#cat("draw ah rotated ",direction," shifted (",radius_in_cm,"cm*dir ",direction,");\n",sep="")
if(direction>90 && direction<270){
write(paste("draw thelabel.lft(btex ",paste(steps[i],"\\hskip3mm",items-i,"\\hskip3mm")," $\\triangleright$ etex scaled scalefactor,origin) rotated ",(direction+180)," shifted (",radius_in_cm,"cm*dir ",direction,");\n",sep=""),file=paste(filename,".mp",sep=""),append=T)
}else{
write(paste("draw thelabel.rt(btex $\\triangleleft$ ",paste("\\hskip3mm",items-i,"\\hskip3mm",steps[i])," etex scaled scalefactor,origin) rotated ",direction," shifted (",radius_in_cm,"cm*dir ",direction,");\n",sep=""),file=paste(filename,".mp",sep=""),append=T)
}
}

write("endfig;",file=paste(filename,".mp",sep=""),append=T)
write("end;",file=paste(filename,".mp",sep=""),append=T)

system(paste("mpost ",filename,".mp",sep=""))
system(paste("epstopdf ",filename,".0",sep=""))
