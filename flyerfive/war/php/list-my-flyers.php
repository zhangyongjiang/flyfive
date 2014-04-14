<?php
require_once 'common_include.php';

$userId = getCookieUserId();
$query = "select id from flyers where user_id='$userId' order by updated desc limit 100";
$result = mysql_query($query);

while ($row = mysql_fetch_assoc($result)) {
	echo $row['id'];
	echo "\n";
}
?>