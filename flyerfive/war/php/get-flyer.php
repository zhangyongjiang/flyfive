<?php
require_once 'common_include.php';
	
$query = "select content from flyers where id=" . $_REQUEST['id'];
$result = mysql_query($query);
while ($row = mysql_fetch_assoc($result)) {
	$data = $row['content'];
	echo $data;
}
?>