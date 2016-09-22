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
fill buildcycle(subpath(4,10) of c1,c4 rotated -85,subpath(4,10) of c3,c4 rotated 90) withcolor red;
fill buildcycle(subpath(0,7.9) of c1,c4 rotated -85,subpath(0,7.9) of c3,c4 rotated 90) withcolor 0.7[blue,white];


draw c1;
draw c3;
draw c6;

for i=0 step length(c1)/8 until length(c1):
draw ah rotated (i*45-3) shifted (5cm*dir (i*45-3));
%    draw point i of c1 withpen pencircle scaled 4bp;
  endfor;

draw textpathFont("\usefont{T1}{bch}{m}{n}\huge","consumption",reverse(c2),1.1);
draw textpathFont("\usefont{T1}{bch}{m}{n}\huge","Production",reverse(c2),1.65);
draw textpathFont("\usefont{T1}{bch}{m}{n}\large","Labor \& Capital mkts",reverse(c5),1.87);
draw textpathFont("\usefont{T1}{bch}{m}{n}\large","Capital \& Credit mkts",reverse(c5),1.59);
draw textpathFont("\usefont{T1}{bch}{m}{n}\large","Goods and Credit mkts",reverse(c5),2.35);

draw c4 rotated 175 intersectionpoint c3--c4 rotated 175 intersectionpoint c6;
%draw c4 rotated 105 intersectionpoint c3--c4 rotated 105 intersectionpoint c6;
draw c4 rotated -85 intersectionpoint c1--c4 rotated -85 intersectionpoint c6;
draw c4 rotated 90 intersectionpoint c1--c4 rotated 90 intersectionpoint c6;


l1 = (1cm*dir 45)--(6cm*dir 45);
l2 = (1cm*dir 80)--(6cm*dir 80);

%draw l1;
%draw l2;

pp = buildcycle(l1,c1,l2,c2);
%fill pp withcolor blue;%0.7white;

picture lab;
lab=thelabel(btex demand side etex,origin) rotated -45 shifted (4.0cm*dir 45);
%unfill bbox lab;

fill fullcircle scaled 3mm shifted (0cm,14.25cm);

draw fullcircle scaled 4mm shifted (0cm,14.25cm);

picture tl;

tl=thelabel(btex initialization etex scaled scalefactor,(0cm,13.0cm));

draw (0cm,14cm)--(0cm,5.2cm){dir -90}..{dir 0}(2mm,5cm);

unfill bbox tl;
draw tl;
draw bbox tl;


draw thelabel.rt(btex $\triangleleft$ \hskip3mm 28 \hskip3mm \color{blue}F\color{black}: make production etex scaled scalefactor,origin) rotated 84.2068965517241 shifted (5cm*dir 84.2068965517241);

draw thelabel.lft(btex OFS: substitute retired consumers \hskip3mm 27 \hskip3mm $\triangleright$ etex scaled scalefactor,origin) rotated 276.620689655172 shifted (5cm*dir 96.6206896551724);

draw thelabel.lft(btex OFS: compute and allocate investments \hskip3mm 26 \hskip3mm $\triangleright$ etex scaled scalefactor,origin) rotated 289.034482758621 shifted (5cm*dir 109.034482758621);

draw thelabel.lft(btex \color{blue}F\color{black}: set production capital and bank account \hskip3mm 25 \hskip3mm $\triangleright$ etex scaled scalefactor,origin) rotated 301.448275862069 shifted (5cm*dir 121.448275862069);

draw thelabel.lft(btex LM: match vacancies and unemployed \hskip3mm 24 \hskip3mm $\triangleright$ etex scaled scalefactor,origin) rotated 313.862068965517 shifted (5cm*dir 133.862068965517);

draw thelabel.lft(btex \color{blue}F\color{black}: hire \hskip3mm 23 \hskip3mm $\triangleright$ etex scaled scalefactor,origin) rotated 326.275862068966 shifted (5cm*dir 146.275862068966);

draw thelabel.lft(btex \color{red}C$_U$\color{black}: send curricula \hskip3mm 22 \hskip3mm $\triangleright$ etex scaled scalefactor,origin) rotated 338.689655172414 shifted (5cm*dir 158.689655172414);

draw thelabel.lft(btex \color{blue}F\color{black}: retire and fire workers \hskip3mm 21 \hskip3mm $\triangleright$ etex scaled scalefactor,origin) rotated 351.103448275862 shifted (5cm*dir 171.103448275862);

draw thelabel.lft(btex OFS: perform firm entry \hskip3mm 20 \hskip3mm $\triangleright$ etex scaled scalefactor,origin) rotated 363.51724137931 shifted (5cm*dir 183.51724137931);

draw thelabel.lft(btex \color{blue}F\color{black}: set possible investment  \hskip3mm 19 \hskip3mm $\triangleright$ etex scaled scalefactor,origin) rotated 375.931034482759 shifted (5cm*dir 195.931034482759);

