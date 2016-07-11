input latexmp
setupLaTeXMP(preamblefile="textpathfigs");
input textpath
input rboxes

prologues:=3;

verbatimtex
  \documentclass{article}
  \begin{document}
  etex;

beginfig(0);
color householdsbg,firmsbg,governmentbg,finsysbg,marketsbg,centralbankbg,laborofficebg,statbg;
withColors:=0;
if withColors > 0:
householdsbg:=(0.9,0.9,0.9);
firmsbg:=(0.9,0.9,0.9);
governmentbg:=(0.9,0.9,0.9);
finsysbg:=(0.9,0.9,0.9);
centralbankbg:=(0.9,0.9,0.9);
laborofficebg:=(0.85,1.0,1.0);
statbg:=(0.85,1.0,1.0);
marketsbg:=(1.0,1.0,1.0);
else:
householdsbg:=white;
firmsbg:=white;
governmentbg:=white;
finsysbg:=white;
centralbankbg:=white;
laborofficebg:=(0.9,0.9,0.9);
statbg:=white;
marketsbg:=(0.9,0.9,0.9);
fi;

path p;
p:= (0,0)--(2cm,2cm)--(4cm,0);
%draw textpath("Greetings from MetaPost!", p, 0);
picture firmspic,householdspic,goodsmktpic,creditmktpic,labormktpic,bankingsystempic,laborofficepic,publicsectorpic,centralbankpic,indonepic,indtwopic,indthreepic,statpic,unusedproductioncapitalmktpic;
u:=0.5;
fs:=0.7;

%firms

label(btex industry 1 etex scaled 1.5fs,(0u*4cm,4.2u*4cm));
rboxit.f11(thelabel(btex firm 1,1 etex scaled 1.0fs,(0.0u*4cm,4u*4cm)));
drawboxed(f11);
rboxit.f12(thelabel(btex firm 1,2 etex scaled 1.0fs,(0.0u*4cm,3.7u*4cm)));
drawboxed(f12);
rboxit.f13(thelabel(btex firm 1,3 etex scaled 1.0fs,(0.0u*4cm,3.4u*4cm)));
drawboxed(f13);
label(btex $\vdots$ etex scaled 1.0fs,(0.0u*4cm,3.1u*4cm));
indonepic:=currentpicture;
currentpicture:=nullpicture;

label(btex industry 2 etex scaled 1.5fs,(1u*4cm,4.2u*4cm));
rboxit.f21(thelabel(btex firm 2,1 etex scaled 1.0fs,(1.0u*4cm,4u*4cm)));
drawboxed(f21);
rboxit.f22(thelabel(btex firm 2,2 etex scaled 1.0fs,(1.0u*4cm,3.7u*4cm)));
drawboxed(f22);
rboxit.f23(thelabel(btex firm 2,3 etex scaled 1.0fs,(1.0u*4cm,3.4u*4cm)));
drawboxed(f23);
label(btex $\vdots$ etex scaled 1.0fs,(1.0u*4cm,3.1u*4cm));
indtwopic:=currentpicture;
currentpicture:=nullpicture;

label(btex industry 3 etex scaled 1.5fs,(2u*4cm,4.2u*4cm));
rboxit.f31(thelabel(btex firm 3,1 etex scaled 1.0fs,(2.0u*4cm,4u*4cm)));
drawboxed(f31);
rboxit.f32(thelabel(btex firm 3,2 etex scaled 1.0fs,(2.0u*4cm,3.7u*4cm)));
drawboxed(f32);
rboxit.f33(thelabel(btex firm 3,3 etex scaled 1.0fs,(2.0u*4cm,3.4u*4cm)));
drawboxed(f33);
label(btex $\vdots$ etex scaled 1.0fs,(2.0u*4cm,3.1u*4cm));
indthreepic:=currentpicture;
currentpicture:=nullpicture;



label(btex firms etex scaled 2.0fs,(u*6cm,4.5u*4cm));

rboxit.ind1(indonepic);
drawboxed(ind1);
rboxit.ind2(indtwopic);
drawboxed(ind2);
rboxit.ind3(indthreepic);
drawboxed(ind3);
label(btex $\cdots$ etex scaled 1.0fs,(u*3*4cm,4u*4cm));
%rboxit.firms(thelabel(btex firms etex scaled 2.0u,(u*6cm,2u*4cm)));
rboxit.firms(currentpicture);
firms.dx=u*5cm;
firms.dy=u*1cm;
fill bpath.firms withcolor firmsbg;
drawboxed(firms);
firmspic:=currentpicture;
currentpicture:=nullpicture;



