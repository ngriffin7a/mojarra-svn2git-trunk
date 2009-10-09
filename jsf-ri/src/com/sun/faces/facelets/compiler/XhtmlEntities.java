package com.sun.faces.facelets.compiler;

import java.util.HashSet;
import java.util.Set;

public class XhtmlEntities {

    static protected Set<String> entities = new HashSet<String>(248);

    static {
        entities.add("nbsp");
        entities.add("iexcl");
        entities.add("cent");
        entities.add("pound");
        entities.add("curren");
        entities.add("yen");
        entities.add("brvbar");
        entities.add("sect");
        entities.add("uml");
        entities.add("copy");
        entities.add("ordf");
        entities.add("laquo");
        entities.add("not");
        entities.add("shy");
        entities.add("reg");
        entities.add("macr");
        entities.add("deg");
        entities.add("plusmn");
        entities.add("sup2");
        entities.add("sup3");
        entities.add("acute");
        entities.add("micro");
        entities.add("para");
        entities.add("middot");
        entities.add("cedil");
        entities.add("sup1");
        entities.add("ordm");
        entities.add("raquo");
        entities.add("frac14");
        entities.add("frac12");
        entities.add("frac34");
        entities.add("iquest");
        entities.add("Agrave");
        entities.add("Aacute");
        entities.add("Acirc");
        entities.add("Atilde");
        entities.add("Auml");
        entities.add("Aring");
        entities.add("AElig");
        entities.add("Ccedil");
        entities.add("Egrave");
        entities.add("Eacute");
        entities.add("Ecirc");
        entities.add("Euml");
        entities.add("Igrave");
        entities.add("Iacute");
        entities.add("Icirc");
        entities.add("Iuml");
        entities.add("ETH");
        entities.add("Ntilde");
        entities.add("Ograve");
        entities.add("Oacute");
        entities.add("Ocirc");
        entities.add("Otilde");
        entities.add("Ouml");
        entities.add("times");
        entities.add("Oslash");
        entities.add("Ugrave");
        entities.add("Uacute");
        entities.add("Ucirc");
        entities.add("Uuml");
        entities.add("Yacute");
        entities.add("THORN");
        entities.add("szlig");
        entities.add("agrave");
        entities.add("aacute");
        entities.add("acirc");
        entities.add("atilde");
        entities.add("auml");
        entities.add("aring");
        entities.add("aelig");
        entities.add("ccedil");
        entities.add("egrave");
        entities.add("eacute");
        entities.add("ecirc");
        entities.add("euml");
        entities.add("igrave");
        entities.add("iacute");
        entities.add("icirc");
        entities.add("iuml");
        entities.add("eth");
        entities.add("ntilde");
        entities.add("ograve");
        entities.add("oacute");
        entities.add("ocirc");
        entities.add("otilde");
        entities.add("ouml");
        entities.add("divide");
        entities.add("oslash");
        entities.add("ugrave");
        entities.add("uacute");
        entities.add("ucirc");
        entities.add("uuml");
        entities.add("yacute");
        entities.add("thorn");
        entities.add("yuml");
        entities.add("OElig");
        entities.add("oelig");
        entities.add("Scaron");
        entities.add("scaron");
        entities.add("Yuml");
        entities.add("fnof");
        entities.add("circ");
        entities.add("tilde");
        entities.add("Alpha");
        entities.add("Beta");
        entities.add("Gamma");
        entities.add("Delta");
        entities.add("Epsilon");
        entities.add("Zeta");
        entities.add("Eta");
        entities.add("Theta");
        entities.add("Iota");
        entities.add("Kappa");
        entities.add("Lambda");
        entities.add("Mu");
        entities.add("Nu");
        entities.add("Xi");
        entities.add("Omicron");
        entities.add("Pi");
        entities.add("Rho");
        entities.add("Sigma");
        entities.add("Tau");
        entities.add("Upsilon");
        entities.add("Phi");
        entities.add("Chi");
        entities.add("Psi");
        entities.add("Omega");
        entities.add("alpha");
        entities.add("beta");
        entities.add("gamma");
        entities.add("delta");
        entities.add("epsilon");
        entities.add("zeta");
        entities.add("eta");
        entities.add("theta");
        entities.add("iota");
        entities.add("kappa");
        entities.add("lambda");
        entities.add("mu");
        entities.add("nu");
        entities.add("xi");
        entities.add("omicron");
        entities.add("pi");
        entities.add("rho");
        entities.add("sigmaf");
        entities.add("sigma");
        entities.add("tau");
        entities.add("upsilon");
        entities.add("phi");
        entities.add("chi");
        entities.add("psi");
        entities.add("omega");
        entities.add("thetasym");
        entities.add("upsih");
        entities.add("piv");
        entities.add("ensp");
        entities.add("emsp");
        entities.add("thinsp");
        entities.add("zwnj");
        entities.add("zwj");
        entities.add("lrm");
        entities.add("rlm");
        entities.add("ndash");
        entities.add("mdash");
        entities.add("lsquo");
        entities.add("rsquo");
        entities.add("sbquo");
        entities.add("ldquo");
        entities.add("rdquo");
        entities.add("bdquo");
        entities.add("dagger");
        entities.add("Dagger");
        entities.add("bull");
        entities.add("hellip");
        entities.add("permil");
        entities.add("prime");
        entities.add("Prime");
        entities.add("lsaquo");
        entities.add("rsaquo");
        entities.add("oline");
        entities.add("frasl");
        entities.add("euro");
        entities.add("weierp");
        entities.add("image");
        entities.add("real");
        entities.add("trade");
        entities.add("alefsym");
        entities.add("larr");
        entities.add("uarr");
        entities.add("rarr");
        entities.add("darr");
        entities.add("harr");
        entities.add("crarr");
        entities.add("lArr");
        entities.add("uArr");
        entities.add("rArr");
        entities.add("dArr");
        entities.add("hArr");
        entities.add("forall");
        entities.add("part");
        entities.add("exist");
        entities.add("empty");
        entities.add("nabla");
        entities.add("isin");
        entities.add("notin");
        entities.add("ni");
        entities.add("prod");
        entities.add("sum");
        entities.add("minus");
        entities.add("lowast");
        entities.add("radic");
        entities.add("prop");
        entities.add("infin");
        entities.add("ang");
        entities.add("and");
        entities.add("or");
        entities.add("cap");
        entities.add("cup");
        entities.add("int");
        entities.add("there4");
        entities.add("sim");
        entities.add("cong");
        entities.add("asymp");
        entities.add("ne");
        entities.add("equiv");
        entities.add("le");
        entities.add("ge");
        entities.add("sub");
        entities.add("sup");
        entities.add("nsub");
        entities.add("sube");
        entities.add("supe");
        entities.add("oplus");
        entities.add("otimes");
        entities.add("perp");
        entities.add("sdot");
        entities.add("lceil");
        entities.add("rceil");
        entities.add("lfloor");
        entities.add("rfloor");
        entities.add("lang");
        entities.add("rang");
        entities.add("loz");
        entities.add("spades");
        entities.add("clubs");
        entities.add("hearts");
        entities.add("diams");
    }
}
