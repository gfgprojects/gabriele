input latexmp
setupLaTeXMP(preamblefile="textpathfigs");
input textpath
input rboxes



prologues:=3;

verbatimtex
%&latex 
\documentclass[10pt]{article}
\usepackage{color}
\begin{document}
etex

beginfig(0);

scalefactor:=1.0;

picture ah;

fill(0,1mm)--(-1mm,2mm)--(0,-2mm)--(1mm,2mm)--cycle;

ah=currentpicture;
currentpicture:=nullpicture;

%label(btex \begin{tabular}{l} The\\Agent-based\\Stock-flow Consistent\\Macroeconomic \\Model \end{tabular} etex scaled 2, (0,0));

path c[],l[],pp;

c1 = fullcircle scaled 10cm;
c2 = fullcircle scaled 8.5cm;
c3 = fullcircle scaled 8.0cm;
c5 = fullcircle scaled 7.0cm;
c6 = fullcircle scaled 6.5cm;
c4=(0,0)--(15cm,0);
fill buildcycle(subpath(4,10) of c1,c4 rotated -75,subpath(4,10) of c3,c4 rotated 75) withcolor red;
fill buildcycle(subpath(0,7.9) of c1,c4 rotated -75,subpath(0,7.9) of c3,c4 rotated 75) withcolor 0.7[blue,white];


draw c1;
draw c3;
draw c6;

for i=0 step length(c1)/8 until length(c1):
draw ah rotated (i*45-3) shifted (5cm*dir (i*45-3));
%    draw point i of c1 withpen pencircle scaled 4bp;
  endfor;

draw textpathFont("\usefont{T1}{bch}{m}{n}\huge","consumption",reverse(c2),1.1);
draw textpathFont("\usefont{T1}{bch}{m}{n}\huge","Production",reverse(c2),1.65);
draw textpathFont("\usefont{T1}{bch}{m}{n}\large","Labor mkt",reverse(c5),1.7);
draw textpathFont("\usefont{T1}{bch}{m}{n}\large","Production capital and Credit mkts",reverse(c5),1.72);
draw textpathFont("\usefont{T1}{bch}{m}{n}\large","Goods and Credit mkts",reverse(c5),2.35);

draw c4 rotated 175 intersectionpoint c3--c4 rotated 175 intersectionpoint c6;
draw c4 rotated 105 intersectionpoint c3--c4 rotated 105 intersectionpoint c6;
draw c4 rotated -75 intersectionpoint c1--c4 rotated -75 intersectionpoint c6;
draw c4 rotated 75 intersectionpoint c1--c4 rotated 75 intersectionpoint c6;


l1 = (1cm*dir 45)--(6cm*dir 45);
l2 = (1cm*dir 80)--(6cm*dir 80);

%draw l1;
%draw l2;

pp = buildcycle(l1,c1,l2,c2);
%fill pp withcolor blue;%0.7white;

picture lab;
lab=thelabel(btex demand side etex,origin) rotated -45 shifted (4.0cm*dir 45);
%unfill bbox lab;

fill fullcircle scaled 3mm shifted (0cm,13.25cm);

draw fullcircle scaled 4mm shifted (0cm,13.25cm);

picture tl;

tl=thelabel(btex initialization etex scaled scalefactor,(0cm,12cm));

draw (0cm,13cm)--(0cm,5.2cm){dir -90}..{dir 0}(2mm,5cm);

unfill bbox tl;
draw tl;
draw bbox tl;


draw thelabel.lft(btex \color{blue}F\color{black}: make production \hskip3mm 27 \hskip3mm $\triangleright$ etex scaled scalefactor,origin) rotated 276.428571428571 shifted (5cm*dir 96.4285714285714);

draw thelabel.lft(btex OFS: substitute retired consumers \hskip3mm 26 \hskip3mm $\triangleright$ etex scaled scalefactor,origin) rotated 289.285714285714 shifted (5cm*dir 109.285714285714);

draw thelabel.lft(btex OFS: compute and allocate investments \hskip3mm 25 \hskip3mm $\triangleright$ etex scaled scalefactor,origin) rotated 302.142857142857 shifted (5cm*dir 122.142857142857);

draw thelabel.lft(btex \color{blue}F\color{black}: set production capital and bank account \hskip3mm 24 \hskip3mm $\triangleright$ etex scaled scalefactor,origin) rotated 315 shifted (5cm*dir 135);

draw thelabel.lft(btex LM: match vacancies and unemployed \hskip3mm 23 \hskip3mm $\triangleright$ etex scaled scalefactor,origin) rotated 327.857142857143 shifted (5cm*dir 147.857142857143);

draw thelabel.lft(btex \color{blue}F\color{black}: hire \hskip3mm 22 \hskip3mm $\triangleright$ etex scaled scalefactor,origin) rotated 340.714285714286 shifted (5cm*dir 160.714285714286);

draw thelabel.lft(btex C$_U$: send curricula \hskip3mm 21 \hskip3mm $\triangleright$ etex scaled scalefactor,origin) rotated 353.571428571429 shifted (5cm*dir 173.571428571429);

draw thelabel.lft(btex \color{blue}F\color{black}: retire and fire workers \hskip3mm 20 \hskip3mm $\triangleright$ etex scaled scalefactor,origin) rotated 366.428571428571 shifted (5cm*dir 186.428571428571);

