define("app/templates", ["mustache"], function(Mustache) {
	var cache = {};
	
    function get(id, getter) {
    	if (cache[id] == null) {
    		cache[id] = Mustache.compile(getter());
    	}
    	return cache[id];
    }

    function adsense() {
    	var template = ''
    		+ '<script async="async" src="//pagead2.googlesyndication.com/pagead/js/adsbygoogle.js"></script>'
    		+ '<ins class="adsbygoogle"?'
    		+ '		style="display:inline-block;{{dimensions}}"'
    		+ '		data-ad-client="{{adClient}}"'
    		+ '		data-ad-slot="{{adSlot}}"></ins>'
    		+ '<script>'
    		+ '		(adsbygoogle = window.adsbygoogle || []).push({});'
    		+ '</script>';
    	return template;
    }
    
    function karmacracy() {
    	var template = ''
    			+ '<div class="kcy_karmacracy_widget_h_{{id}}"></div>'
    			+ '<script defer="defer" src="http://rodney.karmacracy.com/widget-3.0/?id={{id}}&type=h&width=725&sc=0&rb=1&c1=f2f2f2&c2=fff&c3=f2f2f2&c4=353535&c5=067dba&c6=ffffff&c7=353535&c9=353535&medio-id=undefined&url={{url}}"></script>';
    	return template;
    }
    
    function lastPosts() {
    	var template = ''
    		+ '<h3>Últimos artículos en {{name}}</h3>'
    		+ '<ul class="list-unstyled">'
    		+ '{{#posts}}'
    		+		'<li><a href="{{url}}">{{title}}</a></li>'
        	+ '{{/posts}}';
    	return template;
    }
    
    return {
    	adsense: function() { return get('adsense', adsense); },
    	karmacracy: function() { return get('karmacracy', karmacracy); },
    	lastPosts: function() { return get('lastPosts', lastPosts); }
    }
});