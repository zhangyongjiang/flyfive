<?php
	header("Content-Type:image");
	echo file_get_contents($_REQUEST['url']);
?>