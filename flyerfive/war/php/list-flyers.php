<?php
require_once 'common_include.php';

$query = "select id from flyers order by updated desc limit 100";
$result = mysql_query($query);

while ($row = mysql_fetch_assoc($result)) {
	echo $row['id'];
	echo "\n";
}
?>