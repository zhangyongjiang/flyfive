<?php require_once 'php/common_include.php';?>
<!doctype html>

<html>
  <head>
	<script type="text/javascript" src="jquery/js/jquery-1.5.min.js"></script>
	<script type="text/javascript" src="scripts/json2.js"></script>
    <script type="text/javascript">
      WebFontConfig = {
        google: { families: [ 'Tangerine'
                              , 'Cantarell'
                              , 'Tangerine'
                              , 'Princess Sofia'
                              , 'Seymour One'
                              , 'Codystar'
                              , 'New Rocker'
                              , 'Sonsie One'
                              , 'Sofia'
                              , 'Grand Hotel'
                              , 'Mrs Saint Delafield'
                              , 'Clicker Script'
                              , 'Monoton'
                              , 'Sacramento'
                              , 'Freckle Face'
                              , 'Merienda'
                              , 'Hanalei'
                              , 'Qwigley'
                              , 'Herr Von Muellerhoff'
                              , 'Bonbon'
                              , 'Ribeye Marrow'
                              , 'Mystery Quest'
                              , 'Ewert'
                               ] },
        active: function() {
                refresh();
		},
		fontactive: function(fontFamily, fontDescription) {
		},
      };

		function refresh() {
			try {
				window.allFontsLoaded();
			}
			catch (e) {
				setTimeout("refresh()", 1000);
			}
		}
      
        function loadImg(url) {
            $('<img>').attr({ src: url }).load(function() {
                window.imgLoaded();
            });
        }
        
      (function() {
        var wf = document.createElement('script');
        wf.src = ('https:' == document.location.protocol ? 'https' : 'http') +
            '://ajax.googleapis.com/ajax/libs/webfont/1/webfont.js';
        wf.type = 'text/javascript';
        wf.async = 'true';
        var s = document.getElementsByTagName('script')[0];
        s.parentNode.insertBefore(wf, s);
      })();
    </script>
    
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <!--link href='http://fonts.googleapis.com/css?family=Tangerine|Princess+Sofia|Seymour+One|Codystar|New+Rocker|Sonsie+One|Sofia|Grand+Hotel|Mrs+Saint+Delafield|Clicker+Script|Monoton|Sacramento|Freckle+Face|Merienda|Hanalei|Qwigley|Herr+Von+Muellerhoff|Bonbon|Ribeye+Marrow|Mystery+Quest|Ewert&effect=shadow-multiple|3d-float' rel='stylesheet' type='text/css'-->
    <link type="text/css" rel="stylesheet" href="FboBuilder.css">
    <title>Flyer Five</title>
    <script type="text/javascript" src="fbobuilder/fbobuilder.nocache.js"></script>
    <script type="text/javascript">
    function getFileManagerUrl() {
        var href = window.location.href;
        var pos = href.indexOf("/FboBuilder.php");
        return href.substring(0, pos) + "/php/file-manager/file-manager.php";
    }
    
    function getProxyUrl() {
        var href = window.location.href;
        var pos = href.indexOf("/FboBuilder.php");
        return href.substring(0, pos) + "/php/proxy.php";
    }
    
    function getUserId() {
        return '<?php echo FB::getInstance()->getUserId(); ?>';
    }
    
    </script>
  </head>
  <body style="width:100%;margin:0;padding:0;" >
  	  </body>
</html>

