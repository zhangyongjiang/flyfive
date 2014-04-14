<?php
	function rrmdir($dir) {
		foreach(glob($dir . '/*') as $file) {
			if(is_dir($file))
				rrmdir($file);
			else
				unlink($file);
		}
		echo "rmdir " . $dir;
		rmdir($dir);
	}		
	
	$path = realpath(dirname(__FILE__));
	foreach(new DirectoryIterator($path) as $info) {
		if($info->isDot()) continue;
		if(is_dir($info->getPathname()))
			rrmdir($info->getPathname());
		else 
			unlink($info->getPathname());
	}
?>