draw thelabel.lft(btex B$\rightarrow$\color{blue}F\color{black}: set allowed credit \hskip3mm 18 \hskip3mm $\triangleright$ etex scaled scalefactor,origin) rotated 388.344827586207 shifted (5cm*dir 208.344827586207);

draw thelabel.lft(btex \color{blue}F\color{black}$\rightarrow$B: set desired investments (ask for credit if needed) \hskip3mm 17 \hskip3mm $\triangleright$ etex scaled scalefactor,origin) rotated 400.758620689655 shifted (5cm*dir 220.758620689655);

draw thelabel.lft(btex \color{blue}F\color{black}$\rightarrow$B: pay back if possible \hskip3mm 16 \hskip3mm $\triangleright$ etex scaled scalefactor,origin) rotated 413.172413793103 shifted (5cm*dir 233.172413793103);

draw thelabel.lft(btex B$\rightarrow$\color{blue}F\color{black}: pay interest and ask refunding \hskip3mm 15 \hskip3mm $\triangleright$ etex scaled scalefactor,origin) rotated 425.586206896552 shifted (5cm*dir 245.586206896552);

draw thelabel.lft(btex OFS: perform firms exit \hskip3mm 14 \hskip3mm $\triangleright$ etex scaled scalefactor,origin) rotated 438 shifted (5cm*dir 258);

draw thelabel.rt(btex $\triangleleft$ \hskip3mm 13 \hskip3mm \color{blue}F\color{black}: compute economic result and capital depreciation etex scaled scalefactor,origin) rotated 270.413793103448 shifted (5cm*dir 270.413793103448);

draw thelabel.rt(btex $\triangleleft$ \hskip3mm 12 \hskip3mm \color{red}C\color{black}$\rightarrow$B: compute saving and deposit them etex scaled scalefactor,origin) rotated 282.827586206897 shifted (5cm*dir 282.827586206897);

draw thelabel.rt(btex $\triangleleft$ \hskip3mm 11 \hskip3mm \color{red}C\color{black}: resize consumption if unsatisfied demand persists etex scaled scalefactor,origin) rotated 295.241379310345 shifted (5cm*dir 295.241379310345);

draw thelabel.rt(btex $\triangleleft$ \hskip3mm 10 \hskip3mm OFS: satisfy unsatified demand if possible etex scaled scalefactor,origin) rotated 307.655172413793 shifted (5cm*dir 307.655172413793);

draw thelabel.rt(btex $\triangleleft$ \hskip3mm 9 \hskip3mm OFS: compute and allocate desired demand etex scaled scalefactor,origin) rotated 320.068965517241 shifted (5cm*dir 320.068965517241);

draw thelabel.rt(btex $\triangleleft$ \hskip3mm 8 \hskip3mm \color{red}C\color{black}: resize consumption if demanded credit is not allowed etex scaled scalefactor,origin) rotated 332.48275862069 shifted (5cm*dir 332.48275862069);

draw thelabel.rt(btex $\triangleleft$ \hskip3mm 7 \hskip3mm B$\rightarrow$\color{red}C\color{black}: set allowed credit etex scaled scalefactor,origin) rotated 344.896551724138 shifted (5cm*dir 344.896551724138);

draw thelabel.rt(btex $\triangleleft$ \hskip3mm 6 \hskip3mm \color{red}C\color{black}$\rightarrow$B: set desired consumption (ask for credit if needed) etex scaled scalefactor,origin) rotated 357.310344827586 shifted (5cm*dir 357.310344827586);

draw thelabel.rt(btex $\triangleleft$ \hskip3mm 5 \hskip3mm \color{red}C$_S$\color{black}: step state etex scaled scalefactor,origin) rotated 9.72413793103448 shifted (5cm*dir 9.72413793103448);

draw thelabel.rt(btex $\triangleleft$ \hskip3mm 4 \hskip3mm \color{red}C\color{black}$\rightarrow$B: pay back if possible etex scaled scalefactor,origin) rotated 22.1379310344828 shifted (5cm*dir 22.1379310344828);

draw thelabel.rt(btex $\triangleleft$ \hskip3mm 3 \hskip3mm B$\rightarrow$\color{red}C\color{black}: account interest and ask refunding etex scaled scalefactor,origin) rotated 34.551724137931 shifted (5cm*dir 34.551724137931);

draw thelabel.rt(btex $\triangleleft$ \hskip3mm 2 \hskip3mm OFS-G-CB: fiscal policy decision and inplementation  etex scaled scalefactor,origin) rotated 46.9655172413793 shifted (5cm*dir 46.9655172413793);

draw thelabel.rt(btex $\triangleleft$ \hskip3mm 1 \hskip3mm \color{red}C$_E$\color{black}: receive wage etex scaled scalefactor,origin) rotated 59.3793103448276 shifted (5cm*dir 59.3793103448276);

draw thelabel.rt(btex $\triangleleft$ \hskip3mm 0 \hskip3mm OFS: compute variables etex scaled scalefactor,origin) rotated 71.7931034482759 shifted (5cm*dir 71.7931034482759);

endfig;
end;