draw thelabel.lft(btex OFS: perform firm entry \hskip3mm 19 \hskip3mm $\triangleright$ etex scaled scalefactor,origin) rotated 379.285714285714 shifted (5cm*dir 199.285714285714);

draw thelabel.lft(btex \color{blue}F\color{black}: set possible investment  \hskip3mm 18 \hskip3mm $\triangleright$ etex scaled scalefactor,origin) rotated 392.142857142857 shifted (5cm*dir 212.142857142857);

draw thelabel.lft(btex B$\rightarrow$\color{blue}F\color{black}: set allowed credit \hskip3mm 17 \hskip3mm $\triangleright$ etex scaled scalefactor,origin) rotated 405 shifted (5cm*dir 225);

draw thelabel.lft(btex \color{blue}F\color{black}$\rightarrow$B: set desired investments (ask for credit if needed) \hskip3mm 16 \hskip3mm $\triangleright$ etex scaled scalefactor,origin) rotated 417.857142857143 shifted (5cm*dir 237.857142857143);

draw thelabel.lft(btex \color{blue}F\color{black}$\rightarrow$B: pay back if possible \hskip3mm 15 \hskip3mm $\triangleright$ etex scaled scalefactor,origin) rotated 430.714285714286 shifted (5cm*dir 250.714285714286);

draw thelabel.lft(btex B$\rightarrow$\color{blue}F\color{black}: pay interest and ask refunding \hskip3mm 14 \hskip3mm $\triangleright$ etex scaled scalefactor,origin) rotated 443.571428571429 shifted (5cm*dir 263.571428571429);

draw thelabel.rt(btex $\triangleleft$ \hskip3mm 13 \hskip3mm OFS: perform firms exit etex scaled scalefactor,origin) rotated 276.428571428571 shifted (5cm*dir 276.428571428571);

draw thelabel.rt(btex $\triangleleft$ \hskip3mm 12 \hskip3mm \color{blue}F\color{black}: compute economic result and capital depreciation etex scaled scalefactor,origin) rotated 289.285714285714 shifted (5cm*dir 289.285714285714);

draw thelabel.rt(btex $\triangleleft$ \hskip3mm 11 \hskip3mm \color{red}C\color{black}$\rightarrow$B: compute saving and deposit them etex scaled scalefactor,origin) rotated 302.142857142857 shifted (5cm*dir 302.142857142857);

draw thelabel.rt(btex $\triangleleft$ \hskip3mm 10 \hskip3mm \color{red}C\color{black}: resize consumption if unsatisfied demand persists etex scaled scalefactor,origin) rotated 315 shifted (5cm*dir 315);

draw thelabel.rt(btex $\triangleleft$ \hskip3mm 9 \hskip3mm OFS: satisfy unsatified demand if possible etex scaled scalefactor,origin) rotated 327.857142857143 shifted (5cm*dir 327.857142857143);

draw thelabel.rt(btex $\triangleleft$ \hskip3mm 8 \hskip3mm OFS: compute and allocate desired demand etex scaled scalefactor,origin) rotated 340.714285714286 shifted (5cm*dir 340.714285714286);

draw thelabel.rt(btex $\triangleleft$ \hskip3mm 7 \hskip3mm \color{red}C\color{black}: resize consumption if demanded credit is not allowed etex scaled scalefactor,origin) rotated 353.571428571429 shifted (5cm*dir 353.571428571429);

draw thelabel.rt(btex $\triangleleft$ \hskip3mm 6 \hskip3mm B$\rightarrow$\color{red}C\color{black}: set allowed credit etex scaled scalefactor,origin) rotated 6.42857142857144 shifted (5cm*dir 6.42857142857144);

draw thelabel.rt(btex $\triangleleft$ \hskip3mm 5 \hskip3mm \color{red}C\color{black}$\rightarrow$B: set desired consumption (ask for credit if needed) etex scaled scalefactor,origin) rotated 19.2857142857143 shifted (5cm*dir 19.2857142857143);

draw thelabel.rt(btex $\triangleleft$ \hskip3mm 4 \hskip3mm \color{red}C\color{black}$_S$: step state etex scaled scalefactor,origin) rotated 32.1428571428571 shifted (5cm*dir 32.1428571428571);

draw thelabel.rt(btex $\triangleleft$ \hskip3mm 3 \hskip3mm \color{red}C\color{black}$\rightarrow$B: pay back if possible etex scaled scalefactor,origin) rotated 45 shifted (5cm*dir 45);

draw thelabel.rt(btex $\triangleleft$ \hskip3mm 2 \hskip3mm B$\rightarrow$\color{red}C\color{black}: pay interest and ask refunding etex scaled scalefactor,origin) rotated 57.8571428571428 shifted (5cm*dir 57.8571428571428);

draw thelabel.rt(btex $\triangleleft$ \hskip3mm 1 \hskip3mm F$\rightarrow$\color{red}C\color{black}: pay wages etex scaled scalefactor,origin) rotated 70.7142857142857 shifted (5cm*dir 70.7142857142857);

draw thelabel.rt(btex $\triangleleft$ \hskip3mm 0 \hskip3mm OFS: compute variables etex scaled scalefactor,origin) rotated 83.5714285714286 shifted (5cm*dir 83.5714285714286);

endfig;
end;