%products
%p:=(-0.5u*4cm,1.15u*3cm)--(-0.5u*4cm,-1.15u*3cm)--(3.4u*4cm,-1.15u*3cm)--(3.4u*4cm,1.15u*3cm)--cycle;
%fill  p withcolor 0.6white;
p:=(-0.4u*4cm,u*3cm)--(-0.4u*4cm,-u*3cm)--(3.3u*4cm,-u*3cm)--(3.3u*4cm,u*3cm)--cycle;
pickup pencircle scaled 2pt;
draw p;
pickup defaultpen;
fill  p withcolor marketsbg;
label(btex goods market etex scaled 1.2fs,(u*1.5*4cm,u*3.4cm));
rboxit.product1(thelabel(btex product 1 etex scaled 1.0fs,(0,0)));
drawboxed(product1);
rboxit.product2(thelabel(btex product 2 etex scaled 1.0fs,(u*4cm,0)));
drawboxed(product2);

rboxit.product3(thelabel(btex product 3 etex scaled 1.0fs,(u*2*4cm,0)));
drawboxed(product3);
label(btex $\cdots$ etex scaled 1.0fs,(u*3*4cm,0));
goodsmktpic:=currentpicture;
currentpicture:=nullpicture;



%%%% household
%
label(btex consumers etex scaled 2.0fs,(u*5.7cm,-3.2u*4cm));
rboxit.stud(thelabel(btex students etex scaled 1.0fs,(u*1.7,-2.8u*4cm)));
drawboxed(stud);
rboxit.employed(thelabel(btex employed etex scaled 1.0fs,(u*(4cm+1.7cm),-2.8u*4cm)));
drawboxed(employed);
rboxit.unempl(thelabel(btex unemployed etex scaled 1.0fs,(u*2*(4cm+1.7cm),-2.8u*4cm)));
drawboxed(unempl);
rboxit.households(currentpicture);
households.dx=u*5cm;
households.dy=u*1cm;
fill bpath.households withcolor householdsbg;
drawboxed(households);
householdspic:=currentpicture;
currentpicture:=nullpicture;



%rboxit.households(thelabel(btex households etex scaled 2.0u,(u*5.7cm,u*-2*4cm)));
%households.dx=20cm;
%households.dy=2cm;
%fill bpath.households withcolor householdsbg;
%drawboxed(households);

%%%%%%%% labor market

%p:=(3.5u*4cm,1.15u*3cm)--(3.5u*4cm,-1.15u*3cm)--(5.2u*4cm,-1.15u*3cm)--(5.2u*4cm,1.15u*3cm)--cycle;
%fill  p withcolor 0.6white;
p:=(3.6u*4cm,u*3cm)--(3.6u*4cm,-u*3cm)--(5.1u*4cm,-u*3cm)--(5.1u*4cm,u*3cm)--cycle;
pickup pencircle scaled 2pt;
draw p;
pickup defaultpen;
fill  p withcolor marketsbg;
rboxit.lo(thelabel(btex labor office etex rotated -90 scaled 2.0fs,(4.9u*4cm,0)));
lo.dx=u*1mm;
lo.dy=u*4mm;
fill bpath.lo withcolor laborofficebg;
drawboxed(lo);
rboxit.ec(thelabel(btex \parbox{2cm}{\begin{center}employment \\ contract\end{center}} etex scaled 1.0fs,(4.1u*4cm,0)));
ec.dy=0mm;
drawboxed(ec);
label(btex labor market etex scaled 1.2fs,(4.3u*4cm,u*3.4cm));
labormktpic:=currentpicture;
currentpicture:=nullpicture;


%%%%%%% government

rboxit.gov(thelabel(btex Government etex rotated -90 scaled 2.0fs,(8u*4cm,0)));
gov.dx=u*1cm;
gov.dy=u*1cm;
fill bpath.gov withcolor governmentbg;
drawboxed(gov);
publicsectorpic:=currentpicture;
currentpicture:=nullpicture;


