<?php

/**
 * PHP handler for jWYSIWYG file uploader.
 *
 * By Alec Gorge <alecgorge@gmail.com>
 */
class AuthHandler extends ResponseHandler {
	private $authorized = false;

	public function __construct () {
		$this->authorized = (array_key_exists('auth', $_GET) && $_GET['auth'] === 'jwysiwyg');
	}

	public function getStatus ($router) {
		return $this->authorized ? "" : ResponseRouter::$Status401;
	}

	public function getStatusNumber ($router) {
		return $this->authorized ? 200 : 401;
	}

	public function getResponse ($router) {
		$cap = $router->getConfig()->getCapabilities();

		$r = array();
		$r['baseUrl'] = $router->getConfig()->getBaseUrl();
		foreach($cap as $k => $v) {
			// var_dump($router->getBaseFile());
			$r[$k] = array(
				"handler" => $router->getBaseFile(),
				"enabled" => $v
			);

			if($k == "upload") {
				$r[$k]["accept_ext"] = $router->getConfig()->getValidExtensions();
			}
		}
		return array(
			"success" => true,
			"data" => $r
		);
	}
}
ResponseRouter::getInstance()->setHandler("auth", new AuthHandler());

class ListHandler extends ResponseHandler {
	private function remove_path($file, $path) {
		$path = rtrim($path, '/');
		if (strpos($file, $path) !== false) {
			return substr($file, strlen($path));
		}
	}

	public function getResponse ($router) {
		$data = array(
			"directories" => array(),
			"files" => array()
		);
		
		try {
			$dst = $this->checkIfAccessible($router);
		} catch (Exception $e) {
			return array(
					"success" => false,
					"data" => $e->getMessage()
			);
		}
		
		$permission = decoct(fileperms($dst) & 0777);
		foreach(new DirectoryIterator($dst) as $info) {
			if($info->isDot()) continue;
			if($info->isFile()) {
				$pos = strpos($info->getFilename(), ".");
				if( $pos === 0 ) continue;
				$data["files"][$info->getFilename()] = decoct($info->getPerms() & 0777);
			}
			else if($info->isDir()) {
				$data["directories"][$info->getFilename()] = decoct($info->getPerms() & 0777);
			}
		}

		return array(
			"success" => true,
			"permission" => $permission,
			"data" => $data
		);
	}
}
ResponseRouter::getInstance()->setHandler("list", new ListHandler());

class RenameHandler extends ResponseHandler {
	public function getResponse ($router) {
		try {
			$dst = $this->checkIfAccessible($router);
		} catch (Exception $e) {
			return array(
					"success" => false,
					"data" => $e->getMessage()
			);
		}
		
		$userDir = new UserDir(FB::getInstance()->getUserId());
		if(isset($_REQUEST['user'])) {
			if($_REQUEST['user'] != FB::getInstance()->getUserId()) {
				if(!$userDir->isSuperUser()) {
					return array(
							"success" => false,
							"data" => "forbidden"
					);
				}
			}
		}
		
		if(!file_exists($dst)) {
			return new Response404();
		}

		throw new Exception("not implemented");
	}
}
ResponseRouter::getInstance()->setHandler("rename", new RenameHandler());

class RemoveHandler extends ResponseHandler {
	public function rrmdir($dir) {
		foreach(glob($dir . '/*') as $file) {
			if(is_dir($file))
				$this->rrmdir($file);
			else
				unlink($file);
		}
		rmdir($dir);
	}
	
	public function getResponse ($router) {
		try {
			$dst = $this->checkIfAccessible($router);
		} catch (Exception $e) {
			return array(
					"success" => false,
					"data" => $e->getMessage()
			);
		}
		
		$userDir = new UserDir(FB::getInstance()->getUserId());
		if(isset($_REQUEST['user'])) {
			if($_REQUEST['user'] != FB::getInstance()->getUserId()) {
				if(!$userDir->isSuperUser()) {
					return array(
							"success" => false,
							"data" => "forbidden"
					);
				}
			}
		}
		
		if(is_file($dst)) {
			unlink($dst);
		}
		else {
			$this->rrmdir($dst);
		}
		
		return array(
			"success" => true,
			"data" => "succeed."
		);
	}
}
ResponseRouter::getInstance()->setHandler("remove", new RemoveHandler());

