/*
 * ------------------------------------------------------------------------------
 * ISC License http://opensource.org/licenses/isc-license.txt
 * ------------------------------------------------------------------------------
 * Copyright (c) 2015, Dittmar Steiner <dittmar.steiner@gmail.com>
 *
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
 * ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
 * OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */
package com.github.dittmarsteiner.xml;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * This utility class encodes and decodes HTML and XML entities. The idea came
 * from an Android project, because {@link android.text.Html Html} does not
 * support all tags or like i.e. '&#8222;' (<code>&amp;bdquo;</code> or
 * <code>&amp;#8222;</code>) and is just too complex for this purpose.
 * </p>
 * <p>
 * The goal is highly performant conversion with a minimum of memory footprint.
 * It is best for frequently usage of relatively short strings like you will
 * find in XML or HTML text elements or attribute values. So Regular Expressions
 * are not an option. <br/>
 * It does not yet support streaming. May be later.
 * </p>
 * <p>
 * The flow is optimized for the most probably occurence of characters in Roman
 * languages, which means ASCII characters lower than 128 are most expected.
 * </p>
 * <p>
 * An apdaption for the Android platform would utilize {@link SparseArray}
 * instead of {@link java.util.Map Map&lt;Integer, String&gt;} for the private
 * <code>encodeMap</code>.
 * </p>
 * 
 * <b>License:</b><br/>
 * <span style="padding-left: 3em;"><a href="http://opensource.org/licenses/isc-license.txt">ISC License</a></span>
 * 
 * @version 1.0
 * @author <a href="mailto:dittmar.steiner@gmail.com">Dittmar Steiner</a>
 */
public class Entities {
    
    /**
     * The acual map size is 252.
     * <br/>
     * The size + 1 will never reach the load factor limit and it will keep
     * incedibly fast.
     * (255 looks nicer and we have only two <code>null</code> references)
     */
    private static final int mapSize = 0xFF;
    
    /**
     * For direct addressing to optimize for probability <code>char &lt; 128</code>
     */
    private static final int lt = '<', gt = '>', 
            amp = '&', quot = '"', apos = '\'', semicolon = ';';
    /**
     * For direct addressing to optimize for probability <code>char &lt; 128</code>
     */
    private static final String ltEnt = "&lt;", gtEnt = "&gt;", 
            ampEnt = "&amp;", quotEnt = "&quot;", aposEnt = "&apos;"; 
    
    /**
     * Contains all codes and entities from <a
     * href="http://www.w3.org/2003/entities/2007xml/unicode.xml"
     * >http://www.w3.org/2003/entities/2007xml/unicode.xml</a>
     */
    private static final Map<Integer, String> encodeMap = 
            new HashMap<>(mapSize);
    // populate
    static {
        // predefined XML entities
        encodeMap.put(lt, ltEnt);
        encodeMap.put(gt, gtEnt);
        encodeMap.put(amp, ampEnt);
        encodeMap.put(quot, quotEnt);
        encodeMap.put(apos, aposEnt);
        
        // HTML 4.0.1 entities
        encodeMap.put(193, "&Aacute;");
        encodeMap.put(225, "&aacute;");
        encodeMap.put(194, "&Acirc;");
        encodeMap.put(226, "&acirc;");
        encodeMap.put(180, "&acute;");
        encodeMap.put(198, "&AElig;");
        encodeMap.put(230, "&aelig;");
        encodeMap.put(192, "&Agrave;");
        encodeMap.put(224, "&agrave;");
        encodeMap.put(8501, "&alefsym;");
        encodeMap.put(913, "&Alpha;");
        encodeMap.put(945, "&alpha;");
        encodeMap.put(8743, "&and;");
        encodeMap.put(8736, "&ang;");
        encodeMap.put(197, "&Aring;");
        encodeMap.put(229, "&aring;");
        encodeMap.put(8776, "&asymp;");
        encodeMap.put(195, "&Atilde;");
        encodeMap.put(227, "&atilde;");
        encodeMap.put(196, "&Auml;");
        encodeMap.put(228, "&auml;");
        encodeMap.put(8222, "&bdquo;");
        encodeMap.put(914, "&Beta;");
        encodeMap.put(946, "&beta;");
        encodeMap.put(166, "&brvbar;");
        encodeMap.put(8226, "&bull;");
        encodeMap.put(8745, "&cap;");
        encodeMap.put(199, "&Ccedil;");
        encodeMap.put(231, "&ccedil;");
        encodeMap.put(184, "&cedil;");
        encodeMap.put(162, "&cent;");
        encodeMap.put(935, "&Chi;");
        encodeMap.put(967, "&chi;");
        encodeMap.put(710, "&circ;");
        encodeMap.put(9827, "&clubs;");
        encodeMap.put(8773, "&cong;");
        encodeMap.put(169, "&copy;");
        encodeMap.put(8629, "&crarr;");
        encodeMap.put(8746, "&cup;");
        encodeMap.put(164, "&curren;");
        encodeMap.put(8224, "&dagger;");
        encodeMap.put(8225, "&Dagger;");
        encodeMap.put(8595, "&darr;");
        encodeMap.put(8659, "&dArr;");
        encodeMap.put(176, "&deg;");
        encodeMap.put(916, "&Delta;");
        encodeMap.put(948, "&delta;");
        encodeMap.put(9830, "&diams;");
        encodeMap.put(247, "&divide;");
        encodeMap.put(201, "&Eacute;");
        encodeMap.put(233, "&eacute;");
        encodeMap.put(202, "&Ecirc;");
        encodeMap.put(234, "&ecirc;");
        encodeMap.put(200, "&Egrave;");
        encodeMap.put(232, "&egrave;");
        encodeMap.put(8709, "&empty;");
        encodeMap.put(8195, "&emsp;");
        encodeMap.put(8194, "&ensp;");
        encodeMap.put(917, "&Epsilon;");
        encodeMap.put(949, "&epsilon;");
        encodeMap.put(8801, "&equiv;");
        encodeMap.put(919, "&Eta;");
        encodeMap.put(951, "&eta;");
        encodeMap.put(208, "&ETH;");
        encodeMap.put(240, "&eth;");
        encodeMap.put(203, "&Euml;");
        encodeMap.put(235, "&euml;");
        encodeMap.put(8364, "&euro;");
        encodeMap.put(8707, "&exist;");
        encodeMap.put(402, "&fnof;");
        encodeMap.put(8704, "&forall;");
        encodeMap.put(189, "&frac12;");
        encodeMap.put(188, "&frac14;");
        encodeMap.put(190, "&frac34;");
        encodeMap.put(8260, "&frasl;");
        encodeMap.put(915, "&Gamma;");
        encodeMap.put(947, "&gamma;");
        encodeMap.put(8805, "&ge;");
        encodeMap.put(8596, "&harr;");
        encodeMap.put(8660, "&hArr;");
        encodeMap.put(9829, "&hearts;");
        encodeMap.put(8230, "&hellip;");
        encodeMap.put(205, "&Iacute;");
        encodeMap.put(237, "&iacute;");
        encodeMap.put(206, "&Icirc;");
        encodeMap.put(238, "&icirc;");
        encodeMap.put(161, "&iexcl;");
        encodeMap.put(204, "&Igrave;");
        encodeMap.put(236, "&igrave;");
        encodeMap.put(8465, "&image;");
        encodeMap.put(8734, "&infin;");
        encodeMap.put(8747, "&int;");
        encodeMap.put(921, "&Iota;");
        encodeMap.put(953, "&iota;");
        encodeMap.put(191, "&iquest;");
        encodeMap.put(8712, "&isin;");
        encodeMap.put(207, "&Iuml;");
        encodeMap.put(239, "&iuml;");
        encodeMap.put(922, "&Kappa;");
        encodeMap.put(954, "&kappa;");
        encodeMap.put(923, "&Lambda;");
        encodeMap.put(955, "&lambda;");
        encodeMap.put(9001, "&lang;");
        encodeMap.put(171, "&laquo;");
        encodeMap.put(8592, "&larr;");
        encodeMap.put(8656, "&lArr;");
        encodeMap.put(8968, "&lceil;");
        encodeMap.put(8220, "&ldquo;");
        encodeMap.put(8804, "&le;");
        encodeMap.put(8970, "&lfloor;");
        encodeMap.put(8727, "&lowast;");
        encodeMap.put(9674, "&loz;");
        encodeMap.put(8206, "&lrm;");
        encodeMap.put(8249, "&lsaquo;");
        encodeMap.put(8216, "&lsquo;");
        encodeMap.put(175, "&macr;");
        encodeMap.put(8212, "&mdash;");
        encodeMap.put(181, "&micro;");
        encodeMap.put(183, "&middot;");
        encodeMap.put(8722, "&minus;");
        encodeMap.put(924, "&Mu;");
        encodeMap.put(956, "&mu;");
        encodeMap.put(8711, "&nabla;");
        encodeMap.put(160, "&nbsp;");
        encodeMap.put(8211, "&ndash;");
        encodeMap.put(8800, "&ne;");
        encodeMap.put(8715, "&ni;");
        encodeMap.put(172, "&not;");
        encodeMap.put(8713, "&notin;");
        encodeMap.put(8836, "&nsub;");
        encodeMap.put(209, "&Ntilde;");
        encodeMap.put(241, "&ntilde;");
        encodeMap.put(925, "&Nu;");
        encodeMap.put(957, "&nu;");
        encodeMap.put(211, "&Oacute;");
        encodeMap.put(243, "&oacute;");
        encodeMap.put(212, "&Ocirc;");
        encodeMap.put(244, "&ocirc;");
        encodeMap.put(338, "&OElig;");
        encodeMap.put(339, "&oelig;");
        encodeMap.put(210, "&Ograve;");
        encodeMap.put(242, "&ograve;");
        encodeMap.put(8254, "&oline;");
        encodeMap.put(937, "&Omega;");
        encodeMap.put(969, "&omega;");
        encodeMap.put(927, "&Omicron;");
        encodeMap.put(959, "&omicron;");
        encodeMap.put(8853, "&oplus;");
        encodeMap.put(8744, "&or;");
        encodeMap.put(170, "&ordf;");
        encodeMap.put(186, "&ordm;");
        encodeMap.put(216, "&Oslash;");
        encodeMap.put(248, "&oslash;");
        encodeMap.put(213, "&Otilde;");
        encodeMap.put(245, "&otilde;");
        encodeMap.put(8855, "&otimes;");
        encodeMap.put(214, "&Ouml;");
        encodeMap.put(246, "&ouml;");
        encodeMap.put(182, "&para;");
        encodeMap.put(8706, "&part;");
        encodeMap.put(8240, "&permil;");
        encodeMap.put(8869, "&perp;");
        encodeMap.put(934, "&Phi;");
        encodeMap.put(966, "&phi;");
        encodeMap.put(928, "&Pi;");
        encodeMap.put(960, "&pi;");
        encodeMap.put(982, "&piv;");
        encodeMap.put(177, "&plusmn;");
        encodeMap.put(163, "&pound;");
        encodeMap.put(8242, "&prime;");
        encodeMap.put(8243, "&Prime;");
        encodeMap.put(8719, "&prod;");
        encodeMap.put(8733, "&prop;");
        encodeMap.put(936, "&Psi;");
        encodeMap.put(968, "&psi;");
        encodeMap.put(8730, "&radic;");
        encodeMap.put(9002, "&rang;");
        encodeMap.put(187, "&raquo;");
        encodeMap.put(8594, "&rarr;");
        encodeMap.put(8658, "&rArr;");
        encodeMap.put(8969, "&rceil;");
        encodeMap.put(8221, "&rdquo;");
        encodeMap.put(8476, "&real;");
        encodeMap.put(174, "&reg;");
        encodeMap.put(8971, "&rfloor;");
        encodeMap.put(929, "&Rho;");
        encodeMap.put(961, "&rho;");
        encodeMap.put(8207, "&rlm;");
        encodeMap.put(8250, "&rsaquo;");
        encodeMap.put(8217, "&rsquo;");
        encodeMap.put(8218, "&sbquo;");
        encodeMap.put(352, "&Scaron;");
        encodeMap.put(353, "&scaron;");
        encodeMap.put(8901, "&sdot;");
        encodeMap.put(167, "&sect;");
        encodeMap.put(173, "&shy;");
        encodeMap.put(931, "&Sigma;");
        encodeMap.put(963, "&sigma;");
        encodeMap.put(962, "&sigmaf;");
        encodeMap.put(8764, "&sim;");
        encodeMap.put(9824, "&spades;");
        encodeMap.put(8834, "&sub;");
        encodeMap.put(8838, "&sube;");
        encodeMap.put(8721, "&sum;");
        encodeMap.put(185, "&sup1;");
        encodeMap.put(178, "&sup2;");
        encodeMap.put(179, "&sup3;");
        encodeMap.put(8835, "&sup;");
        encodeMap.put(8839, "&supe;");
        encodeMap.put(223, "&szlig;");
        encodeMap.put(932, "&Tau;");
        encodeMap.put(964, "&tau;");
        encodeMap.put(8756, "&there4;");
        encodeMap.put(920, "&Theta;");
        encodeMap.put(952, "&theta;");
        encodeMap.put(977, "&thetasym;");
        encodeMap.put(8201, "&thinsp;");
        encodeMap.put(222, "&THORN;");
        encodeMap.put(254, "&thorn;");
        encodeMap.put(732, "&tilde;");
        encodeMap.put(215, "&times;");
        encodeMap.put(8482, "&trade;");
        encodeMap.put(218, "&Uacute;");
        encodeMap.put(250, "&uacute;");
        encodeMap.put(8593, "&uarr;");
        encodeMap.put(8657, "&uArr;");
        encodeMap.put(219, "&Ucirc;");
        encodeMap.put(251, "&ucirc;");
        encodeMap.put(217, "&Ugrave;");
        encodeMap.put(249, "&ugrave;");
        encodeMap.put(168, "&uml;");
        encodeMap.put(978, "&upsih;");
        encodeMap.put(933, "&Upsilon;");
        encodeMap.put(965, "&upsilon;");
        encodeMap.put(220, "&Uuml;");
        encodeMap.put(252, "&uuml;");
        encodeMap.put(8472, "&weierp;");
        encodeMap.put(926, "&Xi;");
        encodeMap.put(958, "&xi;");
        encodeMap.put(221, "&Yacute;");
        encodeMap.put(253, "&yacute;");
        encodeMap.put(165, "&yen;");
        encodeMap.put(255, "&yuml;");
        encodeMap.put(376, "&Yuml;");
        encodeMap.put(918, "&Zeta;");
        encodeMap.put(950, "&zeta;");
        encodeMap.put(8205, "&zwj;");
        encodeMap.put(8204, "&zwnj;");
    }
    
    /**
     * The revese version of {@link #encodeMap}.
     */
    private static final Map<String, Integer> decodeMap = new HashMap<>(mapSize);
    // transfer key/values
    static {
            Set<Integer> codes = encodeMap.keySet();
            for (Integer code : codes) {
                decodeMap.put(encodeMap.get(code), code);
            }
    }
    
    /**
     * Encodes all basic XML and characters &gt; 127 to all known HTML entities
     * like <code>'&Auml;'</code> to <code>&amp;Auml;</code>. Otherwise escapes
     * them to <code>'&amp;#&lt;integer&gt;;'</code> like
     * <code>'&#666;'</code> to <code>&amp;#666;</code>.
     * 
     * @param str
     *            the String to encode
     * @return the encoded String
     * 
     * @see #encodeXml(String)
     */
    public static String encodeHtml(String str) {
        return encode(str, false, false);
    }
    
    /**
     * Encodes all basic XML characters to entities <code>'&lt;'</code> to
     * <code>&amp;lt;</code>. All characters &gt; 127 are not encoded (unicode
     * as is)<br/>
     * <i>Except</i> the soft hyphen (<i>shy</i>) which will be
     * encodes as <code>&amp;#173;</code> just to make it visible.<br/>
     * (I really like hyphenation especially for small screens!)
     * 
     * @param str
     *            the String to encode
     * @return the encoded Unicode-String
     */
    public static String encodeXml(String str) {
        return encode(str, true, false);
    }
    
    /**
     * Just like {@link #encodeXml(String)} plus all characters &gt; 127 in the
     * form of <code>'&amp;#&lt;integer&gt;;'</code>.
     * 
     * @param str
     *            the String to encode
     * @return the encoded ASCII-String
     */
    public static String encodeAsciiXml(String str) {
        return encode(str, true, true);
    }
    
    /**
     * Here the actual conversion is done. All condition and conversion are
     * inline to avoid method calls.
     * 
     * @param str
     * @param xml false for HTML
     * @param ascii do not keep any unicode char &gt; 127
     * @return
     */
    private static String encode(String str, boolean xml, boolean ascii) {
        if (str == null) {
            return "";
        }
        
        // size * 1.2 is just a guess, not based on empirical data
        final StringBuilder builder = new StringBuilder((int)(str.length() * 1.2));
        
        for (int i = 0; i < str.length(); ++i) {
            char c = str.charAt(i);
            
            // xml basic
            if (c < 128) {
                switch (c) {
                case lt:
                    builder.append(ltEnt);
                    continue;
                case gt:
                    builder.append(gtEnt);
                    continue;
                case amp:
                    builder.append(ampEnt);
                    continue;
                case quot:
                    builder.append(quotEnt);
                    continue;
                case apos:
                    builder.append(aposEnt);
                    continue;
                }
            }
            
            // xml
            if (xml) {
                if (c < 128) {
                    builder.append(c);
                }
                else {
                    if (!ascii) {
                        if (c != 0x00AD) {
                            builder.append(c);
                        }
                        else {
                            // we always escape the shy char (0x00AD or #173) to make it visible
                            escape(c, builder);
                        }
                    }
                    else {
                        escape(c, builder);
                    }
                }
                continue;
            }
            
            // html
            String entity = !ascii ? encodeMap.get((int)c) : null;
            if (entity == null) {
                if (c < 128) {
                    builder.append(c);
                }
                else {
                    if (ascii) {
                        escape(c, builder);
                    }
                    else {
                        builder.append(c);
                    }
                }
            }
            else {
                builder.append(entity);
            }
        }
        
        return builder.toString();
    }
    
    /**
     * The only little helper method do encode linke '&#666;' to '&amp;#666;'
     * @param c
     * @param builder
     */
    private static void escape(int c, StringBuilder builder) {
        builder.append("&#");
        builder.append(Integer.toString(c));
        builder.append(";");
    }
    
    /**
     * For example <code>&amp;Auml;</code>, <code>&amp;#196;</code> and
     * <code>&amp;#xC4;</code> are decoded to &#196;
     * 
     * @param encoded
     *            XML or HTML String to decode
     * @return the decoded Unicode-String
     */
    public static String decode(final String encoded) {
        if (encoded.indexOf(amp) == -1) {
            // no StringBuilder allocation required
            return encoded;
        }
        
        final StringBuilder builder = new StringBuilder(encoded.length());
        
        for (int i = 0; i < encoded.length(); ++i) {
            final char c = encoded.charAt(i);
            
            if (c != amp) {
                builder.append(c);
            }
            else {
                final int sOffset = encoded.indexOf(semicolon, i + 1);
                
                // min length is 4 like "&lt;"
                // max length: "&thetasym;" and "&#x000e4;" are possible, 
                // otherwise this would just be a sentence.
                if (sOffset < i + 3 || sOffset >= i + 10) {
                    builder.append(c);
                    continue;
                }
                
                String entity = encoded.substring(i, sOffset + 1);
                if (
                        entity.indexOf(amp)  >  0 ||
                        entity.indexOf(' ')  > -1 ||
                        entity.indexOf('\t') > -1 ||
                        entity.indexOf('\f') > -1 ||
                        entity.indexOf('\r') > -1 ||
                        entity.indexOf('\n') > -1
                    ) {
                    // not a valid entity name
                    builder.append(c);
                    continue;
                }
                
                Integer code;
                if (entity.charAt(1) == '#') {
                    try {
                        if (entity.charAt(2) == 'x') {
                            // example: &#xAD;
                            code = Integer.valueOf(
                                    entity.substring(3, entity.length() - 1)
                                    , 16);
                        }
                        else {
                            // example: &#173;
                            code = Integer.valueOf(
                                    entity.substring(2, entity.length() - 1)
                                    );
                        }
                    }
                    catch (NumberFormatException e) {
                        code = null;
                    }
                    catch (StringIndexOutOfBoundsException e) {
                        code = null;
                    }
                }
                else {
                    code = (Integer)decodeMap.get(entity);
                }
                
                if (code == null) {
                    builder.append(entity);
                }
                else {
                    builder.append((char) code.intValue());
                }
                
                i = sOffset;
            }
        }
        return builder.toString();
    }
}
