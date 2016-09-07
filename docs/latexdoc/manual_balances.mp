prologues:=3;

verbatimtex
%&latex 
\documentclass[10pt]{article}
\usepackage{color}
\begin{document}
etex

beginfig(0);
draw(-8cm,10cm)--(-4cm,10cm);
draw(-6cm,10cm)--(-6cm,7cm);
draw(-2cm,10.5cm)--(2cm,10.5cm);
draw(0cm,10.5cm)--(0cm,7.75cm);
draw(4cm,10.5cm)--(8cm,10.5cm);
draw(6cm,10.5cm)--(6cm,7cm);

draw(-4cm,10.0cm)--(0cm,10.0cm) dashed evenly;
draw(0cm,10.0cm)--(5mm,10.0cm) dashed withdots;
draw(14mm,10.0cm)--(45mm,10.0cm) dashed withdots;
draw(53mm,10.0cm)--(60mm,10.0cm) dashed withdots;
draw(6cm,10.0cm)--(8cm,10.0cm);
draw(6cm,9.7cm)--(2cm,9.7cm) dashed evenly;
draw(2cm,9.7cm)--(0cm,9.7cm);
draw(-6cm,7.75cm)--(6cm,7.75cm) dashed evenly;
draw(-2cm,7.75cm)--(2cm,7.75cm);
draw(-6cm,7.75cm)--(-4cm,7.75cm);
draw(-8cm,7cm)--(8cm,7cm) dashed evenly;
draw(-8cm,7cm)--(-4cm,7cm);
draw(4cm,7cm)--(8cm,7cm);
label(btex Production sector etex,(-6cm,10.25cm));
label(btex Banking sector etex,(0cm,10.75cm));
label(btex Consumers sector etex,(6cm,10.75cm));
label(btex $K_F$ etex,(-7cm,8.5cm));
label(btex $BA_F$ etex,(-5cm,9cm));
label(btex $E_F$ etex,(-5cm,7.25cm));
label(btex $BA_F$ etex,(-1cm,9cm));
label(btex $E_B$ etex,(1cm,10.0cm));
label(btex $BA^+_C$ etex,(1cm,9.0cm));
label(btex $BA^+_C$ etex,(5cm,9.0cm));
label(btex $BA^-_C$ etex,(-1cm,10.25cm));
label(btex $BA^-_C$ etex,(7cm,10.25cm));
label(btex $E_B$ etex,(5cm,10.0cm));
label(btex $E_F$ etex,(5cm,7.25cm));
label(btex $NW_C$ etex,(7cm,8.5cm));
endfig;

	   end

