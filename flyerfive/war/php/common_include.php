<?php
session_start();
require_once 'config/default.php';
require_once 'config/' . $_SERVER['SERVER_NAME'] . '.php';
require_once 'facebook/facebook.php';

mysql_connect(DBSERVER, DBUSER, DBPWD);
mysql_select_db(DBNAME);

class FB {
	public static $instance;
	public $facebook;
	public $user;
	public $userInfo;

	public static function getInstance() {
		if(!(self::$instance instanceof FB)) {
			$fb = new FB();
			self::$instance = $fb;
			
			$facebook = new Facebook(array(
				'appId'  => appId,
				'secret' => secret,
			));
			$fb->facebook = $facebook;
			
			$user = $facebook->getUser();
			if ($user) {
				try {
					$fb->userInfo = $facebook->api('/me');
				} catch (FacebookApiException $e) {
					$user = null;
				}
			}
			$fb->user = $user;
		}
		return self::$instance;
	}
	
	public function getLoginUrl($redirectParams) {
		$redirectUrl = redirect_uri . "?";
		foreach ($redirectParams as $key => $value) {
			$redirectUrl = $redirectUrl . "&" . $key . "=" . urlencode($value);
		}
		$params = array('redirect_uri' => $redirectUrl, 'scope' => 'publish_stream');
		return $this->facebook->getLoginUrl($params);
	}
	
	public function getLogoutUrl() {
		return $this->facebook->getLogoutUrl();
	}
	
	public function getAccessToken() {
		return $this->facebook->getAccessToken();
	}
	
	public function getUserId() {
		if(!$this->user || !$this->userInfo)
			return;
		return $this->userInfo['id'];
	}
}

function changeOwner($oldUserId, $newUserId) {
	mysql_query("update flyers set user_id='" . $newUserId . "' where user_id='" . $oldUserId . "'");
}

function updateUser($userId, $token) {
	$query = "insert into users (fbid, fbtoken, updated) values ('$userId', '" . mysql_real_escape_string($token) . "', now()) on duplicate key update fbtoken='" . mysql_real_escape_string($token) . "'";
	mysql_query($query);
}

function getCookieUserId() {
	return $_COOKIE['userId'];
}

function setCookieUserId($userId) {
	$_COOKIE['userId'] = $userId;
	setcookie("userId", $userId, time()+(60*60*24*365), "/");
}

$fb = FB::getInstance();
$userId = $fb->getUserId();
if(isset($userId)) {
	updateUser($userId, $fb->getAccessToken());
}

$cookieUserId = getCookieUserId();
if(!isset($cookieUserId)) {
	if(isset($userId)) {
		setCookieUserId($userId);
	}
	else {
		setCookieUserId(md5($_SERVER['REMOTE_ADDR']));
	}
}
else {
	if(isset($userId)) {
		if($userId != getCookieUserId()) {
			changeOwner(getCookieUserId(), $userId);
			setCookieUserId($userId);
		}
	}
}
?>
