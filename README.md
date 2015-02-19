XHTML Entities
==========================

This utility class encodes and decodes HTML and XML entities. The idea came from 
an Android project, because {@link android.text.Html Html} does not support all 
tags or like i.e. '&#8222;' (`&amp;bdquo;` or `&amp;#8222;`) and is just too 
complex for this purpose.

The goal is highly performant conversion with a minimum of memory footprint.
It is best for frequently usage of relatively short strings like you will
find in XML or HTML text elements or attribute values. So Regular Expressions
are not an option.  
It does not yet support streaming. May be later.

The flow is optimized for the most probably occurence of characters in Roman
languages, which means ASCII characters lower than 128 are most expected.

An apdaption for the Android platform would utilize 
[SparseArray](http://developer.android.com/reference/android/util/SparseArray.html)
instead of 
[Map&lt;String, Integer&gt;](http://docs.oracle.com/javase/6/docs/api/java/util/Map.html) 
for the private `encodeMap`.

License
=======

	------------------------------------------------------------------------------
	ISC License http://opensource.org/licenses/isc-license.txt
	------------------------------------------------------------------------------
	Copyright (c) 2015, Dittmar Steiner <dittmar.steiner@gmail.com>

	Permission to use, copy, modify, and/or distribute this software for any
	purpose with or without fee is hereby granted, provided that the above
	copyright notice and this permission notice appear in all copies.

	THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
	WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
	MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
	ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
	WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
	ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
	OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.

