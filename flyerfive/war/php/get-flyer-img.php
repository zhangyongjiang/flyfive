<?php
require_once 'common_include.php';
	
$query = "select data from images where id=" . $_REQUEST['id'];
$result = mysql_query($query);
while ($row = mysql_fetch_assoc($result)) {
	$data = $row['data'];
	$img = base64_decode($data);
	header('Content-type:image/png');
	echo $img;
}
?>