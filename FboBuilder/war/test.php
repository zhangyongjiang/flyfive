<?php
	$content = file_get_contents("/tmp/test.64");
	file_put_contents("/tmp/test.png", base64_decode($content));
?>