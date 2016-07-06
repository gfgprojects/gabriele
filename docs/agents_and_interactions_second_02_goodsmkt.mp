draw firmspic;
draw householdspic;
draw goodsmktpic;
draw creditmktpic;
%draw labormktpic;
%draw unusedproductioncapitalmktpic;
draw publicsectorpic;
draw statpic;
draw bankingsystempic;
	    p:=employed.c{dir 0}..{dir 180}firms.c cutbefore bpath.employed cutafter bpath.firms;
drawarrow reverse(subpath(0,0.3) of p);
drawarrow reverse(subpath(0.4,1) of p);
%draw textpathFont("\usefont{T1}{bch}{m}{n}\huge","supply",ind1.c--product1.c,0.4);
			draw textpath("wage",p,0.35);

p:=unempl.c{dir 0}..{dir 90}gov.c cutbefore bpath.unempl cutafter bpath.gov;
drawarrow reverse(subpath(0,0.45) of p);
drawarrow reverse(subpath(0.55,1) of p);
%draw textpathFont("\usefont{T1}{bch}{m}{n}\huge","dole",p,0.55);
draw textpath("dole",p,0.55);

p:=ba.c{dir -10}..{dir -20}households.w cutbefore bpath.ba cutafter bpath.households;
drawarrow reverse(subpath(0,0.4) of p);
drawarrow reverse(subpath(0.6,1) of p);
%draw textpathFont("\usefont{T1}{bch}{m}{n}\huge","dole",p,0.55);
%draw textpath("refund",p,0.5);
draw textpath("refund",reverse(p),0.5);

p:=ba.c{dir -35}..{dir -15}households.w cutbefore bpath.ba cutafter bpath.households;
drawarrow reverse(subpath(0,0.3) of p);
drawarrow reverse(subpath(0.7,1) of p);
%draw textpathFont("\usefont{T1}{bch}{m}{n}\huge","dole",p,0.55);
%draw textpath("refund",p,0.5);
draw textpath("demaded credit",reverse(p),0.5);


p:=ba.c{dir -135}..{dir 0}households.w cutbefore bpath.ba cutafter bpath.households;
drawarrow subpath(0,0.25) of p;
drawarrow subpath(0.6,1) of p;
%draw textpathFont("\usefont{T1}{bch}{m}{n}\huge","dole",p,0.55);
draw textpath("allowed credit",p,0.5);

drawarrow stud.c--product1.c cutbefore bpath.stud cutafter bpath.product1;
drawarrow stud.c--product2.c cutbefore bpath.stud cutafter bpath.product2;
drawarrow stud.c--product3.c cutbefore bpath.stud cutafter bpath.product3;

drawarrow employed.c--product1.c cutbefore bpath.employed cutafter bpath.product1;
drawarrow employed.c--product2.c cutbefore bpath.employed cutafter bpath.product2;
drawarrow employed.c--product3.c cutbefore bpath.employed cutafter bpath.product3;

drawarrow unempl.c--product1.c cutbefore bpath.unempl cutafter bpath.product1;
drawarrow unempl.c--product2.c cutbefore bpath.unempl cutafter bpath.product2;
drawarrow unempl.c--product3.c cutbefore bpath.unempl cutafter bpath.product3;

endfig;
end;

