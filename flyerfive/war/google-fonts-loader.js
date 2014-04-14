loadedFonts = new Array();
function loadFont(font, variant, subset) {
	if (loadedFonts.indexOf(font) > -1)
		return;

	var apiUrl = [];
	apiUrl.push('//fonts.googleapis.com/css?family=');
	apiUrl.push(font.replace(/ /g, '+'));

	if (variant != null) {
		apiUrl.push(':');
		apiUrl.push(variant);
	}
	if (subset != null) {
		apiUrl.push('&subset=');
		apiUrl.push(subset);
	}

	var url = apiUrl.join('');
	loadjscssfile(url, "css");

	loadedFonts[loadedFonts.length] = font;
}

function loadjscssfile(filename, filetype) {
	if (filetype == "js") { // if filename is a external JavaScript file
		var fileref = document.createElement('script')
		fileref.setAttribute("type", "text/javascript")
		fileref.setAttribute("src", filename)
	} else if (filetype == "css") { // if filename is an external CSS file
		var fileref = document.createElement("link")
		fileref.setAttribute("rel", "stylesheet")
		fileref.setAttribute("type", "text/css")
		fileref.setAttribute("href", filename)
	}
	if (typeof fileref != "undefined")
		document.getElementsByTagName("head")[0].appendChild(fileref)
}
