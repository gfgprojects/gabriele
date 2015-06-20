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

