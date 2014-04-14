<html>
<head>
<script type="text/javascript" src="jquery/js/jquery-1.5.min.js"></script>
<script type="text/javascript" src="scripts/json2.js"></script>
<script type="text/javascript">
	function login() {
		top.location = '<?php echo FB::getInstance()->getLoginUrl(array("page" => "edit")); ?>';
	}
</script>
</head>
<body>

<?php if (FB::getInstance()->user && FB::getInstance()->getUserId()): ?> 
	<div style="float:left;margin-right:24px;margin-bottom:24px;">
	    <div style=''>
	        <a href='FboBuilder.php'><img style="height:60px;" src="images/FlyerFive_320x66.png"/></a>
	    </div>
	    <div style='margin-left:10px;margin-top:-6px;'>
	        <a href='FboBuilder.php'><img style="height:30px;" src="images/builton_450x48.png"/></a>
	    </div>
	</div>
<?php else: ?> 
	<div style="float:left;margin-right:24px;margin-bottom:24px;">
	    <div style=''>
	        <a href='#' onclick="login()"><img style="height:60px;" src="images/FlyerFive_320x66.png"/></a>
	    </div>
	    <div style='margin-left:10px;margin-top:-6px;'>
	        <a href='#' onclick="login()"><img style="height:30px;" src="images/builton_450x48.png"/></a>
	    </div>
	</div>
<?php endif ?>


<?php 
	$file = DATA_DIR . "/public-flyers.txt";
	if(file_exists($file)) {
		$items = explode("\n", file_get_contents($file), 1000);
		$items = array_reverse($items);
		foreach ($items as $flyerId) {
			$pos = strrpos($flyerId, ".");
			if(!$pos) continue;
			$img = substr($flyerId, 0, $pos) . ".png";
			if($img == ".png") continue;
			echo "<div style='float:left;padding:3px;margin:3px;border:solid 1px #ddd;'>";
			echo "<a href='details.php?id=" . $flyerId . "'>";
			echo "<img style='height:100px;' src='php/file-manager/file-manager.php?action=get-raw-content&id=" . $img . "'/>";
			echo "</a>";
			echo "<div style='clear:both;height:1px;'></div></div>";
		}
	}
?>

</body>
</html>
