system("ls agents_and_interactions_second*.mp > second_part_files.txt")
firstpart<-"agents_and_interactions_first.mp"
files<-scan("second_part_files.txt",what="character")

for(i in length(files):length(files)){
	filenamenoext<-paste("agents_and_interactions_figure",i,sep="")
	filename<-paste("agents_and_interactions_figure",i,".mp",sep="")
	system(paste("cat agents_and_interactions_first.mp",files[i],">",filename))
	system(paste("mptopdf",filename))
	system(paste("mv ",filenamenoext,"-0.pdf ",filenamenoext,".pdf",sep=""))
}
