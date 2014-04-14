<?php
require_once 'php/common_include.php';

if (FB::getInstance()->user && FB::getInstance()->getUserId()) {
	$userId = FB::getInstance()->getUserId();
	$userDir = new UserDir($userId);
	if(!$userDir->exists()) {
		$userDir->mkdir();
		$userDir->copyExamples();
	}
	$accessToken = FB::getInstance()->getAccessToken();
	$tokenInFile = $userDir->getAccessToken();
	if((!isset($tokenInFile) || $tokenInFile != $accessToken) && strpos($accessToken, appId) === false)
		$userDir->saveAccessToken($accessToken);
}

$showLogin = false;
if(isset($_REQUEST['id'])) {
	$flyerId = new FlyerId($_REQUEST['id']);
	$flyerUserId = $flyerId->getUserId();
	$userDir = new UserDir($flyerUserId);
	$isPub = $userDir->isPublic($flyerId->getPath());
	if($isPub || $flyerUserId == FB::getInstance()->getUserId()) {
		require_once 'details.php';
	}
	else {
		if (FB::getInstance()->user && FB::getInstance()->getUserId()) {
			echo "You don't have permission to see the flyer requested";
		}
		else {
			require_once 'login-required-for-detail.php';
		}
	}
}
else if(isset($_REQUEST['page'])) {
	if (FB::getInstance()->user && FB::getInstance()->getUserId()) {
		require_once 'FboBuilder.php';
	}
	else {
		require_once 'login-required-for-edit.php';
	}
}
else {
	require_once 'home.php';
}
?>
