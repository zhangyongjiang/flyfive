<?php
	require_once 'common_include.php';
	
	function updateFlyer() {
		$userId = getCookieUserId();
		$id = $_REQUEST['id'];
		$content = stripslashes($_REQUEST['content']);
		$img = stripslashes($_REQUEST['img']);
		$query = "update flyers set content='" . mysql_real_escape_string($content) . "', updated=now() where id=$id";
		mysql_query($query);
		
		$query = "update images set data='" . mysql_real_escape_string($img) . "' where id=$id";
		mysql_query($query);
	}
	
	function insertFlyer() {
		$userId = getCookieUserId();
		$content = stripslashes($_REQUEST['content']);
		$img = stripslashes($_REQUEST['img']);
		$query = "insert into flyers (user_id, content, updated) values ('$userId', '" . mysql_real_escape_string($content) . "', now())";
		mysql_query($query);
		$id = mysql_insert_id();

		$query = "insert into images (id, data) values ($id, '" . mysql_real_escape_string($img) . "')";
		mysql_query($query);
		return $id;
	}
	
	$id = $_REQUEST['id'];
	if(isset($id) && strlen($id)>0) {
		updateFlyer();
		echo $id;
	}
	else {
		$id = insertFlyer();
		echo $id;
	}
?>