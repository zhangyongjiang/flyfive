<?php
session_start();
require_once 'config/default.php';
require_once 'config/' . $_SERVER['SERVER_NAME'] . '.php';
require_once 'sdk/facebook.php';

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

class UserDir {
	private $userId;
	
	public function __construct($userId) {
		$this->userId = $userId;
	}
	
	public function getDir() {
		$str = "" . $this->userId;
		$f3 = substr($str, 0, 3);
		return "/" . $f3 . "/" .$this->userId;
	}
	
	public function getFullDir() {
		return DATA_DIR . $this->getDir();
	}
	
	public function mkdir() {
		mkdir($this->getFullDir(), 0777, true);
	}
	
	public function getAccessTokenFile() {
		return $this->getFullDir() . "/.accesstoken";
	}
	
	public function saveAccessToken($token) {
		file_put_contents($this->getAccessTokenFile(), $token);
	}
	
	public function exists() {
		return file_exists($this->getFullDir());
	}
	
	function recurseCopy($src,$dst) {
		$dir = opendir($src);
		@mkdir($dst);
		while(false !== ( $file = readdir($dir)) ) {
			if (( $file != '.' ) && ( $file != '..' )) {
				if ( is_dir($src . '/' . $file) ) {
					recurse_copy($src . '/' . $file,$dst . '/' . $file);
				}
				else {
					copy($src . '/' . $file,$dst . '/' . $file);
				}
			}
		}
		closedir($dir);
	}
	
	public function copyExamples() {
		$this->recurseCopy(DATA_DIR . "/examples", $this->getFullDir() . "/examples");
	}
	
	public function getAccessToken() {
		if(file_exists($this->getAccessTokenFile()))
			return file_get_contents($this->getAccessTokenFile());
	}
	
	public function isSuperUser() {
		return file_exists($this->getFullDir() . "/.super");
	}
	
	public function isPublic($path) {
		$fullPath = $this->getFullDir() . $path;
		if(!file_exists($fullPath))
			return false;
		$permission = fileperms($fullPath);
		return ($permission & 7) == 7;
	}
}

class FlyerId {
	private $id;
	private $userId;
	private $path;
	
	public function __construct($id) {
		$this->id = $id;
		$pos = strpos($id, "/", 1);
		$this->userId = substr($id, 1, $pos-1);
		$this->path = substr($id, $pos);
	}
	
	public function getUserId() {
		return $this->userId;
	}
	
	public function getPath() {
		return $this->path;
	}
}
?>
