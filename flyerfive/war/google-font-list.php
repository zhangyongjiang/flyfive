<?php 
	$apiKey = 'AIzaSyAOzJADcG1AcQUhAjs7gOLSzyO9uCiq7Ow';
	$url = "https://www.googleapis.com/webfonts/v1/webfonts?sort=alpha&key=" . $apiKey;
	echo file_get_contents($url);
?>