%%%%% office for statistics
label(btex Office for Statistics etex rotated -90 scaled 2.0fs,(1*u*4cm,0));
rboxit.goodsmktmoderator(thelabel(btex \parbox{2.5cm}{\begin{center}capital \\ market \\ moderator\end{center}} etex scaled 1.5fs,(0,2.0u*4cm)));%\halign{#\hfil\cr First Line\cr Second  Line\cr} 
goodsmktmoderator.dy=u*0.5cm;
drawboxed(goodsmktmoderator);
rboxit.computing(thelabel(btex \parbox{2.5cm}{\begin{center}statistics \\ computation \\ department\end{center}} etex scaled 1.5fs,(0,0.0u*4cm)));
computing.dy=u*0.5cm;
drawboxed(computing);
rboxit.capitalmktmoderator(thelabel(btex \parbox{2.5cm}{\begin{center}goods \\ market \\ moderator\end{center}} etex scaled 1.5fs,(0,-2.0u*4cm)));
capitalmktmoderator.dy=u*0.5cm;
drawboxed(capitalmktmoderator);
rboxit.stat(currentpicture);
fill bpath.stat withcolor statbg;
drawboxed(stat);
statpic:=currentpicture shifted (9.5*u*4cm,0);
currentpicture:=nullpicture;


%%%%%%%% credit market

%p:=(-0.6u*4cm,1.15u*3cm)--(-0.6u*4cm,-1.15u*3cm)--(-2.35u*4cm,-1.15u*3cm)--(-2.35u*4cm,1.15u*3cm)--cycle;
%fill  p withcolor 0.6white;
p:=(-0.7u*4cm,u*3cm)--(-0.7u*4cm,-u*3cm)--(-2.25u*4cm,-u*3cm)--(-2.25u*4cm,u*3cm)--cycle;
pickup pencircle scaled 1pt;
draw p;
pickup defaultpen;
%fill  p withcolor marketsbg;
rboxit.ba(thelabel(btex bank account etex scaled 1.0fs,(u*-1.5*4cm,0)));
ba.dy=2.0mm;
drawboxed(ba);

label(btex credit market etex scaled 1.2fs,(u*-1.3*4cm,u*3.4cm));
creditmktpic:=currentpicture;
currentpicture:=nullpicture;

%%%%%%%% unused production capital market

%p:=(-0.6u*4cm,1.15u*3cm)--(-0.6u*4cm,-1.15u*3cm)--(-2.35u*4cm,-1.15u*3cm)--(-2.35u*4cm,1.15u*3cm)--cycle;
%fill  p withcolor 0.6white;
p:=(-0.7u*4cm,u*3cm)--(-0.7u*4cm,-u*3cm)--(-2.25u*4cm,-u*3cm)--(-2.25u*4cm,u*3cm)--cycle;
pickup pencircle scaled 2pt;
draw p;
pickup defaultpen;
fill  p withcolor marketsbg;
rboxit.upc(thelabel(btex \parbox{3cm}{\begin{center}unused \\ production \\ capital \end{center}} etex scaled 1.0fs,(u*-1.5*4cm,0)));
upc.dy=2.0mm;
drawboxed(upc);

label(btex unused production capital etex scaled 1.2fs,(u*-1.5*4cm,u*3.4cm));
unusedproductioncapitalmktpic:=currentpicture shifted (7.7*u*4cm,0);
currentpicture:=nullpicture;


%% banking system
label(btex banking system etex rotated 90 scaled 2.0fs,(u*-3.5*4cm,0));
rboxit.bank1(thelabel(btex bank 1 etex scaled 1fs,(u*-3.5*4cm,1.5*u*4cm)));
drawboxed(bank1);
rboxit.bank2(thelabel(btex bank 2 etex scaled 1fs,(u*-3.5*4cm,1*u*4cm)));
drawboxed(bank2);
rboxit.bank3(thelabel(btex bank 3 etex scaled 1fs,(u*-3.5*4cm,-1*u*4cm)));
drawboxed(bank3);
label(btex $\vdots$ etex scaled 1fs,(u*-3.5*4cm,-1.5*u*4cm));
rboxit.banksys(currentpicture);
banksys.dx=u*4mm;
banksys.dy=u*10mm;
fill bpath.banksys withcolor finsysbg;
drawboxed(banksys);
bankingsystempic:=currentpicture;
currentpicture:=nullpicture;

%%%%%%% central bank
rboxit.cb(thelabel(btex Central Bank etex rotated 90 scaled 2.0fs,(-3.0u*4cm,0)));
cb.dx=u*1cm;
cb.dy=u*4.5cm;
fill bpath.cb withcolor centralbankbg;
drawboxed(cb);
label(btex Central Bank etex rotated 90 scaled 2.0fs,(-3.0u*4cm,0)) withcolor 0.5white;
centralbankpic:=currentpicture;
currentpicture:=nullpicture;


draw firmspic;
draw householdspic;
draw goodsmktpic;
draw creditmktpic;
draw labormktpic;
draw unusedproductioncapitalmktpic;
draw publicsectorpic;
draw statpic;
draw bankingsystempic;
%pickup pencircle scaled 2;
%p:=ind1.c--product1.c cutbefore bpath.ind1 cutafter bpath.product1;
%draw subpath(0,0.3) of p;
%drawarrow subpath(0.5,1) of p;
%draw textpathFont("\usefont{T1}{bch}{m}{n}\huge","supply",ind1.c--product1.c,0.4);

%p:=unempl.c{dir 0}..{dir 90}gov.c cutbefore bpath.unempl cutafter bpath.gov;
%draw subpath(0,0.45) of p;
%draw subpath(0.55,1) of p;
%draw textpathFont("\usefont{T1}{bch}{m}{n}\huge","dole",p,0.55);

endfig;
end;

