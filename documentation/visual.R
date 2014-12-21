#this file creates a pdf with the visual representation of the model (visual.pdf).
#it takes as input the file visual.txt that contains the first part of the metapost code
#This R code creates a metapost file, compiles it and transaforms the resulting eps in a pdf.
#R, metapost or context, and epstopdf must be installed in your computer.


steps<-c("F: make production",
	"OFS: substitute retired consumers",
	"LM: match vacancies and unemployed",
	"F: hire",
	"C$_U$: send curricula",
	"F: retire and fire workers",
	"OFS: perform firm entry, compute and allocate investments ",
	"F: set production capital according to allowed credit",
	"B$\\rightarrow$F: set allowed credit",
	"F$\\rightarrow$B: set desired investments (ask for credit if needed)",
	"F$\\rightarrow$B: pay back if possible",
	"B$\\rightarrow$F: pay interest and ask refunding",
	"OFS: perform firms exit",
	"F: compute economic result and capital depreciation",
	"C$\\rightarrow$B: compute saving and deposit them",
	"C: resize consumption if unsatisfied demand persists",
	"OFS: satisfy unsatified demand if possible",
	"OFS: compute and allocate desired demand",
	"C: resize consumption if demanded credit is not allowed",
	"B$\\rightarrow$C: set allowed credit",
	"C$\\rightarrow$B: set desired consumption (ask for credit if needed)",
	"C$_S$: step state",
	"C$\\rightarrow$B: pay back if possible",
	"B$\\rightarrow$C: pay interest and ask refunding",
	"F$\\rightarrow$C: pay wages",
	"OFS: compute variables")

items<-length(steps)
delta<-360/items

start<-90-delta/2

radius_in_cm<-5

system("cp visual1.txt visual.mp")


for(i in 1:(items)){
direction<-(start+i*delta)%%360
#cat("draw ah rotated ",direction," shifted (",radius_in_cm,"cm*dir ",direction,");\n",sep="")
if(direction>90 && direction<270){
write(paste("draw thelabel.lft(btex ",steps[i]," $\\triangleright$ etex,origin) rotated ",(direction+180)," shifted (",radius_in_cm,"cm*dir ",direction,");\n",sep=""),file="visual.mp",append=T)
}else{
write(paste("draw thelabel.rt(btex $\\triangleleft$ ",steps[i]," etex,origin) rotated ",direction," shifted (",radius_in_cm,"cm*dir ",direction,");\n",sep=""),file="visual.mp",append=T)
}
}

write("endfig;",file="visual.mp",append=T)
write("end;",file="visual.mp",append=T)

system("mpost visual.mp")
system("epstopdf visual.0")