class MkdirHandler extends ResponseHandler {
	public function getResponse ($router) {
		$newName = $router->cleanFile($_GET['newName']);
		
		try {
			$dst = $this->checkIfAccessible($router);
		} catch (Exception $e) {
			return array(
					"success" => false,
					"data" => $e->getMessage()
			);
		}
		
		$fileName = $dst . "/" . $newName;
		if(mkdir($fileName, 0700)) {
			return array(
				"success" => true,
				"data" => "Made: ".$fileName
			);
		}
		else {
			return array(
				"success" => false,
				"data" => "Couldn't make directory."
			);
		}
	}
}
ResponseRouter::getInstance()->setHandler("mkdir", new MkdirHandler());

class MoveHandler extends ResponseHandler {
	public function getResponse ($router) {
		$dir = $router->normalizeDir($_GET['dir']);
		$root = $router->getConfig()->getRootDir();
		$newPath = $router->cleanFile($_GET['newPath']);
		$file = $router->cleanFile($_GET['file']);

		if(!file_exists($root.$dir.$file)) {
			return new Response404();
		}
		if(rename($root.$dir.$file, $root.$newPath)) {
			return array(
				"success" => true,
				"data" => "message .. "
			);
		}
		else {
			return array(
				"success" => false,
				"data" => "Couldn't move file."
			);
		}
	}
}
ResponseRouter::getInstance()->setHandler("move", new MoveHandler());

class UploadHandler extends ResponseHandler {
	public function getResponse ($router) {
		$dir = $router->normalizeDir($_POST['dir']);
		$root = $router->getConfig()->getRootDir();
		$dst = $root . $dir . $_POST['newName'];

		if (file_exists($dst)) {
			return array(
				'success' => false,
				'data' => sprintf('Destination file "%s" exists.', $dir . $_POST['newName'])
			);
		}

		if (!is_uploaded_file($_FILES['handle']['tmp_name'])) {
			return array(
				'success' => false,
				'data' => 'Couldn\'t upload file.'
			);
		}

		if (!move_uploaded_file($_FILES['handle']['tmp_name'], $dst)) {
			return array(
				'success' => false,
				'data' => 'Couldn\'t upload file.'
			);
		}

		return array(
			'success' => true,
			'data' => 'File upload successful.'
		);
	}
}
ResponseRouter::getInstance()->setHandler("upload", new UploadHandler());

class SaveContentHandler extends ResponseHandler {
	public function getResponse ($router) {
		try {
			$dst = $this->checkIfAccessible($router);
		} catch (Exception $e) {
			return array(
					"success" => false,
					"data" => $e->getMessage()
			);
		}
		$dst = $dst . "/" . $_POST['newName'];
		$content = stripslashes($_POST['content']);
		
		if(isset($_REQUEST['base64'])) {
			$content = base64_decode($content);
		}

		if(file_exists($dst)) {
			file_put_contents($dst, $content);
			if($_REQUEST['permission']) {
				chmod($dst, octdec($_REQUEST['permission']));
			}
		}
		else {
			file_put_contents($dst, $content);
			if($_REQUEST['permission']) {
				chmod($dst, octdec($_REQUEST['permission']));
			}
			else {
				chmod($dst, 0700);
			}
		}

		return array(
				'success' => true,
				'data' => 'File upload successful.',
				'permission' => decoct(fileperms($dst))
		);
	}
}
ResponseRouter::getInstance()->setHandler("save", new SaveContentHandler());

class GetContentHandler extends ResponseHandler {
	public function getResponse ($router) {
		try {
			$file = $this->checkIfAccessible($router);
		} catch (Exception $e) {
			return array(
					"success" => false,
					"data" => $e->getMessage()
			);
		}
		
		$content = file_get_contents($file);
		$json = json_decode($content);
		$permission = decoct(fileperms($file) & 0777);
		
		if($this->endsWith($file, ".png")) {
			header('Content-type:image/png');
			echo $content;
			exit();
		}

		return array(
				'success' => true,
				'permission' => $permission,
				'data' => $json,
		);
	}
}
ResponseRouter::getInstance()->setHandler("get-file-content", new GetContentHandler());

class GetRawContentHandler extends ResponseHandler {
	public function getResponse ($router) {
		$id = $_REQUEST['id'];
		$pos = strpos($id, "/", 1);
		$userId = substr($id, 1, $pos-1);
		$dir = substr($id, $pos);
		$userDir = new UserDir($userId);
		$file = $userDir->getFullDir() . $dir;
		if(!file_exists($file))
			echo "not exist " . $file;
		else {
			$content = file_get_contents($file);
			if((strpos($file, ".png") + 4) == strlen($file))
				header('Content-type:image/png');
			echo $content;
		}
	}
}
ResponseRouter::getInstance()->setHandler("get-raw-content", new GetRawContentHandler());

