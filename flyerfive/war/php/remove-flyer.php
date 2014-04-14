<?php
	require_once 'common_include.php';

	$userId = getCookieUserId();
	$id = $_REQUEST['id'];
	if(isset($id) && strlen($id)>0) {
		$query = "delete from flyers where id=" . mysql_real_escape_string($id) . " and user_id='" . mysql_real_escape_string($userId) . "'";
		mysql_query($query);
		
		$query = "delete from images where id=" . mysql_real_escape_string($id) . " and user_id='" . mysql_real_escape_string($userId) . "'";
		mysql_query($query);
	}
?>