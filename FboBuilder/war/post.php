<?php
require_once 'php/common_include.php';

if (FB::getInstance()->user && FB::getInstance()->getUserId()) {
	$accessToken = FB::getInstance()->getAccessToken();
	if($accessToken) {
		FB::getInstance()->facebook->api( '/me/feed/', 'post', 
			array('access_token' => $accessToken
				, 'message' => $_REQUEST['msg']
				, 'picture' => $_REQUEST['picture']
				, 'link' => $_REQUEST['link']
				));
		echo "ok";
		return;
	}
}
echo "error";
?>