class AccessControlHandler extends ResponseHandler {
	public function getResponse ($router) {
		try {
			$dst = $this->checkIfAccessible($router);
		} catch (Exception $e) {
			return array(
					"success" => false,
					"data" => $e->getMessage()
			);
		}
		
		$userDir = new UserDir(FB::getInstance()->getUserId());
		if(isset($_REQUEST['user'])) {
			if($_REQUEST['user'] != FB::getInstance()->getUserId()) {
				if(!$userDir->isSuperUser()) {
					return array(
							"success" => false,
							"data" => "forbidden"
					);
				}
			}
		}
		
		$permission = $_REQUEST['permission'];
		if($permission == "public") {
			chmod($dst, 0777);
			$this->addToPublicList($dst);
		}
		if($permission == "group") {
			chmod($dst, 0770);
		}
		if($permission == "private") {
			chmod($dst, 0700);
			$this->removeFromPublicList($dst);
		}

		return array(
				'success' => true,
				'data' => decoct(fileperms($dst) & 0777)
		);
	}
	
	public function addToPublicList($dst) {
		$dst = substr($dst, strlen(DATA_DIR) + 4);
		
		$file = DATA_DIR . "/public-flyers.txt";
		if(file_exists($file)) {
			$content = file_get_contents($file);
			$items = explode("\n", $content, 100000);
			$exist = false;
			$index = 0;
			foreach ($items as $item) {
				if($item == $dst) {
					$exist = true;
					break;
				}
				$index = $index + 1;
			}
			array_push($items, $dst);
			if($exist) {
				array_splice($items, $index, 1);
			}
			if(sizeof($items) > 200) {
				array_splice($items, 0, 1);
			}
			$content = implode("\n", $items);
			file_put_contents($file, $content);
		}
		else {
			file_put_contents($file, $dst);
		}
	}
	
	public function removeFromPublicList($dst) {
		$dst = substr($dst, strlen(DATA_DIR) + 4);
		
		$file = DATA_DIR . "/public-flyers.txt";
		if(file_exists($file)) {
			$content = file_get_contents($file);
			$items = explode("\n", $content, 100000);
			$exist = false;
			$index = 0;
			foreach ($items as $item) {
				if($item == $dst) {
					$exist = true;
					break;
				}
				else {
				}
				$index = $index + 1;
			}
			if($exist) {
				array_splice($items, $index, 1);
			}
			else {
			}
			$content = implode("\n", $items);
			file_put_contents($file, $content);
		}
		else {
		}
	}
}
ResponseRouter::getInstance()->setHandler("access-control", new AccessControlHandler());

class LatestHandler extends ResponseHandler {
	public function getResponse ($router) {
		$file = DATA_DIR . "/public-flyers.txt";
		if(file_exists($file)) {
			return array(
					'success' => true,
					'data' => explode("\n", file_get_contents($file))
			);
		}
		else {
			return array(
					'success' => true,
					'data' => array()
			);
		}
	}
}
ResponseRouter::getInstance()->setHandler("latest", new LatestHandler());


class FbPostHandler extends ResponseHandler {
	public function getResponse ($router) {
		if (FB::getInstance()->user && FB::getInstance()->getUserId()) {
			$accessToken = FB::getInstance()->getAccessToken();
			if($accessToken) {
				$picture = $_REQUEST['imageUrl'];
				$picture = str_replace("%26", "&", $picture);
				FB::getInstance()->facebook->api( '/me/feed/', 'post', 
					array('access_token' => $accessToken
						, 'message' => $_REQUEST['message']
						, 'picture' => $picture
						, 'link' => $_REQUEST['link']
						));
				return array(
						'success' => true,
						'data' => array("message" => $_REQUEST['message']
									, "imageUrl" => $picture
									, "link" => $_REQUEST['link']
								)
				);
			}
		}
		return array(
				'success' => false,
				'data' => array("message" => $_REQUEST['message']
							, "imageUrl" => $_REQUEST['imageUrl']
							, "link" => $_REQUEST['link']
						)
		);
	}
}
ResponseRouter::getInstance()->setHandler("post-to-facebook", new FbPostHandler());
